package pousada.modelo;

public enum FormaPagamento {
    DINHEIRO("Dinheiro"),
    PIX("Pix"),
    CARTAO("Cartão");

    private final String descricao;

    FormaPagamento(String descricao) {
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
