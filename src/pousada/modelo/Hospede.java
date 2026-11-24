package pousada.modelo;

import java.util.Objects;

public class Hospede extends Pessoa {

    private static final long serialVersionUID = 1L;

    private String email;
    private String endereco;

    public Hospede(String nome, String cpf, String telefone, String email, String endereco) {
        super(nome, cpf, telefone);
        this.email = email == null ? "" : email;
        this.endereco = endereco == null ? "" : endereco;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? "" : email;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco == null ? "" : endereco;
    }

    @Override
    public String toString() {
        String emailTexto = email.isBlank() ? "não informado" : email;
        String enderecoTexto = endereco.isBlank() ? "não informado" : endereco;
        return "Hóspede: " + super.toString()
                + " | E-mail: " + emailTexto
                + " | Endereço: " + enderecoTexto;
    }

    @Override
    public boolean equals(Object outro) {
        if (this == outro) {
            return true;
        }
        if (!(outro instanceof Hospede)) {
            return false;
        }
        return getCpf().equals(((Hospede) outro).getCpf());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCpf());
    }
}
