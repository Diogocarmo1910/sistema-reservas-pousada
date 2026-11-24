package pousada.modelo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

public class Quarto implements Serializable {

    private static final long serialVersionUID = 1L;

    private final int numero;
    private TipoQuarto tipo;
    private int capacidade;
    private BigDecimal valorDiaria;
    private StatusQuarto status;

    public Quarto(int numero, TipoQuarto tipo, int capacidade, BigDecimal valorDiaria) {
        if (tipo == null || valorDiaria == null) {
            throw new IllegalArgumentException("Tipo e diária são obrigatórios.");
        }
        this.numero = numero;
        this.tipo = tipo;
        this.capacidade = capacidade;
        this.valorDiaria = valorDiaria;
        this.status = StatusQuarto.DISPONIVEL;
    }

    public int getNumero() {
        return numero;
    }

    public TipoQuarto getTipo() {
        return tipo;
    }

    public void setTipo(TipoQuarto tipo) {
        this.tipo = tipo;
    }

    public int getCapacidade() {
        return capacidade;
    }

    public void setCapacidade(int capacidade) {
        this.capacidade = capacidade;
    }

    public BigDecimal getValorDiaria() {
        return valorDiaria;
    }

    public void setValorDiaria(BigDecimal valorDiaria) {
        this.valorDiaria = valorDiaria;
    }

    public StatusQuarto getStatus() {
        return status;
    }

    public void setStatus(StatusQuarto status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Quarto " + numero
                + " | " + tipo
                + " | capacidade " + capacidade
                + " | diária " + Textos.dinheiro(valorDiaria)
                + " | " + status;
    }

    @Override
    public boolean equals(Object outro) {
        if (this == outro) {
            return true;
        }
        if (!(outro instanceof Quarto)) {
            return false;
        }
        return numero == ((Quarto) outro).numero;
    }

    @Override
    public int hashCode() {
        return Objects.hash(numero);
    }
}
