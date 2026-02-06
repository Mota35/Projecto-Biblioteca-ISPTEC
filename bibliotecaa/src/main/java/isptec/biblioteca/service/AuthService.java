package isptec.biblioteca.service;

import isptec.biblioteca.model.Membro;
import isptec.biblioteca.model.Usuario;
import isptec.biblioteca.enumeracao.TipoUsuario;

import java.util.ArrayList;
import java.util.List;

public class AuthService {
    private static AuthService instance;
    private List<Usuario> usuarios;
    private Usuario usuarioLogado;
    private Membro membroLogado;
    private int proximoNumeroAdmin = 2; // Começa em 2 porque admin001 já existe

    private AuthService() {
        usuarios = new ArrayList<>();
        carregarUsuariosMock();
    }

    public static AuthService getInstance() {
        if (instance == null) {
            instance = new AuthService();
        }
        return instance;
    }

    private void carregarUsuariosMock() {
        // Admin padrão com email no formato correto
        usuarios.add(new Usuario("1", "Administrador", "admin001@isptec.co.ao", 
                                "admin123", TipoUsuario.ADMINISTRADOR));
    }

    public boolean login(String email, String senha) {
        // Primeiro tenta login como usuário (admin ou estudante cadastrado como usuário)
        for (Usuario usuario : usuarios) {
            if (usuario.getEmail().equals(email) && usuario.getSenha().equals(senha)) {
                usuarioLogado = usuario;
                membroLogado = null;
                return true;
            }
        }
        
        // Se não encontrou, tenta login como membro
        LibraryService libraryService = LibraryService.getInstance();
        for (Membro membro : libraryService.listarMembros()) {
            if (membro.getEmail().equals(email) && membro.getSenha().equals(senha)) {
                // Cria usuário temporário para o membro
                usuarioLogado = new Usuario(membro.getId(), membro.getNome(), 
                                           membro.getEmail(), membro.getSenha(), 
                                           TipoUsuario.ESTUDANTE);
                usuarioLogado.setPrimeiraSenha(membro.isPrimeiraSenha());
                membroLogado = membro;
                return true;
            }
        }
        
        return false;
    }

    public void logout() {
        usuarioLogado = null;
        membroLogado = null;
    }

    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    public Membro getMembroLogado() {
        return membroLogado;
    }

    public boolean isLogado() {
        return usuarioLogado != null;
    }

    public boolean isAdministrador() {
        return usuarioLogado != null && usuarioLogado.isAdministrador();
    }

    public boolean isPrimeiraSenha() {
        if (membroLogado != null) {
            return membroLogado.isPrimeiraSenha();
        }
        return usuarioLogado != null && usuarioLogado.isPrimeiraSenha();
    }

    public void alterarSenha(String senhaAtual, String novaSenha) {
        if (usuarioLogado != null && usuarioLogado.getSenha().equals(senhaAtual)) {
            usuarioLogado.setSenha(novaSenha);
            
            // Se for um membro, atualiza também no membro
            if (membroLogado != null) {
                membroLogado.setSenha(novaSenha);
                membroLogado.setPrimeiraSenha(false);
            } else {
                usuarioLogado.setPrimeiraSenha(false);
            }
        }
    }

    public void adicionarAdministrador(String nome, String senha) {
        String numeroAdmin = String.format("admin%03d", proximoNumeroAdmin);
        String email = numeroAdmin + "@isptec.co.ao";
        String id = String.valueOf(proximoNumeroAdmin);
        
        Usuario novoAdmin = new Usuario(id, nome, email, senha, TipoUsuario.ADMINISTRADOR);
        usuarios.add(novoAdmin);
        proximoNumeroAdmin++;
    }

    public String getProximoEmailAdmin() {
        return String.format("admin%03d@isptec.co.ao", proximoNumeroAdmin);
    }

    public static boolean validarEmailEstudante(String matricula) {
        String emailEsperado = matricula + "@isptec.co.ao";
        // Retorna o email esperado para validação
        return true;
    }

    public static String gerarEmailEstudante(String matricula) {
        return matricula + "@isptec.co.ao";
    }

    public static boolean validarFormatoEmailEstudante(String email, String matricula) {
        String emailEsperado = matricula + "@isptec.co.ao";
        return email.equals(emailEsperado);
    }

    public static boolean validarFormatoEmailAdmin(String email) {
        return email.matches("admin\\d{3}@isptec\\.co\\.ao");
    }
}
