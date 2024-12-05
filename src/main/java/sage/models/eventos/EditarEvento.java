package sage.models.eventos;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
/**
 * Classe que representa a edição de um evento.
 */
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

    @JsonProperty("cargaHoraria")
    String cargaHoraria;

    /**
     * Construtor padrão.
     */
    public EditarEvento() {

    }

    /**
     * Construtor com parâmetros.
     *
     * @param id O ID do evento.
     * @param nome O nome do evento.
     * @param descricao A descrição do evento.
     * @param codigo O código do evento.
     * @param status O status do evento.
     * @param periodo O período do evento.
     * @param tipo O tipo do evento.
     * @param financiamento O financiamento do evento.
     * @param palavrasChaves As palavras-chave do evento.
     * @param programacao A programação do evento.
     * @param dataLocais A lista de datas e locais do evento.
     * @param cargaHoraria A carga horária do evento.
     */
    public EditarEvento(String id, String nome, String descricao, String codigo, String status, String periodo, String tipo, String financiamento, String palavrasChaves, String programacao, List<DataSala> dataLocais, String cargaHoraria) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.codigo = codigo;
        this.status = status;
        this.periodo = periodo;
        this.tipo = tipo;
        this.financiamento = financiamento;
        this.palavrasChaves = palavrasChaves;
        this.programacao = programacao;
        this.dataLocais = dataLocais;
        this.cargaHoraria = cargaHoraria;
    }

    /**
     * Obtém o ID do evento.
     *
     * @return O ID do evento.
     */
    public String getId() {
        return id;
    }

    /**
     * Define o ID do evento.
     *
     * @param id O novo ID do evento.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Obtém o nome do evento.
     *
     * @return O nome do evento.
     */
    public String getNome() {
        return nome;
    }

    /**
     * Define o nome do evento.
     *
     * @param nome O novo nome do evento.
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Obtém a descrição do evento.
     *
     * @return A descrição do evento.
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * Define a descrição do evento.
     *
     * @param descricao A nova descrição do evento.
     */
    public void setDescricao(String descricao) {
        this.descricao = descricao;
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
     * Obtém o status do evento.
     *
     * @return O status do evento.
     */
    public String getStatus() {
        return status;
    }

    /**
     * Define o status do evento.
     *
     * @param status O novo status do evento.
     */
    public void setStatus(String status) {
        this.status = status;
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
     * Obtém a lista de datas e locais do evento.
     *
     * @return A lista de datas e locais do evento.
     */
    public List<DataSala> getDataLocais() {
        return dataLocais;
    }

    /**
     * Define a lista de datas e locais do evento.
     *
     * @param dataLocais A nova lista de datas e locais do evento.
     */
    public void setDataLocais(List<DataSala> dataLocais) {
        this.dataLocais = dataLocais;
    }

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
}
