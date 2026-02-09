package isptec.biblioteca.dao;

import isptec.biblioteca.enumeracao.EstadoEmprestimo;
import isptec.biblioteca.model.entities.Emprestimo;
import isptec.biblioteca.model.entities.Livro;
import isptec.biblioteca.model.entities.Membro;
import isptec.biblioteca.util.DatabaseManager;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para operações de Empréstimo no banco de dados.
 */
public class EmprestimoDAO {

    private final DatabaseManager dbManager;
    private final LivroDAO livroDAO;
    private final MembroDAO membroDAO;

    public EmprestimoDAO() {
        this.dbManager = DatabaseManager.getInstance();
        this.livroDAO = new LivroDAO();
        this.membroDAO = new MembroDAO();
    }

    /**
     * Busca empréstimo por ID.
     */
    public Emprestimo findById(int id) throws SQLException {
        String sql = "SELECT * FROM emprestimo WHERE id = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToEmprestimo(rs);
                }
            }
        }
        return null;
    }

    /**
     * Busca empréstimos de um membro.
     */
    public List<Emprestimo> findByMembro(int membroId) throws SQLException {
        String sql = "SELECT * FROM emprestimo WHERE membro_id = ? ORDER BY data_emprestimo DESC";

        List<Emprestimo> emprestimos = new ArrayList<>();

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, membroId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    emprestimos.add(mapResultSetToEmprestimo(rs));
                }
            }
        }
        return emprestimos;
    }

    /**
     * Busca empréstimos de um livro.
     */
    public List<Emprestimo> findByLivro(int livroId) throws SQLException {
        String sql = "SELECT * FROM emprestimo WHERE livro_id = ? ORDER BY data_emprestimo DESC";

        List<Emprestimo> emprestimos = new ArrayList<>();

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, livroId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    emprestimos.add(mapResultSetToEmprestimo(rs));
                }
            }
        }
        return emprestimos;
    }

    /**
     * Lista empréstimos ativos.
     */
    public List<Emprestimo> findAtivos() throws SQLException {
        String sql = "SELECT * FROM emprestimo WHERE estado = 'ATIVO' ORDER BY data_devolucao_prevista";

        List<Emprestimo> emprestimos = new ArrayList<>();

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                emprestimos.add(mapResultSetToEmprestimo(rs));
            }
        }
        return emprestimos;
    }

    /**
     * Lista empréstimos atrasados.
     */
    public List<Emprestimo> findAtrasados() throws SQLException {
        String sql = "SELECT * FROM emprestimo WHERE estado = 'ATRASADO' " +
                     "ORDER BY data_devolucao_prevista";

        List<Emprestimo> emprestimos = new ArrayList<>();

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                emprestimos.add(mapResultSetToEmprestimo(rs));
            }
        }
        return emprestimos;
    }

    /**
     * Lista empréstimos ativos de um membro.
     */
    public List<Emprestimo> findAtivosByMembro(int membroId) throws SQLException {
        String sql = "SELECT * FROM emprestimo WHERE membro_id = ? AND estado = 'ATIVO' " +
                     "ORDER BY data_devolucao_prevista";

        List<Emprestimo> emprestimos = new ArrayList<>();

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, membroId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    emprestimos.add(mapResultSetToEmprestimo(rs));
                }
            }
        }
        return emprestimos;
    }

    /**
     * Conta empréstimos ativos de um membro.
     */
    public int countAtivosByMembro(int membroId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM emprestimo WHERE membro_id = ? AND estado = 'ATIVO'";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, membroId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    /**
     * Insere novo empréstimo.
     */
    public int insert(Emprestimo emprestimo) throws SQLException {
        String sql = "INSERT INTO emprestimo (livro_id, membro_id, data_emprestimo, " +
                    "data_devolucao_prevista, numero_renovacoes, estado) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, emprestimo.getLivro().getId());
            stmt.setInt(2, emprestimo.getMembro().getId());
            stmt.setDate(3, Date.valueOf(emprestimo.getDataEmprestimo()));
            stmt.setDate(4, Date.valueOf(emprestimo.getDataDevolucaoPrevista()));
            stmt.setInt(5, emprestimo.getNumeroRenovacoes());
            stmt.setString(6, emprestimo.getEstado().name());

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
     * Atualiza dados de empréstimo.
     */
    public boolean update(Emprestimo emprestimo) throws SQLException {
        String sql = "UPDATE emprestimo SET data_devolucao_prevista = ?, data_devolucao_real = ?, " +
                    "numero_renovacoes = ?, estado = ?, valor_multa = ? WHERE id = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(emprestimo.getDataDevolucaoPrevista()));

            if (emprestimo.getDataDevolucaoReal() != null) {
                stmt.setDate(2, Date.valueOf(emprestimo.getDataDevolucaoReal()));
            } else {
                stmt.setNull(2, Types.DATE);
            }

            stmt.setInt(3, emprestimo.getNumeroRenovacoes());
            stmt.setString(4, emprestimo.getEstado().name());
            stmt.setDouble(5, emprestimo.calcularMulta());
            stmt.setInt(6, emprestimo.getId());

            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Registra devolução de livro.
     */
    public boolean registrarDevolucao(int emprestimoId) throws SQLException {
        String sql = "UPDATE emprestimo SET data_devolucao_real = ?, estado = 'DEVOLVIDO' " +
                    "WHERE id = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(LocalDate.now()));
            stmt.setInt(2, emprestimoId);

            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Renova empréstimo (estende prazo).
     */
    public boolean renovar(int emprestimoId, LocalDate novaDataPrevista) throws SQLException {
        String sql = "UPDATE emprestimo SET data_devolucao_prevista = ?, " +
                    "numero_renovacoes = numero_renovacoes + 1 WHERE id = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(novaDataPrevista));
            stmt.setInt(2, emprestimoId);

            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Atualiza estado de um empréstimo.
     */
    public boolean updateEstado(int id, EstadoEmprestimo estado) throws SQLException {
        String sql = "UPDATE emprestimo SET estado = ? WHERE id = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, estado.name());
            stmt.setInt(2, id);

            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Atualiza empréstimos atrasados (para ser chamado periodicamente).
     */
    public int marcarEmprestimosAtrasados() throws SQLException {
        String sql = "UPDATE emprestimo SET estado = 'ATRASADO' " +
                    "WHERE estado = 'ATIVO' AND data_devolucao_prevista < CURDATE()";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            return stmt.executeUpdate();
        }
    }

    /**
     * Lista todos os empréstimos.
     */
    public List<Emprestimo> findAll() throws SQLException {
        String sql = "SELECT * FROM emprestimo ORDER BY data_emprestimo DESC";
        List<Emprestimo> emprestimos = new ArrayList<>();

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                emprestimos.add(mapResultSetToEmprestimo(rs));
            }
        }
        return emprestimos;
    }

    /**
     * Deleta empréstimo por ID.
     */
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM emprestimo WHERE id = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    private Emprestimo mapResultSetToEmprestimo(ResultSet rs) throws SQLException {
        Emprestimo emprestimo = new Emprestimo();

        emprestimo.setId(rs.getInt("id"));

        // Carrega livro e membro
        int livroId = rs.getInt("livro_id");
        int membroId = rs.getInt("membro_id");

        try {
            emprestimo.setLivro(livroDAO.findById(livroId));
            emprestimo.setMembro(membroDAO.findById(membroId));
        } catch (SQLException e) {
            // Log error but continue
            System.err.println("Erro ao carregar livro/membro: " + e.getMessage());
        }

        emprestimo.setDataEmprestimo(rs.getDate("data_emprestimo").toLocalDate());
        emprestimo.setDataDevolucaoPrevista(rs.getDate("data_devolucao_prevista").toLocalDate());

        Date dataDevolucaoReal = rs.getDate("data_devolucao_real");
        if (dataDevolucaoReal != null) {
            emprestimo.setDataDevolucaoReal(dataDevolucaoReal.toLocalDate());
        }

        emprestimo.setNumeroRenovacoes(rs.getInt("numero_renovacoes"));
        emprestimo.setEstado(EstadoEmprestimo.valueOf(rs.getString("estado")));

        return emprestimo;
    }
}

