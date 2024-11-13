package sage.models.avaliacao;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Avaliacao {

    @JsonProperty("nota")
    private Double nota;

    @JsonProperty("comentario")
    private String comentario;

    @JsonProperty("userId")
    private String userId;

    @JsonProperty("nome")
    private String nome;

    public Avaliacao() {}

    public Avaliacao(Double nota, String comentario, String userId, String nome) {
        this.nota = nota;
        this.comentario = comentario;
        this.userId = userId;
        this.nome = nome;
    }

    // Getters e Setters
    public Double getNota() {
        return nota;
    }

    public void setNota(Double nota) {
        this.nota = nota;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
