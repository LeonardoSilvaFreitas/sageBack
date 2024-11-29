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
    LoginService loginService;

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(Login loginDTO) {
        Map<String, String> response = new HashMap<>();

        try {
            var tokenResponse = loginService.login(loginDTO);
            return Response.status(Response.Status.OK)
                    .entity(tokenResponse)
                    .build();
        } catch (RuntimeException e) {
            String errorMessage = e.getMessage();
            if (errorMessage.startsWith("403:")) {
                response.put("error", errorMessage.substring(5)); // Remove "403: "
                return Response.status(Response.Status.FORBIDDEN)
                        .entity(response)
                        .build();
            } else if (errorMessage.startsWith("401:")) {
                response.put("error", errorMessage.substring(5)); // Remove "401: "
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity(response)
                        .build();
            } else {
                response.put("error", "Erro ao realizar login.");
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity(response)
                        .build();
            }
        }
    }
}


