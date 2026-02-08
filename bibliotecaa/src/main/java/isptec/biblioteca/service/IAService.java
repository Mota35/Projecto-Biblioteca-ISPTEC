package isptec.biblioteca.service;

import isptec.biblioteca.model.entities.Categoria;
import isptec.biblioteca.model.entities.Livro;
import isptec.biblioteca.model.entities.Membro;
import isptec.biblioteca.model.entities.Recomendacao;
import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Interface de serviço para funcionalidades de Inteligência Artificial.
 * Inclui recomendações, classificação e OCR.
 */
public interface IAService {

    /**
     * Recomenda livros para um membro baseado no seu histórico.
     *
     * @param membro o membro para recomendar
     * @return objeto Recomendacao com livros e motivo
     */
    Recomendacao recomendarLivros(Membro membro);

    /**
     * Recomenda livros de uma categoria específica.
     */
    List<Livro> recomendarPorCategoria(Categoria categoria, int limite);

    /**
     * Classifica automaticamente a categoria de um livro.
     */
    Categoria classificarCategoria(Livro livro);

    /**
     * Extrai dados de um livro a partir de uma imagem (OCR).
     */
    Livro extrairDadosImagem(File imagem);

    /**
     * Busca livros similares a um livro dado.
     */
    List<Livro> buscarLivrosSimilares(Livro livro, int limite);

    /**
     * Gera estatísticas sobre o histórico de leitura de um membro.
     */
    Map<String, Object> gerarEstatisticas(Membro membro);

    /**
     * Define o serviço de livros para buscar recomendações.
     */
    void setLivroService(LivroService livroService);
}
