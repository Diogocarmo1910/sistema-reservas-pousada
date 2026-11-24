package pousada.negocio;

import pousada.modelo.Textos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class Validador {

    private Validador() {
    }

    public static String nome(String nome) throws RegraNegocioException {
        if (nome == null || nome.trim().length() < 3) {
            throw new RegraNegocioException("Nome deve ter pelo menos 3 caracteres.");
        }
        return nome.trim();
    }

    public static String cpf(String cpf) throws RegraNegocioException {
        if (cpf == null) {
            throw new RegraNegocioException("CPF inválido.");
        }
        String digitos = cpf.replaceAll("\\D", "");
        if (digitos.length() != 11) {
            throw new RegraNegocioException("CPF inválido. Informe 11 dígitos.");
        }
        if (digitos.chars().distinct().count() == 1) {
            throw new RegraNegocioException("CPF inválido.");
        }
        return digitos;
    }

    public static String telefone(String telefone) throws RegraNegocioException {
        if (telefone == null) {
            throw new RegraNegocioException("Telefone é obrigatório.");
        }
        String digitos = telefone.replaceAll("\\D", "");
        if (digitos.length() < 10 || digitos.length() > 11) {
            throw new RegraNegocioException("Telefone deve ter 10 ou 11 dígitos.");
        }
        return digitos;
    }

    public static String email(String email) throws RegraNegocioException {
        if (email == null || email.isBlank()) {
            return "";
        }
        String texto = email.trim();
        int arroba = texto.indexOf('@');
        if (arroba <= 0 || arroba != texto.lastIndexOf('@') || !texto.substring(arroba).contains(".")) {
            throw new RegraNegocioException("E-mail inválido.");
        }
        return texto;
    }

    public static String endereco(String endereco) {
        if (endereco == null) {
            return "";
        }
        return endereco.trim();
    }

    public static int numeroQuarto(int numero) throws RegraNegocioException {
        if (numero <= 0) {
            throw new RegraNegocioException("Número do quarto deve ser positivo.");
        }
        return numero;
    }

    public static int capacidade(int capacidade) throws RegraNegocioException {
        if (capacidade < 1) {
            throw new RegraNegocioException("Capacidade deve ser de pelo menos 1 pessoa.");
        }
        return capacidade;
    }

    public static BigDecimal diaria(BigDecimal valor) throws RegraNegocioException {
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RegraNegocioException("Diária deve ser maior que zero.");
        }
        return valor.setScale(2, java.math.RoundingMode.HALF_UP);
    }

    public static BigDecimal pagamento(BigDecimal valor) throws RegraNegocioException {
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RegraNegocioException("Valor do pagamento deve ser maior que zero.");
        }
        return valor.setScale(2, java.math.RoundingMode.HALF_UP);
    }

    public static LocalDate data(String texto) throws RegraNegocioException {
        if (texto == null || texto.isBlank()) {
            throw new RegraNegocioException("Data inválida. Use dd/MM/aaaa.");
        }
        try {
            return LocalDate.parse(texto.trim(), Textos.DATA);
        } catch (DateTimeParseException e) {
            throw new RegraNegocioException("Data inválida. Use dd/MM/aaaa.");
        }
    }

    @Override
    public String toString() {
        return "Validador de dados da pousada";
    }

    @Override
    public boolean equals(Object outro) {
        return outro instanceof Validador;
    }

    @Override
    public int hashCode() {
        return Validador.class.hashCode();
    }
}
