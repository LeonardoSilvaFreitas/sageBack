package sage.models.qrcode;

import com.fasterxml.jackson.annotation.JsonProperty;

public class QRCodeRequisicao {

    @JsonProperty("eventoId")
    private String eventoId;

    @JsonProperty("data")
    private String data;


    // Construtor padrão
    public QRCodeRequisicao() {
    }

    // Construtor completo
    public QRCodeRequisicao(String eventoId, String data, String qrCodeId) {
        this.eventoId = eventoId;
        this.data = data;
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


}
