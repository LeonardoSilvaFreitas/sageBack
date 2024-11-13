package sage.models.reservas;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

public class ReservarSalas {

    @JsonProperty("area")
    private String area;

    @JsonProperty("sala")
    private String sala;

    @JsonProperty("data")
    private Long dataTimestamp;  // Usa Long para armazenar timestamp em milissegundos

    @JsonProperty("horarios")
    private List<String> horarios;

    // Construtores
    public ReservarSalas() {}

    public ReservarSalas(String area, String sala, Instant data, List<String> horarios) {
        this.area = area;
        this.sala = sala;
        this.dataTimestamp = data.toEpochMilli();  // Converte Instant para Long (milissegundos)
        this.horarios = horarios;
    }

    // Getters e Setters

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getSala() {
        return sala;
    }

    public void setSala(String sala) {
        this.sala = sala;
    }

    public Long getData() {
        return dataTimestamp;
    }

    public void setData(Long dataTimestamp) {
        this.dataTimestamp = dataTimestamp;
    }

    // Converte o timestamp (Long) para Instant
    public Instant getInstantData() {
        return Instant.ofEpochMilli(dataTimestamp);
    }

    // Define a data usando Instant e armazena como Long
    public void setInstantData(Instant data) {
        this.dataTimestamp = data.toEpochMilli();
    }

    // Converte o timestamp (Long) para LocalDate
    public LocalDate getLocalDate() {
        return Instant.ofEpochMilli(dataTimestamp).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    // Converte LocalDate para timestamp (Long)
    public void setLocalDate(LocalDate data) {
        this.dataTimestamp = data.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public List<String> getHorarios() {
        return horarios;
    }

    public void setHorarios(List<String> horarios) {
        this.horarios = horarios;
    }

    @Override
    public String toString() {
        return "ReservarSalas{" +
                "area='" + area + '\'' +
                ", sala='" + sala + '\'' +
                ", data=" + dataTimestamp +
                ", horarios=" + horarios +
                '}';
    }
}
