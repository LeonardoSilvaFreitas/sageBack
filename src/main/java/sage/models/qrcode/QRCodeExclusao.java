package sage.models.qrcode;

import com.fasterxml.jackson.annotation.JsonProperty;

public class QRCodeExclusao {

    @JsonProperty("qrCodeId")
    private String qrCodeId;

    // Construtor padrão
    public QRCodeExclusao() {
    }

    // Construtor completo
    public QRCodeExclusao(String qrCodeId) {
        this.qrCodeId = qrCodeId;
    }

    // Getter e Setter para qrCodeId
    public String getQrCodeId() {
        return qrCodeId;
    }

    public void setQrCodeId(String qrCodeId) {
        this.qrCodeId = qrCodeId;
    }
}
