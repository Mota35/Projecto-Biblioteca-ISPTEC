package isptec.biblioteca.model.entities;

import isptec.biblioteca.enumeracao.Perfil;

/**
 * Classe Bibliotecario - Representa um funcionário da biblioteca.
 * Herda de Pessoa e tem acesso administrativo ao sistema.
 */
public class Bibliotecario extends Pessoa {

    private String funcionarioId;
    private String departamento;
    private String cargo;

    public Bibliotecario() {
        super();
        this.funcionarioId = "";
        this.departamento = "Biblioteca Central";
        this.cargo = "Bibliotecário";
    }

    public Bibliotecario(int id, String nome, String email, String funcionarioId) {
        super(id, nome, email, Perfil.ADMIN);
        this.funcionarioId = funcionarioId;
        this.departamento = "Biblioteca Central";
        this.cargo = "Bibliotecário";
    }

    public Bibliotecario(int id, String nome, String email, String funcionarioId, String departamento, String cargo) {
        super(id, nome, email, Perfil.ADMIN);
        this.funcionarioId = funcionarioId;
        this.departamento = departamento;
        this.cargo = cargo;
    }

    // Getters e Setters
    public String getFuncionarioId() {
        return funcionarioId;
    }

    public void setFuncionarioId(String funcionarioId) {
        this.funcionarioId = funcionarioId;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    @Override
    public String getIdentificador() {
        return this.funcionarioId;
    }

    @Override
    public String toString() {
        return "Bibliotecario{id=" + id + ", nome='" + nome + "', funcionarioId='" + funcionarioId +
               "', departamento='" + departamento + "', cargo='" + cargo + "'}";
    }
}
