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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import sage.models.eventos.ProcessarEvento;
import sage.models.login.Login;
import sage.models.login.TokenResponse;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Serviço de login para autenticação de usuários no sistema através de webscrapping
 */
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

    /**
     * Construtor do LoginService.
     * Inicializa o OkHttpClient com configurações de tempo limite e gerenciamento de cookies.
     */
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

    /**
     * Realiza o login do usuário.
     *
     * @param loginDTO Objeto contendo as credenciais de login.
     * @return TokenResponse contendo o token JWT gerado.
     */
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
                throw new IllegalArgumentException("Usuário e/ou senha inválidos.");
            }
        } catch (IllegalArgumentException e) {
            // Tratamento específico para mensagens de erro do login
            System.err.println("Erro de login: " + e.getMessage());
            throw new RuntimeException("401: " + e.getMessage());
        } catch (IllegalStateException e) {
            // Tratamento específico para erros de acesso negado
            System.err.println("Erro de autorização: " + e.getMessage());
            throw new RuntimeException("403: " + e.getMessage());
        } catch (IOException e) {
            // Tratamento de erros internos
            System.err.println("Erro ao executar operações: " + e.getMessage());
            throw new RuntimeException("Erro interno ao realizar login.", e);
        }
    }

    /**
     * Gera um token JWT para o usuário.
     *
     * @param cpf O CPF do usuário.
     * @return TokenResponse contendo o token JWT gerado.
     */
    private TokenResponse gerarTokenJWT(String cpf) {
        String token = Jwt.issuer("sage-app")
                .subject(cpf)
                .groups("coordenador")
                .expiresIn(86400)  // Token válido por 3 horas
                .sign();

        return new TokenResponse(token);
    }

    /**
     * Realiza o login no sistema SIGAA.
     *
     * @param user O nome de usuário.
     * @param password A senha do usuário.
     * @return true se o login for bem-sucedido, false caso contrário.
     * @throws IOException Se ocorrer um erro durante a requisição.
     */
    private boolean performLogin(String user, String password) throws IOException {
        // Limpa os cookies antes de realizar uma nova tentativa de login
        cookieStore.clear();

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
            if (response.isSuccessful()) {
                if (responseBodyString.contains("Usuário e/ou senha inválidos")) {
                    System.err.println("Usuário e/ou senha inválidos detectados.");
                    throw new IllegalArgumentException("Usuário e/ou senha inválidos.");
                }
                return true; // Login bem-sucedido
            } else {
                System.err.println("Falha no login: " + response.code());
                throw new IOException("Erro no login: código " + response.code());
            }
        }
    }

    /**
     * Realiza a escolha de vínculo no sistema SIGAA.
     *
     * @throws IOException Se ocorrer um erro durante a requisição.
     */
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

    /**
     * Segue um redirecionamento para a nova URL.
     *
     * @param newLocation A nova URL para redirecionamento.
     * @throws IOException Se ocorrer um erro durante a requisição.
     */
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

    /**
     * Realiza a requisição para a página do docente no sistema SIGAA.
     *
     * @param cpf O CPF do usuário.
     * @throws IOException Se ocorrer um erro durante a requisição.
     */
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

    /**
     * Obtém os detalhes de um evento específico.
     *
     * @param id O ID do evento.
     * @param cpf O CPF do usuário.
     * @throws IOException Se ocorrer um erro durante a requisição.
     */
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
                String cargaHoraria = document.select("th:contains(Carga Horária:) + td").text();
                String status = "Sem Status";
                long dataProcessamento = Instant.now().toEpochMilli();


                eventos.add(new ProcessarEvento(cpf, codigo, titulo, resumo, periodo, tipo, fonteFinanciamento, palavrasChave, programacao, false, cargaHoraria, status, dataProcessamento));
            } else {
                System.err.println("Falha na requisição ao pegar evento POST: " + response.code());
                System.err.println("Corpo da Resposta: " + response.body().string());
            }
        }
    }

    /**
     * Analisa o HTML da página e extrai os eventos.
     *
     * @param html O HTML da página.
     * @param cpf O CPF do usuário.
     */
    private void parseHtml(String html, String cpf) {
        Document doc = Jsoup.parse(html);
        Elements rows = doc.select("table.listagem:first-of-type tbody tr");

        if (rows.isEmpty()) {
            // Lista vazia: nenhuma linha encontrada
            System.out.println("Nenhum evento encontrado para o CPF: " + cpf + ". Continuando o processamento...");
            return; // Retorna sem lançar erros
        }

        for (Element row : rows) {
            Elements cells = row.select("td");
            if (cells.size() >= 5) {
                Element imgElement = cells.get(4).selectFirst("img[onclick^=exibirOpcoes]");
                String id = imgElement != null
                        ? imgElement.attr("onclick").replaceAll(".*exibirOpcoes\\((\\d+)\\).*", "$1")
                        : null;

                if (id != null) {
                    try {
                        obterDetalhesEvento(Integer.parseInt(id), cpf);
                    } catch (IOException e) {
                        System.err.println("Erro ao obter detalhes do evento: " + e.getMessage());
                    }
                } else {
                    System.out.println("Nenhum ID de evento encontrado nesta linha. Ignorando.");
                }
            }
        }
    }

    /**
     * Salva os eventos no banco de dados.
     *
     * @param cpf O CPF do usuário.
     */
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
                eventoData.put("cargaHoraria", evento.getCargaHoraria());
                eventoData.put("status", evento.getStatus());
                eventoData.put("dataProcessamento", evento.getDataProcessamento());

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









