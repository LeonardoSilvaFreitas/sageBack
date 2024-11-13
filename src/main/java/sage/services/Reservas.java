package sage.services;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;
import sage.models.reservas.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;

@Path("/reservas")
public class Reservas {

    Firestore db = FirestoreClient.getFirestore();

    @Inject
    JsonWebToken jwt;  // Injetando o JWT para acessar claims


    @POST
    @Path("/salas-horarios")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listarSalasComHorarios() {
        // Cria uma instância de AreasComHorariosResponse para acessar os locais
        AreasComHorariosResponse areasComHorarios = new AreasComHorariosResponse(null, null);

        // Obtém as áreas definidas no método getLocais
        List<Area> areas = areasComHorarios.getLocais();

        // Criando a resposta que contém as áreas e os horários comuns
        AreasComHorariosResponse response = new AreasComHorariosResponse(areas, Horarios.getHorarios());

        return Response.ok(response).build();
    }

    @POST
    @Path("/verificarReservas")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("coordenador")
    public Response verificarReservas(Map<String, String> requestData) {
        try {
            String eventoId = requestData.get("eventoId");

            // Referência à coleção 'reservas' e busca por todos os documentos que possuem o eventoId
            CollectionReference reservasCollectionRef = db.collection("reservas");
            ApiFuture<QuerySnapshot> future = reservasCollectionRef.whereEqualTo("eventoId", eventoId).get();
            QuerySnapshot reservasSnapshot = future.get();

            // Verifica se há reservas para o evento
            if (reservasSnapshot.isEmpty()) {
                System.out.println("Reservas não encontradas para o evento: " + eventoId);
                return Response.status(Response.Status.NOT_FOUND).entity("Reservas não encontradas para o evento: " + eventoId).build();
            }

            // Obtem a lista de documentos de reservas para o evento
            List<Map<String, Object>> reservasList = new ArrayList<>();
            for (DocumentSnapshot reservaDoc : reservasSnapshot.getDocuments()) {
                Map<String, Object> reservaData = reservaDoc.getData();
                if (reservaData != null) {
                    // Converte o campo "data" para Instant ISO string
                    reservaData.put("data", Instant.ofEpochMilli((Long) reservaData.get("data")).toString());

                    // Adiciona o ID da reserva
                    String reservaId = reservaDoc.getId();
                    reservaData.put("reservaId", reservaId);

                    reservasList.add(reservaData);
                }
            }

            // Ordena a lista de reservas pela data (campo 'data' convertido para Instant)
            reservasList.sort(Comparator.comparing(reserva -> Instant.parse((String) reserva.get("data"))));

            return Response.ok(reservasList).build();

        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao verificar as reservas: " + e.getMessage())
                    .build();
        }
    }




    @POST
    @Path("/reservasOcupadas")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("coordenador")
    public Response reservasOcupadas() {
        try {
            // Referência à coleção de reservas
            CollectionReference reservasCollection = db.collection("reservas");

            // Faz uma consulta para obter todos os documentos da coleção de reservas
            ApiFuture<QuerySnapshot> future = reservasCollection.get();
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();

            List<Map<String, Object>> todasReservas = new ArrayList<>();

            // Itera sobre todos os documentos da coleção de reservas
            for (QueryDocumentSnapshot document : documents) {
                Map<String, Object> reservaData = document.getData();

                // Verifica se o campo "data" está presente e converte para o formato ISO string (Instant)
                if (reservaData.get("data") != null) {
                    Long timestamp = (Long) reservaData.get("data");
                    reservaData.put("data", Instant.ofEpochMilli(timestamp).toString());
                }

                // Adiciona o ID da reserva aos dados coletados
                reservaData.put("reservaId", document.getId());

                // Log detalhado da reserva encontrada
                System.out.println("Reserva encontrada: " + document.getId() + " com dados: " + reservaData);

                // Adiciona a reserva à lista principal
                todasReservas.add(reservaData);
            }

            // Verifica se alguma reserva foi adicionada
            if (todasReservas.isEmpty()) {
                System.out.println("Nenhuma reserva ocupada encontrada.");
                return Response.ok(Collections.emptyList()).build();
            }

            // Retorna todas as reservas ocupadas
            return Response.ok(todasReservas).build();

        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao verificar as reservas ocupadas: " + e.getMessage())
                    .build();
        }
    }


    @POST
    @Path("/excluirReserva")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("coordenador")
    public Response excluirReserva(Map<String, Object> dados) {
        try {
            String reservaId = (String) dados.get("reservaId");

            if (reservaId == null || reservaId.isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Reserva ID inválido.")
                        .build();
            }

            // Referência ao Firestore
            Firestore db = FirestoreClient.getFirestore();

            // Referência ao documento da reserva que será excluído
            DocumentReference reservaDocRef = db.collection("reservas").document(reservaId);

            // Verifica se o documento existe antes de tentar excluí-lo
            ApiFuture<DocumentSnapshot> future = reservaDocRef.get();
            DocumentSnapshot document = future.get();

            if (!document.exists()) {
                return Response.status(Response.Status.NO_CONTENT)
                        .entity("Reserva não encontrada ou já excluída.")
                        .build();
            }

            // Exclui o documento da reserva
            ApiFuture<WriteResult> writeResult = reservaDocRef.delete();
            writeResult.get(); // Aguarda a conclusão da exclusão

            return Response.ok("Reserva excluída com sucesso").build();

        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao excluir a reserva: " + e.getMessage())
                    .build();
        }
    }


    @POST
    @Path("/reservar")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("coordenador")
    public Response reservar(ReservasRequest reservasRequest) {
        try {
            String eventoId = reservasRequest.getEventoId();
            List<ReservarSalas> novasReservas = reservasRequest.getReservas();

            // Itera sobre as novas reservas
            for (ReservarSalas novaReserva : novasReservas) {
                boolean reservaExistente = false;

                // Converte o timestamp da nova reserva para LocalDate
                LocalDate novaReservaDia = Instant.ofEpochMilli(novaReserva.getData())
                        .atZone(ZoneId.systemDefault()).toLocalDate();

                // Busca todas as reservas existentes na coleção 'reservas' para verificar conflitos
                CollectionReference reservasCollection = db.collection("reservas");
                ApiFuture<QuerySnapshot> future = reservasCollection.whereEqualTo("eventoId", eventoId).get();
                List<QueryDocumentSnapshot> reservasExistentes = future.get().getDocuments();

                for (QueryDocumentSnapshot reservaDoc : reservasExistentes) {
                    LocalDate reservaAtualDia = Instant.ofEpochMilli((Long) reservaDoc.get("data"))
                            .atZone(ZoneId.systemDefault()).toLocalDate();

                    // Verifica se a sala e a data (apenas dia) são iguais
                    if (reservaDoc.get("sala").equals(novaReserva.getSala()) && reservaAtualDia.equals(novaReservaDia)) {
                        List<String> horariosExistentes = (List<String>) reservaDoc.get("horarios");
                        List<String> novosHorarios = novaReserva.getHorarios();

                        // Adiciona novos horários sem duplicar
                        for (String novoHorario : novosHorarios) {
                            if (!horariosExistentes.contains(novoHorario)) {
                                horariosExistentes.add(novoHorario);
                            }
                        }

                        // Ordena os horários em ordem cronológica antes de atualizar
                        horariosExistentes.sort(Comparator.comparing(horario -> {
                            // Extrai o horário inicial "HH:mm - HH:mm" e converte para LocalTime
                            return LocalTime.parse(horario.split(" - ")[0]);
                        }));

                        // Atualiza a reserva existente com os horários ordenados
                        reservaDoc.getReference().update("horarios", horariosExistentes);
                        reservaExistente = true;
                        break; // Encerra ao encontrar uma reserva correspondente
                    }
                }

                if (!reservaExistente) {
                    // Ordena os horários da nova reserva antes de salvar
                    List<String> horariosOrdenados = novaReserva.getHorarios();
                    horariosOrdenados.sort(Comparator.comparing(horario -> {
                        // Extrai o horário inicial "HH:mm - HH:mm" e converte para LocalTime
                        return LocalTime.parse(horario.split(" - ")[0]);
                    }));

                    // Cria uma nova reserva com os horários ordenados
                    Map<String, Object> novaReservaData = new HashMap<>();
                    novaReservaData.put("eventoId", eventoId);
                    novaReservaData.put("area", novaReserva.getArea());
                    novaReservaData.put("sala", novaReserva.getSala());
                    novaReservaData.put("data", novaReserva.getData());
                    novaReservaData.put("horarios", horariosOrdenados);

                    // Adiciona a nova reserva à coleção 'reservas' com ID único
                    db.collection("reservas").add(novaReservaData).get();
                }
            }

            return Response.ok("Reservas criadas/atualizadas com sucesso.").build();

        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao processar reservas: " + e.getMessage())
                    .build();
        }
    }





}
