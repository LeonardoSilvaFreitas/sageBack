package sage.models.avaliacao;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Classe que representa uma requisição de avaliação.
 */
public class AvaliacaoRequisicao {

    @JsonProperty("eventoId")
    private String eventoId;

    /**
     * Construtor padrão.
     */
    public AvaliacaoRequisicao() {}

    /**
     * Construtor com parâmetros.
     *
     * @param eventoId O ID do evento para a requisição de avaliação.
     */
    public AvaliacaoRequisicao(String eventoId) {
        this.eventoId = eventoId;
    }

    /**
     * Obtém o ID do evento.
     *
     * @return O ID do evento.
     */
    public String getEventoId() {
        return eventoId;
    }

    /**
     * Define o ID do evento.
     *
     * @param eventoId O novo ID do evento.
     */
    public void setEventoId(String eventoId) {
        this.eventoId = eventoId;
    }
}
