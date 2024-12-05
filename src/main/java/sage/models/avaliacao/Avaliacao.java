package sage.models.avaliacao;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Classe que representa uma Avaliação.
 */
public class Avaliacao {

    @JsonProperty("nota")
    private Double nota;

    @JsonProperty("comentario")
    private String comentario;

    @JsonProperty("userId")
    private String userId;

    @JsonProperty("nome")
    private String nome;

    /**
     * Construtor padrão.
     */
    public Avaliacao() {}

    /**
     * Construtor com parâmetros.
     *
     * @param nota A nota da avaliação.
     * @param comentario O comentário da avaliação.
     * @param userId O ID do usuário que fez a avaliação.
     * @param nome O nome do usuário que fez a avaliação.
     */
    public Avaliacao(Double nota, String comentario, String userId, String nome) {
        this.nota = nota;
        this.comentario = comentario;
        this.userId = userId;
        this.nome = nome;
    }

    // Getters e Setters
    /**
     * Obtém a nota da avaliação.
     *
     * @return A nota da avaliação.
     */
    public Double getNota() {
        return nota;
    }

    /**
     * Define a nota da avaliação.
     *
     * @param nota A nova nota da avaliação.
     */
    public void setNota(Double nota) {
        this.nota = nota;
    }

    /**
     * Obtém o comentário da avaliação.
     *
     * @return O comentário da avaliação.
     */
    public String getComentario() {
        return comentario;
    }

    /**
     * Define o comentário da avaliação.
     *
     * @param comentario O novo comentário da avaliação.
     */
    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    /**
     * Obtém o ID do usuário que fez a avaliação.
     *
     * @return O ID do usuário que fez a avaliação.
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Define o ID do usuário que fez a avaliação.
     *
     * @param userId O novo ID do usuário que fez a avaliação.
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * Obtém o nome do usuário que fez a avaliação.
     *
     * @return O nome do usuário que fez a avaliação.
     */
    public String getNome() {
        return nome;
    }

    /**
     * Define o nome do usuário que fez a avaliação.
     *
     * @param nome O novo nome do usuário que fez a avaliação.
     */
    public void setNome(String nome) {
        this.nome = nome;
    }
}
