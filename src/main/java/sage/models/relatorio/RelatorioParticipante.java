package sage.models.relatorio;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;


public class RelatorioParticipante {

    @JsonProperty("nome")
    private String nome;

    @JsonProperty("cpf")
    private String cpf;

    @JsonProperty("email")
    private String email;

    @JsonProperty("condicao")
    private String condicao;

    @JsonProperty("formaAcao")
    private String formaAcao;

    @JsonProperty("tituloAcao")
    private String tituloAcao;

    @JsonProperty("datasConfirmacao")
    private List<String> datasConfirmacao;

    // Construtor padrão
    public RelatorioParticipante() {}

    // Construtor completo (com sete parâmetros)
    public RelatorioParticipante(String nome, String cpf, String email, String condicao, String formaAcao, String tituloAcao, List<String> datasConfirmacao) {
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.condicao = condicao;
        this.formaAcao = formaAcao;
        this.tituloAcao = tituloAcao;
        this.datasConfirmacao = datasConfirmacao;
    }

    // Getters e Setters
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCondicao() {
        return condicao;
    }

    public void setCondicao(String condicao) {
        this.condicao = condicao;
    }

    public String getFormaAcao() {
        return formaAcao;
    }

    public void setFormaAcao(String formaAcao) {
        this.formaAcao = formaAcao;
    }

    public String getTituloAcao() {
        return tituloAcao;
    }

    public void setTituloAcao(String tituloAcao) {
        this.tituloAcao = tituloAcao;
    }

    public List<String> getDatasConfirmacao() {
        return datasConfirmacao;
    }

    public void setDatasConfirmacao(List<String> datasConfirmacao) {
        this.datasConfirmacao = datasConfirmacao;
    }
}
