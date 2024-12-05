package sage.models.login;

/**
 * Classe que representa uma resposta contendo um token.
 */
public class TokenResponse {

    private String token;

    /**
     * Construtor padrão.
     */
    public TokenResponse() {
    }

    /**
     * Construtor com parâmetros.
     *
     * @param token O token.
     */
    public TokenResponse(String token) {
        this.token = token;
    }

    /**
     * Obtém o token.
     *
     * @return O token.
     */
    public String getToken() {
        return token;
    }
}
