package sage.models.qrcode;

import com.fasterxml.jackson.annotation.JsonProperty;

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
    public QRCodeResposta() {
    }

    // Construtor completo
    public QRCodeResposta(String message, String qrCode, String eventoId, String data, String updateTime, String id) {
        this.message = message;
        this.qrCode = qrCode;
        this.eventoId = eventoId;
        this.data = data;
        this.updateTime = updateTime;
        this.id = id;
    }

    // Getters e Setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getEventoId() {
        return eventoId;
    }

    public void setEventoId(String eventoId) {
        this.eventoId = eventoId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
