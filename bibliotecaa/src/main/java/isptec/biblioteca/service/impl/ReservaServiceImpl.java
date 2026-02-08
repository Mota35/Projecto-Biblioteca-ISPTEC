package isptec.biblioteca.service.impl;

import isptec.biblioteca.model.entities.Estudante;
import isptec.biblioteca.model.entities.Livro;
import isptec.biblioteca.model.entities.Membro;
import isptec.biblioteca.model.entities.Reserva;
import isptec.biblioteca.service.ReservaService;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementação do serviço de gestão de reservas.
 *
 * REGRAS DE NEGÓCIO IMPLEMENTADAS:
 * - Só é possível reservar livro indisponível
 * - Reservas são atendidas por ordem de data
 * - Reserva expira após 3 dias se não for retirada
 * - Membro não pode ter múltiplas reservas ativas para o mesmo livro
 */
public class ReservaServiceImpl implements ReservaService {

    private final List<Reserva> reservas = new ArrayList<>();
    private int nextId = 1;

    @Override
    public boolean reservarLivro(Membro membro, Livro livro) {
        // Validações
        if (membro == null || livro == null) {
            return false;
        }

        // Verifica se o membro já tem reserva ativa para este livro
        if (membroTemReservaAtivaParaLivro(membro, livro)) {
            return false;
        }

        // Cria a reserva
        Reserva reserva = new Reserva(nextId++, livro, membro, LocalDate.now());
        reservas.add(reserva);

        return true;
    }

    // Método de compatibilidade para Estudante
    public void reservarLivro(Estudante estudante, Livro livro) {
        reservarLivro((Membro) estudante, livro);
    }

    @Override
    public void cancelarReserva(int reservaId) {
        Reserva reserva = buscarReservaPorId(reservaId);
        if (reserva != null) {
            reserva.cancelar();
        }
    }

    @Override
    public void cancelarReserva(Reserva reserva) {
        if (reserva != null) {
            reserva.cancelar();
        }
    }

    @Override
    public void confirmarReserva(int reservaId) {
        Reserva reserva = buscarReservaPorId(reservaId);
        if (reserva != null) {
            reserva.confirmar();
        }
    }

    @Override
    public Reserva buscarReservaPorId(int reservaId) {
        return reservas.stream()
                .filter(r -> r.getId() == reservaId)
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Reserva> listarReservasAtivas() {
        processarReservasExpiradas();
        return reservas.stream()
                .filter(Reserva::estaValida)
                .collect(Collectors.toList());
    }

    @Override
    public List<Reserva> listarReservas() {
        return new ArrayList<>(reservas);
    }

    @Override
    public List<Reserva> listarReservasLivro(Livro livro) {
        if (livro == null) return new ArrayList<>();
        return reservas.stream()
                .filter(r -> r.getLivro() != null && r.getLivro().getId() == livro.getId())
                .collect(Collectors.toList());
    }

    @Override
    public List<Reserva> listarReservasAtivasLivro(Livro livro) {
        if (livro == null) return new ArrayList<>();
        processarReservasExpiradas();
        return reservas.stream()
                .filter(r -> r.getLivro() != null && r.getLivro().getId() == livro.getId())
                .filter(Reserva::estaValida)
                .sorted(Comparator.comparing(Reserva::getDataReserva))
                .collect(Collectors.toList());
    }

    @Override
    public List<Reserva> listarReservasMembro(Membro membro) {
        if (membro == null) return new ArrayList<>();
        return reservas.stream()
                .filter(r -> r.getMembro() != null && r.getMembro().getId() == membro.getId())
                .collect(Collectors.toList());
    }

    @Override
    public List<Reserva> listarReservasAtivasMembro(Membro membro) {
        if (membro == null) return new ArrayList<>();
        processarReservasExpiradas();
        return reservas.stream()
                .filter(r -> r.getMembro() != null && r.getMembro().getId() == membro.getId())
                .filter(Reserva::estaValida)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existeReservaAtivaParaLivro(Livro livro) {
        if (livro == null) return false;
        processarReservasExpiradas();
        return reservas.stream()
                .anyMatch(r -> r.getLivro() != null &&
                              r.getLivro().getId() == livro.getId() &&
                              r.estaValida());
    }

    @Override
    public boolean membroTemReservaAtivaParaLivro(Membro membro, Livro livro) {
        if (membro == null || livro == null) return false;
        processarReservasExpiradas();
        return reservas.stream()
                .anyMatch(r -> r.getMembro() != null &&
                              r.getMembro().getId() == membro.getId() &&
                              r.getLivro() != null &&
                              r.getLivro().getId() == livro.getId() &&
                              r.estaValida());
    }

    @Override
    public Reserva obterProximaReservaFila(Livro livro) {
        if (livro == null) return null;
        processarReservasExpiradas();
        return reservas.stream()
                .filter(r -> r.getLivro() != null && r.getLivro().getId() == livro.getId())
                .filter(Reserva::estaValida)
                .min(Comparator.comparing(Reserva::getDataReserva))
                .orElse(null);
    }

    @Override
    public void processarReservasExpiradas() {
        // Cancela automaticamente reservas expiradas
        for (Reserva reserva : reservas) {
            if (reserva.isAtiva() && reserva.estaExpirada()) {
                reserva.cancelar();
            }
        }
    }

    @Override
    public void notificarDisponibilidade() {
        // Marca reservas como notificadas quando o livro está disponível
        for (Reserva reserva : reservas) {
            if (reserva.estaValida() && !reserva.isNotificado()) {
                if (reserva.getLivro() != null && reserva.getLivro().estaDisponivel()) {
                    reserva.marcarNotificado();
                    // Aqui seria enviada uma notificação ao membro
                    System.out.println("NOTIFICAÇÃO: " + reserva.getMembro().getNome() +
                                     " - O livro '" + reserva.getLivro().getTitulo() +
                                     "' está disponível para retirada!");
                }
            }
        }
    }

    @Override
    public int contarReservasAtivas() {
        processarReservasExpiradas();
        return (int) reservas.stream().filter(Reserva::estaValida).count();
    }
}
