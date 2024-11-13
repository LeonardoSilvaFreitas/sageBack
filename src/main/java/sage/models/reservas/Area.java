package sage.models.reservas;

import java.util.List;

public class Area {
    private String nome;
    private List<Sala> salas;

    public Area(String nome, List<Sala> salas) {
        this.nome = nome;
        this.salas = salas;
    }

    // Getters e Setters
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<Sala> getSalas() {
        return salas;
    }

    public void setSalas(List<Sala> salas) {
        this.salas = salas;
    }


}

