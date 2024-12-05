package sage.controller;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import org.eclipse.microprofile.jwt.JsonWebToken;
import sage.models.eventos.EditarEvento;
import sage.services.EventosService;

import java.util.Map;

/**
 * Classe controladora para lidar com requisições relacionadas a "Eventos".
 * Esta classe é segura para ser acessada apenas por usuários com a função "coordenador".
 */
@Path("/eventos")
@RolesAllowed("coordenador")
public class EventosController {

    @Inject
    EventosService eventosService;

    @Inject
    JsonWebToken jwt;

    /**
     * Endpoint para listar "Eventos" com base no sujeito do JWT (CPF) e no contexto de segurança.
     *
     * @param securityContext O contexto de segurança da requisição.
     * @return Um objeto Response contendo a lista de "Eventos".
     */
    @POST
    @Path("/listar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response listarEventos(@Context SecurityContext securityContext) {
        String cpf = jwt.getSubject(); // Obtém o CPF do token JWT para filtrar eventos
        return eventosService.listarEventos(cpf, securityContext);
    }

    /**
     * Endpoint para editar um "Evento" com base nos dados do evento fornecidos.
     *
     * @param evento Os dados do evento a serem editados.
     * @return Um objeto Response indicando o resultado da operação de edição.
     */
    @POST
    @Path("/editar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response editarEvento(EditarEvento evento) {
        if (evento.getId() == null || evento.getId().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "ID do evento é obrigatório")).build();
        }
        return eventosService.editarEvento(evento);
    }
}
