package isptec.biblioteca.service.impl;

import isptec.biblioteca.domain.entities.Reserva;
import isptec.biblioteca.domain.entities.Estudante;
import isptec.biblioteca.domain.entities.Livro;
import isptec.biblioteca.service.ReservaService;
import java.util.ArrayList;
import java.util.List;

public class ReservaServiceImpl implements ReservaService {

    private final List<Reserva> reservas = new ArrayList<>();
    private int nextId = 1;

    @Override
    public void reservarLivro(Estudante membro, Livro livro) {
        Reserva r = new Reserva(nextId++, livro, membro, java.time.LocalDate.now());
        reservas.add(r);
    }

    @Override
    public void cancelarReserva(int reservaId) {
        for (Reserva r : reservas) {
            if (r.getId() == reservaId) {
                r.setAtiva(false);
                break;
            }
        }
    }

    @Override
    public List<Reserva> listarReservas() {
        return new ArrayList<>(reservas);
    }
}
