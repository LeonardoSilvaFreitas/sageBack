package sage.services;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Path("/avaliacoes")
public class Avaliacoes {

    Firestore db = FirestoreClient.getFirestore();

    @POST
    @Path("/listar")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("coordenador")
    public Response listarAvaliacoes(Map<String, String> requestData) {
        try {
            String eventoId = requestData.get("eventoId");

            if (eventoId == null || eventoId.isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("O ID do evento é obrigatório.").build();
            }

            // Referência à coleção de participantes dentro do evento
            CollectionReference participantesCollection = db.collection("eventos")
                    .document(eventoId)
                    .collection("participantes");

            // Busca os documentos dos participantes
            ApiFuture<QuerySnapshot> future = participantesCollection.get();
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();

            // Lista para armazenar as avaliações com os nomes dos usuários
            List<Map<String, Object>> avaliacoesComNomes = new ArrayList<>();

            // Itera sobre as avaliações dos participantes
            for (QueryDocumentSnapshot document : documents) {
                Map<String, Object> avaliacaoData = document.getData();

                // Verifica se os campos "nota" e "comentario" estão presentes
                if (avaliacaoData.containsKey("nota") && avaliacaoData.containsKey("comentario")) {
                    // Obtém o ID do usuário
                    String userId = (String) avaliacaoData.get("userId");

                    // Busca o nome do usuário na coleção "users"
                    DocumentReference userRef = db.collection("users").document(userId);
                    ApiFuture<DocumentSnapshot> userFuture = userRef.get();
                    DocumentSnapshot userDocument = userFuture.get();

                    String nomeUsuario = userDocument.exists() ? userDocument.getString("nome") : "Nome não disponível";

                    // Adiciona o nome do usuário à avaliação
                    Map<String, Object> avaliacaoComNome = new HashMap<>(avaliacaoData);
                    avaliacaoComNome.put("nome", nomeUsuario);

                    // Adiciona à lista de avaliações apenas se os campos existirem
                    avaliacoesComNomes.add(avaliacaoComNome);
                }
            }

            // Retorna a lista de avaliações com os nomes dos participantes
            return Response.ok(avaliacoesComNomes).build();

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao buscar as avaliações: " + e.getMessage())
                    .build();
        }
    }


}
