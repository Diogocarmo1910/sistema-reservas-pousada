package pousada.modelo;

public enum StatusFinanceiro {
    EM_ABERTO("Em aberto"),
    PARCIAL("Parcial"),
    QUITADO("Quitado");

    private final String descricao;

    StatusFinanceiro(String descricao) {
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
