package sage.models.reservas;

import java.util.List;

public class Reserva {
    private String eventoId;
    private String area;
    private Long data;
    private List<String> horarios;
    private String sala;

    // Construtor
    public Reserva(String eventoId, String area, Long data, List<String> horarios, String sala) {
        this.eventoId = eventoId;
        this.area = area;
        this.data = data;
        this.horarios = horarios;
        this.sala = sala;
    }

    // Getters e Setters
    public String getEventoId() {
        return eventoId;
    }

    public void setEventoId(String eventoId) {
        this.eventoId = eventoId;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public Long getData() {
        return data;
    }

    public void setData(Long data) {
        this.data = data;
    }

    public List<String> getHorarios() {
        return horarios;
    }

    public void setHorarios(List<String> horarios) {
        this.horarios = horarios;
    }

    public String getSala() {
        return sala;
    }

    public void setSala(String sala) {
        this.sala = sala;
    }
}
