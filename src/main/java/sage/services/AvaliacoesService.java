package sage.services;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Response;
import sage.models.avaliacao.Avaliacao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@ApplicationScoped
public class AvaliacoesService {

    private final Firestore db = FirestoreClient.getFirestore();

    public Response listarAvaliacoes(String eventoId) {
        List<Avaliacao> avaliacoesComNomes = new ArrayList<>();

        try {
            // Referência à coleção de participantes
            CollectionReference participantesCollection = db.collection("eventos")
                    .document(eventoId)
                    .collection("participantes");

            // Busca os documentos dos participantes
            ApiFuture<QuerySnapshot> future = participantesCollection.get();
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();

            for (QueryDocumentSnapshot document : documents) {
                Map<String, Object> avaliacaoData = document.getData();

                // Verifica se os campos "nota" e "comentario" estão presentes
                if (avaliacaoData.containsKey("nota") && avaliacaoData.containsKey("comentario")) {
                    String userId = (String) avaliacaoData.get("userId");
                    Double nota = ((Number) avaliacaoData.get("nota")).doubleValue();
                    String comentario = (String) avaliacaoData.get("comentario");

                    // Busca o nome do usuário na coleção "users"
                    DocumentReference userRef = db.collection("users").document(userId);
                    DocumentSnapshot userDocument = userRef.get().get();

                    String nomeUsuario = userDocument.exists() ? userDocument.getString("nome") : "Nome não disponível";

                    // Cria o objeto Avaliacao e adiciona à lista
                    Avaliacao avaliacao = new Avaliacao(nota, comentario, userId, nomeUsuario);
                    avaliacoesComNomes.add(avaliacao);
                }
            }

            return Response.ok(avaliacoesComNomes).build();

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao buscar as avaliações: " + e.getMessage())
                    .build();
        }
    }
}
