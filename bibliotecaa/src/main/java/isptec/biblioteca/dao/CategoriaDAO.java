package isptec.biblioteca.dao;

import isptec.biblioteca.model.entities.Categoria;
import isptec.biblioteca.util.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para operações de Categoria no banco de dados.
 */
public class CategoriaDAO {

    private final DatabaseManager dbManager;

    public CategoriaDAO() {
        this.dbManager = DatabaseManager.getInstance();
    }

    /**
     * Busca categoria por ID.
     */
    public Categoria findById(int id) throws SQLException {
        String sql = "SELECT * FROM categoria WHERE id = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToCategoria(rs);
                }
            }
        }
        return null;
    }

    /**
     * Busca categoria por nome exato.
     */
    public Categoria findByNome(String nome) throws SQLException {
        String sql = "SELECT * FROM categoria WHERE nome = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nome);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToCategoria(rs);
                }
            }
        }
        return null;
    }

    /**
     * Busca categorias por nome parcial.
     */
    public List<Categoria> findByNomeContaining(String nome) throws SQLException {
        String sql = "SELECT * FROM categoria WHERE nome LIKE ? ORDER BY nome";

        List<Categoria> categorias = new ArrayList<>();

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + nome + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    categorias.add(mapResultSetToCategoria(rs));
                }
            }
        }
        return categorias;
    }

    /**
     * Insere nova categoria.
     */
    public int insert(Categoria categoria) throws SQLException {
        String sql = "INSERT INTO categoria (nome, descricao) VALUES (?, ?)";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, categoria.getNome());
            stmt.setString(2, categoria.getDescricao());

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
     * Atualiza dados de categoria.
     */
    public boolean update(Categoria categoria) throws SQLException {
        String sql = "UPDATE categoria SET nome = ?, descricao = ? WHERE id = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, categoria.getNome());
            stmt.setString(2, categoria.getDescricao());
            stmt.setInt(3, categoria.getId());

            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Deleta categoria por ID.
     */
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM categoria WHERE id = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Lista todas as categorias.
     */
    public List<Categoria> findAll() throws SQLException {
        String sql = "SELECT * FROM categoria ORDER BY nome";
        List<Categoria> categorias = new ArrayList<>();

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                categorias.add(mapResultSetToCategoria(rs));
            }
        }
        return categorias;
    }

    /**
     * Busca categorias de um livro específico.
     */
    public List<Categoria> findByLivroId(int livroId) throws SQLException {
        String sql = "SELECT c.* FROM categoria c " +
                     "INNER JOIN livro_categoria lc ON c.id = lc.categoria_id " +
                     "WHERE lc.livro_id = ? " +
                     "ORDER BY c.nome";

        List<Categoria> categorias = new ArrayList<>();

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, livroId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    categorias.add(mapResultSetToCategoria(rs));
                }
            }
        }
        return categorias;
    }

    private Categoria mapResultSetToCategoria(ResultSet rs) throws SQLException {
        Categoria categoria = new Categoria();
        categoria.setId(rs.getInt("id"));
        categoria.setNome(rs.getString("nome"));
        categoria.setDescricao(rs.getString("descricao"));
        return categoria;
    }
}

