package isptec.biblioteca.service;

import isptec.biblioteca.model.Livro;

import java.util.List;

/**
 * Interface do serviço de Chatbot com IA.
 * Permite integração com APIs de IA como OpenAI, Google AI, etc.
 */
public interface ChatbotService {

    /**
     * Processa uma mensagem do usuário e retorna uma resposta.
     * @param mensagem a mensagem do usuário
     * @param contexto contexto adicional (histórico, dados do usuário, etc.)
     * @return resposta gerada
     */
    String processarMensagem(String mensagem, String contexto);

    /**
     * Gera recomendações de livros baseadas no histórico.
     * @param historico lista de livros já lidos
     * @return lista de livros recomendados
     */
    List<Livro> recomendarLivros(List<Livro> historico);

    /**
     * Responde a uma pergunta sobre a biblioteca.
     * @param pergunta a pergunta do usuário
     * @return resposta
     */
    String responderPergunta(String pergunta);

    /**
     * Verifica se o serviço de IA está disponível.
     * @return true se a IA está disponível
     */
    boolean isIADisponivel();

    /**
     * Define a chave de API para serviços externos.
     * @param apiKey a chave de API
     */
    void setApiKey(String apiKey);
}

