package isptec.biblioteca.dao;

import isptec.biblioteca.enumeracao.Perfil;
import isptec.biblioteca.model.entities.Membro;
import isptec.biblioteca.util.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para operações de Membro no banco de dados.
 */
public class MembroDAO {

    private final DatabaseManager dbManager;
    private final PessoaDAO pessoaDAO;

    public MembroDAO() {
        this.dbManager = DatabaseManager.getInstance();
        this.pessoaDAO = new PessoaDAO();
    }

    /**
     * Busca membro por ID.
     */
    public Membro findById(int id) throws SQLException {
        String sql = "SELECT p.*, m.* FROM pessoa p " +
                     "INNER JOIN membro m ON p.id = m.id " +
                     "WHERE p.id = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToMembro(rs);
                }
            }
        }
        return null;
    }

    /**
     * Busca membro por matrícula.
     */
    public Membro findByMatricula(String matricula) throws SQLException {
        String sql = "SELECT p.*, m.* FROM pessoa p " +
                     "INNER JOIN membro m ON p.id = m.id " +
                     "WHERE m.matricula = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, matricula);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToMembro(rs);
                }
            }
        }
        return null;
    }

    /**
     * Busca membro por email.
     */
    public Membro findByEmail(String email) throws SQLException {
        String sql = "SELECT p.*, m.* FROM pessoa p " +
                     "INNER JOIN membro m ON p.id = m.id " +
                     "WHERE p.email = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToMembro(rs);
                }
            }
        }
        return null;
    }

    /**
     * Insere novo membro no banco.
     */
    public int insert(Membro membro) throws SQLException {
        Connection conn = null;
        try {
            conn = dbManager.getConnection();
            conn.setAutoCommit(false);

            // Insere na tabela pessoa
            int pessoaId = insertPessoa(conn, membro);

            if (pessoaId > 0) {
                // Insere na tabela membro
                String sql = "INSERT INTO membro (id, matricula, bloqueado, multa_pendente) " +
                            "VALUES (?, ?, ?, ?)";

                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setInt(1, pessoaId);
                    stmt.setString(2, membro.getMatricula());
                    stmt.setBoolean(3, membro.isBloqueado());
                    stmt.setDouble(4, membro.getMultaPendente());

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
     * Atualiza dados de membro.
     */
    public boolean update(Membro membro) throws SQLException {
        Connection conn = null;
        try {
            conn = dbManager.getConnection();
            conn.setAutoCommit(false);

            // Atualiza pessoa
            updatePessoa(conn, membro);

            // Atualiza membro
            String sql = "UPDATE membro SET matricula = ?, bloqueado = ?, multa_pendente = ? " +
                        "WHERE id = ?";

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, membro.getMatricula());
                stmt.setBoolean(2, membro.isBloqueado());
                stmt.setDouble(3, membro.getMultaPendente());
                stmt.setInt(4, membro.getId());

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
     * Deleta membro por ID.
     */
    public boolean delete(int id) throws SQLException {
        // O CASCADE do banco de dados cuidará da exclusão em cascata
        return pessoaDAO.delete(id);
    }

    /**
     * Lista todos os membros.
     */
    public List<Membro> findAll() throws SQLException {
        String sql = "SELECT p.*, m.* FROM pessoa p " +
                     "INNER JOIN membro m ON p.id = m.id " +
                     "ORDER BY p.nome";

        List<Membro> membros = new ArrayList<>();

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                membros.add(mapResultSetToMembro(rs));
            }
        }
        return membros;
    }

    /**
     * Lista membros não bloqueados.
     */
    public List<Membro> findAtivos() throws SQLException {
        String sql = "SELECT p.*, m.* FROM pessoa p " +
                     "INNER JOIN membro m ON p.id = m.id " +
                     "WHERE m.bloqueado = FALSE " +
                     "ORDER BY p.nome";

        List<Membro> membros = new ArrayList<>();

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                membros.add(mapResultSetToMembro(rs));
            }
        }
        return membros;
    }

    /**
     * Bloqueia ou desbloqueia um membro.
     */
    public boolean setBloqueado(int id, boolean bloqueado) throws SQLException {
        String sql = "UPDATE membro SET bloqueado = ? WHERE id = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setBoolean(1, bloqueado);
            stmt.setInt(2, id);

            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Atualiza multa pendente de um membro.
     */
    public boolean updateMulta(int id, double multa) throws SQLException {
        String sql = "UPDATE membro SET multa_pendente = ? WHERE id = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, multa);
            stmt.setInt(2, id);

            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Adiciona valor à multa pendente.
     */
    public boolean adicionarMulta(int id, double valor) throws SQLException {
        String sql = "UPDATE membro SET multa_pendente = multa_pendente + ? WHERE id = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, valor);
            stmt.setInt(2, id);

            return stmt.executeUpdate() > 0;
        }
    }

    // === MÉTODOS AUXILIARES ===

    private int insertPessoa(Connection conn, Membro membro) throws SQLException {
        String sql = "INSERT INTO pessoa (nome, email, senha, perfil, tipo_pessoa) " +
                    "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, membro.getNome());
            stmt.setString(2, membro.getEmail());
            stmt.setString(3, membro.getSenha());
            stmt.setString(4, membro.getPerfil().name());
            stmt.setString(5, "MEMBRO");

            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        }
        return -1;
    }

    private void updatePessoa(Connection conn, Membro membro) throws SQLException {
        String sql = "UPDATE pessoa SET nome = ?, email = ?, perfil = ? WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, membro.getNome());
            stmt.setString(2, membro.getEmail());
            stmt.setString(3, membro.getPerfil().name());
            stmt.setInt(4, membro.getId());

            stmt.executeUpdate();
        }
    }

    private Membro mapResultSetToMembro(ResultSet rs) throws SQLException {
        Membro membro = new Membro();

        // Dados de Pessoa
        membro.setId(rs.getInt("id"));
        membro.setNome(rs.getString("nome"));
        membro.setEmail(rs.getString("email"));
        membro.setSenha(rs.getString("senha"));
        membro.setPerfil(Perfil.valueOf(rs.getString("perfil")));

        // Dados de Membro
        membro.setMatricula(rs.getString("matricula"));
        membro.setBloqueado(rs.getBoolean("bloqueado"));
        membro.setMultaPendente(rs.getDouble("multa_pendente"));

        return membro;
    }

    /**
     * Verifica se matrícula já existe.
     */
    public boolean matriculaExists(String matricula) throws SQLException {
        String sql = "SELECT COUNT(*) FROM membro WHERE matricula = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, matricula);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
}

