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

@Path("/eventos")
@RolesAllowed("coordenador")
public class EventosController {

    @Inject
    EventosService eventosService;

    @Inject
    JsonWebToken jwt;

    @POST
    @Path("/listar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response listarEventos(@Context SecurityContext securityContext) {
        String cpf = jwt.getSubject(); // Obtém o CPF do token JWT para filtrar eventos
        return eventosService.listarEventos(cpf, securityContext);
    }

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
