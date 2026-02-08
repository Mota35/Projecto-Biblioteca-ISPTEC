package isptec.biblioteca.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * Utilitários para manipulação de datas.
 */
public final class DataUtil {

    private static final DateTimeFormatter FORMATTER_DATA =
            DateTimeFormatter.ofPattern(Constantes.FORMATO_DATA);
    private static final DateTimeFormatter FORMATTER_DATA_HORA =
            DateTimeFormatter.ofPattern(Constantes.FORMATO_DATA_HORA);

    private DataUtil() {
        // Impede instanciação
    }

    /**
     * Formata uma data para string no formato dd/MM/yyyy.
     */
    public static String formatar(LocalDate data) {
        if (data == null) {
            return "";
        }
        return data.format(FORMATTER_DATA);
    }

    /**
     * Formata uma data/hora para string.
     */
    public static String formatar(LocalDateTime dataHora) {
        if (dataHora == null) {
            return "";
        }
        return dataHora.format(FORMATTER_DATA_HORA);
    }

    /**
     * Parse de string para LocalDate.
     */
    public static LocalDate parse(String dataString) {
        if (dataString == null || dataString.isEmpty()) {
            return null;
        }
        try {
            return LocalDate.parse(dataString, FORMATTER_DATA);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Calcula a diferença em dias entre duas datas.
     */
    public static long diasEntre(LocalDate inicio, LocalDate fim) {
        if (inicio == null || fim == null) {
            return 0;
        }
        return ChronoUnit.DAYS.between(inicio, fim);
    }

    /**
     * Verifica se uma data já passou.
     */
    public static boolean jaPassou(LocalDate data) {
        if (data == null) {
            return false;
        }
        return data.isBefore(LocalDate.now());
    }

    /**
     * Calcula a data de devolução baseada no prazo padrão.
     */
    public static LocalDate calcularDataDevolucao(LocalDate dataEmprestimo) {
        if (dataEmprestimo == null) {
            dataEmprestimo = LocalDate.now();
        }
        return dataEmprestimo.plusDays(Constantes.PRAZO_EMPRESTIMO_DIAS);
    }

    /**
     * Calcula a data de expiração de uma reserva.
     */
    public static LocalDate calcularDataExpiracaoReserva(LocalDate dataReserva) {
        if (dataReserva == null) {
            dataReserva = LocalDate.now();
        }
        return dataReserva.plusDays(Constantes.DIAS_EXPIRACAO_RESERVA);
    }

    /**
     * Retorna a data atual.
     */
    public static LocalDate hoje() {
        return LocalDate.now();
    }

    /**
     * Retorna a data/hora atual.
     */
    public static LocalDateTime agora() {
        return LocalDateTime.now();
    }
}

