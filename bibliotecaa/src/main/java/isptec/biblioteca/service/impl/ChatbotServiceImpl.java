package isptec.biblioteca.service.impl;

import isptec.biblioteca.model.Livro;
import isptec.biblioteca.service.ChatbotService;
import isptec.biblioteca.service.LibraryService;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Implementação do serviço de Chatbot com IA.
 * Suporta integração com OpenAI API ou funciona em modo offline com respostas inteligentes.
 */
public class ChatbotServiceImpl implements ChatbotService {

    private static final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";
    private static final String SYSTEM_PROMPT = """
        Você é o assistente virtual da Biblioteca Universitária ISPTEC.
        Seu nome é BiblioBot. Você ajuda estudantes e funcionários com:
        - Informações sobre livros e catálogo
        - Regras de empréstimo e devolução
        - Políticas da biblioteca
        - Reservas de livros
        - Multas e prazos
        - Recomendações de leitura
        
        Seja sempre educado, prestativo e conciso nas respostas.
        Use emojis ocasionalmente para tornar a conversa mais amigável.
        Responda sempre em português de Portugal/Angola.
        
        Regras da biblioteca:
        - Máximo 3 livros por empréstimo
        - Prazo de 14 dias
        - Até 2 renovações permitidas
        - Multa: 50 KZ por dia de atraso
        - Reservas só para livros indisponíveis
        """;

    private String apiKey;
    private final HttpClient httpClient;
    private final LibraryService libraryService;
    private boolean useAI = false;

    public ChatbotServiceImpl() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(30))
                .build();
        this.libraryService = LibraryService.getInstance();
    }

    @Override
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
        this.useAI = apiKey != null && !apiKey.isEmpty();
    }

    @Override
    public boolean isIADisponivel() {
        return useAI && apiKey != null && !apiKey.isEmpty();
    }

    @Override
    public String processarMensagem(String mensagem, String contexto) {
        if (isIADisponivel()) {
            try {
                return chamarOpenAI(mensagem, contexto);
            } catch (Exception e) {
                System.err.println("Erro ao chamar IA: " + e.getMessage());
                return gerarRespostaLocal(mensagem);
            }
        }
        return gerarRespostaLocal(mensagem);
    }

    @Override
    public String responderPergunta(String pergunta) {
        String contexto = construirContextoBiblioteca();
        return processarMensagem(pergunta, contexto);
    }

    @Override
    public List<Livro> recomendarLivros(List<Livro> historico) {
        List<Livro> recomendacoes = new ArrayList<>();
        List<Livro> todosLivros = libraryService.listarLivros();

        if (historico == null || historico.isEmpty()) {
            // Se não há histórico, retorna livros populares (disponíveis)
            for (Livro livro : todosLivros) {
                if (livro.isDisponivel() && recomendacoes.size() < 5) {
                    recomendacoes.add(livro);
                }
            }
            return recomendacoes;
        }

        // Extrair categorias do histórico
        List<String> categoriasLidas = historico.stream()
                .map(Livro::getCategoria)
                .distinct()
                .toList();

        // Recomendar livros das mesmas categorias que não foram lidos
        for (Livro livro : todosLivros) {
            if (livro.isDisponivel() &&
                categoriasLidas.contains(livro.getCategoria()) &&
                !historico.contains(livro) &&
                recomendacoes.size() < 5) {
                recomendacoes.add(livro);
            }
        }

        // Se não há recomendações suficientes, adiciona livros aleatórios
        if (recomendacoes.size() < 3) {
            Random random = new Random();
            for (Livro livro : todosLivros) {
                if (livro.isDisponivel() &&
                    !historico.contains(livro) &&
                    !recomendacoes.contains(livro) &&
                    recomendacoes.size() < 5) {
                    if (random.nextBoolean()) {
                        recomendacoes.add(livro);
                    }
                }
            }
        }

        return recomendacoes;
    }

    private String chamarOpenAI(String mensagem, String contexto) throws Exception {
        // Construir JSON manualmente (sem dependência externa)
        String escapedSystemPrompt = escapeJson(SYSTEM_PROMPT + "\n\nContexto atual:\n" + contexto);
        String escapedUserMessage = escapeJson(mensagem);

        String requestBody = String.format("""
            {
                "model": "gpt-3.5-turbo",
                "max_tokens": 500,
                "temperature": 0.7,
                "messages": [
                    {"role": "system", "content": "%s"},
                    {"role": "user", "content": "%s"}
                ]
            }
            """, escapedSystemPrompt, escapedUserMessage);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(OPENAI_API_URL))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + apiKey)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .timeout(Duration.ofSeconds(30))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            // Parse simples da resposta JSON
            String responseBody = response.body();
            int contentStart = responseBody.indexOf("\"content\":\"") + 11;
            int contentEnd = responseBody.indexOf("\"", contentStart);
            if (contentStart > 10 && contentEnd > contentStart) {
                return unescapeJson(responseBody.substring(contentStart, contentEnd));
            }
            // Fallback: tenta encontrar o content de outra forma
            contentStart = responseBody.indexOf("\"content\": \"") + 12;
            contentEnd = responseBody.indexOf("\"\n", contentStart);
            if (contentEnd == -1) contentEnd = responseBody.indexOf("\"}", contentStart);
            if (contentStart > 11 && contentEnd > contentStart) {
                return unescapeJson(responseBody.substring(contentStart, contentEnd));
            }
            throw new Exception("Não foi possível parsear a resposta da API");
        } else {
            throw new Exception("API Error: " + response.statusCode());
        }
    }

    private String escapeJson(String text) {
        return text
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    private String unescapeJson(String text) {
        return text
                .replace("\\n", "\n")
                .replace("\\r", "\r")
                .replace("\\t", "\t")
                .replace("\\\"", "\"")
                .replace("\\\\", "\\");
    }

    private String construirContextoBiblioteca() {
        return String.format("""
            Estatísticas atuais da biblioteca:
            - Total de livros: %d
            - Livros emprestados: %d
            - Reservas pendentes: %d
            - Membros ativos: %d
            - Total em multas: R$ %.2f
            """,
            libraryService.getTotalLivros(),
            libraryService.getLivrosEmprestados(),
            libraryService.getReservasPendentes(),
            libraryService.getMembrosAtivos(),
            libraryService.getTotalMultas()
        );
    }

    /**
     * Gera respostas locais inteligentes quando a IA não está disponível.
     */
    private String gerarRespostaLocal(String mensagem) {
        String msg = mensagem.toLowerCase().trim();

        // Buscar livros
        if (msg.startsWith("buscar ") || msg.startsWith("procurar ") || msg.startsWith("encontrar ")) {
            String termo = msg.replaceFirst("(buscar|procurar|encontrar)\\s+", "").trim();
            List<Livro> resultados = libraryService.buscarLivros(termo);
            if (resultados.isEmpty()) {
                return "📚 Não encontrei livros com \"" + termo + "\".\n\n" +
                       "💡 Dicas:\n" +
                       "• Verifique a ortografia\n" +
                       "• Tente palavras-chave diferentes\n" +
                       "• Busque pelo autor ou categoria";
            }
            StringBuilder sb = new StringBuilder("📚 Encontrei " + resultados.size() + " livro(s):\n\n");
            for (Livro livro : resultados) {
                sb.append(livro.isDisponivel() ? "✅ " : "❌ ")
                  .append("**").append(livro.getTitulo()).append("**\n")
                  .append("   📝 ").append(livro.getAutor())
                  .append(" | 📂 ").append(livro.getCategoria())
                  .append("\n\n");
            }
            return sb.toString();
        }

        // Estatísticas
        if (msg.contains("estatística") || msg.contains("numero") || msg.contains("quantidade") || msg.contains("dados")) {
            return String.format("""
                📊 **Estatísticas da Biblioteca ISPTEC**
                
                📚 Acervo total: %d exemplares
                📖 Em empréstimo: %d livros
                ⏳ Reservas na fila: %d
                👥 Membros ativos: %d
                💰 Multas acumuladas: R$ %.2f
                
                📈 Taxa de utilização: %.1f%%
                """,
                libraryService.getTotalLivros(),
                libraryService.getLivrosEmprestados(),
                libraryService.getReservasPendentes(),
                libraryService.getMembrosAtivos(),
                libraryService.getTotalMultas(),
                calcularTaxaUtilizacao()
            );
        }

        // Prazos e devolução
        if (msg.contains("prazo") || msg.contains("devolução") || msg.contains("devolver") || msg.contains("renovar")) {
            return """
                ⏰ **Prazos e Renovações**
                
                📅 Prazo de empréstimo: **14 dias**
                🔄 Renovações permitidas: **até 2 vezes**
                ⏱️ Cada renovação: **+14 dias**
                
                ⚠️ **Importante:**
                • Não é possível renovar se houver reserva
                • Livros atrasados geram multa automática
                • Renove pelo sistema antes do vencimento
                
                💡 **Dica:** Configure lembretes no seu calendário!
                """;
        }

        // Políticas
        if (msg.contains("política") || msg.contains("regra") || msg.contains("regulamento") || msg.contains("norma")) {
            return """
                📋 **Políticas da Biblioteca ISPTEC**
                
                📚 **Empréstimos:**
                • Máximo: 3 livros simultâneos
                • Prazo: 14 dias
                • Renovações: até 2 vezes
                
                📝 **Reservas:**
                • Apenas para livros indisponíveis
                • Ordem de chegada (FIFO)
                • Retirada em até 3 dias após disponível
                
                💰 **Multas:**
                • R$ 2,00 por dia de atraso
                • Multa > R$ 20,00 bloqueia novos empréstimos
                • Pagamento na secretaria
                
                🔒 **Acesso:**
                • Cartão de estudante obrigatório
                • Silêncio na área de leitura
                """;
        }

        // Multas
        if (msg.contains("multa") || msg.contains("atraso") || msg.contains("pagar") || msg.contains("dívida")) {
            return String.format("""
                💰 **Informações sobre Multas**
                
                📌 Valor: **R$ 2,00/dia** de atraso
                
                ⚠️ **Consequências:**
                • Multa > R$ 20,00 = bloqueio de empréstimos
                • Multa > R$ 50,00 = notificação ao coordenador
                
                💳 **Como pagar:**
                1. Vá à secretaria da biblioteca
                2. Apresente seu cartão de estudante
                3. Pague em dinheiro ou Multicaixa
                
                📊 Total atual em multas: **R$ %.2f**
                """,
                libraryService.getTotalMultas()
            );
        }

        // Reservas
        if (msg.contains("reserva") || msg.contains("fila") || msg.contains("espera")) {
            return String.format("""
                📝 **Sistema de Reservas**
                
                📌 **Como reservar:**
                1. Acesse "Consultar Livros"
                2. Encontre o livro desejado
                3. Clique em "Entrar na Fila" (se indisponível)
                
                ⏳ **Acompanhamento:**
                • Veja sua posição em "Minhas Reservas"
                • Receba notificação quando disponível
                • Retire em até 3 dias
                
                📊 Reservas pendentes agora: **%d**
                """,
                libraryService.getReservasPendentes()
            );
        }

        // Recomendações
        if (msg.contains("recomend") || msg.contains("sugest") || msg.contains("ler") || msg.contains("livro bom")) {
            List<Livro> recomendados = recomendarLivros(new ArrayList<>());
            if (recomendados.isEmpty()) {
                return "📚 No momento não tenho recomendações disponíveis. Tente novamente mais tarde!";
            }
            StringBuilder sb = new StringBuilder("📚 **Recomendações para você:**\n\n");
            for (int i = 0; i < recomendados.size(); i++) {
                Livro livro = recomendados.get(i);
                sb.append(String.format("%d. **%s**\n   📝 %s | 📂 %s\n\n",
                        i + 1, livro.getTitulo(), livro.getAutor(), livro.getCategoria()));
            }
            sb.append("💡 Visite o catálogo para ver mais opções!");
            return sb.toString();
        }

        // Horário
        if (msg.contains("horário") || msg.contains("hora") || msg.contains("aberto") || msg.contains("funciona")) {
            return """
                🕐 **Horário de Funcionamento**
                
                📅 Segunda a Sexta: 08h00 - 20h00
                📅 Sábado: 09h00 - 13h00
                📅 Domingo e Feriados: Fechado
                
                📍 Localização: Edifício Central, Piso 1
                📞 Contacto: +244 XXX XXX XXX
                📧 Email: biblioteca@isptec.co.ao
                """;
        }

        // Ajuda
        if (msg.contains("ajuda") || msg.contains("help") || msg.contains("opções") || msg.contains("comandos")) {
            return """
                🤖 **Como posso ajudá-lo?**
                
                📚 **Pesquisa:**
                • "buscar [termo]" - Procurar livros
                • "recomendações" - Sugestões de leitura
                
                ℹ️ **Informações:**
                • "estatísticas" - Dados da biblioteca
                • "prazos" - Sobre devolução e renovação
                • "políticas" - Regras da biblioteca
                • "multas" - Sobre atrasos e pagamentos
                • "reservas" - Como reservar livros
                • "horário" - Funcionamento da biblioteca
                
                💬 Ou faça qualquer pergunta em linguagem natural!
                """;
        }

        // Saudações
        if (msg.matches(".*(olá|oi|bom dia|boa tarde|boa noite|hey|hello).*")) {
            String[] saudacoes = {
                "Olá! 👋 Sou o BiblioBot, assistente da Biblioteca ISPTEC. Como posso ajudá-lo?",
                "Oi! 😊 Bem-vindo à biblioteca! Em que posso ser útil?",
                "Olá! 📚 Estou aqui para ajudar. O que precisa saber?"
            };
            return saudacoes[new Random().nextInt(saudacoes.length)] + "\n\nDigite **ajuda** para ver as opções.";
        }

        // Agradecimentos
        if (msg.matches(".*(obrigad|valeu|thanks|agradeço).*")) {
            String[] respostas = {
                "De nada! 😊 Estou sempre aqui para ajudar!",
                "Por nada! 📚 Boas leituras!",
                "Disponha! 👍 Qualquer dúvida, é só perguntar!"
            };
            return respostas[new Random().nextInt(respostas.length)];
        }

        // Despedida
        if (msg.matches(".*(tchau|adeus|até logo|bye).*")) {
            return "Até logo! 👋 Boas leituras e volte sempre! 📚";
        }

        // Resposta padrão inteligente
        return """
            🤔 Não tenho certeza se entendi sua pergunta.
            
            Posso ajudá-lo com:
            • 📚 Buscar livros
            • 📊 Estatísticas da biblioteca
            • ⏰ Prazos e renovações
            • 📋 Políticas e regras
            • 💰 Multas e pagamentos
            • 📝 Reservas de livros
            • 💡 Recomendações de leitura
            
            Digite **ajuda** para ver todos os comandos disponíveis.
            """;
    }

    private double calcularTaxaUtilizacao() {
        int total = libraryService.getTotalLivros();
        if (total == 0) return 0;
        return (libraryService.getLivrosEmprestados() * 100.0) / total;
    }
}



