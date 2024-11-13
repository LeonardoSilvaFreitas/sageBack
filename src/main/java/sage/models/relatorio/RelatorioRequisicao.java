package sage.models.relatorio;



import com.fasterxml.jackson.annotation.JsonProperty;

public class RelatorioRequisicao {

    @JsonProperty("eventoId")
    private String eventoId;

    public RelatorioRequisicao() {
    }

    public RelatorioRequisicao(String eventoId) {
        this.eventoId = eventoId;
    }

    public String getEventoId() {
        return eventoId;
    }

    public void setEventoId(String eventoId) {
        this.eventoId = eventoId;
    }
}
