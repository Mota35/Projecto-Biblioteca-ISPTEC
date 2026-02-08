package isptec.biblioteca.model.entities;

import java.io.File;

/**
 * Classe OCRService - Serviço de Reconhecimento Óptico de Caracteres.
 *
 * Responsável por extrair dados de livros a partir de imagens
 * (capa ou folha de rosto).
 *
 * DADOS QUE PODEM SER EXTRAÍDOS:
 * - Título
 * - Autor
 * - ISBN
 * - Editora
 *
 * NOTA: Esta é uma implementação simulada para fins de demonstração.
 * Em um sistema real, utilizaria bibliotecas como Tesseract OCR.
 */
public class OCRService {

    private String nome;
    private boolean disponivel;

    public OCRService() {
        this.nome = "BiblioOCR";
        this.disponivel = true;
    }

    public OCRService(String nome) {
        this.nome = nome;
        this.disponivel = true;
    }

    // === MÉTODOS PRINCIPAIS ===

    /**
     * Extrai dados de um livro a partir de uma imagem.
     *
     * @param imagem arquivo de imagem (JPG, PNG, etc.)
     * @return objeto Livro com os dados extraídos, ou null se falhar
     */
    public Livro extrairDadosImagem(File imagem) {
        if (imagem == null || !imagem.exists()) {
            return null;
        }

        // Simulação de extração OCR
        // Em um sistema real, aqui usaríamos Tesseract ou Google Vision API

        Livro livro = new Livro();

        // Extrair nome do arquivo como título simulado
        String nomeArquivo = imagem.getName();
        if (nomeArquivo.contains(".")) {
            nomeArquivo = nomeArquivo.substring(0, nomeArquivo.lastIndexOf('.'));
        }

        livro.setTitulo(formatarTitulo(nomeArquivo));
        livro.setIsbn(gerarISBNSimulado());

        return livro;
    }

    /**
     * Extrai dados de um livro a partir do caminho de uma imagem.
     *
     * @param caminhoImagem caminho para o arquivo de imagem
     * @return objeto Livro com os dados extraídos, ou null se falhar
     */
    public Livro extrairDadosImagem(String caminhoImagem) {
        if (caminhoImagem == null || caminhoImagem.isEmpty()) {
            return null;
        }
        return extrairDadosImagem(new File(caminhoImagem));
    }

    /**
     * Verifica se o serviço de OCR está disponível.
     *
     * @return true se o serviço está operacional
     */
    public boolean estaDisponivel() {
        return disponivel;
    }

    /**
     * Processa texto extraído e identifica campos do livro.
     *
     * @param textoExtraido texto bruto da imagem
     * @return objeto Livro com campos identificados
     */
    public Livro processarTextoExtraido(String textoExtraido) {
        if (textoExtraido == null || textoExtraido.isEmpty()) {
            return null;
        }

        Livro livro = new Livro();
        String[] linhas = textoExtraido.split("\n");

        for (String linha : linhas) {
            linha = linha.trim();

            // Identificar ISBN
            if (linha.matches(".*\\d{10}.*") || linha.matches(".*\\d{13}.*")) {
                String isbn = extrairISBN(linha);
                if (isbn != null) {
                    livro.setIsbn(isbn);
                }
            }

            // Identificar título (geralmente a primeira linha não vazia)
            if (livro.getTitulo() == null || livro.getTitulo().isEmpty()) {
                if (!linha.isEmpty() && !linha.matches("\\d+")) {
                    livro.setTitulo(linha);
                }
            }
        }

        return livro;
    }

    // === MÉTODOS AUXILIARES ===

    /**
     * Formata um título extraído.
     */
    private String formatarTitulo(String texto) {
        if (texto == null || texto.isEmpty()) {
            return "Título Desconhecido";
        }

        // Remove underscores e hífens extras
        texto = texto.replace("_", " ").replace("-", " ");

        // Capitaliza primeira letra de cada palavra
        StringBuilder resultado = new StringBuilder();
        boolean proximaMaiuscula = true;

        for (char c : texto.toCharArray()) {
            if (Character.isWhitespace(c)) {
                proximaMaiuscula = true;
                resultado.append(c);
            } else if (proximaMaiuscula) {
                resultado.append(Character.toUpperCase(c));
                proximaMaiuscula = false;
            } else {
                resultado.append(Character.toLowerCase(c));
            }
        }

        return resultado.toString().trim();
    }

    /**
     * Extrai ISBN de uma linha de texto.
     */
    private String extrairISBN(String texto) {
        // Remove caracteres não numéricos exceto 'X' (usado em ISBN-10)
        String numeros = texto.replaceAll("[^0-9X]", "");

        // Verifica se tem tamanho válido de ISBN
        if (numeros.length() == 10 || numeros.length() == 13) {
            return numeros;
        }

        return null;
    }

    /**
     * Gera um ISBN simulado para demonstração.
     */
    private String gerarISBNSimulado() {
        StringBuilder isbn = new StringBuilder("978");
        for (int i = 0; i < 10; i++) {
            isbn.append((int) (Math.random() * 10));
        }
        return isbn.toString();
    }

    // === GETTERS E SETTERS ===

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public boolean isDisponivel() {
        return disponivel;
    }

    public void setDisponivel(boolean disponivel) {
        this.disponivel = disponivel;
    }

    @Override
    public String toString() {
        return "OCRService{nome='" + nome + "', disponivel=" + disponivel + "}";
    }
}

