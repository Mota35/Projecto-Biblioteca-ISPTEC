package isptec.biblioteca.dao;

import isptec.biblioteca.enumeracao.Perfil;
import isptec.biblioteca.model.entities.Estudante;
import isptec.biblioteca.util.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para operações de Estudante no banco de dados.
 */
public class EstudanteDAO {

    private final DatabaseManager dbManager;

    public EstudanteDAO() {
        this.dbManager = DatabaseManager.getInstance();
    }

    /**
     * Busca estudante por ID.
     */
    public Estudante findById(int id) throws SQLException {
        String sql = "SELECT p.*, m.*, e.* FROM pessoa p " +
                     "INNER JOIN membro m ON p.id = m.id " +
                     "INNER JOIN estudante e ON m.id = e.id " +
                     "WHERE p.id = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToEstudante(rs);
                }
            }
        }
        return null;
    }

    /**
     * Busca estudante por matrícula.
     */
    public Estudante findByMatricula(String matricula) throws SQLException {
        String sql = "SELECT p.*, m.*, e.* FROM pessoa p " +
                     "INNER JOIN membro m ON p.id = m.id " +
                     "INNER JOIN estudante e ON m.id = e.id " +
                     "WHERE m.matricula = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, matricula);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToEstudante(rs);
                }
            }
        }
        return null;
    }

    /**
     * Insere novo estudante no banco.
     */
    public int insert(Estudante estudante) throws SQLException {
        Connection conn = null;
        try {
            conn = dbManager.getConnection();
            conn.setAutoCommit(false);

            // Insere pessoa
            int pessoaId = insertPessoa(conn, estudante);

            if (pessoaId > 0) {
                // Insere membro
                insertMembro(conn, pessoaId, estudante);

                // Insere estudante
                String sql = "INSERT INTO estudante (id, curso, ano_letivo) VALUES (?, ?, ?)";

                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setInt(1, pessoaId);
                    stmt.setString(2, estudante.getCurso());
                    stmt.setInt(3, estudante.getAnoLetivo());

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
     * Atualiza dados de estudante.
     */
    public boolean update(Estudante estudante) throws SQLException {
        Connection conn = null;
        try {
            conn = dbManager.getConnection();
            conn.setAutoCommit(false);

            // Atualiza pessoa
            updatePessoa(conn, estudante);

            // Atualiza membro
            updateMembro(conn, estudante);

            // Atualiza estudante
            String sql = "UPDATE estudante SET curso = ?, ano_letivo = ? WHERE id = ?";

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, estudante.getCurso());
                stmt.setInt(2, estudante.getAnoLetivo());
                stmt.setInt(3, estudante.getId());

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
     * Lista todos os estudantes.
     */
    public List<Estudante> findAll() throws SQLException {
        String sql = "SELECT p.*, m.*, e.* FROM pessoa p " +
                     "INNER JOIN membro m ON p.id = m.id " +
                     "INNER JOIN estudante e ON m.id = e.id " +
                     "ORDER BY p.nome";

        List<Estudante> estudantes = new ArrayList<>();

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                estudantes.add(mapResultSetToEstudante(rs));
            }
        }
        return estudantes;
    }

    /**
     * Busca estudantes por curso.
     */
    public List<Estudante> findByCurso(String curso) throws SQLException {
        String sql = "SELECT p.*, m.*, e.* FROM pessoa p " +
                     "INNER JOIN membro m ON p.id = m.id " +
                     "INNER JOIN estudante e ON m.id = e.id " +
                     "WHERE e.curso LIKE ? " +
                     "ORDER BY p.nome";

        List<Estudante> estudantes = new ArrayList<>();

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + curso + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    estudantes.add(mapResultSetToEstudante(rs));
                }
            }
        }
        return estudantes;
    }

    /**
     * Busca estudantes por ano letivo.
     */
    public List<Estudante> findByAnoLetivo(int anoLetivo) throws SQLException {
        String sql = "SELECT p.*, m.*, e.* FROM pessoa p " +
                     "INNER JOIN membro m ON p.id = m.id " +
                     "INNER JOIN estudante e ON m.id = e.id " +
                     "WHERE e.ano_letivo = ? " +
                     "ORDER BY p.nome";

        List<Estudante> estudantes = new ArrayList<>();

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, anoLetivo);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    estudantes.add(mapResultSetToEstudante(rs));
                }
            }
        }
        return estudantes;
    }

    // === MÉTODOS AUXILIARES ===

    private int insertPessoa(Connection conn, Estudante estudante) throws SQLException {
        String sql = "INSERT INTO pessoa (nome, email, senha, perfil, tipo_pessoa) " +
                    "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, estudante.getNome());
            stmt.setString(2, estudante.getEmail());
            stmt.setString(3, estudante.getSenha());
            stmt.setString(4, estudante.getPerfil().name());
            stmt.setString(5, "ESTUDANTE");

            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        }
        return -1;
    }

    private void insertMembro(Connection conn, int id, Estudante estudante) throws SQLException {
        String sql = "INSERT INTO membro (id, matricula, bloqueado, multa_pendente) " +
                    "VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.setString(2, estudante.getMatricula());
            stmt.setBoolean(3, estudante.isBloqueado());
            stmt.setDouble(4, estudante.getMultaPendente());

            stmt.executeUpdate();
        }
    }

    private void updatePessoa(Connection conn, Estudante estudante) throws SQLException {
        String sql = "UPDATE pessoa SET nome = ?, email = ?, perfil = ? WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, estudante.getNome());
            stmt.setString(2, estudante.getEmail());
            stmt.setString(3, estudante.getPerfil().name());
            stmt.setInt(4, estudante.getId());

            stmt.executeUpdate();
        }
    }

    private void updateMembro(Connection conn, Estudante estudante) throws SQLException {
        String sql = "UPDATE membro SET matricula = ?, bloqueado = ?, multa_pendente = ? " +
                    "WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, estudante.getMatricula());
            stmt.setBoolean(2, estudante.isBloqueado());
            stmt.setDouble(3, estudante.getMultaPendente());
            stmt.setInt(4, estudante.getId());

            stmt.executeUpdate();
        }
    }

    private Estudante mapResultSetToEstudante(ResultSet rs) throws SQLException {
        Estudante estudante = new Estudante();

        // Dados de Pessoa
        estudante.setId(rs.getInt("id"));
        estudante.setNome(rs.getString("nome"));
        estudante.setEmail(rs.getString("email"));
        estudante.setSenha(rs.getString("senha"));
        estudante.setPerfil(Perfil.valueOf(rs.getString("perfil")));

        // Dados de Membro
        estudante.setMatricula(rs.getString("matricula"));
        estudante.setBloqueado(rs.getBoolean("bloqueado"));
        estudante.setMultaPendente(rs.getDouble("multa_pendente"));

        // Dados de Estudante
        estudante.setCurso(rs.getString("curso"));
        estudante.setAnoLetivo(rs.getInt("ano_letivo"));

        return estudante;
    }
}

