package pousada.persistencia;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** Grava e lê os cadastros na pasta dados. */
public class GerenciadorArquivo {

    public static final String ARQUIVO_HOSPEDES = "hospedes.dat";
    public static final String ARQUIVO_QUARTOS = "quartos.dat";
    public static final String ARQUIVO_RESERVAS = "reservas.dat";
    public static final String ARQUIVO_PAGAMENTOS = "pagamentos.dat";
    public static final String ARQUIVO_SEQUENCIA = "sequencia.dat";

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

    public <T> List<T> carregarLista(String arquivo) throws PersistenciaException {
        Path caminho = pasta.resolve(arquivo);
        if (!Files.exists(caminho)) {
            return new ArrayList<>();
        }
        try (ObjectInputStream entrada = new ObjectInputStream(
                new BufferedInputStream(Files.newInputStream(caminho)))) {
            Object lido = entrada.readObject();
            if (!(lido instanceof List<?>)) {
                throw new PersistenciaException("Arquivo de dados inválido: " + arquivo);
            }
            List<?> listaLida = (List<?>) lido;
            List<T> lista = new ArrayList<>();
            for (Object item : listaLida) {
                @SuppressWarnings("unchecked")
                T convertido = (T) item;
                lista.add(convertido);
            }
            return lista;
        } catch (IOException | ClassNotFoundException e) {
            throw new PersistenciaException("Não foi possível ler o arquivo " + arquivo + ".", e);
        }
    }

    public void salvarLista(String arquivo, List<?> lista) throws PersistenciaException {
        escrever(arquivo, new ArrayList<>(lista));
    }

    public Sequencia carregarSequencia() throws PersistenciaException {
        Path caminho = pasta.resolve(ARQUIVO_SEQUENCIA);
        if (!Files.exists(caminho)) {
            return new Sequencia();
        }
        try (ObjectInputStream entrada = new ObjectInputStream(
                new BufferedInputStream(Files.newInputStream(caminho)))) {
            Object lido = entrada.readObject();
            if (!(lido instanceof Sequencia)) {
                throw new PersistenciaException("Arquivo de sequência inválido.");
            }
            return (Sequencia) lido;
        } catch (IOException | ClassNotFoundException e) {
            throw new PersistenciaException("Não foi possível ler a sequência de códigos.", e);
        }
    }

    public void salvarSequencia(Sequencia sequencia) throws PersistenciaException {
        escrever(ARQUIVO_SEQUENCIA, sequencia);
    }

    private void escrever(String arquivo, Object conteudo) throws PersistenciaException {
        try {
            Files.createDirectories(pasta);
            Path caminho = pasta.resolve(arquivo);
            try (ObjectOutputStream saida = new ObjectOutputStream(
                    new BufferedOutputStream(Files.newOutputStream(caminho)))) {
                saida.writeObject(conteudo);
            }
        } catch (IOException e) {
            throw new PersistenciaException("Não foi possível gravar o arquivo " + arquivo + ".", e);
        }
    }

    @Override
    public String toString() {
        return "Arquivos em " + pasta.toAbsolutePath();
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
