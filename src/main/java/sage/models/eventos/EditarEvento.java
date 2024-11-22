package sage.models.eventos;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class EditarEvento {

    @JsonProperty("id")
    private String id;

    @JsonProperty("nome")
    private String nome;

    @JsonProperty("descricao")
    private String descricao;

    @JsonProperty("codigo")
    private String codigo;

    @JsonProperty("status")
    private String status;

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

    public EditarEvento() {

    }

    public EditarEvento(String id, String nome, String descricao, String codigo, String status, String periodo, String tipo, String palavrasChaves, String financiamento, String programacao, List<DataSala> dataLocais) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.codigo = codigo;
        this.status = status;
        this.periodo = periodo;
        this.tipo = tipo;
        this.palavrasChaves = palavrasChaves;
        this.financiamento = financiamento;
        this.programacao = programacao;
        this.dataLocais = dataLocais;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
}
