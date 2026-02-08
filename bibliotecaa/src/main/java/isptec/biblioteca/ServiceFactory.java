package isptec.biblioteca;

import isptec.biblioteca.service.*;
import isptec.biblioteca.service.impl.*;

/**
 * Service Factory - Fábrica de serviços do sistema.
 * Implementa padrão Singleton para garantir instâncias únicas dos serviços.
 *
 * Facilita a substituição futura por um container de injeção de dependências.
 */
public final class ServiceFactory {

    private static ServiceFactory instance;

    // Serviços
    private AuthService authService;
    private LivroService livroService;
    private MembroService membroService;
    private EstudanteService estudanteService;
    private EmprestimoService emprestimoService;
    private ReservaService reservaService;
    private IAService iaService;

    private ServiceFactory() {
        initializeServices();
    }

    /**
     * Obtém a instância única da fábrica de serviços.
     *
     * @return a instância do ServiceFactory
     */
    public static synchronized ServiceFactory getInstance() {
        if (instance == null) {
            instance = new ServiceFactory();
        }
        return instance;
    }

    /**
     * Inicializa todos os serviços com suas dependências.
     */
    private void initializeServices() {
        // Inicializa serviços básicos
        authService = new AuthServiceImpl();
        livroService = new LivroServiceImpl();
        membroService = new MembroServiceImpl();
        estudanteService = new EstudanteServiceImpl();

        // Inicializa serviço de reserva
        reservaService = new ReservaServiceImpl();

        // Inicializa serviço de empréstimo com dependência de reserva
        EmprestimoServiceImpl emprestimoImpl = new EmprestimoServiceImpl();
        emprestimoImpl.setReservaService(reservaService);
        emprestimoService = emprestimoImpl;

        // Inicializa serviço de IA com dependência de livro
        IAServiceImpl iaImpl = new IAServiceImpl();
        iaImpl.setLivroService(livroService);
        iaService = iaImpl;
    }

    /**
     * Reinicializa todos os serviços (útil para testes).
     */
    public void reset() {
        initializeServices();
    }

    // === GETTERS DOS SERVIÇOS ===

    public AuthService getAuthService() {
        return authService;
    }

    public LivroService getLivroService() {
        return livroService;
    }

    public MembroService getMembroService() {
        return membroService;
    }

    public EstudanteService getEstudanteService() {
        return estudanteService;
    }

    public EmprestimoService getEmprestimoService() {
        return emprestimoService;
    }

    public ReservaService getReservaService() {
        return reservaService;
    }

    public IAService getIAService() {
        return iaService;
    }
}

