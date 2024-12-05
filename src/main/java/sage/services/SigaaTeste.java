package sage.services;

import okhttp3.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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
            if (sigaaTeste.performLogin("59629304449", "alexdgis01")) {
                System.out.println("Login realizado com sucesso!");
                sigaaTeste.performEscolhaVinculo();
                sigaaTeste.performPaginaDocente();
            } else {
                System.out.println("Falha no login.");
            }
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
            String responseBodyString = response.body().string();  // Armazene a string do corpo da resposta
            if (response.isSuccessful() && !responseBodyString.contains("Usuário e/ou senha inválidos")) {
                //System.out.println(responseBodyString);
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
        // Primeira requisição: Obtém a página de vínculos
        Request vinculosRequest = new Request.Builder()
                .url("https://sig.ifrs.edu.br/sigaa/vinculos.jsf")
                .get()
                .header("User-Agent", USER_AGENT)
                .build();

        try (Response vinculosResponse = client.newCall(vinculosRequest).execute()) {
            if (vinculosResponse.isSuccessful()) {
                String vinculosBody = vinculosResponse.body().string();
                Document vinculosDocument = Jsoup.parse(vinculosBody);

                // Busca o link do vínculo "Servidor"
                Element servidorLink = vinculosDocument.select("a:contains(Servidor)").first();

                if (servidorLink != null) {
                    String servidorHref = servidorLink.attr("href");
                    String newLocation = "https://sig.ifrs.edu.br" + servidorHref;

                    System.out.println("Redirecionando para o vínculo 'Servidor': " + newLocation);

                    // Segunda requisição: Redireciona para o link encontrado

                    performPaginaDocente();


                } else {
                    System.err.println("Vínculo 'Servidor' não encontrado na página de vínculos.");
                    throw new IllegalStateException("Vínculo 'Servidor' não encontrado.");
                }
            } else {
                System.err.println("Falha ao acessar a página de vínculos: " + vinculosResponse.code());
                throw new IOException("Erro ao acessar a página de vínculos.");
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

    private void performPaginaDocente() throws IOException {
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
                System.out.println("aqui o html: " + response.body().string());
                parseHtml(response.body().string());
            } else {
                System.err.println("Falha na requisição POST: " + response.code());
                System.err.println("Corpo da Resposta: " + response.body().string());
            }
        }
    }

    private void obterDetalhesEvento(int id) throws IOException {
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
                System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

                String html = response.body().string();
                Document document = Jsoup.parse(html);

                // Extrair os dados desejados
                String titulo = document.select("th:contains(Título:) + td").text();
                String periodo = document.select("th:contains(Período de Realização:) + td").text();
                String fonteFinanciamento = document.select("th:contains(Fonte de Financiamento:) + td").text();
                // Resumo
                Element resumoElement = document.selectFirst("b:containsOwn(Resumo:) + br + p");
                String resumo = resumoElement != null ? resumoElement.text() : "Resumo não encontrado";

                // Palavras-Chave
                Element palavrasChaveElement = document.selectFirst("b:containsOwn(Palavras-Chave:) + br");
                String palavrasChave = palavrasChaveElement != null ? palavrasChaveElement.nextSibling().toString().trim() : "Palavras-chave não encontradas";

                // Programação
                Element programacaoElement = document.selectFirst("b:containsOwn(Programação:) + br + p");
                String programacao = programacaoElement != null ? programacaoElement.text() : "Programação não encontrada";


                // Exibir os resultados
                System.out.println("Título: " + titulo);
                System.out.println("Período: " + periodo);
                System.out.println("Fonte de Financiamento: " + fonteFinanciamento);
                System.out.println("Resumo: " + resumo);
                System.out.println("Palavras-chave: " + palavrasChave);
                System.out.println("Programação: " + programacao);
            } else {
                System.err.println("Falha na requisição ao pegar evento POST: " + response.code());
                System.err.println("Corpo da Resposta: " + response.body().string());
            }
        }
    }

    private void parseHtml(String html) {
        Document doc = Jsoup.parse(html);
        // Todas as tabelas
        //Elements rows = doc.select("tbody tr");
        //Primeira tabela
        Elements rows = doc.select("table.listagem:first-of-type tbody tr");

        for (Element row : rows) {
            Elements cells = row.select("td");
            if (cells.size() >= 5) {
                Element imgElement = cells.get(4).selectFirst("img[onclick^=exibirOpcoes]");
                String id = imgElement != null ? imgElement.attr("onclick").replaceAll(".exibirOpcoes\\((\\d+)\\).", "$1") : "ID não encontrado";

                try {
                    obterDetalhesEvento(Integer.parseInt(id));
                    System.out.println("aqui o ID: " + id);
                } catch (IOException e) {
                    System.err.println("Erro ao obter detalhes do evento: " + e.getMessage());
                }
            }
        }
    }
}

/*
private void performEscolhaVinculo() throws IOException {
    Request request = new Request.Builder()
            .url(SIGAA_ESCOLHA_VINCULO)
            .get()
            .header("User-Agent", USER_AGENT)
            .build();

    try (Response response = client.newCall(request).execute()) {
        if (response.body() == null) {
            throw new IOException("Resposta sem corpo ao tentar escolher vínculo.");
        }

        String responseBody = response.body().string();

        if (response.isRedirect()) {
            String newLocation = response.header("Location");
            if (newLocation != null) {
                followRedirect(newLocation);
            }
        } else if (response.isSuccessful()) {
            if (responseBody.contains("Siape:")) {
                System.out.println("Requisição para escolha de vínculo realizada com sucesso e validação 'Siape' confirmada.");
                String newLocation = response.header("Location");
                if (newLocation != null) {
                    followRedirect(newLocation);
                }
            } else {
                System.err.println("Acesso Negado: Usuário não autorizado para escolha de vínculo.");
                throw new IllegalStateException("Acesso Negado: Usuário Não Autorizado.");
            }
        } else {
            System.err.println("Falha na requisição de escolha de vínculo: " + response.code());
            throw new IOException("Erro na requisição de escolha de vínculo. Código: " + response.code());
        }
    }
}

 */