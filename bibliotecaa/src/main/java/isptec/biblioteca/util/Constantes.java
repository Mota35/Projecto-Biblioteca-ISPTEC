package isptec.biblioteca.util;

/**
 * Constantes do sistema de biblioteca.
 * Centraliza todas as regras de negócio e configurações.
 */
public final class Constantes {

    private Constantes() {
        // Impede instanciação
    }

    // === REGRAS DE EMPRÉSTIMO ===

    /** Número máximo de empréstimos ativos por membro */
    public static final int MAX_EMPRESTIMOS_ATIVOS = 3;

    /** Prazo padrão de empréstimo em dias */
    public static final int PRAZO_EMPRESTIMO_DIAS = 14;

    /** Número máximo de renovações permitidas */
    public static final int MAX_RENOVACOES = 2;

    /** Valor da multa diária por atraso (em KZ) */
    public static final double MULTA_DIARIA_KZ = 50.0;

    /** Multa máxima permitida antes de bloquear o membro (em KZ) */
    public static final double MULTA_MAXIMA_KZ = 1000.0;

    // === REGRAS DE RESERVA ===

    /** Dias para expiração de uma reserva */
    public static final int DIAS_EXPIRACAO_RESERVA = 3;

    /** Número máximo de reservas ativas por membro */
    public static final int MAX_RESERVAS_ATIVAS = 5;

    // === CONFIGURAÇÕES DE RECOMENDAÇÃO (IA) ===

    /** Número máximo de recomendações a gerar */
    public static final int MAX_RECOMENDACOES = 5;

    // === INFORMAÇÕES DA BIBLIOTECA ===

    /** Nome da biblioteca */
    public static final String NOME_BIBLIOTECA = "Biblioteca ISPTEC";

    /** Horário de funcionamento */
    public static final String HORARIO_FUNCIONAMENTO = "Segunda a Sexta: 8h-20h | Sábado: 8h-14h";

    /** Localização */
    public static final String LOCALIZACAO = "Campus Principal do ISPTEC, Prédio Central, 2º Andar";

    /** Email de contato */
    public static final String EMAIL_CONTATO = "biblioteca@isptec.ao";

    // === MENSAGENS DO SISTEMA ===

    public static final String MSG_EMPRESTIMO_SUCESSO = "Empréstimo realizado com sucesso!";
    public static final String MSG_EMPRESTIMO_FALHA_LIMITE = "Limite de empréstimos atingido (máx. 3).";
    public static final String MSG_EMPRESTIMO_FALHA_BLOQUEADO = "Membro bloqueado. Regularize sua situação.";
    public static final String MSG_EMPRESTIMO_FALHA_INDISPONIVEL = "Livro não disponível para empréstimo.";
    public static final String MSG_EMPRESTIMO_FALHA_MULTA = "Multa pendente acima do limite permitido.";

    public static final String MSG_DEVOLUCAO_SUCESSO = "Devolução realizada com sucesso!";
    public static final String MSG_DEVOLUCAO_COM_MULTA = "Devolução realizada. Multa por atraso: %s KZ";

    public static final String MSG_RENOVACAO_SUCESSO = "Empréstimo renovado com sucesso!";
    public static final String MSG_RENOVACAO_FALHA_LIMITE = "Limite de renovações atingido (máx. 2).";
    public static final String MSG_RENOVACAO_FALHA_RESERVA = "Não é possível renovar. Existe reserva pendente.";
    public static final String MSG_RENOVACAO_FALHA_ATRASADO = "Não é possível renovar empréstimo atrasado.";

    public static final String MSG_RESERVA_SUCESSO = "Reserva realizada com sucesso!";
    public static final String MSG_RESERVA_FALHA_DISPONIVEL = "Livro disponível. Faça o empréstimo diretamente.";
    public static final String MSG_RESERVA_FALHA_DUPLICADA = "Você já possui uma reserva ativa para este livro.";

    // === FORMATOS DE DATA ===

    public static final String FORMATO_DATA = "dd/MM/yyyy";
    public static final String FORMATO_DATA_HORA = "dd/MM/yyyy HH:mm";
}

