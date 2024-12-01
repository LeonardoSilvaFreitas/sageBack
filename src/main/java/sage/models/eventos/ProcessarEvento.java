package sage.models.eventos;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

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

    public ProcessarEvento() {
    }

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

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getResumo() {
        return resumo;
    }

    public void setResumo(String resumo) {
        this.resumo = resumo;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getFinanciamento() {
        return financiamento;
    }

    public void setFinanciamento(String financiamento) {
        this.financiamento = financiamento;
    }

    public String getPalavrasChaves() {
        return palavrasChaves;
    }

    public void setPalavrasChaves(String palavrasChaves) {
        this.palavrasChaves = palavrasChaves;
    }

    public String getProgramacao() {
        return programacao;
    }

    public void setProgramacao(String programacao) {
        this.programacao = programacao;
    }

    public boolean isExcluido() {
        return excluido;
    }

    public void setExcluido(boolean excluido) {
        this.excluido = excluido;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCargaHoraria() {
        return cargaHoraria;
    }

    public void setCargaHoraria(String cargaHoraria) {
        this.cargaHoraria = cargaHoraria;
    }

    public long getDataProcessamento() {
        return dataProcessamento;
    }

    public void setDataProcessamento(long dataProcessamento) {
        this.dataProcessamento = dataProcessamento;
    }
}
