package pousada.modelo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/** Liga um hóspede (pelo CPF) a um quarto (pelo número) e guarda o valor fechado. */
public class Reserva implements Serializable {

    private static final long serialVersionUID = 1L;

    private final int codigo;
    private final String cpfHospede;
    private int numeroQuarto;
    private LocalDate dataEntrada;
    private LocalDate dataSaida;
    private int quantidadePessoas;
    private StatusReserva status;
    private BigDecimal valorHospedagem;
    private BigDecimal valorMulta;
    private final LocalDate dataCriacao;

    public Reserva(int codigo, String cpfHospede, int numeroQuarto, LocalDate dataEntrada,
                   LocalDate dataSaida, int quantidadePessoas, BigDecimal valorHospedagem,
                   LocalDate dataCriacao) {
        this.codigo = codigo;
        this.cpfHospede = cpfHospede;
        this.numeroQuarto = numeroQuarto;
        this.dataEntrada = dataEntrada;
        this.dataSaida = dataSaida;
        this.quantidadePessoas = quantidadePessoas;
        this.status = StatusReserva.CONFIRMADA;
        this.valorHospedagem = valorHospedagem;
        this.valorMulta = BigDecimal.ZERO.setScale(2);
        this.dataCriacao = dataCriacao;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getCpfHospede() {
        return cpfHospede;
    }

    public int getNumeroQuarto() {
        return numeroQuarto;
    }

    public void setNumeroQuarto(int numeroQuarto) {
        this.numeroQuarto = numeroQuarto;
    }

    public LocalDate getDataEntrada() {
        return dataEntrada;
    }

    public void setDataEntrada(LocalDate dataEntrada) {
        this.dataEntrada = dataEntrada;
    }

    public LocalDate getDataSaida() {
        return dataSaida;
    }

    public void setDataSaida(LocalDate dataSaida) {
        this.dataSaida = dataSaida;
    }

    public int getQuantidadePessoas() {
        return quantidadePessoas;
    }

    public void setQuantidadePessoas(int quantidadePessoas) {
        this.quantidadePessoas = quantidadePessoas;
    }

    public StatusReserva getStatus() {
        return status;
    }

    public void setStatus(StatusReserva status) {
        this.status = status;
    }

    public BigDecimal getValorHospedagem() {
        return valorHospedagem;
    }

    public void setValorHospedagem(BigDecimal valorHospedagem) {
        this.valorHospedagem = valorHospedagem;
    }

    public BigDecimal getValorMulta() {
        return valorMulta;
    }

    public void setValorMulta(BigDecimal valorMulta) {
        this.valorMulta = valorMulta;
    }

    public LocalDate getDataCriacao() {
        return dataCriacao;
    }

    @Override
    public String toString() {
        return "Reserva " + codigo
                + " | CPF " + Textos.cpf(cpfHospede)
                + " | quarto " + numeroQuarto
                + " | " + Textos.data(dataEntrada) + " a " + Textos.data(dataSaida)
                + " | " + quantidadePessoas + " pessoa(s)"
                + " | " + status
                + " | hospedagem " + Textos.dinheiro(valorHospedagem)
                + " | multa " + Textos.dinheiro(valorMulta);
    }

    @Override
    public boolean equals(Object outro) {
        if (this == outro) {
            return true;
        }
        if (!(outro instanceof Reserva)) {
            return false;
        }
        return codigo == ((Reserva) outro).codigo;
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }
}
