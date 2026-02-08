package isptec.biblioteca.service;

import isptec.biblioteca.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class LibraryService {
    private static LibraryService instance;
    private List<Livro> livros;
    private List<Membro> membros;
    private List<Emprestimo> emprestimos;
    private List<Reserva> reservas;

    private LibraryService() {
        livros = new ArrayList<>();
        membros = new ArrayList<>();
        emprestimos = new ArrayList<>();
        reservas = new ArrayList<>();
        carregarDadosMock();
    }

    public static LibraryService getInstance() {
        if (instance == null) {
            instance = new LibraryService();
        }
        return instance;
    }

    private void carregarDadosMock() {
        // Livros de exemplo
        adicionarLivro(new Livro("1", "Clean Code", "Robert C. Martin", "978-0132350884", 
            "Prentice Hall", 2008, "Programação", 5, 
            "Um guia sobre como escrever código limpo e manutenível"));
        
        adicionarLivro(new Livro("2", "Design Patterns", "Gang of Four", "978-0201633612", 
            "Addison-Wesley", 1994, "Programação", 3, 
            "Padrões de projeto orientados a objetos"));
        
        adicionarLivro(new Livro("3", "Estruturas de Dados e Algoritmos em Java", "Robert Lafore", 
            "978-8576050841", "Ciência Moderna", 2005, "Algoritmos", 4, 
            "Introdução completa a estruturas de dados"));

        adicionarLivro(new Livro("4", "The Pragmatic Programmer", "David Thomas", "978-0135957059",
            "Addison-Wesley", 2019, "Programação", 3,
            "Seu caminho para a maestria em programação"));

        adicionarLivro(new Livro("5", "Introduction to Algorithms", "Thomas H. Cormen", "978-0262033848",
            "MIT Press", 2009, "Algoritmos", 2,
            "O texto mais completo sobre algoritmos"));

        adicionarLivro(new Livro("6", "Engenharia de Software", "Ian Sommerville", "978-8579361081",
            "Pearson", 2011, "Engenharia de Software", 6,
            "Fundamentos de engenharia de software"));

        adicionarLivro(new Livro("7", "Redes de Computadores", "Andrew Tanenbaum", "978-8582604274",
            "Pearson", 2011, "Redes", 4,
            "Conceitos fundamentais de redes de computadores"));

        adicionarLivro(new Livro("8", "Sistemas Operacionais Modernos", "Andrew Tanenbaum", "978-8543005676",
            "Pearson", 2015, "Sistemas Operacionais", 3,
            "Princípios de sistemas operacionais"));

        adicionarLivro(new Livro("9", "Inteligência Artificial", "Stuart Russell", "978-8535237016",
            "Campus", 2013, "Inteligência Artificial", 2,
            "Uma abordagem moderna à IA"));

        adicionarLivro(new Livro("10", "Banco de Dados", "Abraham Silberschatz", "978-8535245356",
            "Campus", 2012, "Banco de Dados", 5,
            "Conceitos de sistemas de banco de dados"));

        // Membros de exemplo
        adicionarMembro(new Membro("1", "João Silva", "20230001@isptec.co.ao",
            "923456789", "20230001"));
        adicionarMembro(new Membro("2", "Maria Santos", "20230002@isptec.co.ao", 
            "923456788", "20230002"));
        adicionarMembro(new Membro("3", "Pedro Costa", "20230003@isptec.co.ao",
            "923456787", "20230003"));
        adicionarMembro(new Membro("4", "Ana Fernandes", "20230004@isptec.co.ao",
            "923456786", "20230004"));

        // Criar um empréstimo de exemplo
        realizarEmprestimo("1", "1"); // João emprestou Clean Code
        realizarEmprestimo("2", "2"); // Maria emprestou Design Patterns

        // Criar uma reserva de exemplo
        realizarReserva("3", "3"); // Pedro reservou Estruturas de Dados
    }

    // === LIVROS ===
    public void adicionarLivro(Livro livro) {
        livros.add(livro);
    }

    public void atualizarLivro(Livro livro) {
        for (int i = 0; i < livros.size(); i++) {
            if (livros.get(i).getId().equals(livro.getId())) {
                livros.set(i, livro);
                break;
            }
        }
    }

    public void removerLivro(String livroId) {
        livros.removeIf(l -> l.getId().equals(livroId));
    }

    public Livro buscarLivroPorId(String id) {
        return livros.stream()
                .filter(l -> l.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public List<Livro> listarLivros() {
        return new ArrayList<>(livros);
    }

    public List<Livro> buscarLivros(String termo) {
        String termoLower = termo.toLowerCase();
        return livros.stream()
                .filter(l -> l.getTitulo().toLowerCase().contains(termoLower) ||
                           l.getAutor().toLowerCase().contains(termoLower) ||
                           l.getCategoria().toLowerCase().contains(termoLower))
                .collect(Collectors.toList());
    }

    // === MEMBROS ===
    public void adicionarMembro(Membro membro) {
        membros.add(membro);
    }

    public void atualizarMembro(Membro membro) {
        for (int i = 0; i < membros.size(); i++) {
            if (membros.get(i).getId().equals(membro.getId())) {
                membros.set(i, membro);
                break;
            }
        }
    }

    public Membro buscarMembroPorId(String id) {
        return membros.stream()
                .filter(m -> m.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public List<Membro> listarMembros() {
        return new ArrayList<>(membros);
    }

    public void bloquearMembro(String membroId) {
        Membro membro = buscarMembroPorId(membroId);
        if (membro != null) {
            membro.bloquear();
        }
    }

    public void desbloquearMembro(String membroId) {
        Membro membro = buscarMembroPorId(membroId);
        if (membro != null) {
            membro.desbloquear();
        }
    }

    // === EMPRÉSTIMOS ===
    public boolean realizarEmprestimo(String livroId, String membroId) {
        Livro livro = buscarLivroPorId(livroId);
        Membro membro = buscarMembroPorId(membroId);

        if (livro == null || membro == null || !livro.isDisponivel() || !membro.isAtivo()) {
            return false;
        }

        String id = UUID.randomUUID().toString();
        Emprestimo emprestimo = new Emprestimo(id, livroId, membroId, 
                                              livro.getTitulo(), membro.getNome());
        emprestimos.add(emprestimo);

        livro.setQuantidadeDisponivel(livro.getQuantidadeDisponivel() - 1);
        return true;
    }

    public void devolverLivro(String emprestimoId) {
        Emprestimo emprestimo = buscarEmprestimoPorId(emprestimoId);
        if (emprestimo != null && emprestimo.isAtivo()) {
            emprestimo.setDataDevolucaoReal(java.time.LocalDate.now());
            emprestimo.calcularMulta();

            Livro livro = buscarLivroPorId(emprestimo.getLivroId());
            if (livro != null) {
                livro.setQuantidadeDisponivel(livro.getQuantidadeDisponivel() + 1);
            }
        }
    }

    public boolean renovarEmprestimo(String emprestimoId) {
        Emprestimo emprestimo = buscarEmprestimoPorId(emprestimoId);
        if (emprestimo == null || !emprestimo.podeRenovar()) {
            return false;
        }

        // Verificar se há reservas pendentes para este livro
        boolean temReservas = reservas.stream()
                .anyMatch(r -> r.getLivroId().equals(emprestimo.getLivroId()) && r.isPendente());

        if (temReservas) {
            return false;
        }

        emprestimo.renovar();
        return true;
    }

    public void aplicarMulta(String emprestimoId, double valor) {
        Emprestimo emprestimo = buscarEmprestimoPorId(emprestimoId);
        if (emprestimo != null) {
            emprestimo.setMulta(valor);
        }
    }

    public Emprestimo buscarEmprestimoPorId(String id) {
        return emprestimos.stream()
                .filter(e -> e.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public List<Emprestimo> listarEmprestimos() {
        return new ArrayList<>(emprestimos);
    }

    public List<Emprestimo> listarEmprestimosAtivos() {
        return emprestimos.stream()
                .filter(Emprestimo::isAtivo)
                .collect(Collectors.toList());
    }

    public List<Emprestimo> listarEmprestimosAtrasados() {
        return emprestimos.stream()
                .filter(Emprestimo::isAtrasado)
                .collect(Collectors.toList());
    }

    public List<Emprestimo> listarEmprestimosPorMembro(String membroId) {
        return emprestimos.stream()
                .filter(e -> e.getMembroId().equals(membroId) && e.isAtivo())
                .collect(Collectors.toList());
    }

    // === RESERVAS ===
    public boolean realizarReserva(String livroId, String membroId) {
        Livro livro = buscarLivroPorId(livroId);
        Membro membro = buscarMembroPorId(membroId);

        if (livro == null || membro == null) {
            return false;
        }

        String id = UUID.randomUUID().toString();
        Reserva reserva = new Reserva(id, livroId, membroId, 
                                     livro.getTitulo(), membro.getNome());
        reservas.add(reserva);
        return true;
    }

    public void cancelarReserva(String reservaId) {
        Reserva reserva = buscarReservaPorId(reservaId);
        if (reserva != null) {
            reserva.cancelar();
        }
    }

    public void atenderReserva(String reservaId) {
        Reserva reserva = buscarReservaPorId(reservaId);
        if (reserva != null) {
            reserva.concluir();
        }
    }

    public Reserva buscarReservaPorId(String id) {
        return reservas.stream()
                .filter(r -> r.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public List<Reserva> listarReservas() {
        return new ArrayList<>(reservas);
    }

    public List<Reserva> listarReservasPendentes() {
        return reservas.stream()
                .filter(Reserva::isPendente)
                .collect(Collectors.toList());
    }

    public List<Reserva> listarReservasPorMembro(String membroId) {
        return reservas.stream()
                .filter(r -> r.getMembroId().equals(membroId))
                .collect(Collectors.toList());
    }

    // === ESTATÍSTICAS ===
    public int getTotalLivros() {
        return livros.stream()
                .mapToInt(Livro::getQuantidade)
                .sum();
    }

    public int getLivrosEmprestados() {
        return (int) emprestimos.stream()
                .filter(Emprestimo::isAtivo)
                .count();
    }

    public int getReservasPendentes() {
        return (int) reservas.stream()
                .filter(Reserva::isPendente)
                .count();
    }

    public int getMembrosAtivos() {
        return (int) membros.stream()
                .filter(Membro::isAtivo)
                .count();
    }

    public double getTotalMultas() {
        return emprestimos.stream()
                .filter(Emprestimo::isAtivo)
                .peek(Emprestimo::calcularMulta)
                .mapToDouble(Emprestimo::getMulta)
                .sum();
    }
}