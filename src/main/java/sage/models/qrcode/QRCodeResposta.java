package sage.models.qrcode;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Classe que representa uma resposta de QR Code.
 */
public class QRCodeResposta {

    @JsonProperty("message")
    private String message;

    @JsonProperty("qrCode")
    private String qrCode;  // QR Code em formato Base64

    @JsonProperty("eventoId")
    private String eventoId;

    @JsonProperty("data")
    private String data;

    @JsonProperty("updateTime")
    private String updateTime;

    @JsonProperty("id")
    private String id;  // ID do documento, útil no endpoint de busca

    // Construtor padrão
    /**
     * Construtor padrão.
     */
    public QRCodeResposta() {
    }

    // Construtor completo
    /**
     * Construtor completo.
     *
     * @param message A mensagem associada à resposta do QR Code.
     * @param qrCode O QR Code em formato Base64.
     * @param eventoId O ID do evento.
     * @param data A data do evento.
     * @param updateTime O horário da última atualização do QR Code.
     * @param id O ID do documento.
     */
    public QRCodeResposta(String message, String qrCode, String eventoId, String data, String updateTime, String id) {
        this.message = message;
        this.qrCode = qrCode;
        this.eventoId = eventoId;
        this.data = data;
        this.updateTime = updateTime;
        this.id = id;
    }

    // Getters e Setters
    /**
     * Obtém a mensagem associada à resposta do QR Code.
     *
     * @return A mensagem.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Define a mensagem associada à resposta do QR Code.
     *
     * @param message A nova mensagem.
     */
    public void setMessage(String message) {
        this.message = message;
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
     * Obtém o horário da última atualização do QR Code.
     *
     * @return O horário da última atualização.
     */
    public String getUpdateTime() {
        return updateTime;
    }

    /**
     * Define o horário da última atualização do QR Code.
     *
     * @param updateTime O novo horário da última atualização.
     */
    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * Obtém o ID do documento.
     *
     * @return O ID do documento.
     */
    public String getId() {
        return id;
    }

    /**
     * Define o ID do documento.
     *
     * @param id O novo ID do documento.
     */
    public void setId(String id) {
        this.id = id;
    }
}
