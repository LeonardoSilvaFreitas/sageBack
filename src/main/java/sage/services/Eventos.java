package sage.services;

import com.google.api.core.ApiFuture;

import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;
import sage.models.EditarEvento;
import sage.models.Evento;
import org.eclipse.microprofile.jwt.JsonWebToken;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.core.SecurityContext;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Path("/eventos")
public class Eventos {

    private static final Logger logger = Logger.getLogger(Eventos.class);

    @Inject
    JsonWebToken jwt;  // Injetando o JWT para acessar claims

    @POST
    @Path("/listar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("coordenador")
    public Response listarEventos(@Context SecurityContext securityContext) {
        // Pegar o CPF do token JWT
        String cpf = jwt.getSubject();  // O 'sub' no JWT geralmente é o identificador principal, como CPF ou email

        // Caso precise verificar o usuário autenticado via securityContext
        String userPrincipal = securityContext.getUserPrincipal().getName();

        Firestore db = FirestoreClient.getFirestore();
        List<Evento> eventos = new ArrayList<>();

        try {
            // Buscando eventos no Firestore com base no CPF do usuário extraído do JWT
            ApiFuture<QuerySnapshot> future = db.collection("eventos")
                    .whereEqualTo("cpf", cpf)  // Filtro por CPF do token JWT
                    .get();

            List<QueryDocumentSnapshot> documents = future.get().getDocuments();

            for (QueryDocumentSnapshot document : documents) {
                Evento evento = document.toObject(Evento.class);
                evento.setId(document.getId());  // Adiciona o ID do documento ao DTO

                // Agora, buscamos as reservas para cada evento
                DocumentReference reservasDocRef = db.collection("reservas").document(evento.getId());
                ApiFuture<DocumentSnapshot> reservasFuture = reservasDocRef.get();
                DocumentSnapshot reservasDocument = reservasFuture.get();

                if (reservasDocument.exists()) {
                    Map<String, Object> reservasData = reservasDocument.getData();
                    if (reservasData != null && reservasData.containsKey("reservas")) {
                        List<Map<String, Object>> reservas = (List<Map<String, Object>>) reservasData.get("reservas");

                        // Converte o campo "data" para Instant ISO string (se necessário)
                        List<Map<String, Object>> formattedReservations = reservas.stream()
                                .distinct()
                                .map(reserva -> {
                                    reserva.put("data", Instant.ofEpochMilli((Long) reserva.get("data")).toString());
                                    return reserva;
                                })
                                .collect(Collectors.toList());

                        // Adiciona as reservas ao evento
                        evento.setReservas(formattedReservations);
                    }
                } else {
                    // Se não houver reservas, seta uma lista vazia
                    evento.setReservas(Collections.emptyList());
                }

                eventos.add(evento);
            }

            // Retornando a lista de eventos para o front-end com as reservas incluídas
            return Response.ok(eventos).build();

        } catch (InterruptedException | ExecutionException e) {
            logger.error("Erro ao buscar eventos no Firestore", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }



    @Path("/editar")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("coordenador")
    public Response editarEvento(EditarEvento evento) {
        Firestore db = FirestoreClient.getFirestore();

        // Verifica se o ID do evento não é nulo ou vazio
        if (evento.getId() == null || evento.getId().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("ID do evento é obrigatório").build();
        }

        try {
            // Criar um mapa contendo apenas os campos que serão atualizados
            Map<String, Object> updateFields = new HashMap<>();
            updateFields.put("nome", evento.getNome());
            updateFields.put("descricao", evento.getDescricao());
            updateFields.put("status", evento.getStatus());

            // Usa o ID do evento para acessar o documento correspondente e atualiza apenas os campos necessários
            db.collection("eventos").document(evento.getId())
                    .update(updateFields)
                    .get();

            return Response.ok(evento).build();
        } catch (InterruptedException | ExecutionException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao atualizar o evento: " + e.getMessage()).build();
        }
    }

}
