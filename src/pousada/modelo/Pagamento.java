package pousada.modelo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class Pagamento implements Serializable {

    private static final long serialVersionUID = 1L;

    private final int codigo;
    private final int codigoReserva;
    private BigDecimal valor;
    private FormaPagamento forma;
    private LocalDate data;

    public Pagamento(int codigo, int codigoReserva, BigDecimal valor, FormaPagamento forma, LocalDate data) {
        if (valor == null || forma == null || data == null) {
            throw new IllegalArgumentException("Valor, forma e data do pagamento são obrigatórios.");
        }
        this.codigo = codigo;
        this.codigoReserva = codigoReserva;
        this.valor = valor;
        this.forma = forma;
        this.data = data;
    }

    public int getCodigo() {
        return codigo;
    }

    public int getCodigoReserva() {
        return codigoReserva;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public FormaPagamento getForma() {
        return forma;
    }

    public void setForma(FormaPagamento forma) {
        this.forma = forma;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Pagamento " + codigo
                + " | reserva " + codigoReserva
                + " | " + Textos.data(data)
                + " | " + forma
                + " | " + Textos.dinheiro(valor);
    }

    @Override
    public boolean equals(Object outro) {
        if (this == outro) {
            return true;
        }
        if (!(outro instanceof Pagamento)) {
            return false;
        }
        return codigo == ((Pagamento) outro).codigo;
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }
}
