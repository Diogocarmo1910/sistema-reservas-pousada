package pousada.modelo;

import java.io.Serializable;
import java.util.Objects;

public class Pessoa implements Serializable {

    private static final long serialVersionUID = 1L;

    private String nome;
    private final String cpf;
    private String telefone;

    public Pessoa(String nome, String cpf, String telefone) {
        if (nome == null || cpf == null || telefone == null) {
            throw new IllegalArgumentException("Nome, CPF e telefone são obrigatórios.");
        }
        this.nome = nome;
        this.cpf = cpf;
        this.telefone = telefone;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    @Override
    public String toString() {
        return nome + " | CPF: " + Textos.cpf(cpf) + " | Tel: " + telefone;
    }

    @Override
    public boolean equals(Object outro) {
        if (this == outro) {
            return true;
        }
        if (outro == null || getClass() != outro.getClass()) {
            return false;
        }
        Pessoa pessoa = (Pessoa) outro;
        return cpf.equals(pessoa.cpf);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cpf);
    }
}
