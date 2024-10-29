package sage.services;

import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.resteasy.spi.WriterException;
import sage.models.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Path("/qrCode")
public class QRCode {

    Firestore db = FirestoreClient.getFirestore();

    @Inject
    JsonWebToken jwt;  // Injetando o JWT para acessar claims

    @POST
    @Path("/gerarQRCode")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response gerarQRCode(Map<String, Object> payload) {
        // Valida o payload recebido
        String eventoId = (String) payload.get("eventoId");
        String data = (String) payload.get("data");

        if (eventoId == null || data == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("ID do evento e data são obrigatórios.")
                    .build();
        }

        try {
            // Gera o QR code em Base64
            String qrCodeBase64 = gerarQRCodeBase64(eventoId + ":" + data);

            // Salva no Firestore
            Map<String, Object> qrCodeData = new HashMap<>();
            qrCodeData.put("eventoId", eventoId);
            qrCodeData.put("data", data);
            qrCodeData.put("qrCode", qrCodeBase64);

            // Referência para a coleção 'qrcode'
            DocumentReference docRef = db.collection("qrcode").document();

            // Salva os dados no Firestore
            WriteResult result = docRef.set(qrCodeData).get();

            // Retorna a resposta com o QR code gerado
            Map<String, Object> response = new HashMap<>();
            response.put("message", "QR Code gerado e salvo com sucesso.");
            response.put("qrCode", qrCodeBase64);
            response.put("eventoId", eventoId);
            response.put("data", data);
            response.put("updateTime", result.getUpdateTime().toString());

            return Response.ok(response).build();
        } catch (WriterException | IOException | InterruptedException | com.google.zxing.WriterException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao gerar e salvar o QR Code: " + e.getMessage())
                    .build();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    // Método para gerar o QR Code em Base64
    private String gerarQRCodeBase64(String text) throws WriterException, IOException, com.google.zxing.WriterException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, 200, 200);

        // Converte a matriz do QR code para imagem PNG
        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        byte[] pngData = pngOutputStream.toByteArray();

        // Converte para Base64
        return Base64.getEncoder().encodeToString(pngData);
    }

    @POST
    @Path("/buscarQRCode")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response buscarQRCode(Map<String, Object> payload) {
        try {
            // Extrai os filtros do payload
            String eventoId = (String) payload.get("eventoId");
            String data = (String) payload.get("data");

            // Inicia a query na coleção 'qrcode'
            Query query = db.collection("qrcode");

            // Adiciona filtros conforme o payload
            if (eventoId != null && !eventoId.isEmpty()) {
                query = query.whereEqualTo("eventoId", eventoId);
            }
            if (data != null && !data.isEmpty()) {
                query = query.whereEqualTo("data", data);
            }

            // Executa a query e coleta os resultados
            QuerySnapshot querySnapshot = query.get().get();
            List<Map<String, Object>> qrCodesList = new ArrayList<>();

            // Itera sobre os documentos e adiciona à lista de QR codes
            for (QueryDocumentSnapshot document : querySnapshot.getDocuments()) {
                Map<String, Object> qrCodeData = document.getData();
                qrCodeData.put("id", document.getId()); // Adiciona o ID do documento
                qrCodesList.add(qrCodeData);
            }

            // Ordena a lista de QR codes pela data
            Collections.sort(qrCodesList, (qr1, qr2) -> {
                String data1 = (String) qr1.get("data");
                String data2 = (String) qr2.get("data");
                return data1.compareTo(data2); // Ordenação ascendente
            });

            // Retorna a lista de QR codes filtrados e ordenados como resposta
            return Response.ok(qrCodesList).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao buscar QR codes: " + e.getMessage())
                    .build();
        }
    }


    @POST
    @Path("/excluirQRCode")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response excluirQRCode(Map<String, Object> request) {
        // Obtém o ID do QR code do JSON enviado
        String qrCodeId = (String) request.get("qrCodeId");

        // Valida se o ID foi enviado
        if (qrCodeId == null || qrCodeId.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("message", "ID do QR code não fornecido."))
                    .build();
        }

        try {
            // Exclui o QR code do Firestore usando o ID
            db.collection("qrcode").document(qrCodeId).delete().get();

            return Response.ok(Map.of("message", "QR Code excluído com sucesso.")).build();

        } catch (Exception e) {
            e.printStackTrace();

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("message", "Erro ao excluir o QR code."))
                    .build();
        }
    }

}

