package sage.models.eventos;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
/**
 * Classe que representa o processamento de um evento.
 */
public class ProcessarEvento {

    @JsonProperty("cpf")
    String cpf;

    @JsonProperty("codigo")
    String codigo;

    @JsonProperty("nome")
    String titulo;

    @JsonProperty("descricao")
    String resumo;

    @JsonProperty("periodo")
    String periodo;

    @JsonProperty("tipo")
    String tipo;

    @JsonProperty("financiamento")
    String financiamento;

    @JsonProperty("palavrasChaves")
    String palavrasChaves;

    @JsonProperty("programacao")
    String programacao;

    @JsonProperty("excluido")
    boolean excluido;

    @JsonProperty("cargaHoraria")
    String cargaHoraria;

    @JsonProperty("status")
    String status;

    @JsonProperty("dataProcessamento")
    long dataProcessamento;

    /**
     * Construtor padrão.
     */
    public ProcessarEvento() {
    }

    /**
     * Construtor com parâmetros.
     *
     * @param cpf O CPF do organizador do evento.
     * @param codigo O código do evento.
     * @param titulo O título do evento.
     * @param resumo O resumo do evento.
     * @param periodo O período do evento.
     * @param tipo O tipo do evento.
     * @param financiamento O financiamento do evento.
     * @param palavrasChaves As palavras-chave do evento.
     * @param programacao A programação do evento.
     * @param excluido Se o evento está excluído.
     * @param cargaHoraria A carga horária do evento.
     * @param status O status do evento.
     * @param dataProcessamento A data de processamento do evento.
     */
    public ProcessarEvento(String cpf, String codigo, String titulo, String resumo, String periodo, String tipo, String financiamento, String palavrasChaves, String programacao, boolean excluido, String cargaHoraria, String status, long dataProcessamento) {
        this.cpf = cpf;
        this.codigo = codigo;
        this.titulo = titulo;
        this.resumo = resumo;
        this.periodo = periodo;
        this.tipo = tipo;
        this.financiamento = financiamento;
        this.palavrasChaves = palavrasChaves;
        this.programacao = programacao;
        this.excluido = excluido;
        this.cargaHoraria = cargaHoraria;
        this.status = status;
        this.dataProcessamento = dataProcessamento;
    }

    /**
     * Obtém o CPF do organizador do evento.
     *
     * @return O CPF do organizador do evento.
     */
    public String getCpf() {
        return cpf;
    }

    /**
     * Define o CPF do organizador do evento.
     *
     * @param cpf O novo CPF do organizador do evento.
     */
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    /**
     * Obtém o resumo do evento.
     *
     * @return O resumo do evento.
     */
    public String getResumo() {
        return resumo;
    }

    /**
     * Define o resumo do evento.
     *
     * @param resumo O novo resumo do evento.
     */
    public void setResumo(String resumo) {
        this.resumo = resumo;
    }

    /**
     * Obtém o código do evento.
     *
     * @return O código do evento.
     */
    public String getCodigo() {
        return codigo;
    }

    /**
     * Define o código do evento.
     *
     * @param codigo O novo código do evento.
     */
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    /**
     * Obtém o título do evento.
     *
     * @return O título do evento.
     */
    public String getTitulo() {
        return titulo;
    }
    /**
     * Define o título do evento.
     *
     * @param titulo O novo título do evento.
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    /**
     * Obtém o período do evento.
     *
     * @return O período do evento.
     */
    public String getPeriodo() {
        return periodo;
    }

    /**
     * Define o período do evento.
     *
     * @param periodo O novo período do evento.
     */
    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }


    /**
     * Obtém o tipo do evento.
     *
     * @return O tipo do evento.
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * Define o tipo do evento.
     *
     * @param tipo O novo tipo do evento.
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /**
     * Obtém o financiamento do evento.
     *
     * @return O financiamento do evento.
     */
    public String getFinanciamento() {
        return financiamento;
    }

    /**
     * Define o financiamento do evento.
     *
     * @param financiamento O novo financiamento do evento.
     */
    public void setFinanciamento(String financiamento) {
        this.financiamento = financiamento;
    }

    /**
     * Obtém as palavras-chave do evento.
     *
     * @return As palavras-chave do evento.
     */
    public String getPalavrasChaves() {
        return palavrasChaves;
    }

    /**
     * Define as palavras-chave do evento.
     *
     * @param palavrasChaves As novas palavras-chave do evento.
     */
    public void setPalavrasChaves(String palavrasChaves) {
        this.palavrasChaves = palavrasChaves;
    }

    /**
     * Obtém a programação do evento.
     *
     * @return A programação do evento.
     */
    public String getProgramacao() {
        return programacao;
    }

    /**
     * Define a programação do evento.
     *
     * @param programacao A nova programação do evento.
     */
    public void setProgramacao(String programacao) {
        this.programacao = programacao;
    }

    /**
     * Verifica se o evento está excluído.
     *
     * @return True se o evento está excluído, false caso contrário.
     */
    public boolean isExcluido() {
        return excluido;
    }

    /**
     * Define o status de exclusão do evento.
     *
     * @param excluido O novo status de exclusão do evento.
     */
    public void setExcluido(boolean excluido) {
        this.excluido = excluido;
    }

    /**
     * Obtém o status do evento.
     *
     * @return O status do evento.
     */
    public String getStatus() {return status;}

    /**
     * Define o status do evento.
     *
     * @param status O novo status do evento.
     */
    public void setStatus(String status) {this.status = status;}

    /**
     * Obtém a carga horária do evento.
     *
     * @return A carga horária do evento.
     */
    public String getCargaHoraria() {return cargaHoraria;}

    /**
     * Define a carga horária do evento.
     *
     * @param cargaHoraria A nova carga horária do evento.
     */
    public void setCargaHoraria(String cargaHoraria) {this.cargaHoraria = cargaHoraria;}

    /**
     * Obtém a data de processamento do evento.
     *
     * @return A data de processamento do evento.
     */
    public long getDataProcessamento() {return dataProcessamento;}

    /**
     * Define a data de processamento do evento.
     *
     * @param dataProcessamento A nova data de processamento do evento.
     */
    public void setDataProcessamento(long dataProcessamento) {this.dataProcessamento = dataProcessamento;}
}
