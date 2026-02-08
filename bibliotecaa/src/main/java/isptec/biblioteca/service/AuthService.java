package isptec.biblioteca.service;

import isptec.biblioteca.model.entities.Bibliotecario;
import isptec.biblioteca.model.entities.Estudante;
import isptec.biblioteca.model.entities.Pessoa;

/**
 * Interface de serviço para autenticação.
 * Define as operações de login, logout e gestão de credenciais.
 */
public interface AuthService {

    /**
     * Realiza o login de um utilizador.
     * @param email o email do utilizador
     * @param senha a senha do utilizador
     * @return a Pessoa autenticada ou null se falhar
     */
    Pessoa login(String email, String senha);

    /**
     * Realiza o logout do utilizador atual.
     */
    void logout();

    /**
     * Obtém o utilizador atualmente logado.
     */
    Pessoa getUsuarioLogado();

    /**
     * Verifica se há um utilizador autenticado.
     */
    boolean isAutenticado();

    /**
     * Registra um novo estudante no sistema.
     * @return true se registrado com sucesso
     */
    boolean registrarEstudante(Estudante estudante);

    /**
     * Registra um novo bibliotecário no sistema.
     * @return true se registrado com sucesso
     */
    boolean registrarBibliotecario(Bibliotecario bibliotecario);

    /**
     * Altera a senha de um utilizador.
     * @return true se alterada com sucesso
     */
    boolean alterarSenha(String email, String senhaAntiga, String senhaNova);

    /**
     * Verifica se um email já está registrado.
     */
    boolean emailExiste(String email);

    /**
     * Verifica se uma matrícula já está registrada.
     */
    boolean matriculaExiste(String matricula);
}
