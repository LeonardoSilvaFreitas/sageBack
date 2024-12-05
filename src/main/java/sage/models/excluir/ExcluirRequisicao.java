package sage.models.excluir;


import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * Classe que representa uma requisição para excluir um evento.
 */
public class ExcluirRequisicao {

    @JsonProperty("eventoId")
    private String eventoId;

    /**
     * Construtor padrão.
     */
    public ExcluirRequisicao() {}

    /**
     * Construtor com parâmetros.
     *
     * @param eventoId O ID do evento a ser excluído.
     */
    public ExcluirRequisicao(String eventoId) {
        this.eventoId = eventoId;
    }

    /**
     * Obtém o ID do evento a ser excluído.
     *
     * @return O ID do evento a ser excluído.
     */
    public String getEventoId() {
        return eventoId;
    }

    /**
     * Define o ID do evento a ser excluído.
     *
     * @param eventoId O novo ID do evento a ser excluído.
     */
    public void setEventoId(String eventoId) {
        this.eventoId = eventoId;
    }
}
