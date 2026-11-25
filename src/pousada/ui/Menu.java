package pousada.ui;

import pousada.modelo.FormaPagamento;
import pousada.modelo.Hospede;
import pousada.modelo.Quarto;
import pousada.modelo.Reserva;
import pousada.modelo.TipoQuarto;
import pousada.negocio.RegraNegocioException;
import pousada.negocio.SistemaPousada;
import pousada.persistencia.PersistenciaException;

import java.util.List;

/** Telas do console. Lê o teclado e chama o SistemaPousada. */
public class Menu {

    private final SistemaPousada sistema;
    private final Console console;

    public Menu(SistemaPousada sistema, Console console) {
        this.sistema = sistema;
        this.console = console;
    }

    public void iniciar() {
        System.out.println("Sistema de reservas da pousada");
        System.out.println("Dados em: " + sistema.getCaminhoDados());
        if (sistema.isExemploCriado()) {
            System.out.println("Não havia arquivos. Foi criado um exemplo com 3 hóspedes e 4 quartos.");
        }
        boolean executando = true;
        while (executando) {
            System.out.println();
            System.out.println("=== Menu principal ===");
            System.out.println("1 - Hóspedes");
            System.out.println("2 - Quartos");
            System.out.println("3 - Reservas");
            System.out.println("4 - Pagamentos");
            System.out.println("0 - Sair");
            switch (console.lerOpcao("Opção: ", 0, 4)) {
                case 1 -> menuHospedes();
                case 2 -> menuQuartos();
                case 3 -> menuReservas();
                case 4 -> menuPagamentos();
                default -> executando = false;
            }
        }
        System.out.println("Sistema encerrado. Os dados foram gravados.");
    }

    private void menuHospedes() {
        boolean voltar = false;
        while (!voltar) {
            System.out.println();
            System.out.println("=== Hóspedes ===");
            System.out.println("1 - Incluir");
            System.out.println("2 - Listar");
            System.out.println("3 - Buscar");
            System.out.println("4 - Alterar");
            System.out.println("5 - Excluir");
            System.out.println("0 - Voltar");
            switch (console.lerOpcao("Opção: ", 0, 5)) {
                case 1 -> incluirHospede();
                case 2 -> listarHospedes();
                case 3 -> buscarHospede();
                case 4 -> alterarHospede();
                case 5 -> excluirHospede();
                default -> voltar = true;
            }
        }
    }

    private void incluirHospede() {
        try {
            sistema.incluirHospede(
                    console.lerTextoObrigatorio("Nome: "),
                    console.lerTextoObrigatorio("CPF: "),
                    console.lerTextoObrigatorio("Telefone: "),
                    console.lerTexto("E-mail (Enter para vazio): "),
                    console.lerTexto("Endereço (Enter para vazio): "));
            System.out.println("Hóspede incluído.");
        } catch (RegraNegocioException | PersistenciaException e) {
            console.mostrarErro(e);
        }
    }

    private void listarHospedes() {
        List<Hospede> hospedes = sistema.listarHospedes();
        if (hospedes.isEmpty()) {
            System.out.println("Nenhum hóspede cadastrado.");
            return;
        }
        for (Hospede hospede : hospedes) {
            System.out.println(hospede);
        }
    }

    private void buscarHospede() {
        System.out.println("1 - Por CPF");
        System.out.println("2 - Por nome");
        System.out.println("0 - Voltar");
        switch (console.lerOpcao("Opção: ", 0, 2)) {
            case 1 -> buscarPorCpf();
            case 2 -> buscarPorNome();
            default -> {
            }
        }
    }

    private void buscarPorCpf() {
        try {
            System.out.println(sistema.buscarHospedePorCpf(console.lerTextoObrigatorio("CPF: ")));
        } catch (RegraNegocioException e) {
            console.mostrarErro(e);
        }
    }

    private void buscarPorNome() {
        try {
            List<Hospede> achados = sistema.buscarHospedesPorNome(console.lerTextoObrigatorio("Nome: "));
            if (achados.isEmpty()) {
                System.out.println("Nenhum hóspede encontrado.");
                return;
            }
            for (Hospede hospede : achados) {
                System.out.println(hospede);
            }
        } catch (RegraNegocioException e) {
            console.mostrarErro(e);
        }
    }

    private void alterarHospede() {
        try {
            Hospede hospede = sistema.buscarHospedePorCpf(console.lerTextoObrigatorio("CPF do hóspede: "));
            System.out.println("Atual: " + hospede);
            sistema.alterarHospede(
                    hospede.getCpf(),
                    console.lerTextoObrigatorio("Novo nome: "),
                    console.lerTextoObrigatorio("Novo telefone: "),
                    console.lerTexto("Novo e-mail (Enter para vazio): "),
                    console.lerTexto("Novo endereço (Enter para vazio): "));
            System.out.println("Hóspede alterado. O CPF permanece o mesmo.");
        } catch (RegraNegocioException | PersistenciaException e) {
            console.mostrarErro(e);
        }
    }

    private void excluirHospede() {
        try {
            String cpf = console.lerTextoObrigatorio("CPF do hóspede: ");
            System.out.println(sistema.buscarHospedePorCpf(cpf));
            if (!console.lerSimNao("Confirma a exclusão? (S/N): ")) {
                System.out.println("Exclusão cancelada.");
                return;
            }
            sistema.excluirHospede(cpf);
            System.out.println("Hóspede excluído.");
        } catch (RegraNegocioException | PersistenciaException e) {
            console.mostrarErro(e);
        }
    }

    private void menuQuartos() {
        boolean voltar = false;
        while (!voltar) {
            System.out.println();
            System.out.println("=== Quartos ===");
            System.out.println("1 - Incluir");
            System.out.println("2 - Listar");
            System.out.println("3 - Buscar por número");
            System.out.println("4 - Alterar");
            System.out.println("5 - Manutenção");
            System.out.println("6 - Excluir");
            System.out.println("0 - Voltar");
            switch (console.lerOpcao("Opção: ", 0, 6)) {
                case 1 -> incluirQuarto();
                case 2 -> listarQuartos();
                case 3 -> buscarQuarto();
                case 4 -> alterarQuarto();
                case 5 -> alternarManutencao();
                case 6 -> excluirQuarto();
                default -> voltar = true;
            }
        }
    }

    private void incluirQuarto() {
        try {
            sistema.incluirQuarto(
                    console.lerInteiro("Número: "),
                    lerTipoQuarto(),
                    console.lerInteiro("Capacidade: "),
                    console.lerDinheiro("Diária: "));
            System.out.println("Quarto incluído.");
        } catch (RegraNegocioException | PersistenciaException e) {
            console.mostrarErro(e);
        }
    }

    private void listarQuartos() {
        List<Quarto> quartos = sistema.listarQuartos();
        if (quartos.isEmpty()) {
            System.out.println("Nenhum quarto cadastrado.");
            return;
        }
        for (Quarto quarto : quartos) {
            System.out.println(quarto);
        }
    }

    private void buscarQuarto() {
        try {
            System.out.println(sistema.buscarQuarto(console.lerInteiro("Número do quarto: ")));
        } catch (RegraNegocioException e) {
            console.mostrarErro(e);
        }
    }

    private void alterarQuarto() {
        try {
            int numero = console.lerInteiro("Número do quarto: ");
            System.out.println("Atual: " + sistema.buscarQuarto(numero));
            sistema.alterarQuarto(numero, lerTipoQuarto(), console.lerInteiro("Nova capacidade: "),
                    console.lerDinheiro("Nova diária: "));
            System.out.println("Quarto alterado. O número permanece o mesmo.");
        } catch (RegraNegocioException | PersistenciaException e) {
            console.mostrarErro(e);
        }
    }

    private void alternarManutencao() {
        try {
            int numero = console.lerInteiro("Número do quarto: ");
            sistema.alternarManutencao(numero);
            System.out.println("Situação atual: " + sistema.buscarQuarto(numero).getStatus());
        } catch (RegraNegocioException | PersistenciaException e) {
            console.mostrarErro(e);
        }
    }

    private void excluirQuarto() {
        try {
            int numero = console.lerInteiro("Número do quarto: ");
            System.out.println(sistema.buscarQuarto(numero));
            if (!console.lerSimNao("Confirma a exclusão? (S/N): ")) {
                System.out.println("Exclusão cancelada.");
                return;
            }
            sistema.excluirQuarto(numero);
            System.out.println("Quarto excluído.");
        } catch (RegraNegocioException | PersistenciaException e) {
            console.mostrarErro(e);
        }
    }

    private TipoQuarto lerTipoQuarto() {
        System.out.println("Tipo do quarto:");
        TipoQuarto[] tipos = TipoQuarto.values();
        for (int i = 0; i < tipos.length; i++) {
            System.out.println((i + 1) + " - " + tipos[i]);
        }
        return tipos[console.lerOpcao("Opção: ", 1, tipos.length) - 1];
    }

    private void menuReservas() {
        boolean voltar = false;
        while (!voltar) {
            System.out.println();
            System.out.println("=== Reservas ===");
            System.out.println("1 - Incluir");
            System.out.println("2 - Listar");
            System.out.println("3 - Buscar por código");
            System.out.println("4 - Alterar");
            System.out.println("5 - Cancelar");
            System.out.println("6 - Check-in");
            System.out.println("7 - Check-out");
            System.out.println("0 - Voltar");
            switch (console.lerOpcao("Opção: ", 0, 7)) {
                case 1 -> incluirReserva();
                case 2 -> listarReservas();
                case 3 -> buscarReserva();
                case 4 -> alterarReserva();
                case 5 -> cancelarReserva();
                case 6 -> realizarCheckIn();
                case 7 -> realizarCheckOut();
                default -> voltar = true;
            }
        }
    }

    private void incluirReserva() {
        try {
            Reserva reserva = sistema.incluirReserva(
                    console.lerTextoObrigatorio("CPF do hóspede: "),
                    console.lerInteiro("Número do quarto: "),
                    console.lerData("Entrada (dd/MM/aaaa): "),
                    console.lerData("Saída (dd/MM/aaaa): "),
                    console.lerInteiro("Quantidade de pessoas: "));
            System.out.println("Reserva confirmada.");
            System.out.println(reserva);
        } catch (RegraNegocioException | PersistenciaException e) {
            console.mostrarErro(e);
        }
    }

    private void listarReservas() {
        List<Reserva> reservas = sistema.listarReservas();
        if (reservas.isEmpty()) {
            System.out.println("Nenhuma reserva cadastrada.");
            return;
        }
        for (Reserva reserva : reservas) {
            System.out.println(reserva);
        }
    }

    private void buscarReserva() {
        try {
            System.out.println(sistema.consultarReserva(console.lerInteiro("Código da reserva: ")));
        } catch (RegraNegocioException e) {
            console.mostrarErro(e);
        }
    }

    private void alterarReserva() {
        try {
            int codigo = console.lerInteiro("Código da reserva: ");
            System.out.println(sistema.consultarReserva(codigo));
            sistema.alterarReserva(
                    codigo,
                    console.lerData("Nova entrada (dd/MM/aaaa): "),
                    console.lerData("Nova saída (dd/MM/aaaa): "),
                    console.lerInteiro("Novo número do quarto: "),
                    console.lerInteiro("Nova quantidade de pessoas: "));
            System.out.println("Reserva alterada.");
            System.out.println(sistema.consultarReserva(codigo));
        } catch (RegraNegocioException | PersistenciaException e) {
            console.mostrarErro(e);
        }
    }

    private void cancelarReserva() {
        try {
            int codigo = console.lerInteiro("Código da reserva: ");
            System.out.println(sistema.consultarReserva(codigo));
            if (!console.lerSimNao("Confirma o cancelamento? (S/N): ")) {
                System.out.println("Cancelamento não realizado.");
                return;
            }
            System.out.println("Reserva cancelada.");
            System.out.println(sistema.cancelarReserva(codigo));
        } catch (RegraNegocioException | PersistenciaException e) {
            console.mostrarErro(e);
        }
    }

    private void realizarCheckIn() {
        try {
            sistema.realizarCheckIn(console.lerInteiro("Código da reserva: "));
            System.out.println("Check-in realizado. O quarto está ocupado.");
        } catch (RegraNegocioException | PersistenciaException e) {
            console.mostrarErro(e);
        }
    }

    private void realizarCheckOut() {
        try {
            System.out.println(sistema.realizarCheckOut(
                    console.lerInteiro("Código da reserva: "),
                    console.lerData("Data real de saída (dd/MM/aaaa): ")));
        } catch (RegraNegocioException | PersistenciaException e) {
            console.mostrarErro(e);
        }
    }

    private void menuPagamentos() {
        boolean voltar = false;
        while (!voltar) {
            System.out.println();
            System.out.println("=== Pagamentos ===");
            System.out.println("1 - Lançar pagamento");
            System.out.println("2 - Consultar saldo da reserva");
            System.out.println("0 - Voltar");
            switch (console.lerOpcao("Opção: ", 0, 2)) {
                case 1 -> lancarPagamento();
                case 2 -> consultarSaldo();
                default -> voltar = true;
            }
        }
    }

    private void lancarPagamento() {
        try {
            System.out.println("Pagamento lançado.");
            System.out.println(sistema.lancarPagamento(
                    console.lerInteiro("Código da reserva: "),
                    console.lerDinheiro("Valor: "),
                    lerFormaPagamento()));
        } catch (RegraNegocioException | PersistenciaException e) {
            console.mostrarErro(e);
        }
    }

    private void consultarSaldo() {
        try {
            System.out.println(sistema.consultarReserva(console.lerInteiro("Código da reserva: ")));
        } catch (RegraNegocioException e) {
            console.mostrarErro(e);
        }
    }

    private FormaPagamento lerFormaPagamento() {
        System.out.println("Forma de pagamento:");
        FormaPagamento[] formas = FormaPagamento.values();
        for (int i = 0; i < formas.length; i++) {
            System.out.println((i + 1) + " - " + formas[i]);
        }
        return formas[console.lerOpcao("Opção: ", 1, formas.length) - 1];
    }

    @Override
    public String toString() {
        return "Menu da recepção";
    }

    @Override
    public boolean equals(Object outro) {
        if (this == outro) {
            return true;
        }
        if (!(outro instanceof Menu)) {
            return false;
        }
        return sistema.equals(((Menu) outro).sistema);
    }

    @Override
    public int hashCode() {
        return sistema.hashCode();
    }
}
