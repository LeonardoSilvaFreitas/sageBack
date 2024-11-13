package sage.controller;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import sage.models.avaliacao.AvaliacaoRequisicao;
import sage.services.AvaliacoesService;

@Path("/avaliacoes")
@RolesAllowed("coordenador")
public class AvaliacoesController {

    @Inject
    AvaliacoesService avaliacoesService;

    @POST
    @Path("/listar")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response listarAvaliacoes(AvaliacaoRequisicao requestData) {
        String eventoId = requestData.getEventoId();

        if (eventoId == null || eventoId.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("O ID do evento é obrigatório.").build();
        }

        return avaliacoesService.listarAvaliacoes(eventoId);
    }
}
