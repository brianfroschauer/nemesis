package dao;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

/**
 * Author: brianfroschauer
 * Date: 10/05/2018
 */
public interface DAO<T> {

    /**
     * Persist the given transient instance, first assigning a generated identifier.
     *
     * @param entity a transient instance of a persistent class.
     *
     * @return the generated identifier.
     */
    Integer create(@NotNull T entity);

    /**
     * Copy the state of the given entity onto the persistent object with the same
     * identifier. If there is no persistent instance currently associated with
     * the session, it will be loaded. Return the persistent instance.
     *
     * @param entity a detached instance with state to be copied.
     * @return an updated entity
     */
    T update(@NotNull T entity);

    /**
     * Remove a persistent instance from the database.
     *
     * @param entity the entity to be removed.
     */
    void delete(@NotNull T entity);

    /**
     * Return an optional persistent instance of the given entity class with the given identifier.
     *
     * @param entityType the entity type.
     * @param id an identifier.
     *
     * @return a optional persistent instance.
     */
    Optional<T> get(Class<T> entityType, Integer id);

    /**
     * Return all persistent instances of the given entity class.
     *
     * @param entityType the entity type
     *
     * @return a list of with the persistent instances
     */
    List<T> getAll(Class<T> entityType);
}