package sage.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import sage.models.login.Login;
import sage.services.LoginService;

import java.util.HashMap;
import java.util.Map;

@Path("/auth")
public class LoginController {

    @Inject
    LoginService loginService;// Injeta o serviço de login


    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(Login loginDTO) {

        Map<String, String> response = new HashMap<>();

        try {
            return Response.status(Response.Status.OK)
                    .entity(loginService.login(loginDTO))
                    .build();
        }catch (IllegalArgumentException e) {
            response.put("error", e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(response).build();
        }
        catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao realizar login: " + e.getMessage()).build();
        }
    }
}
