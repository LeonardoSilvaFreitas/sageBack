package sage.controller;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import sage.models.excluir.ExcluirRequisicao;
import sage.services.ExcluirService;

import java.util.Map;

@Path("/excluir")
@RolesAllowed("coordenador")
public class ExcluirController {

    @Inject
    ExcluirService excluirService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response excluir(ExcluirRequisicao payload) {
        String eventoId = payload.getEventoId();

        if (eventoId == null || eventoId.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "O ID do evento é obrigatório")).build();
        }

        return excluirService.excluirEvento(eventoId);
    }
}
