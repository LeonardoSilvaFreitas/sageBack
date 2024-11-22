package sage.services;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import org.jboss.logging.Logger;
import sage.models.eventos.EditarEvento;
import sage.models.eventos.Evento;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@ApplicationScoped
public class EventosService {

    private static final Logger logger = Logger.getLogger(EventosService.class);
    private final Firestore db = FirestoreClient.getFirestore();

    public Response listarEventos(String cpf, SecurityContext securityContext) {
        List<Evento> eventos = new ArrayList<>();

        try {
            // Cria uma nova lista modificável para combinar resultados
            List<QueryDocumentSnapshot> documents = new ArrayList<>(fetchEventosPorCPF(cpf, false));
            documents.addAll(fetchEventosPorCPF(cpf, null)); // Consulta eventos onde `excluido` é nulo

            for (QueryDocumentSnapshot document : documents) {
                Evento evento = document.toObject(Evento.class);
                evento.setId(document.getId());

                // Busca reservas para cada evento
                DocumentReference reservasDocRef = db.collection("reservas").document(evento.getId());
                DocumentSnapshot reservasDocument = reservasDocRef.get().get();

                if (reservasDocument.exists()) {
                    Map<String, Object> reservasData = reservasDocument.getData();
                    if (reservasData != null && reservasData.containsKey("reservas")) {
                        List<Map<String, Object>> reservas = (List<Map<String, Object>>) reservasData.get("reservas");

                        List<Map<String, Object>> formattedReservations = reservas.stream()
                                .distinct()
                                .map(reserva -> {
                                    reserva.put("data", Instant.ofEpochMilli((Long) reserva.get("data")).toString());
                                    return reserva;
                                })
                                .collect(Collectors.toList());

                        evento.setReservas(formattedReservations);
                    }
                } else {
                    evento.setReservas(Collections.emptyList());
                }

                eventos.add(evento);
            }

            return Response.ok(eventos).build();


        } catch (InterruptedException | ExecutionException e) {
            logger.error("Erro ao buscar eventos no Firestore", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }


    public Response editarEvento(EditarEvento evento) {
        try {
            Map<String, Object> updateFields = new HashMap<>();
            updateFields.put("nome", evento.getNome());
            updateFields.put("descricao", evento.getDescricao());
            updateFields.put("codigo", evento.getCodigo());
            updateFields.put("status", evento.getStatus());
            updateFields.put("periodo", evento.getPeriodo());
            updateFields.put("tipo", evento.getTipo());
            updateFields.put("financiamento", evento.getFinanciamento());
            updateFields.put("palavrasChaves", evento.getPalavrasChaves());
            updateFields.put("programacao", evento.getProgramacao());
            updateFields.put("dataLocais", evento.getDataLocais());

            db.collection("eventos").document(evento.getId()).update(updateFields).get();
            return Response.ok(evento).build();

        } catch (InterruptedException | ExecutionException e) {
            logger.error("Erro ao atualizar o evento", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("error", "Erro ao atualizar o evento: " + e.getMessage())).build();
        }
    }

    private List<QueryDocumentSnapshot> fetchEventosPorCPF(String cpf, Object excluidoValue) throws InterruptedException, ExecutionException {
        ApiFuture<QuerySnapshot> future = db.collection("eventos")
                .whereEqualTo("cpf", cpf)
                .whereEqualTo("excluido", excluidoValue)
                .get();
        return future.get().getDocuments();
    }

}
