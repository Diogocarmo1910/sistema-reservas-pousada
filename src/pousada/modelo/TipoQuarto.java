package pousada.modelo;

public enum TipoQuarto {
    SIMPLES("Simples"),
    DUPLO("Duplo"),
    SUITE("Suíte"),
    CHALE("Chalé");

    private final String descricao;

    TipoQuarto(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    @Override
    public String toString() {
        return descricao;
    }
}
