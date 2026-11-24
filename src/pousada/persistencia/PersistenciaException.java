package pousada.persistencia;

public class PersistenciaException extends Exception {

    private static final long serialVersionUID = 1L;

    public PersistenciaException(String mensagem) {
        super(mensagem);
    }

    public PersistenciaException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }

    @Override
    public String toString() {
        return getMessage();
    }

    @Override
    public boolean equals(Object outro) {
        if (this == outro) {
            return true;
        }
        if (!(outro instanceof PersistenciaException)) {
            return false;
        }
        return getMessage().equals(((PersistenciaException) outro).getMessage());
    }

    @Override
    public int hashCode() {
        return getMessage().hashCode();
    }
}
