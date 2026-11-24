package pousada.negocio;

import pousada.modelo.StatusFinanceiro;
import pousada.modelo.StatusReserva;
import pousada.modelo.Textos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ResultadoFinanceiro {

    private final int codigo;
    private final StatusReserva status;
    private final String nomeHospede;
    private final int numeroQuarto;
    private final LocalDate entrada;
    private final LocalDate saida;
    private final BigDecimal valorHospedagem;
    private final BigDecimal valorMulta;
    private final BigDecimal totalPago;
    private final BigDecimal saldo;
    private final StatusFinanceiro situacao;
    private final List<String> pagamentos;

    public ResultadoFinanceiro(int codigo, StatusReserva status, String nomeHospede, int numeroQuarto,
                                LocalDate entrada, LocalDate saida, BigDecimal valorHospedagem,
                                BigDecimal valorMulta, BigDecimal totalPago, BigDecimal saldo,
                                StatusFinanceiro situacao, List<String> pagamentos) {
        this.codigo = codigo;
        this.status = status;
        this.nomeHospede = nomeHospede;
        this.numeroQuarto = numeroQuarto;
        this.entrada = entrada;
        this.saida = saida;
        this.valorHospedagem = valorHospedagem;
        this.valorMulta = valorMulta;
        this.totalPago = totalPago;
        this.saldo = saldo;
        this.situacao = situacao;
        this.pagamentos = new ArrayList<>(pagamentos);
    }

    public int getCodigo() {
        return codigo;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public BigDecimal getSaldoEmAberto() {
        return saldo.max(BigDecimal.ZERO);
    }

    public BigDecimal getValorRestituir() {
        return saldo.negate().max(BigDecimal.ZERO);
    }

    public StatusFinanceiro getSituacao() {
        return situacao;
    }

    public BigDecimal getValorMulta() {
        return valorMulta;
    }

    @Override
    public String toString() {
        StringBuilder texto = new StringBuilder();
        texto.append("Reserva ").append(codigo).append(" - ").append(status).append('\n');
        texto.append("Hóspede: ").append(nomeHospede).append('\n');
        texto.append("Quarto: ").append(numeroQuarto).append('\n');
        texto.append("Período: ").append(Textos.data(entrada)).append(" a ").append(Textos.data(saida)).append('\n');
        texto.append("Hospedagem: ").append(Textos.dinheiro(valorHospedagem)).append('\n');
        texto.append("Multa: ").append(Textos.dinheiro(valorMulta)).append('\n');
        texto.append("Pago: ").append(Textos.dinheiro(totalPago)).append('\n');
        texto.append("Saldo em aberto: ").append(Textos.dinheiro(getSaldoEmAberto())).append('\n');
        if (getValorRestituir().compareTo(BigDecimal.ZERO) > 0) {
            texto.append("Valor a restituir: ").append(Textos.dinheiro(getValorRestituir())).append('\n');
        }
        texto.append("Situação: ").append(situacao).append('\n');
        texto.append("Pagamentos:");
        if (pagamentos.isEmpty()) {
            texto.append(" nenhum");
        } else {
            for (String pagamento : pagamentos) {
                texto.append('\n').append("  ").append(pagamento);
            }
        }
        return texto.toString();
    }

    @Override
    public boolean equals(Object outro) {
        if (this == outro) {
            return true;
        }
        if (!(outro instanceof ResultadoFinanceiro)) {
            return false;
        }
        ResultadoFinanceiro resultado = (ResultadoFinanceiro) outro;
        return codigo == resultado.codigo
                && numeroQuarto == resultado.numeroQuarto
                && status == resultado.status
                && nomeHospede.equals(resultado.nomeHospede)
                && entrada.equals(resultado.entrada)
                && saida.equals(resultado.saida)
                && valorHospedagem.compareTo(resultado.valorHospedagem) == 0
                && valorMulta.compareTo(resultado.valorMulta) == 0
                && totalPago.compareTo(resultado.totalPago) == 0
                && saldo.compareTo(resultado.saldo) == 0
                && situacao == resultado.situacao
                && pagamentos.equals(resultado.pagamentos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo, status, nomeHospede, numeroQuarto, entrada, saida, situacao);
    }
}
