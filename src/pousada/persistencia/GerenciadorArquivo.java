package pousada.persistencia;

import pousada.modelo.FormaPagamento;
import pousada.modelo.Hospede;
import pousada.modelo.Pagamento;
import pousada.modelo.Quarto;
import pousada.modelo.Reserva;
import pousada.modelo.StatusQuarto;
import pousada.modelo.StatusReserva;
import pousada.modelo.Textos;
import pousada.modelo.TipoQuarto;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/** Grava os cadastros em arquivos de texto na pasta dados. Dá para abrir no Bloco de Notas. */
public class GerenciadorArquivo {

    public static final String ARQUIVO_HOSPEDES = "hospedes.txt";
    public static final String ARQUIVO_QUARTOS = "quartos.txt";
    public static final String ARQUIVO_RESERVAS = "reservas.txt";
    public static final String ARQUIVO_PAGAMENTOS = "pagamentos.txt";
    public static final String ARQUIVO_SEQUENCIA = "sequencia.txt";

    private final Path pasta;

    public GerenciadorArquivo() {
        this.pasta = Path.of(System.getProperty("user.dir"), "dados");
    }

    public Path getPasta() {
        return pasta;
    }

    public boolean possuiCadastro() {
        return Files.exists(pasta.resolve(ARQUIVO_HOSPEDES))
                || Files.exists(pasta.resolve(ARQUIVO_QUARTOS))
                || Files.exists(pasta.resolve(ARQUIVO_RESERVAS))
                || Files.exists(pasta.resolve(ARQUIVO_PAGAMENTOS));
    }

    public boolean possuiSequencia() {
        return Files.exists(pasta.resolve(ARQUIVO_SEQUENCIA));
    }

    public List<Hospede> carregarHospedes() throws PersistenciaException {
        List<Hospede> lista = new ArrayList<>();
        for (Map<String, String> bloco : lerBlocos(ARQUIVO_HOSPEDES)) {
            lista.add(new Hospede(
                    valor(bloco, "nome"),
                    valor(bloco, "cpf"),
                    valor(bloco, "telefone"),
                    valor(bloco, "email"),
                    valor(bloco, "endereco")));
        }
        return lista;
    }

    public List<Quarto> carregarQuartos() throws PersistenciaException {
        List<Quarto> lista = new ArrayList<>();
        for (Map<String, String> bloco : lerBlocos(ARQUIVO_QUARTOS)) {
            Quarto quarto = new Quarto(
                    inteiro(bloco, "numero"),
                    TipoQuarto.valueOf(valor(bloco, "tipo")),
                    inteiro(bloco, "capacidade"),
                    new BigDecimal(valor(bloco, "diaria")));
            quarto.setStatus(StatusQuarto.valueOf(valor(bloco, "status")));
            lista.add(quarto);
        }
        return lista;
    }

    public List<Reserva> carregarReservas() throws PersistenciaException {
        List<Reserva> lista = new ArrayList<>();
        for (Map<String, String> bloco : lerBlocos(ARQUIVO_RESERVAS)) {
            Reserva reserva = new Reserva(
                    inteiro(bloco, "codigo"),
                    valor(bloco, "cpf"),
                    inteiro(bloco, "quarto"),
                    data(bloco, "entrada"),
                    data(bloco, "saida"),
                    inteiro(bloco, "quantidade"),
                    new BigDecimal(valor(bloco, "hospedagem")),
                    data(bloco, "criacao"));
            reserva.setStatus(StatusReserva.valueOf(valor(bloco, "status")));
            reserva.setValorMulta(new BigDecimal(valor(bloco, "multa")));
            lista.add(reserva);
        }
        return lista;
    }

    public List<Pagamento> carregarPagamentos() throws PersistenciaException {
        List<Pagamento> lista = new ArrayList<>();
        for (Map<String, String> bloco : lerBlocos(ARQUIVO_PAGAMENTOS)) {
            lista.add(new Pagamento(
                    inteiro(bloco, "codigo"),
                    inteiro(bloco, "reserva"),
                    new BigDecimal(valor(bloco, "valor")),
                    FormaPagamento.valueOf(valor(bloco, "forma")),
                    data(bloco, "data")));
        }
        return lista;
    }

    public Sequencia carregarSequencia() throws PersistenciaException {
        List<Map<String, String>> blocos = lerBlocos(ARQUIVO_SEQUENCIA);
        Sequencia sequencia = new Sequencia();
        if (!blocos.isEmpty()) {
            sequencia.setProximoCodigoReserva(inteiro(blocos.get(0), "proximaReserva"));
            sequencia.setProximoCodigoPagamento(inteiro(blocos.get(0), "proximoPagamento"));
        }
        return sequencia;
    }

    public void salvarHospedes(List<Hospede> hospedes) throws PersistenciaException {
        List<String> blocos = new ArrayList<>();
        for (Hospede hospede : hospedes) {
            blocos.add(linhas(
                    "nome", hospede.getNome(),
                    "cpf", hospede.getCpf(),
                    "telefone", hospede.getTelefone(),
                    "email", hospede.getEmail(),
                    "endereco", hospede.getEndereco()));
        }
        gravar(ARQUIVO_HOSPEDES, blocos);
    }

    public void salvarQuartos(List<Quarto> quartos) throws PersistenciaException {
        List<String> blocos = new ArrayList<>();
        for (Quarto quarto : quartos) {
            blocos.add(linhas(
                    "numero", String.valueOf(quarto.getNumero()),
                    "tipo", quarto.getTipo().name(),
                    "capacidade", String.valueOf(quarto.getCapacidade()),
                    "diaria", quarto.getValorDiaria().toPlainString(),
                    "status", quarto.getStatus().name()));
        }
        gravar(ARQUIVO_QUARTOS, blocos);
    }

    public void salvarReservas(List<Reserva> reservas) throws PersistenciaException {
        List<String> blocos = new ArrayList<>();
        for (Reserva reserva : reservas) {
            blocos.add(linhas(
                    "codigo", String.valueOf(reserva.getCodigo()),
                    "cpf", reserva.getCpfHospede(),
                    "quarto", String.valueOf(reserva.getNumeroQuarto()),
                    "entrada", Textos.data(reserva.getDataEntrada()),
                    "saida", Textos.data(reserva.getDataSaida()),
                    "quantidade", String.valueOf(reserva.getQuantidadePessoas()),
                    "status", reserva.getStatus().name(),
                    "hospedagem", reserva.getValorHospedagem().toPlainString(),
                    "multa", reserva.getValorMulta().toPlainString(),
                    "criacao", Textos.data(reserva.getDataCriacao())));
        }
        gravar(ARQUIVO_RESERVAS, blocos);
    }

    public void salvarPagamentos(List<Pagamento> pagamentos) throws PersistenciaException {
        List<String> blocos = new ArrayList<>();
        for (Pagamento pagamento : pagamentos) {
            blocos.add(linhas(
                    "codigo", String.valueOf(pagamento.getCodigo()),
                    "reserva", String.valueOf(pagamento.getCodigoReserva()),
                    "valor", pagamento.getValor().toPlainString(),
                    "forma", pagamento.getForma().name(),
                    "data", Textos.data(pagamento.getData())));
        }
        gravar(ARQUIVO_PAGAMENTOS, blocos);
    }

    public void salvarSequencia(Sequencia sequencia) throws PersistenciaException {
        gravar(ARQUIVO_SEQUENCIA, List.of(linhas(
                "proximaReserva", String.valueOf(sequencia.getProximoCodigoReserva()),
                "proximoPagamento", String.valueOf(sequencia.getProximoCodigoPagamento()))));
    }

    private List<Map<String, String>> lerBlocos(String arquivo) throws PersistenciaException {
        Path caminho = pasta.resolve(arquivo);
        if (!Files.exists(caminho)) {
            return new ArrayList<>();
        }
        try {
            String texto = Files.readString(caminho, StandardCharsets.UTF_8).replace("\r\n", "\n");
            List<Map<String, String>> blocos = new ArrayList<>();
            if (texto.isBlank()) {
                return blocos;
            }
            for (String parte : texto.split("\n\n")) {
                Map<String, String> campos = new LinkedHashMap<>();
                for (String linha : parte.split("\n")) {
                    if (linha.isBlank()) {
                        continue;
                    }
                    int separador = linha.indexOf(':');
                    if (separador < 0) {
                        throw new PersistenciaException("Linha inválida em " + arquivo + ": " + linha);
                    }
                    campos.put(linha.substring(0, separador).trim(), linha.substring(separador + 1).trim());
                }
                if (!campos.isEmpty()) {
                    blocos.add(campos);
                }
            }
            return blocos;
        } catch (IOException e) {
            throw new PersistenciaException("Não foi possível ler o arquivo " + arquivo + ".", e);
        } catch (IllegalArgumentException e) {
            throw new PersistenciaException("Dados inválidos no arquivo " + arquivo + ".", e);
        }
    }

    private void gravar(String arquivo, List<String> blocos) throws PersistenciaException {
        try {
            Files.createDirectories(pasta);
            String texto = String.join(System.lineSeparator() + System.lineSeparator(), blocos);
            if (!texto.isEmpty()) {
                texto = texto + System.lineSeparator();
            }
            Files.writeString(pasta.resolve(arquivo), texto, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new PersistenciaException("Não foi possível gravar o arquivo " + arquivo + ".", e);
        }
    }

    private static String linhas(String... pares) {
        StringBuilder texto = new StringBuilder();
        for (int i = 0; i < pares.length; i += 2) {
            if (texto.length() > 0) {
                texto.append(System.lineSeparator());
            }
            String valor = pares[i + 1] == null ? "" : pares[i + 1].replace("\r", " ").replace("\n", " ");
            texto.append(pares[i]).append(": ").append(valor);
        }
        return texto.toString();
    }

    private static String valor(Map<String, String> bloco, String campo) throws PersistenciaException {
        if (!bloco.containsKey(campo)) {
            throw new PersistenciaException("Falta o campo " + campo + " no arquivo de texto.");
        }
        return bloco.get(campo);
    }

    private static int inteiro(Map<String, String> bloco, String campo) throws PersistenciaException {
        try {
            return Integer.parseInt(valor(bloco, campo));
        } catch (NumberFormatException e) {
            throw new PersistenciaException("Número inválido no campo " + campo + ".", e);
        }
    }

    private static LocalDate data(Map<String, String> bloco, String campo) throws PersistenciaException {
        try {
            return LocalDate.parse(valor(bloco, campo), Textos.DATA);
        } catch (Exception e) {
            throw new PersistenciaException("Data inválida no campo " + campo + ".", e);
        }
    }

    @Override
    public String toString() {
        return "Arquivos de texto em " + pasta.toAbsolutePath();
    }

    @Override
    public boolean equals(Object outro) {
        if (this == outro) {
            return true;
        }
        if (!(outro instanceof GerenciadorArquivo)) {
            return false;
        }
        return pasta.equals(((GerenciadorArquivo) outro).pasta);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pasta);
    }
}
