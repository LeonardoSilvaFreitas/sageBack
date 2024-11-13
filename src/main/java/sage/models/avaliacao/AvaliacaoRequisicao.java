package sage.models.avaliacao;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AvaliacaoRequisicao {

    @JsonProperty("eventoId")
    private String eventoId;

    public AvaliacaoRequisicao() {}

    public AvaliacaoRequisicao(String eventoId) {
        this.eventoId = eventoId;
    }

    public String getEventoId() {
        return eventoId;
    }

    public void setEventoId(String eventoId) {
        this.eventoId = eventoId;
    }
}
