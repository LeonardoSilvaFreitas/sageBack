package sage.models.relatorio;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Classe que representa o relatório de um participante.
 */
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
    /**
     * Construtor padrão.
     */
    public RelatorioParticipante() {}

    // Construtor completo (com sete parâmetros)
    /**
     * Construtor completo.
     *
     * @param nome O nome do participante.
     * @param cpf O CPF (Cadastro de Pessoas Físicas) do participante.
     * @param email O email do participante.
     * @param condicao A condição do participante.
     * @param formaAcao A forma de ação do participante.
     * @param tituloAcao O título da ação.
     * @param datasConfirmacao As datas de confirmação.
     */
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
    /**
     * Obtém o nome do participante.
     *
     * @return O nome do participante.
     */
    public String getNome() {
        return nome;
    }

    /**
     * Define o nome do participante.
     *
     * @param nome O novo nome do participante.
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Obtém o CPF do participante.
     *
     * @return O CPF do participante.
     */
    public String getCpf() {
        return cpf;
    }

    /**
     * Define o CPF do participante.
     *
     * @param cpf O novo CPF do participante.
     */
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    /**
     * Obtém o email do participante.
     *
     * @return O email do participante.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Define o email do participante.
     *
     * @param email O novo email do participante.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Obtém a condição do participante.
     *
     * @return A condição do participante.
     */
    public String getCondicao() {
        return condicao;
    }

    /**
     * Define a condição do participante.
     *
     * @param condicao A nova condição do participante.
     */
    public void setCondicao(String condicao) {
        this.condicao = condicao;
    }

    /**
     * Obtém a forma de ação do participante.
     *
     * @return A forma de ação do participante.
     */
    public String getFormaAcao() {
        return formaAcao;
    }

    /**
     * Define a forma de ação do participante.
     *
     * @param formaAcao A nova forma de ação do participante.
     */
    public void setFormaAcao(String formaAcao) {
        this.formaAcao = formaAcao;
    }

    /**
     * Obtém o título da ação.
     *
     * @return O título da ação.
     */
    public String getTituloAcao() {
        return tituloAcao;
    }

    /**
     * Define o título da ação.
     *
     * @param tituloAcao O novo título da ação.
     */
    public void setTituloAcao(String tituloAcao) {
        this.tituloAcao = tituloAcao;
    }

    /**
     * Obtém as datas de confirmação.
     *
     * @return As datas de confirmação.
     */
    public List<String> getDatasConfirmacao() {
        return datasConfirmacao;
    }

    /**
     * Define as datas de confirmação.
     *
     * @param datasConfirmacao As novas datas de confirmação.
     */
    public void setDatasConfirmacao(List<String> datasConfirmacao) {
        this.datasConfirmacao = datasConfirmacao;
    }
}
