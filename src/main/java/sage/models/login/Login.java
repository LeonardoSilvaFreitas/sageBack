package sage.models.login;

import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * Classe que representa uma requisição de login.
 */
public class Login {

    @JsonProperty("cpf")
    private String cpf;

    @JsonProperty("senha")
    private String senha;

    // Construtor padrão
    /**
     * Construtor padrão.
     */
    public Login() {}

    // Construtor completo
    /**
     * Construtor completo.
     *
     * @param cpf O CPF do usuário.
     * @param senha A senha do usuário.
     */
    public Login(String cpf, String senha) {
        this.cpf = cpf;
        this.senha = senha;
    }

    // Getters e Setters
    /**
     * Obtém o CPF do usuário.
     *
     * @return O CPF do usuário.
     */
    public String getCpf() {
        return cpf;
    }

    /**
     * Define o CPF do usuário.
     *
     * @param cpf O novo CPF do usuário.
     */
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    /**
     * Obtém a senha do usuário.
     *
     * @return A senha do usuário.
     */
    public String getSenha() {
        return senha;
    }

    /**
     * Define a senha do usuário.
     *
     * @param senha A nova senha do usuário.
     */
    public void setSenha(String senha) {
        this.senha = senha;
    }
}
