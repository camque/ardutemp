package com.jhca.ardutemp.persistence.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.jhca.ardutemp.persistence.IPersistencia;

/**
 * Session Bean implementation class Persistencia
 */
public abstract class AbstractPersistencia implements IPersistencia {

	/** Manejador de persistencia */
	@PersistenceContext
	protected transient EntityManager entityManager;
	/** Builder de consultas */
	private transient CriteriaBuilder builder;

	/**
	 * @see com.jhca.ardutemp.persistence.IPersistencia#getEntityManager()
	 */
	@Override
	public EntityManager getEntityManager() {
		return this.entityManager;
	}

	/**
	 * @see com.jhca.ardutemp.persistence.IPersistencia#getBuilder()
	 */
	@Override
	public CriteriaBuilder getBuilder() {
		return this.builder;
	}

	/**
	 * @see com.jhca.ardutemp.persistence.IPersistencia#create(java.lang.Object)
	 */
	@Override
	public <T> T create(final T t){
		this.entityManager.persist(t);
		this.entityManager.flush();
		this.entityManager.refresh(t);

		return t;
	}

	/**
	 * @see com.jhca.ardutemp.persistence.IPersistencia#update(java.lang.Object)
	 */
	@Override
	public <T> T update(final T t) {
		return this.entityManager.merge(t);
	}

	/**
	 * @see com.jhca.ardutemp.persistence.IPersistencia#delete(java.lang.Object)
	 */
	@Override
	public void delete(final Object obj){
		this.entityManager.remove( this.entityManager.merge(obj) );
	}

	/**
	 * @see com.jhca.ardutemp.persistence.IPersistencia#findAll(java.lang.Class)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List findAll(final Class clase) {
		final CriteriaQuery query = this.builder.createQuery(clase);
		final Root objetos = query.from(clase);
		query.select(objetos);

		return this.entityManager.createQuery(query).getResultList();
	}

	/**
	 * @see com.jhca.ardutemp.persistence.IPersistencia#findById(java.lang.Class, java.lang.Object)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Object findById(final Class clase, final Object idObject) {
		return this.entityManager.find(clase, idObject);
	}

}
