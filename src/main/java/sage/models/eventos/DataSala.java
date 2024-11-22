package sage.models.eventos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DataSala {

    @JsonProperty("data")
    private String data; // Representa a data no formato desejado (string, timestamp, etc.)

    @JsonProperty("horario")
    private String horario;

    @JsonProperty("local")
    private String local; // Representa a sala

    // Construtor padrão
    public DataSala() {}

    // Construtor com parâmetros


    public DataSala(String data, String horario, String local) {
        this.data = data;
        this.horario = horario;
        this.local = local;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    // Getters e Setters
    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }
}
