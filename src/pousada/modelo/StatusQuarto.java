package pousada.modelo;

public enum StatusQuarto {
    DISPONIVEL("Disponível"),
    OCUPADO("Ocupado"),
    MANUTENCAO("Manutenção");

    private final String descricao;

    StatusQuarto(String descricao) {
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
