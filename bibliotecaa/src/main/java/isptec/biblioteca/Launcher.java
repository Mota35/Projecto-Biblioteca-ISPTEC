package isptec.biblioteca;

/**
 * Classe Launcher para iniciar a aplicação JavaFX.
 * Esta classe é necessária para contornar o problema
 * "JavaFX runtime components are missing" quando se executa
 * a aplicação diretamente sem o plugin Maven.
 *
 * Use esta classe como ponto de entrada principal.
 */
public class Launcher {

    public static void main(String[] args) {
        Program.main(args);
    }
}

