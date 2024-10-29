package sage.services;

import com.microsoft.playwright.options.WaitForSelectorState;
import io.smallrye.jwt.build.Jwt;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;
import jakarta.ws.rs.core.MediaType;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.LoadState;
import sage.dto.LoginDTO;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Path("/auth")
public class Login {

    private static final Logger logger = Logger.getLogger(Login.class);

    @Path("/login")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(LoginDTO loginDTO) {

        String cpf = loginDTO.cpf();
        String senha = loginDTO.senha();

        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(false));
            BrowserContext context = browser.newContext();
            Page page = context.newPage();

            page.navigate("https://sighomologa.ifrs.edu.br/admin/login.jsf");
            page.waitForLoadState(LoadState.DOMCONTENTLOADED);
            page.fill("input[name='login']", cpf);
            page.fill("input[name='senha']", senha);
            page.click("input[type='submit']");

            //page.waitForLoadState(LoadState.NETWORKIDLE);

            boolean loginError = page.isVisible("center:has-text(\"Usuário e/ou senha inválidos\")");

            if (loginError) {
                throw new RuntimeException("Usuário e/ou senha inválidos");
            } else {

                page.click("#modulos > ul > li.academico > a");

                //page.navigate("https://sighomologa.ifrs.edu.br/admin/logonSIGAA");

                page.navigate("https://sighomologa.ifrs.edu.br/sigaa/escolhaVinculo.do?dispatch=escolher&vinculo=1");



                // page.navigate("https://sighomologa.ifrs.edu.br/sigaa/portais/docente/docente.jsf");

                page.click("#portais > ul > li.docente.on > a");

                page.hover("span.ThemeOfficeMainFolderText:has-text('Extensão')");

                // Espera o submenu aparecer
                page.waitForSelector("div.ThemeOfficeSubMenu#cmSubMenuID33", new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE));

                // Passa o mouse sobre o item "Planos de Trabalho" para exibir o segundo submenu
                page.hover("div.ThemeOfficeSubMenu#cmSubMenuID33 tr.ThemeOfficeMenuItem:has(td.ThemeOfficeMenuFolderText:has-text('Ações de Extensão'))");

                // Espera o segundo submenu aparecer
                page.waitForSelector("div.ThemeOfficeSubMenu#cmSubMenuID34", new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE));

                page.hover("div.ThemeOfficeSubMenu#cmSubMenuID34 tr.ThemeOfficeMenuItem:has(td.ThemeOfficeMenuFolderText:has-text('Gerenciar Ações'))");

                page.waitForSelector("div.ThemeOfficeSubMenu#cmSubMenuID37", new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE));

                page.click("div.ThemeOfficeSubMenu#cmSubMenuID37 tr.ThemeOfficeMenuItem:has(td.ThemeOfficeMenuItemText:has-text('Listar Minhas Ações'))");


                //page.wait(5000);

                page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("screenshot.png")));

                // Após o scraping bem-sucedido, geramos o token JWT
                String token = Jwt.issuer("sage-app")
                        .subject(cpf)
                        .groups("coordenador")
                        .expiresIn(10800)  // Token válido por 1 hora
                        .sign();

                // Retornando o token JWT no corpo da resposta
                Map<String, String> responseMap = new HashMap<>();
                responseMap.put("token", token);
                return Response.ok(responseMap).build();
            }

        } catch (Exception e) {
            logger.error("Erro ao obter dados do usuário", e);
            return Response.status(Response.Status.UNAUTHORIZED).entity("Login failed").build();
        }
    }


    /*
    @Path("/login")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(LoginDTO loginDTO) {
        // Logando a mensagem
        //logger.info("Endpoint '/hello' foi chamado.");

        String cpf = loginDTO.cpf();
        String senha = loginDTO.senha();

        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(false));
            BrowserContext context = browser.newContext();
            Page page = context.newPage();

            page.navigate("https://sighomologa.ifrs.edu.br/admin/login.jsf");
            page.waitForLoadState(LoadState.DOMCONTENTLOADED);
            page.fill("input[name='login']", cpf);
            page.fill("input[name='senha']", senha);
            page.click("input[type='submit']");

            //page.waitForLoadState(LoadState.NETWORKIDLE);

            boolean loginError = page.isVisible("center:has-text(\"Usuário e/ou senha inválidos\")");

            if (loginError) {
                throw new RuntimeException("Usuário e/ou senha inválidos");
            } else {

                page.click("#modulos > ul > li.academico > a");

                //page.navigate("https://sighomologa.ifrs.edu.br/admin/logonSIGAA");

                page.navigate("https://sighomologa.ifrs.edu.br/sigaa/escolhaVinculo.do?dispatch=escolher&vinculo=1");



               // page.navigate("https://sighomologa.ifrs.edu.br/sigaa/portais/docente/docente.jsf");

                page.click("#portais > ul > li.docente.on > a");

                page.hover("span.ThemeOfficeMainFolderText:has-text('Extensão')");

                // Espera o submenu aparecer
                page.waitForSelector("div.ThemeOfficeSubMenu#cmSubMenuID33", new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE));

                // Passa o mouse sobre o item "Planos de Trabalho" para exibir o segundo submenu
                page.hover("div.ThemeOfficeSubMenu#cmSubMenuID33 tr.ThemeOfficeMenuItem:has(td.ThemeOfficeMenuFolderText:has-text('Ações de Extensão'))");

                // Espera o segundo submenu aparecer
                page.waitForSelector("div.ThemeOfficeSubMenu#cmSubMenuID34", new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE));

                page.hover("div.ThemeOfficeSubMenu#cmSubMenuID34 tr.ThemeOfficeMenuItem:has(td.ThemeOfficeMenuFolderText:has-text('Gerenciar Ações'))");

                page.waitForSelector("div.ThemeOfficeSubMenu#cmSubMenuID37", new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE));

                page.click("div.ThemeOfficeSubMenu#cmSubMenuID37 tr.ThemeOfficeMenuItem:has(td.ThemeOfficeMenuItemText:has-text('Listar Minhas Ações'))");


                //page.wait(5000);

                page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("screenshot.png")));

                // Clica no item "Listar Meus Planos de Trabalho"
                /*








            }
        } catch (Exception e) {
            logger.error( "Erro ao obter dados do usuário", e);
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        return Response.ok(loginDTO).build();

    }
    */
    }
