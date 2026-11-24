package pousada.modelo;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.Locale;
import java.util.Objects;

public final class Textos {

    public static final DateTimeFormatter DATA = DateTimeFormatter.ofPattern("dd/MM/uuuu")
            .withResolverStyle(ResolverStyle.STRICT);

    private static final DecimalFormat DINHEIRO;

    static {
        DecimalFormatSymbols simbolos = new DecimalFormatSymbols(new Locale("pt", "BR"));
        DINHEIRO = new DecimalFormat("#,##0.00", simbolos);
    }

    private Textos() {
    }

    public static String cpf(String digitos) {
        return digitos.substring(0, 3) + "."
                + digitos.substring(3, 6) + "."
                + digitos.substring(6, 9) + "-"
                + digitos.substring(9);
    }

    public static String data(LocalDate data) {
        return data.format(DATA);
    }

    public static String dinheiro(BigDecimal valor) {
        return "R$ " + DINHEIRO.format(valor);
    }

    @Override
    public String toString() {
        return "Formatador de textos da pousada";
    }

    @Override
    public boolean equals(Object outro) {
        return outro instanceof Textos;
    }

    @Override
    public int hashCode() {
        return Objects.hash(Textos.class);
    }
}
