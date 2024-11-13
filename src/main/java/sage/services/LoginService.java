package sage.services;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import io.smallrye.jwt.build.Jwt;
import okhttp3.*;
import jakarta.enterprise.context.ApplicationScoped;

import okhttp3.FormBody;

import java.io.FileInputStream;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import sage.models.eventos.ProcessarEvento;
import sage.models.login.Login;
import sage.models.login.TokenResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@ApplicationScoped
public class LoginService {

    private final Map<String, List<Cookie>> cookieStore = new HashMap<>();
    private final OkHttpClient client;
    private static final String SIGAA_URL_LOGIN = "https://sig.ifrs.edu.br/sigaa/logar.do?dispatch=logOn";
    private static final String SIGAA_ESCOLHA_VINCULO = "https://sig.ifrs.edu.br/sigaa/escolhaVinculo.do?dispatch=escolher&vinculo=1";
    private static final String SIGAA_URL_DOCENTE = "https://sig.ifrs.edu.br/sigaa/portais/docente/docente.jsf";
    private static final String SIGAA_URL_LISTAR_ATIVIDADES = "https://sig.ifrs.edu.br/sigaa/extensao/Atividade/lista_minhas_atividades.jsf";
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.82 Safari/537.36";

    private final List<ProcessarEvento> eventos = new ArrayList<>(); // Lista para armazenar os eventos
    private final Firestore db = FirestoreClient.getFirestore();

    public LoginService() {
        this.client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .cookieJar(new CookieJar() {
                    @Override
                    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                        cookieStore.put(url.host(), cookies);
                    }

                    @Override
                    public List<Cookie> loadForRequest(HttpUrl url) {
                        return cookieStore.getOrDefault(url.host(), new ArrayList<>());
                    }
                })
                .followRedirects(true)
                .build();
    }

    public TokenResponse login(Login loginDTO) {
        try {
            String cpf = loginDTO.getCpf();
            String senha = loginDTO.getSenha();

            if (performLogin(cpf, senha)) {
                performEscolhaVinculo();
                performPaginaDocente(cpf);
                salvarEventosNoBanco(cpf);
                System.out.println("Login realizado com sucesso!");

                // Gerar e retornar o token JWT
                return gerarTokenJWT(cpf);
            } else {
                System.out.println("Falha no login.");
                return null;
            }
        } catch (IOException e) {
            System.err.println("Erro ao executar operações: " + e.getMessage());
            return null;
        }
    }

    private TokenResponse gerarTokenJWT(String cpf) {
        String token = Jwt.issuer("sage-app")
                .subject(cpf)
                .groups("coordenador")
                .expiresIn(10800)  // Token válido por 3 horas
                .sign();

        return new TokenResponse(token);
    }

    private boolean performLogin(String user, String password) throws IOException {
        RequestBody formBody = new FormBody.Builder()
                .add("user.login", user)
                .add("user.senha", password)
                .build();

        Request loginRequest = new Request.Builder()
                .url(SIGAA_URL_LOGIN)
                .post(formBody)
                .header("User-Agent", USER_AGENT)
                .build();

        try (Response response = client.newCall(loginRequest).execute()) {
            String responseBodyString = response.body().string();
            if (response.isSuccessful() && !responseBodyString.contains("Usuário e/ou senha inválidos")) {
                return true;
            } else if (response.isSuccessful() && responseBodyString.contains("Usuário e/ou senha inválidos")) {
                System.err.println("Usuário e/ou senha inválidos");
                return false;
            } else {
                System.err.println("Falha no login: " + response.code());
                System.err.println("Corpo da Resposta: " + responseBodyString);
                return false;
            }
        }
    }

    private void performEscolhaVinculo() throws IOException {
        Request request = new Request.Builder()
                .url(SIGAA_ESCOLHA_VINCULO)
                .get()
                .header("User-Agent", USER_AGENT)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isRedirect()) {
                String newLocation = response.header("Location");
                if (newLocation != null) {
                    followRedirect(newLocation);
                }
            } else if (response.isSuccessful()) {
                System.out.println("Requisição para escolha de vínculo realizada com sucesso!");
            } else {
                System.err.println("Falha na requisição de escolha de vínculo: " + response.code());
            }
        }
    }

    private void followRedirect(String newLocation) throws IOException {
        String url = newLocation.startsWith("http") ? newLocation : "https://sig.ifrs.edu.br" + newLocation;
        Request redirectRequest = new Request.Builder()
                .url(url)
                .get()
                .header("User-Agent", USER_AGENT)
                .build();

        try (Response response = client.newCall(redirectRequest).execute()) {
            if (response.isSuccessful()) {
                System.out.println("Redirecionamento concluído com sucesso!");
            } else {
                System.err.println("Falha no redirecionamento: " + response.code());
            }
        }
    }

    private void performPaginaDocente(String cpf) throws IOException {
        RequestBody formBody = new FormBody.Builder()
                .add("menu:j_id_jsp_798026457_3", "menu:j_id_jsp_798026457_3")
                .add("jscook_action", "menu_j_id_jsp_798026457_3_j_id_jsp_798026457_4_menu:A]#{atividadeExtensao.listarMinhasAtividades}")
                .add("javax.faces.ViewState", "j_id2")
                .build();

        Request postRequest = new Request.Builder()
                .url(SIGAA_URL_DOCENTE)
                .post(formBody)
                .header("User-Agent", USER_AGENT)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .build();

        try (Response response = client.newCall(postRequest).execute()) {
            if (response.isSuccessful()) {
                System.out.println("Requisição POST bem-sucedida!");
                parseHtml(response.body().string(), cpf);
            } else {
                System.err.println("Falha na requisição POST: " + response.code());
                System.err.println("Corpo da Resposta: " + response.body().string());
            }
        }
    }

    private void obterDetalhesEvento(int id, String cpf) throws IOException {
        RequestBody formDetalheEvento = new FormBody.Builder()
                .add("formAtividade", "formAtividade")
                .add("javax.faces.ViewState", "j_id3")
                .add("formAtividade:imprimirVersao", "formAtividade:imprimirVersao")
                .add("id", Integer.toString(id))
                .add("print", "true")
                .build();

        Request postRequest = new Request.Builder()
                .url(SIGAA_URL_LISTAR_ATIVIDADES)
                .post(formDetalheEvento)
                .header("User-Agent", USER_AGENT)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .build();

        try (Response response = client.newCall(postRequest).execute()) {
            if (response.isSuccessful()) {
                String html = response.body().string();
                Document document = Jsoup.parse(html);

                String codigo = document.select("th:contains(Código:) + td").text();
                String titulo = document.select("th:contains(Título:) + td").text();
                String periodo = document.select("th:contains(Período de Realização:) + td").text();
                String tipo = document.select("th:contains(Tipo:) + td").text();
                String fonteFinanciamento = document.select("th:contains(Fonte de Financiamento:) + td").text();
                Element resumoElement = document.selectFirst("b:containsOwn(Resumo:) + br + p");
                String resumo = resumoElement != null ? resumoElement.text() : "Resumo não encontrado";
                Element palavrasChaveElement = document.selectFirst("b:containsOwn(Palavras-Chave:) + br");
                String palavrasChave = palavrasChaveElement != null ? palavrasChaveElement.nextSibling().toString().trim() : "Palavras-chave não encontradas";
                Element programacaoElement = document.selectFirst("b:containsOwn(Programação:) + br + p");
                String programacao = programacaoElement != null ? programacaoElement.text() : "Programação não encontrada";

                eventos.add(new ProcessarEvento(cpf, codigo, titulo, resumo, periodo, tipo, fonteFinanciamento, palavrasChave, programacao, false));
            } else {
                System.err.println("Falha na requisição ao pegar evento POST: " + response.code());
                System.err.println("Corpo da Resposta: " + response.body().string());
            }
        }
    }

    private void parseHtml(String html, String cpf) {
        Document doc = Jsoup.parse(html);
        Elements rows = doc.select("table.listagem:first-of-type tbody tr");

        for (Element row : rows) {
            Elements cells = row.select("td");
            if (cells.size() >= 5) {
                Element imgElement = cells.get(4).selectFirst("img[onclick^=exibirOpcoes]");
                String id = imgElement != null ? imgElement.attr("onclick").replaceAll(".*exibirOpcoes\\((\\d+)\\).*", "$1") : "ID não encontrado";

                try {
                    obterDetalhesEvento(Integer.parseInt(id), cpf);
                } catch (IOException e) {
                    System.err.println("Erro ao obter detalhes do evento: " + e.getMessage());
                }
            }
        }
    }

    private void salvarEventosNoBanco(String cpf) {
        try {
            for (ProcessarEvento evento : eventos) {
                // Verificar se o tipo do evento é "EVENTO"
                if (!"EVENTO".equalsIgnoreCase(evento.getTipo())) {
                    continue; // Ignorar eventos que não são do tipo "EVENTO"
                }

                // Verificar se o evento com o mesmo código já existe no banco
                boolean eventoExiste = !db.collection("eventos")
                        .whereEqualTo("codigo", evento.getCodigo())
                        .get()
                        .get()
                        .getDocuments()
                        .isEmpty();

                if (eventoExiste) {
                    System.out.println("Evento com código " + evento.getCodigo() + " já existe. Não será salvo.");
                    continue; // Ignorar a inserção deste evento
                }

                // Criar um mapa para armazenar os campos do evento
                Map<String, Object> eventoData = new HashMap<>();
                eventoData.put("cpf", cpf);
                eventoData.put("codigo", evento.getCodigo());
                eventoData.put("nome", evento.getTitulo());
                eventoData.put("descricao", evento.getResumo());
                eventoData.put("periodo", evento.getPeriodo());
                eventoData.put("tipo", evento.getTipo());
                eventoData.put("financiamento", evento.getFinanciamento());
                eventoData.put("palavrasChaves", evento.getPalavrasChaves());
                eventoData.put("programacao", evento.getProgramacao());
                eventoData.put("excluido", evento.isExcluido());

                // Insere o documento com um ID gerado automaticamente
                DocumentReference docRef = db.collection("eventos").document();
                WriteResult result = docRef.set(eventoData).get(); // Bloqueia até a conclusão para garantir a escrita
                System.out.println("Evento salvo com sucesso com ID: " + docRef.getId() + " em: " + result.getUpdateTime());
            }
        } catch (Exception e) {
            System.err.println("Erro ao salvar eventos no Firebase: " + e.getMessage());
        }
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

