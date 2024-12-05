package sage.services;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;

import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Serviço para exclusão de eventos.
 */
@ApplicationScoped
public class ExcluirService {

    private static final Logger logger = Logger.getLogger(ExcluirService.class);
    private final Firestore db = FirestoreClient.getFirestore();

    /**
     * Marca um evento como com uma exclusão lógica, atualizando o campo "excluido" para true.
     *
     * @param eventoId O ID do evento a ser excluído.
     * @return Uma resposta indicando o resultado da operação.
     */
    public Response excluirEvento(String eventoId) {
        try {
            // Referência ao documento do evento no Firestore
            DocumentReference eventoRef = db.collection("eventos").document(eventoId);

            // Atualiza o campo "excluido" para true
            ApiFuture<WriteResult> writeResult = eventoRef.update("excluido", true);

            // Aguarda a operação de atualização ser concluída
            writeResult.get();

            return Response.ok(Map.of("message", "Evento marcado como excluído")).build();
        } catch (InterruptedException | ExecutionException e) {
            logger.error("Erro ao marcar o evento como excluído", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("error", "Erro ao marcar o evento como excluído"))
                    .build();
        }
    }
}
