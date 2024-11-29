package sage.services;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import okhttp3.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import sage.models.eventos.ProcessarEvento;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class SigaaTeste {
    private final Map<String, List<Cookie>> cookieStore = new HashMap<>();
    private final OkHttpClient client;
    private static final String SIGAA_URL_LOGIN = "https://sig.ifrs.edu.br/sigaa/logar.do?dispatch=logOn";
    private static final String SIGAA_ESCOLHA_VINCULO = "https://sig.ifrs.edu.br/sigaa/escolhaVinculo.do?dispatch=escolher&vinculo=1";
    private static final String SIGAA_URL_DOCENTE = "https://sig.ifrs.edu.br/sigaa/portais/docente/docente.jsf";
    private static final String SIGAA_URL_LISTAR_ATIVIDADES = "https://sig.ifrs.edu.br/sigaa/extensao/Atividade/lista_minhas_atividades.jsf";
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.82 Safari/537.36";

    private final List<ProcessarEvento> eventos = new ArrayList<>(); // Lista para armazenar os eventos
    private String cpfAtual = ""; // Armazenar o CPF do usuário



    public SigaaTeste() {
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

    public static void main(String[] args) {
        SigaaTeste sigaaTeste = new SigaaTeste();

        try {
            String userCpf = "03320058010"; // Substitua pelo CPF correto
            sigaaTeste.cpfAtual = userCpf; // Armazena o CPF para associar aos eventos
            if (sigaaTeste.performLogin(userCpf, "4460Chd1*")) {
                System.out.println("Login realizado com sucesso!");
                sigaaTeste.performEscolhaVinculo();
                sigaaTeste.performPaginaDocente(userCpf);
            } else {
                System.out.println("Falha no login.");
            }
        } catch (IllegalStateException e) {
            System.err.println("Erro de autorização: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Erro ao executar operações: " + e.getMessage());
        }
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

    //comentário aqui
    private void performPaginaDocente(String cpf) throws IOException {
        Request postRequest = new Request.Builder()
                .url(SIGAA_URL_DOCENTE)
                .header("User-Agent", USER_AGENT)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .build();

        try (Response response = client.newCall(postRequest).execute()) {
            String responseBody = response.body().string();

            if (response.isSuccessful()) {
                // Parse do HTML retornado
                Document document = Jsoup.parse(responseBody);

                // Localizando a mensagem de erro na estrutura: div#container > div#conteudo > table > tbody > tr > td
                Element erroElement = document.selectFirst("div#container > div#conteudo > table > tbody > tr > td");

                if (erroElement != null) {
                    String erroTexto = erroElement.text();

                    // Verifica "Acesso Negado"
                    if (erroTexto.contains("Acesso Negado")) {
                        System.err.println("Erro detectado: Acesso Negado.");
                        throw new IllegalStateException("Acesso Negado: Usuário Não Autorizado.");
                    }

                    // Verifica "Usuário Não Autorizado"
                    if (erroTexto.contains("Usuário Não Autorizado")) {
                        System.err.println("Erro detectado: Usuário Não Autorizado.");
                        throw new IllegalStateException("Acesso Negado: Usuário Não Autorizado.");
                    }
                }

                // Caso não encontre mensagens de erro, processa o HTML
                System.out.println("Requisição para a página docente bem-sucedida!");
                parseHtml(responseBody, cpf);
            } else {
                System.err.println("Falha na requisição POST para a página docente: " + response.code());
                System.err.println("Corpo da Resposta: " + responseBody);
                throw new IOException("Erro ao acessar a página docente.");
            }
        }
    }


    private void parseHtml(String html, String cpf) {
        Document document = Jsoup.parse(html);

        // Extraindo a URL da foto
        Element fotoElement = document.selectFirst("div.foto img#fotoPerfil");
        String fotoUrl = (fotoElement != null) ? fotoElement.attr("src") : "URL da foto não encontrada";
        System.out.println("Foto URL: " + fotoUrl);

        // Extraindo os dados pessoais
        Elements rows = document.select("div#agenda-docente table tbody tr");
        String siape = "", categoria = "", titulacao = "", regimeTrabalho = "", email = "";
        StringBuilder designacoes = new StringBuilder();

        for (Element row : rows) {
            Elements cells = row.select("td");
            if (cells.size() >= 2) {
                String label = cells.get(0).text().trim(); // Corrigido para definir 'label' dentro do loop
                String value = cells.get(1).text().trim();

                switch (label) {
                    case "Siape:":
                        siape = value;
                        break;
                    case "Categoria:":
                        categoria = value;
                        break;
                    case "Titulação:":
                        titulacao = value;
                        break;
                    case "Regime Trabalho:":
                        regimeTrabalho = value;
                        break;
                    case "E-mail:":
                        email = value;
                        break;
                }
            } else if (cells.size() == 1 && row.text().contains("Designações:")) {
                Elements listItems = row.select("ul li");
                for (Element li : listItems) {
                    designacoes.append(li.text().trim()).append(", ");
                }
                if (designacoes.length() > 0) {
                    designacoes.setLength(designacoes.length() - 2); // Remove a última vírgula
                }
            }
        }

        System.out.println("Siape: " + siape);
        System.out.println("Categoria: " + categoria);
        System.out.println("Titulação: " + titulacao);
        System.out.println("Regime Trabalho: " + regimeTrabalho);
        System.out.println("Designações: " + designacoes.toString());
        System.out.println("E-mail: " + email);
    }







}







