package isptec.biblioteca.dao;

import isptec.biblioteca.enumeracao.Perfil;
import isptec.biblioteca.model.entities.Bibliotecario;
import isptec.biblioteca.util.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para operações de Bibliotecario no banco de dados.
 */
public class BibliotecarioDAO {

    private final DatabaseManager dbManager;

    public BibliotecarioDAO() {
        this.dbManager = DatabaseManager.getInstance();
    }

    /**
     * Busca bibliotecário por ID.
     */
    public Bibliotecario findById(int id) throws SQLException {
        String sql = "SELECT p.*, b.* FROM pessoa p " +
                     "INNER JOIN bibliotecario b ON p.id = b.id " +
                     "WHERE p.id = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToBibliotecario(rs);
                }
            }
        }
        return null;
    }

    /**
     * Busca bibliotecário por email.
     */
    public Bibliotecario findByEmail(String email) throws SQLException {
        String sql = "SELECT p.*, b.* FROM pessoa p " +
                     "INNER JOIN bibliotecario b ON p.id = b.id " +
                     "WHERE p.email = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToBibliotecario(rs);
                }
            }
        }
        return null;
    }

    /**
     * Busca bibliotecário por funcionário ID.
     */
    public Bibliotecario findByFuncionarioId(String funcionarioId) throws SQLException {
        String sql = "SELECT p.*, b.* FROM pessoa p " +
                     "INNER JOIN bibliotecario b ON p.id = b.id " +
                     "WHERE b.funcionario_id = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, funcionarioId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToBibliotecario(rs);
                }
            }
        }
        return null;
    }

    /**
     * Insere novo bibliotecário.
     */
    public int insert(Bibliotecario bibliotecario) throws SQLException {
        Connection conn = null;
        try {
            conn = dbManager.getConnection();
            conn.setAutoCommit(false);

            // Insere pessoa
            int pessoaId = insertPessoa(conn, bibliotecario);

            if (pessoaId > 0) {
                // Insere bibliotecário
                String sql = "INSERT INTO bibliotecario (id, funcionario_id, departamento, cargo) " +
                            "VALUES (?, ?, ?, ?)";

                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setInt(1, pessoaId);
                    stmt.setString(2, bibliotecario.getFuncionarioId());
                    stmt.setString(3, bibliotecario.getDepartamento());
                    stmt.setString(4, bibliotecario.getCargo());

                    stmt.executeUpdate();
                }

                conn.commit();
                return pessoaId;
            }

            conn.rollback();
            return -1;

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    /**
     * Atualiza dados de bibliotecário.
     */
    public boolean update(Bibliotecario bibliotecario) throws SQLException {
        Connection conn = null;
        try {
            conn = dbManager.getConnection();
            conn.setAutoCommit(false);

            // Atualiza pessoa
            updatePessoa(conn, bibliotecario);

            // Atualiza bibliotecário
            String sql = "UPDATE bibliotecario SET funcionario_id = ?, departamento = ?, cargo = ? " +
                        "WHERE id = ?";

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, bibliotecario.getFuncionarioId());
                stmt.setString(2, bibliotecario.getDepartamento());
                stmt.setString(3, bibliotecario.getCargo());
                stmt.setInt(4, bibliotecario.getId());

                stmt.executeUpdate();
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    /**
     * Lista todos os bibliotecários.
     */
    public List<Bibliotecario> findAll() throws SQLException {
        String sql = "SELECT p.*, b.* FROM pessoa p " +
                     "INNER JOIN bibliotecario b ON p.id = b.id " +
                     "ORDER BY p.nome";

        List<Bibliotecario> bibliotecarios = new ArrayList<>();

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                bibliotecarios.add(mapResultSetToBibliotecario(rs));
            }
        }
        return bibliotecarios;
    }

    // === MÉTODOS AUXILIARES ===

    private int insertPessoa(Connection conn, Bibliotecario bibliotecario) throws SQLException {
        String sql = "INSERT INTO pessoa (nome, email, senha, perfil, tipo_pessoa) " +
                    "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, bibliotecario.getNome());
            stmt.setString(2, bibliotecario.getEmail());
            stmt.setString(3, bibliotecario.getSenha());
            stmt.setString(4, Perfil.ADMIN.name());
            stmt.setString(5, "BIBLIOTECARIO");

            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        }
        return -1;
    }

    private void updatePessoa(Connection conn, Bibliotecario bibliotecario) throws SQLException {
        String sql = "UPDATE pessoa SET nome = ?, email = ? WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, bibliotecario.getNome());
            stmt.setString(2, bibliotecario.getEmail());
            stmt.setInt(3, bibliotecario.getId());

            stmt.executeUpdate();
        }
    }

    private Bibliotecario mapResultSetToBibliotecario(ResultSet rs) throws SQLException {
        Bibliotecario bibliotecario = new Bibliotecario();

        // Dados de Pessoa
        bibliotecario.setId(rs.getInt("id"));
        bibliotecario.setNome(rs.getString("nome"));
        bibliotecario.setEmail(rs.getString("email"));
        bibliotecario.setSenha(rs.getString("senha"));
        bibliotecario.setPerfil(Perfil.valueOf(rs.getString("perfil")));

        // Dados de Bibliotecário
        bibliotecario.setFuncionarioId(rs.getString("funcionario_id"));
        bibliotecario.setDepartamento(rs.getString("departamento"));
        bibliotecario.setCargo(rs.getString("cargo"));

        return bibliotecario;
    }
}

