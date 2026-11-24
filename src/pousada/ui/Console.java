package pousada.ui;

import pousada.modelo.Textos;
import pousada.negocio.RegraNegocioException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.NoSuchElementException;
import java.util.Objects;

public class Console {

    private final java.util.Scanner scanner = new java.util.Scanner(System.in);

    public String lerTexto(String prompt) {
        System.out.print(prompt);
        return lerLinha();
    }

    public String lerTextoObrigatorio(String prompt) {
        while (true) {
            String texto = lerTexto(prompt).trim();
            if (!texto.isEmpty()) {
                return texto;
            }
            System.out.println("Este campo é obrigatório.");
        }
    }

    public int lerInteiro(String prompt) {
        while (true) {
            String texto = lerTexto(prompt).trim();
            try {
                return Integer.parseInt(texto);
            } catch (NumberFormatException e) {
                System.out.println("Digite um número inteiro válido.");
            }
        }
    }

    public int lerOpcao(String prompt, int minimo, int maximo) {
        while (true) {
            int opcao = lerInteiro(prompt);
            if (opcao >= minimo && opcao <= maximo) {
                return opcao;
            }
            System.out.println("Opção inválida. Escolha entre " + minimo + " e " + maximo + ".");
        }
    }

    public boolean lerSimNao(String prompt) {
        while (true) {
            String texto = lerTexto(prompt).trim();
            if (texto.equalsIgnoreCase("S")) {
                return true;
            }
            if (texto.equalsIgnoreCase("N")) {
                return false;
            }
            System.out.println("Responda S ou N.");
        }
    }

    public LocalDate lerData(String prompt) {
        while (true) {
            String texto = lerTexto(prompt).trim();
            try {
                return LocalDate.parse(texto, Textos.DATA);
            } catch (DateTimeParseException e) {
                System.out.println("Data inválida. Use dd/MM/aaaa.");
            }
        }
    }

    public BigDecimal lerDinheiro(String prompt) {
        while (true) {
            String texto = lerTexto(prompt).trim();
            if (texto.isEmpty()) {
                System.out.println("Informe um valor.");
                continue;
            }
            String normalizado = texto;
            if (texto.contains(",")) {
                normalizado = texto.replace(".", "").replace(",", ".");
            }
            try {
                return new BigDecimal(normalizado);
            } catch (NumberFormatException e) {
                System.out.println("Valor inválido. Exemplo: 150,00.");
            }
        }
    }

    public void mostrarErro(Exception erro) {
        String mensagem = erro.getMessage();
        if (mensagem == null || mensagem.isBlank()) {
            mensagem = "Não foi possível concluir a operação.";
        }
        if (erro instanceof RegraNegocioException) {
            System.out.println("Não foi possível concluir: " + mensagem);
        } else {
            System.out.println(mensagem);
        }
    }

    private String lerLinha() {
        try {
            if (!scanner.hasNextLine()) {
                System.out.println();
                System.exit(0);
            }
            return scanner.nextLine();
        } catch (NoSuchElementException e) {
            System.out.println();
            System.exit(0);
            return "";
        }
    }

    @Override
    public String toString() {
        return "Leitura do teclado";
    }

    @Override
    public boolean equals(Object outro) {
        return this == outro;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(scanner);
    }
}
