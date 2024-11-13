package sage.models.login;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Login {

    @JsonProperty("cpf")
    private String cpf;

    @JsonProperty("senha")
    private String senha;

    // Construtor padrão
    public Login() {}

    // Construtor completo
    public Login(String cpf, String senha) {
        this.cpf = cpf;
        this.senha = senha;
    }

    // Getters e Setters
    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
