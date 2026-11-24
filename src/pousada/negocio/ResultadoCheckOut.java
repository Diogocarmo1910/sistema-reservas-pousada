package pousada.negocio;

import pousada.modelo.Textos;

import java.math.BigDecimal;
import java.util.Objects;

public class ResultadoCheckOut {

    private final boolean concluido;
    private final long diasAtraso;
    private final ResultadoFinanceiro financeiro;

    public ResultadoCheckOut(boolean concluido, long diasAtraso, ResultadoFinanceiro financeiro) {
        this.concluido = concluido;
        this.diasAtraso = diasAtraso;
        this.financeiro = financeiro;
    }

    public boolean isConcluido() {
        return concluido;
    }

    public long getDiasAtraso() {
        return diasAtraso;
    }

    public ResultadoFinanceiro getFinanceiro() {
        return financeiro;
    }

    public String getMensagem() {
        String atraso = diasAtraso > 0
                ? "Atraso de " + diasAtraso + " dia(s). Multa: " + Textos.dinheiro(financeiro.getValorMulta()) + ". "
                : "Sem multa de atraso. ";
        if (concluido) {
            String restituir = "";
            if (financeiro.getValorRestituir().compareTo(BigDecimal.ZERO) > 0) {
                restituir = " Valor a restituir: " + Textos.dinheiro(financeiro.getValorRestituir()) + ".";
            }
            return atraso + "Check-out concluído. O quarto voltou a ficar disponível." + restituir;
        }
        return atraso + "Check-out não concluído. Quite o saldo de "
                + Textos.dinheiro(financeiro.getSaldoEmAberto()) + " e repita o check-out.";
    }

    @Override
    public String toString() {
        return getMensagem() + System.lineSeparator() + financeiro;
    }

    @Override
    public boolean equals(Object outro) {
        if (this == outro) {
            return true;
        }
        if (!(outro instanceof ResultadoCheckOut)) {
            return false;
        }
        ResultadoCheckOut resultado = (ResultadoCheckOut) outro;
        return concluido == resultado.concluido
                && diasAtraso == resultado.diasAtraso
                && financeiro.equals(resultado.financeiro);
    }

    @Override
    public int hashCode() {
        return Objects.hash(concluido, diasAtraso, financeiro);
    }
}
