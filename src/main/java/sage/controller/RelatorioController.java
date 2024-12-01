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
        // Verificação de campo obrigatório
        if (requisicao.getEventoId() == null || requisicao.getEventoId().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "ID do evento é obrigatório."))
                    .build();
        }

        try {
            // Tenta gerar o relatório
            Response relatorioResponse = relatorioService.gerarRelatorio(requisicao);

            // Caso o serviço não retorne um Response válido, um erro será gerado
            if (relatorioResponse.getStatus() != Response.Status.OK.getStatusCode()) {
                return Response.status(relatorioResponse.getStatus())
                        .entity(Map.of("error", "Erro ao gerar relatório."))
                        .build();
            }

            // Caso tudo esteja correto, retorna o relatório
            return relatorioResponse;
        } catch (Exception e) {
            // Em caso de exceção, retorna um erro genérico
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("error", "Ocorreu um erro interno ao gerar o relatório.", "details", e.getMessage()))
                    .build();
        }
    }

}
