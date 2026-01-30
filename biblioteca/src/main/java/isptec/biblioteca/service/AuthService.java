package isptec.biblioteca.service;

import isptec.biblioteca.domain.entities.Pessoa;
import isptec.biblioteca.domain.entities.Bibliotecario;
import isptec.biblioteca.domain.entities.Estudante;

public interface AuthService {

    Pessoa login(String email, String senha);

    void logout();

    boolean registrarEstudante(Estudante estudante);

    boolean registrarBibliotecario(Bibliotecario bibliotecario);
}
