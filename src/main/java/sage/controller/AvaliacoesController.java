package sage.controller;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import sage.models.avaliacao.AvaliacaoRequisicao;
import sage.services.AvaliacoesService;
/**
 * Classe controladora para lidar com requisições relacionadas a "Avaliacoes".
 * Esta classe é segura para ser acessada apenas por usuários com a função "coordenador".
 */

@Path("/avaliacoes")
@RolesAllowed("coordenador")
public class AvaliacoesController {

    @Inject
    AvaliacoesService avaliacoesService;

    /**
     * Endpoint para listar "Avaliacoes" com base nos dados da requisição fornecidos.
     *
     * @param requestData Os dados da requisição contendo o ID do evento.
     * @return Um objeto Response contendo a lista de "Avaliacoes" ou uma mensagem de erro se o ID do evento estiver ausente.
     */

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
