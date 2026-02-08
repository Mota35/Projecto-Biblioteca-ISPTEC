package isptec.biblioteca.util;

import java.util.regex.Pattern;

/**
 * Utilitários de validação de dados.
 */
public final class ValidacaoUtil {

    // Padrões de validação
    private static final Pattern PATTERN_EMAIL =
            Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final Pattern PATTERN_ISBN_10 =
            Pattern.compile("^\\d{9}[\\dX]$");
    private static final Pattern PATTERN_ISBN_13 =
            Pattern.compile("^\\d{13}$");
    private static final Pattern PATTERN_MATRICULA =
            Pattern.compile("^[A-Za-z0-9]+$");

    private ValidacaoUtil() {
        // Impede instanciação
    }

    /**
     * Valida se uma string não é nula ou vazia.
     */
    public static boolean naoVazio(String valor) {
        return valor != null && !valor.trim().isEmpty();
    }

    /**
     * Valida formato de email.
     */
    public static boolean emailValido(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        return PATTERN_EMAIL.matcher(email).matches();
    }

    /**
     * Valida formato de ISBN (10 ou 13 dígitos).
     */
    public static boolean isbnValido(String isbn) {
        if (isbn == null || isbn.isEmpty()) {
            return false;
        }
        String isbnLimpo = isbn.replaceAll("[\\s-]", "");
        return PATTERN_ISBN_10.matcher(isbnLimpo).matches() ||
               PATTERN_ISBN_13.matcher(isbnLimpo).matches();
    }

    /**
     * Valida formato de matrícula.
     */
    public static boolean matriculaValida(String matricula) {
        if (matricula == null || matricula.isEmpty()) {
            return false;
        }
        return PATTERN_MATRICULA.matcher(matricula).matches() &&
               matricula.length() >= 3;
    }

    /**
     * Valida se um valor é positivo.
     */
    public static boolean positivo(int valor) {
        return valor > 0;
    }

    /**
     * Valida se um valor é não-negativo.
     */
    public static boolean naoNegativo(int valor) {
        return valor >= 0;
    }

    /**
     * Valida se um valor está dentro de um intervalo.
     */
    public static boolean dentroDoIntervalo(int valor, int min, int max) {
        return valor >= min && valor <= max;
    }

    /**
     * Valida ano de publicação de livro.
     */
    public static boolean anoPublicacaoValido(int ano) {
        int anoAtual = java.time.LocalDate.now().getYear();
        return ano >= 1000 && ano <= anoAtual + 1;
    }
}

