package sage.models.eventos;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

public class Evento {

    @JsonProperty("id")
    private String id;

    @JsonProperty("nome")
    private String nome;

    @JsonProperty("descricao")
    private String descricao;

    @JsonProperty("status")
    private String status;

    @JsonProperty("reservas")
    private List<Map<String, Object>> reservas; // Campo para armazenar reservas

    // Campos adicionais do ProcessarEvento
    @JsonProperty("cpf")
    private String cpf;

    @JsonProperty("codigo")
    private String codigo;

    @JsonProperty("periodo")
    private String periodo;

    @JsonProperty("tipo")
    private String tipo;

    @JsonProperty("financiamento")
    private String financiamento;

    @JsonProperty("palavrasChaves")
    private String palavrasChaves;

    @JsonProperty("programacao")
    private String programacao;

    @JsonProperty("dataLocais")
    private List<DataSala> dataLocais; // Utilizando a nova classe modelo

    @JsonProperty("cargaHoraria")
    String cargaHoraria;

    public Evento() {
    }

    public Evento(String id, String nome, String descricao, String status, List<Map<String, Object>> reservas, String cpf, String codigo, String periodo, String tipo, String financiamento, String palavrasChaves, String programacao, List<DataSala> dataLocais, String cargaHoraria) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.status = status;
        this.reservas = reservas;
        this.cpf = cpf;
        this.codigo = codigo;
        this.periodo = periodo;
        this.tipo = tipo;
        this.financiamento = financiamento;
        this.palavrasChaves = palavrasChaves;
        this.programacao = programacao;
        this.dataLocais = dataLocais;
        this.cargaHoraria = cargaHoraria;
    }

    // Getters e setters para todos os campos, incluindo os do ProcessarEvento

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Map<String, Object>> getReservas() {
        return reservas;
    }

    public void setReservas(List<Map<String, Object>> reservas) {
        this.reservas = reservas;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
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

    public List<DataSala> getDataLocais() {
        return dataLocais;
    }

    public void setDataLocais(List<DataSala> dataLocais) {
        this.dataLocais = dataLocais;
    }

    public String getCargaHoraria() {return cargaHoraria;}

    public void setCargaHoraria(String cargaHoraria) {this.cargaHoraria = cargaHoraria;}
}
