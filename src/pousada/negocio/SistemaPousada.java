package pousada.negocio;

import pousada.modelo.FormaPagamento;
import pousada.modelo.Hospede;
import pousada.modelo.Pagamento;
import pousada.modelo.Quarto;
import pousada.modelo.Reserva;
import pousada.modelo.StatusQuarto;
import pousada.modelo.StatusReserva;
import pousada.modelo.Textos;
import pousada.modelo.TipoQuarto;
import pousada.persistencia.GerenciadorArquivo;
import pousada.persistencia.PersistenciaException;
import pousada.persistencia.Sequencia;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * Cadastro da pousada: hóspedes, quartos, reservas, pagamentos, multa e saldo.
 * Cada mudança é gravada na hora.
 */
public class SistemaPousada {

    private final GerenciadorArquivo arquivos;
    private List<Hospede> hospedes = new ArrayList<>();
    private List<Quarto> quartos = new ArrayList<>();
    private List<Reserva> reservas = new ArrayList<>();
    private List<Pagamento> pagamentos = new ArrayList<>();
    private Sequencia sequencia = new Sequencia();
    private boolean exemploCriado;

    public SistemaPousada(GerenciadorArquivo arquivos) {
        this.arquivos = arquivos;
    }

    public static SistemaPousada abrir() throws PersistenciaException {
        SistemaPousada sistema = new SistemaPousada(new GerenciadorArquivo());
        sistema.carregar();
        return sistema;
    }

    public boolean isExemploCriado() {
        return exemploCriado;
    }

    public String getCaminhoDados() {
        return arquivos.getPasta().toAbsolutePath().toString();
    }

    public void incluirHospede(String nome, String cpf, String telefone, String email, String endereco)
            throws RegraNegocioException, PersistenciaException {
        String nomeValido = Validador.nome(nome);
        String cpfValido = Validador.cpf(cpf);
        if (localizarHospede(cpfValido) != null) {
            throw new RegraNegocioException("Já existe hóspede com este CPF.");
        }
        hospedes.add(new Hospede(
                nomeValido,
                cpfValido,
                Validador.telefone(telefone),
                Validador.email(email),
                Validador.endereco(endereco)));
        salvar();
    }

    public List<Hospede> listarHospedes() {
        return List.copyOf(hospedes);
    }

    public Hospede buscarHospedePorCpf(String cpf) throws RegraNegocioException {
        Hospede hospede = localizarHospede(Validador.cpf(cpf));
        if (hospede == null) {
            throw new RegraNegocioException("Hóspede não encontrado.");
        }
        return hospede;
    }

    public List<Hospede> buscarHospedesPorNome(String trecho) throws RegraNegocioException {
        if (trecho == null || trecho.trim().length() < 3) {
            throw new RegraNegocioException("Informe pelo menos 3 caracteres do nome.");
        }
        String busca = trecho.trim().toLowerCase(Locale.ROOT);
        List<Hospede> achados = new ArrayList<>();
        for (Hospede hospede : hospedes) {
            if (hospede.getNome().toLowerCase(Locale.ROOT).contains(busca)) {
                achados.add(hospede);
            }
        }
        return achados;
    }

    public void alterarHospede(String cpf, String nome, String telefone, String email, String endereco)
            throws RegraNegocioException, PersistenciaException {
        Hospede hospede = buscarHospedePorCpf(cpf);
        hospede.setNome(Validador.nome(nome));
        hospede.setTelefone(Validador.telefone(telefone));
        hospede.setEmail(Validador.email(email));
        hospede.setEndereco(Validador.endereco(endereco));
        salvar();
    }

    public void excluirHospede(String cpf) throws RegraNegocioException, PersistenciaException {
        Hospede hospede = buscarHospedePorCpf(cpf);
        for (Reserva reserva : reservas) {
            if (reserva.getCpfHospede().equals(hospede.getCpf()) && reservaAtiva(reserva)) {
                throw new RegraNegocioException(
                        "Não é possível excluir hóspede com reserva confirmada ou em check-in.");
            }
        }
        hospedes.remove(hospede);
        salvar();
    }

    public void incluirQuarto(int numero, TipoQuarto tipo, int capacidade, BigDecimal diaria)
            throws RegraNegocioException, PersistenciaException {
        int numeroValido = Validador.numeroQuarto(numero);
        if (tipo == null) {
            throw new RegraNegocioException("Tipo de quarto obrigatório.");
        }
        if (localizarQuarto(numeroValido) != null) {
            throw new RegraNegocioException("Já existe quarto com este número.");
        }
        quartos.add(new Quarto(numeroValido, tipo, Validador.capacidade(capacidade), Validador.diaria(diaria)));
        salvar();
    }

    public List<Quarto> listarQuartos() {
        return List.copyOf(quartos);
    }

    public Quarto buscarQuarto(int numero) throws RegraNegocioException {
        Quarto quarto = localizarQuarto(Validador.numeroQuarto(numero));
        if (quarto == null) {
            throw new RegraNegocioException("Quarto não encontrado.");
        }
        return quarto;
    }

    public void alterarQuarto(int numero, TipoQuarto tipo, int capacidade, BigDecimal diaria)
            throws RegraNegocioException, PersistenciaException {
        Quarto quarto = buscarQuarto(numero);
        if (tipo == null) {
            throw new RegraNegocioException("Tipo de quarto obrigatório.");
        }
        int capacidadeValida = Validador.capacidade(capacidade);
        for (Reserva reserva : reservas) {
            if (reserva.getNumeroQuarto() == quarto.getNumero()
                    && reservaAtiva(reserva)
                    && reserva.getQuantidadePessoas() > capacidadeValida) {
                throw new RegraNegocioException(
                        "Capacidade menor que a quantidade de pessoas de uma reserva ativa deste quarto.");
            }
        }
        quarto.setTipo(tipo);
        quarto.setCapacidade(capacidadeValida);
        quarto.setValorDiaria(Validador.diaria(diaria));
        salvar();
    }

    public void alternarManutencao(int numero) throws RegraNegocioException, PersistenciaException {
        Quarto quarto = buscarQuarto(numero);
        if (quarto.getStatus() == StatusQuarto.OCUPADO) {
            throw new RegraNegocioException("Quarto ocupado não pode ir para manutenção.");
        }
        if (quarto.getStatus() == StatusQuarto.MANUTENCAO) {
            quarto.setStatus(StatusQuarto.DISPONIVEL);
        } else {
            quarto.setStatus(StatusQuarto.MANUTENCAO);
        }
        salvar();
    }

    public void excluirQuarto(int numero) throws RegraNegocioException, PersistenciaException {
        Quarto quarto = buscarQuarto(numero);
        for (Reserva reserva : reservas) {
            if (reserva.getNumeroQuarto() == quarto.getNumero() && reservaAtiva(reserva)) {
                throw new RegraNegocioException(
                        "Não é possível excluir quarto com reserva confirmada ou em check-in.");
            }
        }
        quartos.remove(quarto);
        salvar();
    }

    public Reserva incluirReserva(String cpf, int numeroQuarto, LocalDate entrada, LocalDate saida, int quantidade)
            throws RegraNegocioException, PersistenciaException {
        Hospede hospede = buscarHospedePorCpf(cpf);
        Quarto quarto = prepararQuartoParaReserva(numeroQuarto, quantidade);
        validarDatas(entrada, saida);
        if (periodoConflita(quarto.getNumero(), entrada, saida, -1)) {
            throw new RegraNegocioException("Quarto indisponível no período informado.");
        }
        Reserva reserva = new Reserva(
                sequencia.gerarCodigoReserva(),
                hospede.getCpf(),
                quarto.getNumero(),
                entrada,
                saida,
                quantidade,
                calcularHospedagem(quarto, entrada, saida),
                LocalDate.now());
        reservas.add(reserva);
        salvar();
        return reserva;
    }

    public List<Reserva> listarReservas() {
        return List.copyOf(reservas);
    }

    public Reserva buscarReserva(int codigo) throws RegraNegocioException {
        Reserva reserva = localizarReserva(codigo);
        if (reserva == null) {
            throw new RegraNegocioException("Reserva não encontrada.");
        }
        return reserva;
    }

    public String consultarReserva(int codigo) throws RegraNegocioException {
        return textoConta(buscarReserva(codigo));
    }

    public void alterarReserva(int codigo, LocalDate entrada, LocalDate saida, int numeroQuarto, int quantidade)
            throws RegraNegocioException, PersistenciaException {
        Reserva reserva = buscarReserva(codigo);
        if (reserva.getStatus() != StatusReserva.CONFIRMADA) {
            throw new RegraNegocioException("Reserva só pode ser alterada enquanto estiver confirmada.");
        }
        if (possuiPagamento(codigo)) {
            throw new RegraNegocioException(
                    "Reserva com pagamento não pode ser alterada. Cancele, com a multa, e abra outra.");
        }
        Quarto quarto = prepararQuartoParaReserva(numeroQuarto, quantidade);
        validarDatas(entrada, saida);
        if (periodoConflita(quarto.getNumero(), entrada, saida, reserva.getCodigo())) {
            throw new RegraNegocioException("Quarto indisponível no período informado.");
        }
        reserva.setDataEntrada(entrada);
        reserva.setDataSaida(saida);
        reserva.setNumeroQuarto(quarto.getNumero());
        reserva.setQuantidadePessoas(quantidade);
        reserva.setValorHospedagem(calcularHospedagem(quarto, entrada, saida));
        salvar();
    }

    public String cancelarReserva(int codigo) throws RegraNegocioException, PersistenciaException {
        Reserva reserva = buscarReserva(codigo);
        if (reserva.getStatus() != StatusReserva.CONFIRMADA) {
            throw new RegraNegocioException("Cancelamento permitido apenas para reserva confirmada.");
        }
        reserva.setValorMulta(calcularMultaCancelamento(reserva));
        reserva.setStatus(StatusReserva.CANCELADA);
        salvar();
        return textoConta(reserva);
    }

    public void realizarCheckIn(int codigo) throws RegraNegocioException, PersistenciaException {
        Reserva reserva = buscarReserva(codigo);
        if (reserva.getStatus() != StatusReserva.CONFIRMADA) {
            throw new RegraNegocioException("Check-in exige reserva confirmada.");
        }
        Quarto quarto = buscarQuarto(reserva.getNumeroQuarto());
        if (quarto.getStatus() != StatusQuarto.DISPONIVEL) {
            throw new RegraNegocioException("Check-in exige quarto disponível.");
        }
        reserva.setStatus(StatusReserva.CHECKIN);
        quarto.setStatus(StatusQuarto.OCUPADO);
        salvar();
    }

    public String realizarCheckOut(int codigo, LocalDate dataReal)
            throws RegraNegocioException, PersistenciaException {
        Reserva reserva = buscarReserva(codigo);
        if (reserva.getStatus() != StatusReserva.CHECKIN) {
            throw new RegraNegocioException("Check-out exige reserva em check-in.");
        }
        if (dataReal == null || dataReal.isBefore(reserva.getDataEntrada())) {
            throw new RegraNegocioException("Data real de saída não pode ser anterior à data de entrada.");
        }
        long diasAtraso = Math.max(0, ChronoUnit.DAYS.between(reserva.getDataSaida(), dataReal));
        reserva.setValorMulta(calcularMultaAtraso(reserva, diasAtraso));
        String atraso = diasAtraso > 0
                ? "Atraso de " + diasAtraso + " dia(s). Multa: " + Textos.dinheiro(reserva.getValorMulta()) + ". "
                : "Sem multa de atraso. ";
        if (saldo(reserva).compareTo(BigDecimal.ZERO) > 0) {
            salvar();
            return atraso + "Check-out não concluído. Quite o saldo de "
                    + Textos.dinheiro(saldo(reserva).max(BigDecimal.ZERO))
                    + " e repita o check-out." + System.lineSeparator() + textoConta(reserva);
        }
        reserva.setStatus(StatusReserva.CHECKOUT);
        buscarQuarto(reserva.getNumeroQuarto()).setStatus(StatusQuarto.DISPONIVEL);
        salvar();
        String restituir = "";
        if (saldo(reserva).compareTo(BigDecimal.ZERO) < 0) {
            restituir = " Valor a restituir: " + Textos.dinheiro(saldo(reserva).negate()) + ".";
        }
        return atraso + "Check-out concluído. O quarto voltou a ficar disponível."
                + restituir + System.lineSeparator() + textoConta(reserva);
    }

    public String lancarPagamento(int codigoReserva, BigDecimal valor, FormaPagamento forma)
            throws RegraNegocioException, PersistenciaException {
        if (forma == null) {
            throw new RegraNegocioException("Forma de pagamento obrigatória.");
        }
        Reserva reserva = buscarReserva(codigoReserva);
        validarRecebePagamento(reserva);
        BigDecimal valorValido = Validador.pagamento(valor);
        BigDecimal aberto = saldo(reserva).max(BigDecimal.ZERO);
        if (aberto.compareTo(BigDecimal.ZERO) == 0) {
            throw new RegraNegocioException("Não há saldo em aberto para esta reserva.");
        }
        if (valorValido.compareTo(aberto) > 0) {
            throw new RegraNegocioException(
                    "Pagamento não pode ser maior que o saldo em aberto (" + Textos.dinheiro(aberto) + ").");
        }
        pagamentos.add(new Pagamento(
                sequencia.gerarCodigoPagamento(),
                reserva.getCodigo(),
                valorValido,
                forma,
                LocalDate.now()));
        salvar();
        return textoConta(reserva);
    }

    private void carregar() throws PersistenciaException {
        boolean novo = !arquivos.possuiCadastro();
        hospedes = arquivos.carregarHospedes();
        quartos = arquivos.carregarQuartos();
        reservas = arquivos.carregarReservas();
        pagamentos = arquivos.carregarPagamentos();
        sequencia = arquivos.possuiSequencia() ? arquivos.carregarSequencia() : reconstruirSequencia();
        if (novo) {
            semearExemplo();
            exemploCriado = true;
            salvar();
        }
    }

    private void semearExemplo() {
        hospedes.add(new Hospede("Ana Souza", "52998224725", "11987654321", "ana@email.com", "Rua das Flores, 10"));
        hospedes.add(new Hospede("Bruno Lima", "39053344705", "21999887766", "bruno@email.com", "Av. Brasil, 200"));
        hospedes.add(new Hospede("Carla Mendes", "11144477735", "31988776655", "carla@email.com", "Rua do Porto, 45"));
        quartos.add(new Quarto(101, TipoQuarto.SIMPLES, 1, new BigDecimal("150.00")));
        quartos.add(new Quarto(102, TipoQuarto.DUPLO, 2, new BigDecimal("220.00")));
        quartos.add(new Quarto(201, TipoQuarto.SUITE, 3, new BigDecimal("350.00")));
        quartos.add(new Quarto(301, TipoQuarto.CHALE, 4, new BigDecimal("480.00")));
    }

    private Sequencia reconstruirSequencia() {
        Sequencia nova = new Sequencia();
        int maiorReserva = 0;
        for (Reserva reserva : reservas) {
            maiorReserva = Math.max(maiorReserva, reserva.getCodigo());
        }
        int maiorPagamento = 0;
        for (Pagamento pagamento : pagamentos) {
            maiorPagamento = Math.max(maiorPagamento, pagamento.getCodigo());
        }
        nova.setProximoCodigoReserva(maiorReserva + 1);
        nova.setProximoCodigoPagamento(maiorPagamento + 1);
        return nova;
    }

    private void salvar() throws PersistenciaException {
        arquivos.salvarHospedes(hospedes);
        arquivos.salvarQuartos(quartos);
        arquivos.salvarReservas(reservas);
        arquivos.salvarPagamentos(pagamentos);
        arquivos.salvarSequencia(sequencia);
    }

    private Quarto prepararQuartoParaReserva(int numeroQuarto, int quantidade) throws RegraNegocioException {
        Quarto quarto = buscarQuarto(numeroQuarto);
        if (quarto.getStatus() == StatusQuarto.MANUTENCAO) {
            throw new RegraNegocioException("Quarto em manutenção não pode ser reservado.");
        }
        if (quantidade < 1 || quantidade > quarto.getCapacidade()) {
            throw new RegraNegocioException(
                    "Quantidade de pessoas deve estar entre 1 e a capacidade do quarto ("
                            + quarto.getCapacidade() + ").");
        }
        return quarto;
    }

    private void validarDatas(LocalDate entrada, LocalDate saida) throws RegraNegocioException {
        if (entrada == null || saida == null) {
            throw new RegraNegocioException("Informe a data de entrada e a data de saída.");
        }
        if (entrada.isBefore(LocalDate.now())) {
            throw new RegraNegocioException("Data de entrada não pode ser anterior a hoje.");
        }
        if (!saida.isAfter(entrada)) {
            throw new RegraNegocioException("Data de saída deve ser posterior à data de entrada.");
        }
    }

    private boolean periodoConflita(int numeroQuarto, LocalDate entrada, LocalDate saida, int codigoIgnorado) {
        for (Reserva reserva : reservas) {
            if (reserva.getCodigo() == codigoIgnorado || reserva.getNumeroQuarto() != numeroQuarto) {
                continue;
            }
            if (!reservaAtiva(reserva)) {
                continue;
            }
            if (entrada.isBefore(reserva.getDataSaida()) && reserva.getDataEntrada().isBefore(saida)) {
                return true;
            }
        }
        return false;
    }

    private BigDecimal calcularHospedagem(Quarto quarto, LocalDate entrada, LocalDate saida) {
        long diarias = ChronoUnit.DAYS.between(entrada, saida);
        return quarto.getValorDiaria().multiply(BigDecimal.valueOf(diarias)).setScale(2, RoundingMode.HALF_UP);
    }

    /** 7 dias ou mais: sem multa. De 3 a 6 dias: 30%. Menos de 3 dias: 50%. */
    private BigDecimal calcularMultaCancelamento(Reserva reserva) {
        long antecedencia = ChronoUnit.DAYS.between(LocalDate.now(), reserva.getDataEntrada());
        if (antecedencia >= 7) {
            return zero();
        }
        if (antecedencia >= 3) {
            return percentual(reserva.getValorHospedagem(), "0.30");
        }
        return percentual(reserva.getValorHospedagem(), "0.50");
    }

    /** Cada dia depois da saída prevista custa a diária da reserva mais 50%. */
    private BigDecimal calcularMultaAtraso(Reserva reserva, long diasAtraso) {
        if (diasAtraso <= 0) {
            return zero();
        }
        long diarias = ChronoUnit.DAYS.between(reserva.getDataEntrada(), reserva.getDataSaida());
        BigDecimal diaria = reserva.getValorHospedagem()
                .divide(BigDecimal.valueOf(diarias), 2, RoundingMode.HALF_UP);
        return diaria.multiply(new BigDecimal("1.5"))
                .multiply(BigDecimal.valueOf(diasAtraso))
                .setScale(2, RoundingMode.HALF_UP);
    }

    private void validarRecebePagamento(Reserva reserva) throws RegraNegocioException {
        if (reserva.getStatus() == StatusReserva.CONFIRMADA || reserva.getStatus() == StatusReserva.CHECKIN) {
            return;
        }
        if (reserva.getStatus() == StatusReserva.CANCELADA && saldo(reserva).compareTo(BigDecimal.ZERO) > 0) {
            return;
        }
        throw new RegraNegocioException("Não é possível lançar pagamento nesta reserva.");
    }

    /** Cancelada deve só a multa. As outras devem a hospedagem mais a multa, menos o que já foi pago. */
    private BigDecimal saldo(Reserva reserva) {
        BigDecimal devido = reserva.getStatus() == StatusReserva.CANCELADA
                ? reserva.getValorMulta()
                : reserva.getValorHospedagem().add(reserva.getValorMulta());
        return devido.subtract(totalPago(reserva.getCodigo())).setScale(2, RoundingMode.HALF_UP);
    }

    private String textoConta(Reserva reserva) {
        BigDecimal pago = totalPago(reserva.getCodigo());
        BigDecimal saldoAtual = saldo(reserva);
        String situacao = "Parcial";
        if (saldoAtual.compareTo(BigDecimal.ZERO) <= 0) {
            situacao = "Quitado";
        } else if (pago.compareTo(BigDecimal.ZERO) == 0) {
            situacao = "Em aberto";
        }
        Hospede hospede = localizarHospede(reserva.getCpfHospede());
        String nome = hospede == null
                ? "hóspede removido (" + Textos.cpf(reserva.getCpfHospede()) + ")"
                : hospede.getNome();
        StringBuilder texto = new StringBuilder();
        texto.append("Reserva ").append(reserva.getCodigo()).append(" - ").append(reserva.getStatus()).append('\n');
        texto.append("Hóspede: ").append(nome).append('\n');
        texto.append("Quarto: ").append(reserva.getNumeroQuarto()).append('\n');
        texto.append("Período: ").append(Textos.data(reserva.getDataEntrada()))
                .append(" a ").append(Textos.data(reserva.getDataSaida())).append('\n');
        texto.append("Hospedagem: ").append(Textos.dinheiro(reserva.getValorHospedagem())).append('\n');
        texto.append("Multa: ").append(Textos.dinheiro(reserva.getValorMulta())).append('\n');
        texto.append("Pago: ").append(Textos.dinheiro(pago)).append('\n');
        texto.append("Saldo em aberto: ").append(Textos.dinheiro(saldoAtual.max(BigDecimal.ZERO))).append('\n');
        if (saldoAtual.compareTo(BigDecimal.ZERO) < 0) {
            texto.append("Valor a restituir: ").append(Textos.dinheiro(saldoAtual.negate())).append('\n');
        }
        texto.append("Situação: ").append(situacao).append('\n');
        texto.append("Pagamentos:");
        boolean algum = false;
        for (Pagamento pagamento : pagamentos) {
            if (pagamento.getCodigoReserva() == reserva.getCodigo()) {
                texto.append('\n').append("  ").append(pagamento);
                algum = true;
            }
        }
        if (!algum) {
            texto.append(" nenhum");
        }
        return texto.toString();
    }

    private BigDecimal totalPago(int codigoReserva) {
        BigDecimal total = zero();
        for (Pagamento pagamento : pagamentos) {
            if (pagamento.getCodigoReserva() == codigoReserva) {
                total = total.add(pagamento.getValor());
            }
        }
        return total.setScale(2, RoundingMode.HALF_UP);
    }

    private boolean possuiPagamento(int codigoReserva) {
        for (Pagamento pagamento : pagamentos) {
            if (pagamento.getCodigoReserva() == codigoReserva) {
                return true;
            }
        }
        return false;
    }

    private boolean reservaAtiva(Reserva reserva) {
        return reserva.getStatus() == StatusReserva.CONFIRMADA || reserva.getStatus() == StatusReserva.CHECKIN;
    }

    private Hospede localizarHospede(String cpf) {
        for (Hospede hospede : hospedes) {
            if (hospede.getCpf().equals(cpf)) {
                return hospede;
            }
        }
        return null;
    }

    private Quarto localizarQuarto(int numero) {
        for (Quarto quarto : quartos) {
            if (quarto.getNumero() == numero) {
                return quarto;
            }
        }
        return null;
    }

    private Reserva localizarReserva(int codigo) {
        for (Reserva reserva : reservas) {
            if (reserva.getCodigo() == codigo) {
                return reserva;
            }
        }
        return null;
    }

    private static BigDecimal percentual(BigDecimal base, String fator) {
        return base.multiply(new BigDecimal(fator)).setScale(2, RoundingMode.HALF_UP);
    }

    private static BigDecimal zero() {
        return BigDecimal.ZERO.setScale(2);
    }

    @Override
    public String toString() {
        return "Pousada com " + hospedes.size() + " hóspede(s), "
                + quartos.size() + " quarto(s), "
                + reservas.size() + " reserva(s) e "
                + pagamentos.size() + " pagamento(s).";
    }

    @Override
    public boolean equals(Object outro) {
        if (this == outro) {
            return true;
        }
        if (!(outro instanceof SistemaPousada)) {
            return false;
        }
        SistemaPousada sistema = (SistemaPousada) outro;
        return hospedes.equals(sistema.hospedes)
                && quartos.equals(sistema.quartos)
                && reservas.equals(sistema.reservas)
                && pagamentos.equals(sistema.pagamentos)
                && sequencia.equals(sistema.sequencia);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hospedes, quartos, reservas, pagamentos, sequencia);
    }
}
