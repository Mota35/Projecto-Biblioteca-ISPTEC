package isptec.biblioteca.service.impl;

import isptec.biblioteca.domain.entities.Pessoa;
import isptec.biblioteca.domain.entities.Bibliotecario;
import isptec.biblioteca.domain.entities.Estudante;
import isptec.biblioteca.service.AuthService;
import java.util.HashMap;
import java.util.Map;

public class AuthServiceImpl implements AuthService {

    private final Map<String, String> passwords = new HashMap<>();
    private final Map<String, Pessoa> users = new HashMap<>();

    public AuthServiceImpl() {
        // sample users
        Bibliotecario admin = new Bibliotecario(1, "Administrador", "admin@isptec.local", "FUNC001");
        Estudante estudante = new Estudante(2, "Aluno Teste", "estudante@isptec.local", "2021001");

        users.put(admin.getEmail(), admin);
        passwords.put(admin.getEmail(), "admin");

        users.put(estudante.getEmail(), estudante);
        passwords.put(estudante.getEmail(), "1234");
    }

    @Override
    public Pessoa login(String email, String senha) {
        if (email == null || senha == null) return null;
        String pw = passwords.get(email);
        if (pw != null && pw.equals(senha)) {
            return users.get(email);
        }
        return null;
    }

    @Override
    public void logout() {
        // no-op for this simple implementation
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
        
        // Gera um ID único (simulado)
        int id = users.size() + 1;
        estudante.setId(id);
        
        // Se não tiver matrícula, gera uma
        if (estudante.getMatricula() == null || estudante.getMatricula().isEmpty()) {
            estudante.setMatricula("2024" + String.format("%03d", id));
        }
        
        // Registra o usuário
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
        
        // Se não tiver matrícula, gera uma
        if (bibliotecario.getMatricula() == null || bibliotecario.getMatricula().isEmpty()) {
            bibliotecario.setMatricula("FUNC" + String.format("%03d", id));
        }
        
        // Registra o usuário
        users.put(bibliotecario.getEmail(), bibliotecario);
        passwords.put(bibliotecario.getEmail(), bibliotecario.getSenha());
        
        return true;
    }
}
