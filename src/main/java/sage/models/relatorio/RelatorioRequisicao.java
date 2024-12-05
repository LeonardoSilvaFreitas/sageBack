package sage.models.relatorio;



import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Classe que representa uma requisição de relatório.
 */
public class RelatorioRequisicao {

    @JsonProperty("eventoId")
    private String eventoId;

    /**
     * Construtor padrão.
     */
    public RelatorioRequisicao() {
    }

    /**
     * Construtor completo.
     *
     * @param eventoId O ID do evento.
     */
    public RelatorioRequisicao(String eventoId) {
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
