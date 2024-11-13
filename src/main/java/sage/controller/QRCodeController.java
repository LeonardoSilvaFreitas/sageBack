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

@Path("/qrCode")
@RolesAllowed("coordenador")
public class QRCodeController {

    @Inject
    QRCodeService qrCodeService;

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

    @POST
    @Path("/buscarQRCode")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response buscarQRCode(QRCodeRequisicao payload) {
        return qrCodeService.buscarQRCode(payload);
    }

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
