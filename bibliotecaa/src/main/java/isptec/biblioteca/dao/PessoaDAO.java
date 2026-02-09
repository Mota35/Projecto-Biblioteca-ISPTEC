package isptec.biblioteca.dao;

import isptec.biblioteca.enumeracao.Perfil;
import isptec.biblioteca.model.entities.Pessoa;
import isptec.biblioteca.util.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para operações de Pessoa no banco de dados.
 */
public class PessoaDAO {

    private final DatabaseManager dbManager;

    public PessoaDAO() {
        this.dbManager = DatabaseManager.getInstance();
    }

    /**
     * Busca pessoa por ID.
     */
    public Pessoa findById(int id) throws SQLException {
        String sql = "SELECT * FROM pessoa WHERE id = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToPessoa(rs);
                }
            }
        }
        return null;
    }

    /**
     * Busca pessoa por email.
     */
    public Pessoa findByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM pessoa WHERE email = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToPessoa(rs);
                }
            }
        }
        return null;
    }

    /**
     * Autentica usuário por email e senha.
     */
    public Pessoa authenticate(String email, String senha) throws SQLException {
        String sql = "SELECT * FROM pessoa WHERE email = ? AND senha = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            stmt.setString(2, senha);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToPessoa(rs);
                }
            }
        }
        return null;
    }

    /**
     * Insere nova pessoa no banco.
     */
    public int insert(Pessoa pessoa, String tipoPessoa) throws SQLException {
        String sql = "INSERT INTO pessoa (nome, email, senha, perfil, tipo_pessoa) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, pessoa.getNome());
            stmt.setString(2, pessoa.getEmail());
            stmt.setString(3, pessoa.getSenha());
            stmt.setString(4, pessoa.getPerfil().name());
            stmt.setString(5, tipoPessoa);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }
                }
            }
        }
        return -1;
    }

    /**
     * Atualiza dados de pessoa.
     */
    public boolean update(Pessoa pessoa) throws SQLException {
        String sql = "UPDATE pessoa SET nome = ?, email = ?, senha = ?, perfil = ? WHERE id = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, pessoa.getNome());
            stmt.setString(2, pessoa.getEmail());
            stmt.setString(3, pessoa.getSenha());
            stmt.setString(4, pessoa.getPerfil().name());
            stmt.setInt(5, pessoa.getId());

            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Deleta pessoa por ID.
     */
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM pessoa WHERE id = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Lista todas as pessoas.
     */
    public List<Pessoa> findAll() throws SQLException {
        String sql = "SELECT * FROM pessoa ORDER BY nome";
        List<Pessoa> pessoas = new ArrayList<>();

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                pessoas.add(mapResultSetToPessoa(rs));
            }
        }
        return pessoas;
    }

    /**
     * Mapeia ResultSet para objeto Pessoa.
     * Retorna null pois Pessoa é abstrata - use DAOs específicos.
     */
    private Pessoa mapResultSetToPessoa(ResultSet rs) throws SQLException {
        // Pessoa é abstrata, então retorna apenas os dados básicos
        // Os DAOs específicos (MembroDAO, BibliotecarioDAO) farão a conversão completa
        return null;
    }

    /**
     * Verifica se email já existe.
     */
    public boolean emailExists(String email) throws SQLException {
        String sql = "SELECT COUNT(*) FROM pessoa WHERE email = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
}

