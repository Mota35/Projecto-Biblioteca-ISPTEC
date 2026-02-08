package isptec.biblioteca.model.entities;

/**
 * Classe Estudante - Representa um estudante da universidade.
 * Herda de Membro, tendo acesso a todas as funcionalidades de empréstimo.
 *
 * Pode ser estendida para adicionar atributos específicos de estudante
 * como curso, ano, etc.
 */
public class Estudante extends Membro {

    private String curso;
    private int anoLetivo;

    public Estudante() {
        super();
        this.curso = "";
        this.anoLetivo = 1;
    }

    public Estudante(int id, String nome, String email, String matricula) {
        super(id, nome, email, matricula);
        this.curso = "";
        this.anoLetivo = 1;
    }

    public Estudante(int id, String nome, String email, String matricula, String curso, int anoLetivo) {
        super(id, nome, email, matricula);
        this.curso = curso;
        this.anoLetivo = anoLetivo;
    }

    // Getters e Setters
    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public int getAnoLetivo() {
        return anoLetivo;
    }

    public void setAnoLetivo(int anoLetivo) {
        this.anoLetivo = anoLetivo;
    }

    @Override
    public String getIdentificador() {
        return getMatricula();
    }

    @Override
    public String toString() {
        return "Estudante{id=" + getId() + ", nome='" + getNome() + "', matricula='" + getMatricula() +
               "', curso='" + curso + "', anoLetivo=" + anoLetivo +
               ", emprestimosAtivos=" + numeroEmprestimosAtivos() + "}";
    }
}
