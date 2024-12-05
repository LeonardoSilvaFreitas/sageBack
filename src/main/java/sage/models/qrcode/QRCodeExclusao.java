package sage.models.qrcode;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Classe que representa a exclusão de um QR Code.
 */
public class QRCodeExclusao {

    @JsonProperty("qrCodeId")
    private String qrCodeId;

    // Construtor padrão
    /**
     * Construtor padrão.
     */
    public QRCodeExclusao() {
    }

    // Construtor completo
    /**
     * Construtor completo.
     *
     * @param qrCodeId O ID do QR Code a ser excluído.
     */
    public QRCodeExclusao(String qrCodeId) {
        this.qrCodeId = qrCodeId;
    }

    // Getter e Setter para qrCodeId
    /**
     * Obtém o ID do QR Code.
     *
     * @return O ID do QR Code.
     */
    public String getQrCodeId() {
        return qrCodeId;
    }

    /**
     * Define o ID do QR Code.
     *
     * @param qrCodeId O novo ID do QR Code.
     */
    public void setQrCodeId(String qrCodeId) {
        this.qrCodeId = qrCodeId;
    }
}
