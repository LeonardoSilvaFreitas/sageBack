package sage.controller;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import sage.models.relatorio.RelatorioRequisicao;
import sage.services.RelatorioService;

import java.util.Map;

@Path("/relatorio")
@RolesAllowed("coordenador")
public class RelatorioController {

    @Inject
    RelatorioService relatorioService;

    @POST
    @Path("/gerarRelatorio")
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response gerarRelatorio(RelatorioRequisicao requisicao) {
        if (requisicao.getEventoId() == null || requisicao.getEventoId().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "ID do evento é obrigatório."))
                    .build();
        }
        return relatorioService.gerarRelatorio(requisicao);
    }
}
