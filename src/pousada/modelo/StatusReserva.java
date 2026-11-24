package pousada.modelo;

public enum StatusReserva {
    CONFIRMADA("Confirmada"),
    CHECKIN("Check-in"),
    CHECKOUT("Check-out"),
    CANCELADA("Cancelada");

    private final String descricao;

    StatusReserva(String descricao) {
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
