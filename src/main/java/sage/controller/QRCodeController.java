package sage.controller;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import sage.models.qrcode.QRCodeRequisicao;
import sage.models.qrcode.QRCodeExclusao;
import sage.services.QRCodeService;

import java.util.Map;
/**
 * Classe controladora para lidar com requisições relacionadas a QR code.
 * Esta classe é segura para ser acessada apenas por usuários com a função "coordenador".
 */
@Path("/qrCode")
@RolesAllowed("coordenador")
public class QRCodeController {

    @Inject
    QRCodeService qrCodeService;

    /**
     * Endpoint para gerar um QR code com base nos dados da requisição fornecidos.
     *
     * @param payload Os dados da requisição contendo o ID do evento e a data.
     * @return Um objeto Response contendo o QR code gerado ou uma mensagem de erro.
     */
    @POST
    @Path("/gerarQRCode")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response gerarQRCode(QRCodeRequisicao payload) {
        if (payload.getEventoId() == null || payload.getData() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "ID do evento e data são obrigatórios."))
                    .build();
        }
        return qrCodeService.gerarQRCode(payload);
    }

    /**
     * Endpoint para buscar um QR code com base nos dados da requisição fornecidos.
     *
     * @param payload Os dados da requisição contendo o ID do evento e a data.
     * @return Um objeto Response contendo o QR code ou uma mensagem de erro.
     */
    @POST
    @Path("/buscarQRCode")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response buscarQRCode(QRCodeRequisicao payload) {
        return qrCodeService.buscarQRCode(payload);
    }

    /**
     * Endpoint para excluir um QR code com base nos dados da requisição fornecidos.
     *
     * @param request Os dados da requisição contendo o ID do QR code.
     * @return Um objeto Response indicando o resultado da operação de exclusão.
     */
    @POST
    @Path("/excluirQRCode")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response excluirQRCode(QRCodeExclusao request) {
        if (request.getQrCodeId() == null || request.getQrCodeId().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("message", "ID do QR code não fornecido."))
                    .build();
        }
        return qrCodeService.excluirQRCode(request.getQrCodeId());
    }
}
