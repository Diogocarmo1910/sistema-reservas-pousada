package pousada.ui;

import pousada.negocio.SistemaPousada;
import pousada.persistencia.PersistenciaException;

public class Main {

    public static void main(String[] args) {
        try {
            SistemaPousada sistema = SistemaPousada.abrir();
            new Menu(sistema, new Console()).iniciar();
        } catch (PersistenciaException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public String toString() {
        return "Sistema de reservas da pousada";
    }

    @Override
    public boolean equals(Object outro) {
        return outro instanceof Main;
    }

    @Override
    public int hashCode() {
        return Main.class.hashCode();
    }
}
