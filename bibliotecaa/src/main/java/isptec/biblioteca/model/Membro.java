package isptec.biblioteca.model;

import java.time.LocalDate;
import isptec.biblioteca.enumeracao.StatusMembro;

public class Membro {
    private String id;
    private String nome;
    private String email;
    private String telefone;
    private String matricula;
    private String senha; // Adicionado campo senha
    private boolean primeiraSenha; // Flag para forçar troca de senha
    private LocalDate dataIngresso;
    private StatusMembro status;
    

    public Membro(String id, String nome, String email, String telefone, String matricula) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.matricula = matricula;
        this.senha = "1234"; // Senha padrão
        this.primeiraSenha = true; // Primeira vez logando
        this.dataIngresso = LocalDate.now();
        this.status = StatusMembro.ATIVO;
    }

    // Getters e Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public boolean isPrimeiraSenha() {
        return primeiraSenha;
    }

    public void setPrimeiraSenha(boolean primeiraSenha) {
        this.primeiraSenha = primeiraSenha;
    }

    public LocalDate getDataIngresso() {
        return dataIngresso;
    }

    public void setDataIngresso(LocalDate dataIngresso) {
        this.dataIngresso = dataIngresso;
    }

    public StatusMembro getStatus() {
        return status;
    }

    public void setStatus(StatusMembro status) {
        this.status = status;
    }

    public boolean isAtivo() {
        return status == StatusMembro.ATIVO;
    }

    public void bloquear() {
        this.status = StatusMembro.BLOQUEADO;
    }

    public void desbloquear() {
        this.status = StatusMembro.ATIVO;
    }

    @Override
    public String toString() {
        return nome + " (" + matricula + ")";
    }
}