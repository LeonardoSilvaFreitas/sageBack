package sage.models.reservas;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ReservasRequest {

    @JsonProperty("eventoId")
    private String eventoId;

    @JsonProperty("reservas")
    private List<ReservarSalas> reservas;

    // Construtores
    public ReservasRequest() {}

    public ReservasRequest(String eventoId, List<ReservarSalas> reservas) {
        this.eventoId = eventoId;
        this.reservas = reservas;
    }

    // Getters e Setters
    public String getEventoId() {
        return eventoId;
    }

    public void setEventoId(String eventoId) {
        this.eventoId = eventoId;
    }

    public List<ReservarSalas> getReservas() {
        return reservas;
    }

    public void setReservas(List<ReservarSalas> reservas) {
        this.reservas = reservas;
    }
}
