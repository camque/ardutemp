package com.jhca.ardutemp.persistence;

import java.util.List;

import javax.ejb.Local;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;

/**
 * Interfaz del servicio de acceso a datos de Persistencia
 * @author Kmi Quevedo
 */
@Local
public interface IPersistencia {

	/**
	 * Retorna la instancia del manejador de entidades
	 * @return
	 */
	EntityManager getEntityManager();

	/**
	 * Retorna la instancia del builder de criteria
	 * @return
	 */
	CriteriaBuilder getBuilder();

	/**
	 * Permite crear un objeto dentro de la persistencia del sistema.
	 * @param <T>
	 * @param obj. Objeto que representa la instancia de la entidad que se quiere crear.
	 */
	<T> T create(T t);

	/**
	 * Permite modificar un objeto dentro de la persistencia del sistema.
	 * @param obj. Objeto que representa la instancia de la entidad que se quiere modificar.
	 */
	<T> T update(T t);


	/**
	 * Permite borrar un objeto dentro de la persistencia del sistema.
	 * @param obj. Objeto que representa la instancia de la entidad que se quiere borrar.
	 */
	void delete(Object obj);

	/**
	 * Retorna la lista de todos los elementos de una clase dada que se encuentran en el sistema.
	 * @param c. Clase cuyos objetos quieren encontrarse en el sistema.
	 * @return list. Listado de todos los objetos de una clase dada que se encuentran en el sistema.
	 */
	@SuppressWarnings("rawtypes")
	List findAll(Class clase);

	/**
	 * Retorna la instancia de una entidad dado un identificador y la clase de la entidadi.
	 * @param c. Clase de la instancia que se quiere buscar.
	 * @param id. Identificador unico del objeto.
	 * @return obj. Resultado de la consulta.
	 */
	@SuppressWarnings("rawtypes")
	Object findById(Class clase, Object idObject);

}
