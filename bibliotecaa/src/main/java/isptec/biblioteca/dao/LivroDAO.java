package isptec.biblioteca.dao;

import isptec.biblioteca.enumeracao.EstadoLivro;
import isptec.biblioteca.model.entities.Autor;
import isptec.biblioteca.model.entities.Categoria;
import isptec.biblioteca.model.entities.Livro;
import isptec.biblioteca.util.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para operações de Livro no banco de dados.
 */
public class LivroDAO {

    private final DatabaseManager dbManager;
    private final AutorDAO autorDAO;
    private final CategoriaDAO categoriaDAO;

    public LivroDAO() {
        this.dbManager = DatabaseManager.getInstance();
        this.autorDAO = new AutorDAO();
        this.categoriaDAO = new CategoriaDAO();
    }

    /**
     * Busca livro por ID com autores e categorias.
     */
    public Livro findById(int id) throws SQLException {
        String sql = "SELECT * FROM livro WHERE id = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Livro livro = mapResultSetToLivro(rs);
                    // Carrega autores e categorias
                    livro.setAutores(autorDAO.findByLivroId(id));
                    livro.setCategorias(categoriaDAO.findByLivroId(id));
                    return livro;
                }
            }
        }
        return null;
    }

    /**
     * Busca livro por ISBN.
     */
    public Livro findByIsbn(String isbn) throws SQLException {
        String sql = "SELECT * FROM livro WHERE isbn = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, isbn);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Livro livro = mapResultSetToLivro(rs);
                    livro.setAutores(autorDAO.findByLivroId(livro.getId()));
                    livro.setCategorias(categoriaDAO.findByLivroId(livro.getId()));
                    return livro;
                }
            }
        }
        return null;
    }

    /**
     * Busca livros por título parcial.
     */
    public List<Livro> findByTituloContaining(String titulo) throws SQLException {
        String sql = "SELECT * FROM livro WHERE titulo LIKE ? ORDER BY titulo";

        List<Livro> livros = new ArrayList<>();

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + titulo + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Livro livro = mapResultSetToLivro(rs);
                    livro.setAutores(autorDAO.findByLivroId(livro.getId()));
                    livro.setCategorias(categoriaDAO.findByLivroId(livro.getId()));
                    livros.add(livro);
                }
            }
        }
        return livros;
    }

    /**
     * Busca livros por autor.
     */
    public List<Livro> findByAutor(String nomeAutor) throws SQLException {
        String sql = "SELECT DISTINCT l.* FROM livro l " +
                     "INNER JOIN livro_autor la ON l.id = la.livro_id " +
                     "INNER JOIN autor a ON la.autor_id = a.id " +
                     "WHERE a.nome LIKE ? " +
                     "ORDER BY l.titulo";

        List<Livro> livros = new ArrayList<>();

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + nomeAutor + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Livro livro = mapResultSetToLivro(rs);
                    livro.setAutores(autorDAO.findByLivroId(livro.getId()));
                    livro.setCategorias(categoriaDAO.findByLivroId(livro.getId()));
                    livros.add(livro);
                }
            }
        }
        return livros;
    }

    /**
     * Busca livros por categoria.
     */
    public List<Livro> findByCategoria(String nomeCategoria) throws SQLException {
        String sql = "SELECT DISTINCT l.* FROM livro l " +
                     "INNER JOIN livro_categoria lc ON l.id = lc.livro_id " +
                     "INNER JOIN categoria c ON lc.categoria_id = c.id " +
                     "WHERE c.nome LIKE ? " +
                     "ORDER BY l.titulo";

        List<Livro> livros = new ArrayList<>();

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + nomeCategoria + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Livro livro = mapResultSetToLivro(rs);
                    livro.setAutores(autorDAO.findByLivroId(livro.getId()));
                    livro.setCategorias(categoriaDAO.findByLivroId(livro.getId()));
                    livros.add(livro);
                }
            }
        }
        return livros;
    }

    /**
     * Lista livros disponíveis (quantidade > 0).
     */
    public List<Livro> findDisponiveis() throws SQLException {
        String sql = "SELECT * FROM livro WHERE quantidade_disponivel > 0 ORDER BY titulo";

        List<Livro> livros = new ArrayList<>();

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Livro livro = mapResultSetToLivro(rs);
                livro.setAutores(autorDAO.findByLivroId(livro.getId()));
                livro.setCategorias(categoriaDAO.findByLivroId(livro.getId()));
                livros.add(livro);
            }
        }
        return livros;
    }

    /**
     * Insere novo livro com autores e categorias.
     */
    public int insert(Livro livro) throws SQLException {
        Connection conn = null;
        try {
            conn = dbManager.getConnection();
            conn.setAutoCommit(false);

            // Insere livro
            String sql = "INSERT INTO livro (titulo, isbn, editora, ano_publicacao, " +
                        "quantidade_total, quantidade_disponivel, estado, descricao, localizacao) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

            int livroId;
            try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, livro.getTitulo());
                stmt.setString(2, livro.getIsbn());
                stmt.setString(3, livro.getEditora());
                stmt.setInt(4, livro.getAnoPublicacao());
                stmt.setInt(5, livro.getQuantidadeTotal());
                stmt.setInt(6, livro.getQuantidadeDisponivel());
                stmt.setString(7, livro.getEstado().name());
                stmt.setString(8, livro.getDescricao());
                stmt.setString(9, livro.getLocalizacao());

                stmt.executeUpdate();

                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        livroId = generatedKeys.getInt(1);
                    } else {
                        conn.rollback();
                        return -1;
                    }
                }
            }

            // Associa autores
            if (livro.getAutores() != null && !livro.getAutores().isEmpty()) {
                for (Autor autor : livro.getAutores()) {
                    insertLivroAutor(conn, livroId, autor.getId());
                }
            }

            // Associa categorias
            if (livro.getCategorias() != null && !livro.getCategorias().isEmpty()) {
                for (Categoria categoria : livro.getCategorias()) {
                    insertLivroCategoria(conn, livroId, categoria.getId());
                }
            }

            conn.commit();
            return livroId;

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
     * Atualiza dados de livro.
     */
    public boolean update(Livro livro) throws SQLException {
        Connection conn = null;
        try {
            conn = dbManager.getConnection();
            conn.setAutoCommit(false);

            // Atualiza livro
            String sql = "UPDATE livro SET titulo = ?, isbn = ?, editora = ?, ano_publicacao = ?, " +
                        "quantidade_total = ?, quantidade_disponivel = ?, estado = ?, " +
                        "descricao = ?, localizacao = ? WHERE id = ?";

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, livro.getTitulo());
                stmt.setString(2, livro.getIsbn());
                stmt.setString(3, livro.getEditora());
                stmt.setInt(4, livro.getAnoPublicacao());
                stmt.setInt(5, livro.getQuantidadeTotal());
                stmt.setInt(6, livro.getQuantidadeDisponivel());
                stmt.setString(7, livro.getEstado().name());
                stmt.setString(8, livro.getDescricao());
                stmt.setString(9, livro.getLocalizacao());
                stmt.setInt(10, livro.getId());

                stmt.executeUpdate();
            }

            // Remove associações antigas
            deleteLivroAutores(conn, livro.getId());
            deleteLivroCategorias(conn, livro.getId());

            // Adiciona novas associações
            if (livro.getAutores() != null) {
                for (Autor autor : livro.getAutores()) {
                    insertLivroAutor(conn, livro.getId(), autor.getId());
                }
            }

            if (livro.getCategorias() != null) {
                for (Categoria categoria : livro.getCategorias()) {
                    insertLivroCategoria(conn, livro.getId(), categoria.getId());
                }
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
     * Deleta livro por ID.
     */
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM livro WHERE id = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Lista todos os livros.
     */
    public List<Livro> findAll() throws SQLException {
        String sql = "SELECT * FROM livro ORDER BY titulo";
        List<Livro> livros = new ArrayList<>();

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Livro livro = mapResultSetToLivro(rs);
                livro.setAutores(autorDAO.findByLivroId(livro.getId()));
                livro.setCategorias(categoriaDAO.findByLivroId(livro.getId()));
                livros.add(livro);
            }
        }
        return livros;
    }

    /**
     * Atualiza quantidade disponível de um livro.
     */
    public boolean updateQuantidadeDisponivel(int id, int quantidade) throws SQLException {
        String sql = "UPDATE livro SET quantidade_disponivel = ? WHERE id = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, quantidade);
            stmt.setInt(2, id);

            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Atualiza estado de um livro.
     */
    public boolean updateEstado(int id, EstadoLivro estado) throws SQLException {
        String sql = "UPDATE livro SET estado = ? WHERE id = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, estado.name());
            stmt.setInt(2, id);

            return stmt.executeUpdate() > 0;
        }
    }

    // === MÉTODOS AUXILIARES ===

    private void insertLivroAutor(Connection conn, int livroId, int autorId) throws SQLException {
        String sql = "INSERT INTO livro_autor (livro_id, autor_id) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, livroId);
            stmt.setInt(2, autorId);
            stmt.executeUpdate();
        }
    }

    private void insertLivroCategoria(Connection conn, int livroId, int categoriaId) throws SQLException {
        String sql = "INSERT INTO livro_categoria (livro_id, categoria_id) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, livroId);
            stmt.setInt(2, categoriaId);
            stmt.executeUpdate();
        }
    }

    private void deleteLivroAutores(Connection conn, int livroId) throws SQLException {
        String sql = "DELETE FROM livro_autor WHERE livro_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, livroId);
            stmt.executeUpdate();
        }
    }

    private void deleteLivroCategorias(Connection conn, int livroId) throws SQLException {
        String sql = "DELETE FROM livro_categoria WHERE livro_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, livroId);
            stmt.executeUpdate();
        }
    }

    private Livro mapResultSetToLivro(ResultSet rs) throws SQLException {
        Livro livro = new Livro();
        livro.setId(rs.getInt("id"));
        livro.setTitulo(rs.getString("titulo"));
        livro.setIsbn(rs.getString("isbn"));
        livro.setEditora(rs.getString("editora"));
        livro.setAnoPublicacao(rs.getInt("ano_publicacao"));
        livro.setQuantidadeTotal(rs.getInt("quantidade_total"));
        livro.setQuantidadeDisponivel(rs.getInt("quantidade_disponivel"));
        livro.setEstado(EstadoLivro.valueOf(rs.getString("estado")));
        livro.setDescricao(rs.getString("descricao"));
        livro.setLocalizacao(rs.getString("localizacao"));
        return livro;
    }
}

