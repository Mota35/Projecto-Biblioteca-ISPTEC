package isptec.biblioteca.service;

import java.util.List;
import isptec.biblioteca.domain.entities.Reserva;
import isptec.biblioteca.domain.entities.Estudante;
import isptec.biblioteca.domain.entities.Livro;

public interface ReservaService {
    void reservarLivro(Estudante membro, Livro livro);
    void cancelarReserva(int reservaId);
    List<Reserva> listarReservas();
}
