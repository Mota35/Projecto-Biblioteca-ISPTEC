package isptec.biblioteca.service.impl;

import isptec.biblioteca.model.entities.*;
import isptec.biblioteca.service.IAService;
import isptec.biblioteca.service.LivroService;
import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementação do serviço de Inteligência Artificial.
 *
 * Inclui:
 * - Recomendação de livros baseada em histórico
 * - Classificação automática de categorias
 * - OCR para extração de dados de imagens (simulado)
 */
public class IAServiceImpl implements IAService {

    private LivroService livroService;
    private static final int MAX_RECOMENDACOES = 5;

    public IAServiceImpl() {
    }

    public IAServiceImpl(LivroService livroService) {
        this.livroService = livroService;
    }

    public void setLivroService(LivroService livroService) {
        this.livroService = livroService;
    }

    @Override
    public Recomendacao recomendarLivros(Membro membro) {
        if (membro == null) {
            return new Recomendacao();
        }

        Recomendacao recomendacao = new Recomendacao(0, membro);

        // Coleta categorias do histórico de empréstimos
        Map<Categoria, Integer> categoriaContagem = new HashMap<>();
        Set<Integer> livrosJaLidos = new HashSet<>();

        for (Emprestimo emp : membro.getHistoricoEmprestimos()) {
            if (emp.getLivro() != null) {
                livrosJaLidos.add(emp.getLivro().getId());
                for (Categoria cat : emp.getLivro().getCategorias()) {
                    categoriaContagem.merge(cat, 1, Integer::sum);
                }
            }
        }

        // Se não tem histórico, recomenda livros populares
        if (categoriaContagem.isEmpty()) {
            recomendacao.setMotivo("Livros populares da biblioteca");
            if (livroService != null) {
                List<Livro> populares = livroService.listarLivrosDisponiveis()
                        .stream()
                        .limit(MAX_RECOMENDACOES)
                        .collect(Collectors.toList());
                recomendacao.setLivrosRecomendados(populares);
            }
            return recomendacao;
        }

        // Ordena categorias por frequência (mais lidas primeiro)
        List<Categoria> categoriasOrdenadas = categoriaContagem.entrySet().stream()
                .sorted(Map.Entry.<Categoria, Integer>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        // Busca livros das categorias favoritas que o membro ainda não leu
        List<Livro> recomendados = new ArrayList<>();
        StringBuilder motivoBuilder = new StringBuilder("Baseado nas suas leituras de: ");

        for (Categoria cat : categoriasOrdenadas) {
            if (recomendados.size() >= MAX_RECOMENDACOES) break;

            if (livroService != null) {
                List<Livro> livrosCategoria = livroService.buscarPorCategoria(cat).stream()
                        .filter(l -> !livrosJaLidos.contains(l.getId()))
                        .filter(Livro::estaDisponivel)
                        .limit(MAX_RECOMENDACOES - recomendados.size())
                        .collect(Collectors.toList());

                if (!livrosCategoria.isEmpty()) {
                    recomendados.addAll(livrosCategoria);
                    motivoBuilder.append(cat.getNome()).append(", ");
                }
            }
        }

        String motivo = motivoBuilder.toString();
        if (motivo.endsWith(", ")) {
            motivo = motivo.substring(0, motivo.length() - 2);
        }

        recomendacao.setLivrosRecomendados(recomendados);
        recomendacao.setMotivo(motivo);

        return recomendacao;
    }

    @Override
    public List<Livro> recomendarPorCategoria(Categoria categoria, int limite) {
        if (categoria == null || livroService == null) {
            return new ArrayList<>();
        }

        return livroService.buscarPorCategoria(categoria).stream()
                .filter(Livro::estaDisponivel)
                .limit(limite > 0 ? limite : MAX_RECOMENDACOES)
                .collect(Collectors.toList());
    }

    @Override
    public Categoria classificarCategoria(Livro livro) {
        if (livro == null) {
            return new Categoria(0, "Geral", "Categoria geral");
        }

        String titulo = livro.getTitulo() != null ? livro.getTitulo().toLowerCase() : "";
        String descricao = livro.getDescricao() != null ? livro.getDescricao().toLowerCase() : "";
        String texto = titulo + " " + descricao;

        // Classificação baseada em palavras-chave (simulação de IA)
        if (contem(texto, "programação", "software", "código", "java", "python", "algoritmo")) {
            return new Categoria(1, "Informática", "Livros de ciência da computação e programação");
        }
        if (contem(texto, "matemática", "cálculo", "álgebra", "equação", "estatística")) {
            return new Categoria(2, "Matemática", "Livros de matemática e estatística");
        }
        if (contem(texto, "física", "química", "biologia", "ciência", "laboratório")) {
            return new Categoria(3, "Ciências", "Livros de ciências naturais");
        }
        if (contem(texto, "administração", "gestão", "negócio", "empresa", "marketing")) {
            return new Categoria(4, "Gestão", "Livros de administração e negócios");
        }
        if (contem(texto, "direito", "lei", "jurídico", "constitucional")) {
            return new Categoria(5, "Direito", "Livros de ciências jurídicas");
        }
        if (contem(texto, "romance", "amor", "história", "aventura", "ficção")) {
            return new Categoria(6, "Literatura", "Romances e ficção");
        }
        if (contem(texto, "engenharia", "civil", "mecânica", "elétrica", "estrutura")) {
            return new Categoria(7, "Engenharia", "Livros de engenharia");
        }

        return new Categoria(0, "Geral", "Categoria geral");
    }

    private boolean contem(String texto, String... palavras) {
        for (String palavra : palavras) {
            if (texto.contains(palavra)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Livro extrairDadosImagem(File imagem) {
        // Simulação de OCR - Em produção, usaria bibliotecas como Tesseract
        if (imagem == null || !imagem.exists()) {
            return null;
        }

        // Cria um livro com dados simulados baseados no nome do arquivo
        Livro livro = new Livro();
        String nomeArquivo = imagem.getName();

        // Remove extensão
        if (nomeArquivo.contains(".")) {
            nomeArquivo = nomeArquivo.substring(0, nomeArquivo.lastIndexOf("."));
        }

        livro.setTitulo("Livro extraído: " + nomeArquivo);
        livro.setIsbn(gerarIsbnAleatorio());
        livro.setEditora("Editora Desconhecida");
        livro.setQuantidadeTotal(1);
        livro.setQuantidadeDisponivel(1);

        // Adiciona um autor genérico
        Autor autor = new Autor(0, "Autor a identificar");
        livro.adicionarAutor(autor);

        // Classifica automaticamente
        Categoria categoria = classificarCategoria(livro);
        livro.adicionarCategoria(categoria);

        return livro;
    }

    private String gerarIsbnAleatorio() {
        Random random = new Random();
        StringBuilder isbn = new StringBuilder("978-");
        for (int i = 0; i < 10; i++) {
            if (i == 1 || i == 5 || i == 9) {
                isbn.append("-");
            }
            isbn.append(random.nextInt(10));
        }
        return isbn.toString();
    }

    @Override
    public List<Livro> buscarLivrosSimilares(Livro livro, int limite) {
        if (livro == null || livroService == null) {
            return new ArrayList<>();
        }

        Set<Livro> similares = new HashSet<>();

        // Busca livros da mesma categoria
        for (Categoria cat : livro.getCategorias()) {
            similares.addAll(livroService.buscarPorCategoria(cat));
        }

        // Busca livros do mesmo autor
        for (Autor autor : livro.getAutores()) {
            similares.addAll(livroService.buscarPorAutor(autor.getNome()));
        }

        // Remove o próprio livro e filtra por disponibilidade
        return similares.stream()
                .filter(l -> l.getId() != livro.getId())
                .filter(Livro::estaDisponivel)
                .limit(limite > 0 ? limite : MAX_RECOMENDACOES)
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, Object> gerarEstatisticas(Membro membro) {
        Map<String, Object> estatisticas = new HashMap<>();

        if (membro == null) {
            return estatisticas;
        }

        List<Emprestimo> historico = membro.getHistoricoEmprestimos();

        // Total de empréstimos
        estatisticas.put("totalEmprestimos", historico.size());

        // Empréstimos ativos
        estatisticas.put("emprestimosAtivos", membro.numeroEmprestimosAtivos());

        // Categorias favoritas
        Map<String, Integer> categorias = new HashMap<>();
        for (Emprestimo emp : historico) {
            if (emp.getLivro() != null) {
                for (Categoria cat : emp.getLivro().getCategorias()) {
                    categorias.merge(cat.getNome(), 1, Integer::sum);
                }
            }
        }

        List<String> categoriasFavoritas = categorias.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(3)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        estatisticas.put("categoriasFavoritas", categoriasFavoritas);

        // Autores favoritos
        Map<String, Integer> autores = new HashMap<>();
        for (Emprestimo emp : historico) {
            if (emp.getLivro() != null) {
                for (Autor autor : emp.getLivro().getAutores()) {
                    autores.merge(autor.getNome(), 1, Integer::sum);
                }
            }
        }

        List<String> autoresFavoritos = autores.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(3)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        estatisticas.put("autoresFavoritos", autoresFavoritos);

        // Multa total
        estatisticas.put("multaTotal", membro.calcularTotalMultas());

        return estatisticas;
    }
}

