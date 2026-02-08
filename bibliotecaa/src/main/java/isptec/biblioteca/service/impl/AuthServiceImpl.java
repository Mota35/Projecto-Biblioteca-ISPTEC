package isptec.biblioteca.service.impl;

import isptec.biblioteca.model.entities.Bibliotecario;
import isptec.biblioteca.model.entities.Estudante;
import isptec.biblioteca.model.entities.Pessoa;
import isptec.biblioteca.service.AuthService;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementação do serviço de autenticação.
 * Utiliza mapas em memória (pode ser substituída por base de dados).
 */
public class AuthServiceImpl implements AuthService {

    private final Map<String, String> passwords = new HashMap<>();
    private final Map<String, Pessoa> users = new HashMap<>();
    private Pessoa usuarioLogado = null;

    public AuthServiceImpl() {
        // Utilizadores de exemplo
        Bibliotecario admin = new Bibliotecario(1, "Administrador", "admin001@isptec.co.ao", "FUNC001");
        admin.setSenha("admin123");

        Estudante estudante = new Estudante(2, "Aluno Teste", "20230001@isptec.co.ao", "20230001");
        estudante.setSenha("1234");

        users.put(admin.getEmail(), admin);
        passwords.put(admin.getEmail(), "admin123");

        users.put(estudante.getEmail(), estudante);
        passwords.put(estudante.getEmail(), "1234");
    }

    @Override
    public Pessoa login(String email, String senha) {
        if (email == null || senha == null) {
            return null;
        }
        String pw = passwords.get(email);
        if (pw != null && pw.equals(senha)) {
            this.usuarioLogado = users.get(email);
            return this.usuarioLogado;
        }
        return null;
    }

    @Override
    public void logout() {
        this.usuarioLogado = null;
    }

    @Override
    public Pessoa getUsuarioLogado() {
        return this.usuarioLogado;
    }

    @Override
    public boolean isAutenticado() {
        return this.usuarioLogado != null;
    }

    @Override
    public boolean registrarEstudante(Estudante estudante) {
        if (estudante == null || estudante.getEmail() == null || estudante.getSenha() == null) {
            return false;
        }
        
        // Verifica se o email já está registrado
        if (users.containsKey(estudante.getEmail())) {
            return false;
        }

        // Verifica se a matrícula já existe
        if (matriculaExiste(estudante.getMatricula())) {
            return false;
        }

        // Gera um ID único (simulado)
        int id = users.size() + 1;
        estudante.setId(id);
        
        // Se não tiver matrícula, gera uma
        if (estudante.getMatricula() == null || estudante.getMatricula().isEmpty()) {
            estudante.setMatricula("2024" + String.format("%03d", id));
        }
        
        // Registra o utilizador
        users.put(estudante.getEmail(), estudante);
        passwords.put(estudante.getEmail(), estudante.getSenha());
        
        return true;
    }

    @Override
    public boolean registrarBibliotecario(Bibliotecario bibliotecario) {
        if (bibliotecario == null || bibliotecario.getEmail() == null || bibliotecario.getSenha() == null) {
            return false;
        }
        
        // Verifica se o email já está registrado
        if (users.containsKey(bibliotecario.getEmail())) {
            return false;
        }
        
        // Gera um ID único (simulado)
        int id = users.size() + 1;
        bibliotecario.setId(id);
        
        // Se não tiver funcionarioId, gera um
        if (bibliotecario.getFuncionarioId() == null || bibliotecario.getFuncionarioId().isEmpty()) {
            bibliotecario.setFuncionarioId("FUNC" + String.format("%03d", id));
        }
        
        // Registra o utilizador
        users.put(bibliotecario.getEmail(), bibliotecario);
        passwords.put(bibliotecario.getEmail(), bibliotecario.getSenha());
        
        return true;
    }

    @Override
    public boolean alterarSenha(String email, String senhaAntiga, String senhaNova) {
        if (email == null || senhaAntiga == null || senhaNova == null) {
            return false;
        }

        String senhaAtual = passwords.get(email);
        if (senhaAtual != null && senhaAtual.equals(senhaAntiga)) {
            passwords.put(email, senhaNova);
            Pessoa pessoa = users.get(email);
            if (pessoa != null) {
                pessoa.setSenha(senhaNova);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean emailExiste(String email) {
        return email != null && users.containsKey(email);
    }

    @Override
    public boolean matriculaExiste(String matricula) {
        if (matricula == null || matricula.isEmpty()) {
            return false;
        }
        return users.values().stream()
                .filter(p -> p instanceof Estudante)
                .map(p -> (Estudante) p)
                .anyMatch(e -> matricula.equals(e.getMatricula()));
    }

    /**
     * Obtém todos os utilizadores registados (para administração).
     *
     * @return mapa de utilizadores
     */
    public Map<String, Pessoa> getUsers() {
        return new HashMap<>(users);
    }
}
