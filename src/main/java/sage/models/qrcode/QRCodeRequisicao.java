package sage.models.qrcode;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Classe que representa uma requisição de QR Code.
 */
public class QRCodeRequisicao {

    @JsonProperty("eventoId")
    private String eventoId;

    @JsonProperty("data")
    private String data;


    // Construtor padrão
    /**
     * Construtor padrão.
     */
    public QRCodeRequisicao() {
    }

    // Construtor completo
    /**
     * Construtor completo.
     *
     * @param eventoId O ID do evento.
     * @param data A data do evento.
     */
    public QRCodeRequisicao(String eventoId, String data, String qrCodeId) {
        this.eventoId = eventoId;
        this.data = data;
    }

    // Getters e Setters
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


}
