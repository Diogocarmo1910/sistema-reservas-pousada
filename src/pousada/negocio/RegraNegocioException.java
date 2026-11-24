package pousada.negocio;

public class RegraNegocioException extends Exception {

    private static final long serialVersionUID = 1L;

    public RegraNegocioException(String mensagem) {
        super(mensagem);
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
        if (!(outro instanceof RegraNegocioException)) {
            return false;
        }
        return getMessage().equals(((RegraNegocioException) outro).getMessage());
    }

    @Override
    public int hashCode() {
        return getMessage().hashCode();
    }
}
