package sage.models.qrcode;

import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * Classe que representa os dados de um QR Code.
 */
public class QRCodeData {

    @JsonProperty("eventoId")
    private String eventoId;

    @JsonProperty("data")
    private String data;

    @JsonProperty("qrCode")
    private String qrCode;  // QR Code em formato Base64

    // Construtor padrão
    /**
     * Construtor padrão.
     */
    public QRCodeData() {
    }

    // Construtor completo
    /**
     * Construtor completo.
     *
     * @param eventoId O ID do evento.
     * @param data A data do evento.
     * @param qrCode O QR Code em formato Base64.
     */
    public QRCodeData(String eventoId, String data, String qrCode) {
        this.eventoId = eventoId;
        this.data = data;
        this.qrCode = qrCode;
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

    /**
     * Obtém o QR Code em formato Base64.
     *
     * @return O QR Code em formato Base64.
     */
    public String getQrCode() {
        return qrCode;
    }

    /**
     * Define o QR Code em formato Base64.
     *
     * @param qrCode O novo QR Code em formato Base64.
     */
    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }
}
