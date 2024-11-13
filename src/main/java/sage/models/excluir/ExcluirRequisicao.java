package sage.models.excluir;


import com.fasterxml.jackson.annotation.JsonProperty;

public class ExcluirRequisicao {

    @JsonProperty("eventoId")
    private String eventoId;

    public ExcluirRequisicao() {}

    public ExcluirRequisicao(String eventoId) {
        this.eventoId = eventoId;
    }

    public String getEventoId() {
        return eventoId;
    }

    public void setEventoId(String eventoId) {
        this.eventoId = eventoId;
    }
}
