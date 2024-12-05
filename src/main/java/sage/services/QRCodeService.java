package sage.services;

import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;
import sage.models.qrcode.QRCodeData;
import sage.models.qrcode.QRCodeRequisicao;
import sage.models.qrcode.QRCodeResposta;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.Base64;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Serviço para geração, busca e exclusão de QR Codes.
 */
@ApplicationScoped
public class QRCodeService {

    private static final Logger logger = Logger.getLogger(QRCodeService.class);
    private final Firestore db = FirestoreClient.getFirestore();

    /**
     * Gera um QR Code com base nos dados fornecidos e salva no Firestore.
     *
     * @param payload Os dados da requisição para gerar o QR Code.
     * @return Uma resposta contendo o QR Code gerado e salvo.
     */
    public Response gerarQRCode(QRCodeRequisicao payload) {
        try {
            String eventoId = payload.getEventoId();
            String data = payload.getData();
            String qrCodeBase64 = gerarQRCodeBase64(eventoId + ":" + data);

            QRCodeData qrCodeData = new QRCodeData(eventoId, data, qrCodeBase64);
            DocumentReference docRef = db.collection("qrcode").document();
            WriteResult result = docRef.set(qrCodeData).get();

            // Incluindo o id diretamente no construtor
            QRCodeResposta response = new QRCodeResposta("QR Code gerado e salvo com sucesso.", qrCodeBase64, eventoId, data, result.getUpdateTime().toString(), docRef.getId());

            return Response.ok(response).build();
        } catch (Exception e) {
            logger.error("Erro ao gerar e salvar o QR Code", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("error", "Erro ao gerar e salvar o QR Code: " + e.getMessage()))
                    .build();
        }
    }

    /**
     * Busca QR Codes com base nos critérios fornecidos.
     *
     * @param payload Os dados da requisição para buscar QR Codes.
     * @return Uma resposta contendo a lista de QR Codes encontrados.
     */
    public Response buscarQRCode(QRCodeRequisicao payload) {
        try {
            Query query = db.collection("qrcode");
            if (payload.getEventoId() != null && !payload.getEventoId().isEmpty()) {
                query = query.whereEqualTo("eventoId", payload.getEventoId());
            }
            if (payload.getData() != null && !payload.getData().isEmpty()) {
                query = query.whereEqualTo("data", payload.getData());
            }

            QuerySnapshot querySnapshot = query.get().get();
            List<QRCodeResposta> qrCodesList = new ArrayList<>();
            for (QueryDocumentSnapshot document : querySnapshot.getDocuments()) {
                QRCodeResposta qrCodeResponse = new QRCodeResposta();
                qrCodeResponse.setId(document.getId());
                qrCodeResponse.setEventoId((String) document.get("eventoId"));
                qrCodeResponse.setData((String) document.get("data"));
                qrCodeResponse.setQrCode((String) document.get("qrCode"));
                qrCodesList.add(qrCodeResponse);
            }

            // Ordena a lista de QR Codes pela data
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            // Ordena a lista
            qrCodesList.sort((q1, q2) -> {
                LocalDate date1 = LocalDate.parse(q1.getData(), formatter);
                LocalDate date2 = LocalDate.parse(q2.getData(), formatter);
                return date1.compareTo(date2);
            });

            return Response.ok(qrCodesList).build();
        } catch (Exception e) {
            logger.error("Erro ao buscar QR codes", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("error", "Erro ao buscar QR codes: " + e.getMessage()))
                    .build();
        }
    }

    /**
     * Exclui um QR Code com base no ID fornecido.
     *
     * @param qrCodeId O ID do QR Code a ser excluído.
     * @return Uma resposta indicando o resultado da operação.
     */
    public Response excluirQRCode(String qrCodeId) {
        try {
            db.collection("qrcode").document(qrCodeId).delete().get();
            return Response.ok(Map.of("message", "QR Code excluído com sucesso.")).build();
        } catch (Exception e) {
            logger.error("Erro ao excluir o QR code", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("error", "Erro ao excluir o QR code: " + e.getMessage()))
                    .build();
        }
    }

    /**
     * Gera um QR Code em formato Base64 a partir do texto fornecido.
     *
     * @param text O texto a ser codificado no QR Code.
     * @return Uma string Base64 representando o QR Code gerado.
     * @throws IOException Se ocorrer um erro de entrada/saída.
     * @throws com.google.zxing.WriterException Se ocorrer um erro ao gerar o QR Code.
     */
    private String gerarQRCodeBase64(String text) throws IOException, com.google.zxing.WriterException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, 200, 200);
        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        return Base64.getEncoder().encodeToString(pngOutputStream.toByteArray());
    }
}
