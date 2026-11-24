package pousada.persistencia;

import java.io.Serializable;
import java.util.Objects;

public class Sequencia implements Serializable {

    private static final long serialVersionUID = 1L;

    private int proximoCodigoReserva;
    private int proximoCodigoPagamento;

    public Sequencia() {
        this.proximoCodigoReserva = 1;
        this.proximoCodigoPagamento = 1;
    }

    public int getProximoCodigoReserva() {
        return proximoCodigoReserva;
    }

    public void setProximoCodigoReserva(int proximoCodigoReserva) {
        this.proximoCodigoReserva = proximoCodigoReserva;
    }

    public int getProximoCodigoPagamento() {
        return proximoCodigoPagamento;
    }

    public void setProximoCodigoPagamento(int proximoCodigoPagamento) {
        this.proximoCodigoPagamento = proximoCodigoPagamento;
    }

    public int gerarCodigoReserva() {
        return proximoCodigoReserva++;
    }

    public int gerarCodigoPagamento() {
        return proximoCodigoPagamento++;
    }

    @Override
    public String toString() {
        return "Próxima reserva: " + proximoCodigoReserva + " | próximo pagamento: " + proximoCodigoPagamento;
    }

    @Override
    public boolean equals(Object outro) {
        if (this == outro) {
            return true;
        }
        if (!(outro instanceof Sequencia)) {
            return false;
        }
        Sequencia sequencia = (Sequencia) outro;
        return proximoCodigoReserva == sequencia.proximoCodigoReserva
                && proximoCodigoPagamento == sequencia.proximoCodigoPagamento;
    }

    @Override
    public int hashCode() {
        return Objects.hash(proximoCodigoReserva, proximoCodigoPagamento);
    }
}
