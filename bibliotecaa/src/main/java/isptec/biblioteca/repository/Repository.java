package isptec.biblioteca.repository;

import java.util.List;
import java.util.Optional;

/**
 * Interface genérica para operações de repositório.
 * Define o contrato básico CRUD para todas as entidades.
 *
 * @param <T>  tipo da entidade
 * @param <ID> tipo do identificador
 */
public interface Repository<T, ID> {

    /**
     * Salva uma entidade no repositório.
     *
     * @param entity a entidade a salvar
     * @return a entidade salva (pode ter ID gerado)
     */
    T save(T entity);

    /**
     * Busca uma entidade pelo ID.
     *
     * @param id o identificador da entidade
     * @return Optional contendo a entidade ou vazio
     */
    Optional<T> findById(ID id);

    /**
     * Lista todas as entidades.
     *
     * @return lista de todas as entidades
     */
    List<T> findAll();

    /**
     * Remove uma entidade pelo ID.
     *
     * @param id o identificador da entidade
     */
    void deleteById(ID id);

    /**
     * Remove uma entidade.
     *
     * @param entity a entidade a remover
     */
    void delete(T entity);

    /**
     * Verifica se uma entidade existe pelo ID.
     *
     * @param id o identificador
     * @return true se existe
     */
    boolean existsById(ID id);

    /**
     * Conta o total de entidades.
     *
     * @return número total de entidades
     */
    long count();

    /**
     * Remove todas as entidades.
     */
    void deleteAll();
}
