package isptec.biblioteca.model.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Classe Chatbot - Assistente virtual para ajudar os membros da biblioteca.
 *
 * Funcionalidades:
 * - Responder perguntas frequentes
 * - Sugerir livros baseado em preferências
 * - Ajudar na navegação do sistema
 */
public class Chatbot {

    private String nome;
    private Map<String, String> respostasFrequentes;
    private List<String> historicoConversa;

    public Chatbot() {
        this.nome = "BiblioBot";
        this.respostasFrequentes = new HashMap<>();
        this.historicoConversa = new ArrayList<>();
        inicializarRespostas();
    }

    public Chatbot(String nome) {
        this.nome = nome;
        this.respostasFrequentes = new HashMap<>();
        this.historicoConversa = new ArrayList<>();
        inicializarRespostas();
    }

    /**
     * Inicializa as respostas frequentes do chatbot.
     */
    private void inicializarRespostas() {
        respostasFrequentes.put("horario", "A biblioteca funciona de segunda a sexta, das 8h às 20h, e aos sábados das 8h às 14h.");
        respostasFrequentes.put("emprestimo", "Para realizar um empréstimo, você precisa estar registrado e ter seu cartão de estudante. Máximo de 3 livros por vez.");
        respostasFrequentes.put("renovacao", "Você pode renovar um empréstimo até 2 vezes, desde que não haja reservas pendentes para o livro.");
        respostasFrequentes.put("reserva", "Você pode reservar livros que estão emprestados. Quando o livro estiver disponível, você será notificado.");
        respostasFrequentes.put("multa", "A multa por atraso é de 50 KZ por dia. Multas acumuladas podem bloquear novos empréstimos.");
        respostasFrequentes.put("prazo", "O prazo padrão de empréstimo é de 14 dias.");
        respostasFrequentes.put("cadastro", "Para se cadastrar, você precisa apresentar seu documento de identificação e comprovante de matrícula.");
        respostasFrequentes.put("localizacao", "A biblioteca fica no campus principal do ISPTEC, prédio central, 2º andar.");
        respostasFrequentes.put("ajuda", "Posso ajudá-lo com: horário, empréstimo, renovação, reserva, multa, prazo, cadastro, localização. Digite uma dessas palavras!");
        respostasFrequentes.put("ola", "Olá! Sou o BiblioBot, seu assistente virtual da Biblioteca ISPTEC. Como posso ajudá-lo hoje?");
        respostasFrequentes.put("obrigado", "De nada! Estou sempre à disposição para ajudar. Bons estudos!");
    }

    /**
     * Responde a uma pergunta do usuário.
     *
     * @param pergunta a pergunta do usuário
     * @return resposta do chatbot
     */
    public String responderPergunta(String pergunta) {
        if (pergunta == null || pergunta.trim().isEmpty()) {
            return "Desculpe, não entendi. Pode repetir?";
        }

        String perguntaLower = pergunta.toLowerCase().trim();
        historicoConversa.add("Usuário: " + pergunta);

        // Procura por palavras-chave nas respostas frequentes
        for (Map.Entry<String, String> entry : respostasFrequentes.entrySet()) {
            if (perguntaLower.contains(entry.getKey())) {
                String resposta = entry.getValue();
                historicoConversa.add(nome + ": " + resposta);
                return resposta;
            }
        }

        // Resposta padrão
        String respostaPadrao = "Desculpe, não tenho informação sobre isso. Tente perguntar sobre: horário, empréstimo, renovação, reserva, multa ou cadastro.";
        historicoConversa.add(nome + ": " + respostaPadrao);
        return respostaPadrao;
    }

    /**
     * Sugere livros baseado no histórico do membro.
     *
     * @param membro o membro para o qual sugerir
     * @return lista de livros sugeridos (implementação básica)
     */
    public List<Livro> sugerirLivro(Membro membro) {
        List<Livro> sugestoes = new ArrayList<>();

        if (membro == null) {
            return sugestoes;
        }

        // Coleta categorias do histórico de empréstimos
        List<Categoria> categoriasPreferidas = new ArrayList<>();
        for (Emprestimo emp : membro.getHistoricoEmprestimos()) {
            if (emp.getLivro() != null) {
                categoriasPreferidas.addAll(emp.getLivro().getCategorias());
            }
        }

        // Aqui seria implementada a lógica de recomendação
        // Por enquanto retorna lista vazia (será implementado no IAService)

        return sugestoes;
    }

    /**
     * Adiciona uma nova resposta frequente.
     *
     * @param palavraChave a palavra-chave para identificar a pergunta
     * @param resposta a resposta associada
     */
    public void adicionarResposta(String palavraChave, String resposta) {
        if (palavraChave != null && resposta != null) {
            respostasFrequentes.put(palavraChave.toLowerCase(), resposta);
        }
    }

    /**
     * Limpa o histórico de conversa.
     */
    public void limparHistorico() {
        historicoConversa.clear();
    }

    // === GETTERS E SETTERS ===

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<String> getHistoricoConversa() {
        return new ArrayList<>(historicoConversa);
    }

    public Map<String, String> getRespostasFrequentes() {
        return new HashMap<>(respostasFrequentes);
    }

    @Override
    public String toString() {
        return "Chatbot{nome='" + nome + "', respostas=" + respostasFrequentes.size() +
               ", historicoMensagens=" + historicoConversa.size() + "}";
    }
}

