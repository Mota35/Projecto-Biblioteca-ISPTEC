package isptec.biblioteca.model.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import isptec.biblioteca.enumeracao.EstadoLivro;

/**
 * Classe Livro - Representa um livro no sistema da biblioteca.
 *
 * REGRAS DE NEGÓCIO:
 * - Livro só pode ser emprestado se estiver disponível
 * - Controle de quantidade disponível vs total
 */
public class Livro {

    private int id;
    private String titulo;
    private String isbn;
    private String editora;
    private int anoPublicacao;
    private int quantidadeTotal;
    private int quantidadeDisponivel;
    private List<Autor> autores;
    private List<Categoria> categorias;
    private EstadoLivro estado;
    private String descricao;
    private String localizacao; // Prateleira/Corredor

    public Livro() {
        this.id = 0;
        this.titulo = "";
        this.isbn = "";
        this.editora = "";
        this.anoPublicacao = 0;
        this.quantidadeTotal = 0;
        this.quantidadeDisponivel = 0;
        this.autores = new ArrayList<>();
        this.categorias = new ArrayList<>();
        this.estado = EstadoLivro.DISPONIVEL;
        this.descricao = "";
        this.localizacao = "";
    }

    public Livro(int id, String titulo, String isbn, String editora, int quantidadeTotal,
                 List<Autor> autores, Categoria categoria) {
        this.id = id;
        this.titulo = titulo;
        this.isbn = isbn;
        this.editora = editora;
        this.anoPublicacao = 0;
        this.quantidadeTotal = quantidadeTotal;
        this.quantidadeDisponivel = quantidadeTotal;
        this.autores = autores != null ? autores : new ArrayList<>();
        this.categorias = new ArrayList<>();
        if (categoria != null) {
            this.categorias.add(categoria);
        }
        this.estado = EstadoLivro.DISPONIVEL;
        this.descricao = "";
        this.localizacao = "";
    }

    public Livro(int id, String titulo, String isbn, String editora, int anoPublicacao,
                 int quantidadeTotal, List<Autor> autores, List<Categoria> categorias) {
        this.id = id;
        this.titulo = titulo;
        this.isbn = isbn;
        this.editora = editora;
        this.anoPublicacao = anoPublicacao;
        this.quantidadeTotal = quantidadeTotal;
        this.quantidadeDisponivel = quantidadeTotal;
        this.autores = autores != null ? autores : new ArrayList<>();
        this.categorias = categorias != null ? categorias : new ArrayList<>();
        this.estado = EstadoLivro.DISPONIVEL;
        this.descricao = "";
        this.localizacao = "";
    }

    // === MÉTODOS DE NEGÓCIO ===

    /**
     * Verifica se o livro está disponível para empréstimo.
     *
     * @return true se há exemplares disponíveis
     */
    public boolean estaDisponivel() {
        return quantidadeDisponivel > 0 && estado != EstadoLivro.INDISPONIVEL;
    }

    /**
     * Realiza o empréstimo de um exemplar do livro.
     * Decrementa a quantidade disponível.
     *
     * @return true se o empréstimo foi realizado com sucesso
     */
    public boolean emprestar() {
        if (!estaDisponivel()) {
            return false;
        }

        quantidadeDisponivel--;
        atualizarEstado();
        return true;
    }

    /**
     * Realiza a devolução de um exemplar do livro.
     * Incrementa a quantidade disponível.
     */
    public void devolver() {
        if (quantidadeDisponivel < quantidadeTotal) {
            quantidadeDisponivel++;
            atualizarEstado();
        }
    }

    /**
     * Atualiza o estado do livro baseado na quantidade disponível.
     */
    private void atualizarEstado() {
        if (quantidadeDisponivel == 0) {
            this.estado = EstadoLivro.EMPRESTADO;
        } else if (quantidadeDisponivel < quantidadeTotal) {
            this.estado = EstadoLivro.DISPONIVEL; // Parcialmente disponível
        } else {
            this.estado = EstadoLivro.DISPONIVEL;
        }
    }

    /**
     * Adiciona um autor ao livro.
     *
     * @param autor o autor a adicionar
     */
    public void adicionarAutor(Autor autor) {
        if (autor != null && !autores.contains(autor)) {
            autores.add(autor);
        }
    }

    /**
     * Adiciona uma categoria ao livro.
     *
     * @param categoria a categoria a adicionar
     */
    public void adicionarCategoria(Categoria categoria) {
        if (categoria != null && !categorias.contains(categoria)) {
            categorias.add(categoria);
        }
    }

    /**
     * Retorna os nomes dos autores formatados.
     *
     * @return String com nomes dos autores separados por vírgula
     */
    public String getAutoresFormatados() {
        if (autores == null || autores.isEmpty()) {
            return "Autor desconhecido";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < autores.size(); i++) {
            sb.append(autores.get(i).getNome());
            if (i < autores.size() - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

    /**
     * Retorna os nomes das categorias formatados.
     *
     * @return String com nomes das categorias separados por vírgula
     */
    public String getCategoriasFormatadas() {
        if (categorias == null || categorias.isEmpty()) {
            return "Sem categoria";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < categorias.size(); i++) {
            sb.append(categorias.get(i).getNome());
            if (i < categorias.size() - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

    // === GETTERS E SETTERS ===

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getEditora() {
        return editora;
    }

    public void setEditora(String editora) {
        this.editora = editora;
    }

    public int getAnoPublicacao() {
        return anoPublicacao;
    }

    public void setAnoPublicacao(int anoPublicacao) {
        this.anoPublicacao = anoPublicacao;
    }

    public int getQuantidadeTotal() {
        return quantidadeTotal;
    }

    public void setQuantidadeTotal(int quantidadeTotal) {
        this.quantidadeTotal = quantidadeTotal;
    }

    public int getQuantidadeDisponivel() {
        return quantidadeDisponivel;
    }

    public void setQuantidadeDisponivel(int quantidadeDisponivel) {
        this.quantidadeDisponivel = quantidadeDisponivel;
    }

    public List<Autor> getAutores() {
        return new ArrayList<>(autores);
    }

    public void setAutores(List<Autor> autores) {
        this.autores = autores != null ? autores : new ArrayList<>();
    }

    public List<Categoria> getCategorias() {
        return new ArrayList<>(categorias);
    }

    public void setCategorias(List<Categoria> categorias) {
        this.categorias = categorias != null ? categorias : new ArrayList<>();
    }

    // Método de compatibilidade para código existente
    public Categoria getCategoria() {
        return categorias != null && !categorias.isEmpty() ? categorias.get(0) : null;
    }

    public void setCategoria(Categoria categoria) {
        if (categoria != null) {
            if (categorias == null) {
                categorias = new ArrayList<>();
            }
            if (categorias.isEmpty()) {
                categorias.add(categoria);
            } else {
                categorias.set(0, categoria);
            }
        }
    }

    public EstadoLivro getEstado() {
        return estado;
    }

    public void setEstado(EstadoLivro estado) {
        this.estado = estado;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Livro livro = (Livro) o;
        return id == livro.id || Objects.equals(isbn, livro.isbn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, isbn);
    }

    @Override
    public String toString() {
        return "Livro{id=" + id + ", titulo='" + titulo + "', isbn='" + isbn +
               "', autores=" + getAutoresFormatados() +
               ", disponivel=" + quantidadeDisponivel + "/" + quantidadeTotal +
               ", estado=" + estado + "}";
    }
}
