package isptec.biblioteca.model.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe Recomendacao - Representa uma recomendação de livros para um membro.
 *
 * As recomendações são baseadas em:
 * - Histórico de empréstimos
 * - Categorias mais lidas
 * - Livros populares
 */
public class Recomendacao {

    private int id;
    private Membro membro;
    private List<Livro> livrosRecomendados;
    private LocalDateTime dataGeracao;
    private String motivo;

    public Recomendacao() {
        this.id = 0;
        this.membro = null;
        this.livrosRecomendados = new ArrayList<>();
        this.dataGeracao = LocalDateTime.now();
        this.motivo = "";
    }

    public Recomendacao(int id, Membro membro) {
        this.id = id;
        this.membro = membro;
        this.livrosRecomendados = new ArrayList<>();
        this.dataGeracao = LocalDateTime.now();
        this.motivo = "";
    }

    public Recomendacao(int id, Membro membro, List<Livro> livrosRecomendados, String motivo) {
        this.id = id;
        this.membro = membro;
        this.livrosRecomendados = livrosRecomendados != null ? livrosRecomendados : new ArrayList<>();
        this.dataGeracao = LocalDateTime.now();
        this.motivo = motivo;
    }

    // === MÉTODOS ===

    /**
     * Adiciona um livro à lista de recomendações.
     *
     * @param livro o livro a adicionar
     */
    public void adicionarLivro(Livro livro) {
        if (livro != null && !livrosRecomendados.contains(livro)) {
            livrosRecomendados.add(livro);
        }
    }

    /**
     * Remove um livro da lista de recomendações.
     *
     * @param livro o livro a remover
     */
    public void removerLivro(Livro livro) {
        livrosRecomendados.remove(livro);
    }

    /**
     * Verifica se há recomendações disponíveis.
     *
     * @return true se há livros recomendados
     */
    public boolean temRecomendacoes() {
        return !livrosRecomendados.isEmpty();
    }

    /**
     * Obtém o número de recomendações.
     *
     * @return quantidade de livros recomendados
     */
    public int getQuantidadeRecomendacoes() {
        return livrosRecomendados.size();
    }

    // === GETTERS E SETTERS ===

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Membro getMembro() {
        return membro;
    }

    public void setMembro(Membro membro) {
        this.membro = membro;
    }

    public List<Livro> getLivrosRecomendados() {
        return new ArrayList<>(livrosRecomendados);
    }

    public void setLivrosRecomendados(List<Livro> livrosRecomendados) {
        this.livrosRecomendados = livrosRecomendados != null ? livrosRecomendados : new ArrayList<>();
    }

    public LocalDateTime getDataGeracao() {
        return dataGeracao;
    }

    public void setDataGeracao(LocalDateTime dataGeracao) {
        this.dataGeracao = dataGeracao;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    @Override
    public String toString() {
        return "Recomendacao{id=" + id +
               ", membro='" + (membro != null ? membro.getNome() : "N/A") + "'" +
               ", livrosRecomendados=" + livrosRecomendados.size() +
               ", dataGeracao=" + dataGeracao +
               ", motivo='" + motivo + "'" +
               "}";
    }
}
