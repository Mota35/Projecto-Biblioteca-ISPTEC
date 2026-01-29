package main.java.isptec.biblioteca.service;

import main.java.isptec.biblioteca.domain.entities.Reserva;

public interface ReservaService {
    void reservarLivro(Reserva reserva);
    void cancelarReserva(int reservaId);
    List<Reserva> listarReservas();
}
