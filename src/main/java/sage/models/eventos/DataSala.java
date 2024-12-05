package sage.models.eventos;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Classe que representa os dados de uma sala.
 */
public class DataSala {

    @JsonProperty("data")
    private String data; // Representa a data no formato desejado (string, timestamp, etc.)

    @JsonProperty("horario")
    private String horario;

    @JsonProperty("local")
    private String local; // Representa a sala

    // Construtor padrão
    /**
     * Construtor padrão.
     */
    public DataSala() {}

    /**
     * Construtor com parâmetros.
     *
     * @param data A data do evento.
     * @param horario O horário do evento.
     * @param local A sala do evento.
     */
    public DataSala(String data, String horario, String local) {
        this.data = data;
        this.horario = horario;
        this.local = local;
    }

    /**
     * Obtém o horário do evento.
     *
     * @return O horário do evento.
     */
    public String getHorario() {
        return horario;
    }

    /**
     * Define o horário do evento.
     *
     * @param horario O novo horário do evento.
     */
    public void setHorario(String horario) {
        this.horario = horario;
    }

    // Getters e Setters
    /**
     * Obtém a data do evento.
     *
     * @return A data do evento.
     */
    public String getData() {
        return data;
    }

    /**
     * Define a data do evento.
     *
     * @param data A nova data do evento.
     */
    public void setData(String data) {
        this.data = data;
    }

    /**
     * Obtém a sala do evento.
     *
     * @return A sala do evento.
     */
    public String getLocal() {
        return local;
    }

    /**
     * Define a sala do evento.
     *
     * @param local A nova sala do evento.
     */
    public void setLocal(String local) {
        this.local = local;
    }
}
