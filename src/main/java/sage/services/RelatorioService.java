package sage.services;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.cloud.FirestoreClient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Response;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import sage.models.relatorio.RelatorioParticipante;
import sage.models.relatorio.RelatorioRequisicao;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;

@ApplicationScoped
public class RelatorioService {

    private final Firestore db = FirestoreClient.getFirestore();
    private final FirebaseAuth auth = FirebaseAuth.getInstance();

    public Response gerarRelatorio(RelatorioRequisicao requisicao) {
        String eventoId = requisicao.getEventoId();

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Relatorio " + eventoId);

            // Estilo do cabeçalho
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("NOME_PARTICIPANTE");
            headerRow.createCell(1).setCellValue("CPF_PARTICIPANTE");
            headerRow.createCell(2).setCellValue("EMAIL_PARTICIPANTE");
            headerRow.createCell(3).setCellValue("CONDICAO_PARTICIPANTE");
            headerRow.createCell(4).setCellValue("FORMA_ACAO");
            headerRow.createCell(5).setCellValue("TITULO_ACAO");
            headerRow.createCell(6).setCellValue("PERIODO DE REALIZAÇÃO");

            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            for (int i = 0; i <= 6; i++) {
                headerRow.getCell(i).setCellStyle(headerStyle);
            }

            // Linha de espaço após o cabeçalho
            sheet.createRow(1);

            // Dados do evento
            DocumentSnapshot eventoDoc = db.collection("eventos").document(eventoId).get().get();
            String tituloAcao = eventoDoc.exists() ? eventoDoc.getString("nome") : "Evento Desconhecido";
            int cargaHorariaTotal = 0;

            if (eventoDoc.exists() && eventoDoc.getString("cargaHoraria") != null) {
                String cargaHorariaStr = eventoDoc.getString("cargaHoraria");
                // Usa regex para capturar apenas o número no início da string
                Matcher matcher = Pattern.compile("(\\d+)").matcher(cargaHorariaStr);
                if (matcher.find()) {
                    cargaHorariaTotal = Integer.parseInt(matcher.group(1)); // Extrai o número
                } else {
                    System.out.println("Formato de carga horária inválido: " + cargaHorariaStr);
                }
            }

            // Datas QRCode
            List<String> datasQRCode = new ArrayList<>();
            CollectionReference qrCodeRef = db.collection("qrcode");
            qrCodeRef.whereEqualTo("eventoId", eventoId).get().get().getDocuments()
                    .forEach(doc -> datasQRCode.add(doc.getString("data")));

            // Ordena explicitamente as datas antes de adicioná-las
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            datasQRCode.sort((date1, date2) -> {
                LocalDate localDate1 = LocalDate.parse(date1, formatter);
                LocalDate localDate2 = LocalDate.parse(date2, formatter);
                return localDate1.compareTo(localDate2);
            });

            // Mescla células para "PERIODO DE REALIZAÇÃO"
            int dataColumns = datasQRCode.size();
            if (dataColumns > 1) {
                sheet.addMergedRegion(new CellRangeAddress(0, 0, 6, 6 + dataColumns - 1));
            }

            // Insere datas de realização
            Row periodoRow = sheet.createRow(2);
            CellStyle dateStyle = workbook.createCellStyle();
            dateStyle.setAlignment(HorizontalAlignment.CENTER);

            for (int i = 0; i < datasQRCode.size(); i++) {
                Cell cell = periodoRow.createCell(6 + i);
                cell.setCellValue(datasQRCode.get(i));
                cell.setCellStyle(dateStyle);
            }

            // Célula "CARGA_HORARIA"
            Cell cargaHorariaHeader = headerRow.createCell(6 + dataColumns);
            cargaHorariaHeader.setCellValue("CARGA_HORARIA");
            cargaHorariaHeader.setCellStyle(headerStyle);

            CellStyle cargaHorariaStyle = workbook.createCellStyle();
            DataFormat format = workbook.createDataFormat();
            cargaHorariaStyle.setDataFormat(format.getFormat("0.00"));

            // Dados dos participantes
            CollectionReference participantesRef = db.collection("eventos").document(eventoId).collection("participantes");
            List<RelatorioParticipante> participantes = new ArrayList<>();

            for (DocumentSnapshot participanteDoc : participantesRef.get().get().getDocuments()) {
                String userId = participanteDoc.getId();
                DocumentSnapshot userDoc = db.collection("users").document(userId).get().get();

                if (userDoc.exists()) {
                    String nome = userDoc.getString("nome");
                    String cpf = userDoc.getString("cpf");

                    String email = "";
                    try {
                        UserRecord userRecord = auth.getUser(userId);
                        email = userRecord.getEmail();
                    } catch (FirebaseAuthException e) {
                        e.printStackTrace();
                    }

                    List<String> datasConfirmacao = (List<String>) participanteDoc.get("datasConfirmacao");
                    RelatorioParticipante participante = new RelatorioParticipante(
                            nome, cpf, email, "Participante", "Evento", tituloAcao, datasConfirmacao
                    );

                    participantes.add(participante);
                }
            }

            // Preenche a planilha com os dados dos participantes
            int rowIndex = 3;
            for (RelatorioParticipante participante : participantes) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(participante.getNome());
                row.createCell(1).setCellValue(participante.getCpf());
                row.createCell(2).setCellValue(participante.getEmail());
                row.createCell(3).setCellValue(participante.getCondicao());
                row.createCell(4).setCellValue(participante.getFormaAcao());
                row.createCell(5).setCellValue(participante.getTituloAcao());

                int diasPresentes = 0;
                for (int i = 0; i < datasQRCode.size(); i++) {
                    String dataQRCode = datasQRCode.get(i);
                    Cell cell = row.createCell(6 + i);

                    if (participante.getDatasConfirmacao() != null && participante.getDatasConfirmacao().contains(dataQRCode)) {
                        cell.setCellValue("X");
                        diasPresentes++;
                    }
                }

                double cargaHorariaParticipante = (diasPresentes / (double) dataColumns) * cargaHorariaTotal;
                Cell cargaHorariaCell = row.createCell(6 + dataColumns);
                cargaHorariaCell.setCellValue(cargaHorariaParticipante);
                cargaHorariaCell.setCellStyle(cargaHorariaStyle);
            }

            // Ajusta a largura das colunas
            for (int i = 0; i <= 6 + dataColumns; i++) {
                sheet.autoSizeColumn(i);
            }

            // Finaliza o arquivo
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);

            return Response.ok(outputStream.toByteArray())
                    .header("Content-Disposition", "attachment; filename=\"relatorio_" + eventoId + ".xlsx\"")
                    .build();
        } catch (IOException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao gerar relatório.")
                    .build();
        }
    }

}
