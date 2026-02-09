package isptec.biblioteca.dao;

import isptec.biblioteca.model.entities.Autor;
import isptec.biblioteca.util.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para operações de Autor no banco de dados.
 */
public class AutorDAO {

    private final DatabaseManager dbManager;

    public AutorDAO() {
        this.dbManager = DatabaseManager.getInstance();
    }

    /**
     * Busca autor por ID.
     */
    public Autor findById(int id) throws SQLException {
        String sql = "SELECT * FROM autor WHERE id = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToAutor(rs);
                }
            }
        }
        return null;
    }

    /**
     * Busca autor por nome exato.
     */
    public Autor findByNome(String nome) throws SQLException {
        String sql = "SELECT * FROM autor WHERE nome = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nome);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToAutor(rs);
                }
            }
        }
        return null;
    }

    /**
     * Busca autores por nome parcial.
     */
    public List<Autor> findByNomeContaining(String nome) throws SQLException {
        String sql = "SELECT * FROM autor WHERE nome LIKE ? ORDER BY nome";

        List<Autor> autores = new ArrayList<>();

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + nome + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    autores.add(mapResultSetToAutor(rs));
                }
            }
        }
        return autores;
    }

    /**
     * Insere novo autor.
     */
    public int insert(Autor autor) throws SQLException {
        String sql = "INSERT INTO autor (nome, nacionalidade, biografia) VALUES (?, ?, ?)";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, autor.getNome());
            stmt.setString(2, autor.getNacionalidade());
            stmt.setString(3, autor.getBiografia());

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
     * Atualiza dados de autor.
     */
    public boolean update(Autor autor) throws SQLException {
        String sql = "UPDATE autor SET nome = ?, nacionalidade = ?, biografia = ? WHERE id = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, autor.getNome());
            stmt.setString(2, autor.getNacionalidade());
            stmt.setString(3, autor.getBiografia());
            stmt.setInt(4, autor.getId());

            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Deleta autor por ID.
     */
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM autor WHERE id = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Lista todos os autores.
     */
    public List<Autor> findAll() throws SQLException {
        String sql = "SELECT * FROM autor ORDER BY nome";
        List<Autor> autores = new ArrayList<>();

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                autores.add(mapResultSetToAutor(rs));
            }
        }
        return autores;
    }

    /**
     * Busca autores de um livro específico.
     */
    public List<Autor> findByLivroId(int livroId) throws SQLException {
        String sql = "SELECT a.* FROM autor a " +
                     "INNER JOIN livro_autor la ON a.id = la.autor_id " +
                     "WHERE la.livro_id = ? " +
                     "ORDER BY a.nome";

        List<Autor> autores = new ArrayList<>();

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, livroId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    autores.add(mapResultSetToAutor(rs));
                }
            }
        }
        return autores;
    }

    private Autor mapResultSetToAutor(ResultSet rs) throws SQLException {
        Autor autor = new Autor();
        autor.setId(rs.getInt("id"));
        autor.setNome(rs.getString("nome"));
        autor.setNacionalidade(rs.getString("nacionalidade"));
        autor.setBiografia(rs.getString("biografia"));
        return autor;
    }
}

