package sage.models.qrcode;

import com.fasterxml.jackson.annotation.JsonProperty;

public class QRCodeData {

    @JsonProperty("eventoId")
    private String eventoId;

    @JsonProperty("data")
    private String data;

    @JsonProperty("qrCode")
    private String qrCode;  // QR Code em formato Base64

    // Construtor padrão
    public QRCodeData() {
    }

    // Construtor completo
    public QRCodeData(String eventoId, String data, String qrCode) {
        this.eventoId = eventoId;
        this.data = data;
        this.qrCode = qrCode;
    }

    // Getters e Setters
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

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }
}
