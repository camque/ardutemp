package com.jhca.ardutemp.persistence.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.log4j.Logger;

import com.jhca.ardutemp.genericos.constants.ConstantesInt;
import com.jhca.ardutemp.genericos.constants.ConstantesString;
import com.jhca.ardutemp.genericos.utils.UtilBusiness;
import com.jhca.ardutemp.persistence.ITemperaturaJPA;
import com.jhca.ardutemp.persistence.entities.Temperatura;
import com.jhca.ardutemp.persistence.entities.Temperatura_;

/**
 * Session Bean implementation class TemperaturaJPA
 */
@Stateless
@LocalBean
public class TemperaturaJPA extends AbstractPersistencia implements ITemperaturaJPA {

	/** LOG del sistema */
	private static final Logger LOG = Logger.getLogger(TemperaturaJPA.class);

	/** Criteria builder para generar consultas JPA. */
	private transient CriteriaBuilder builder;

	/** Criteria query para generar consultas JPA. */
	private transient CriteriaQuery<Temperatura> query;

	/** Root para generar consultas JPA. */
	private transient Root<Temperatura> temperaturas;

	/** Lista de parámetros para generar consultas JPA. */
	private transient List<Predicate> listaParametros;

	/**
	 * Inicializa atributos dependientes del entityManager
	 */
	@PostConstruct
	public void init(){
		// Inicializo variables de criteria
		this.builder = this.entityManager.getCriteriaBuilder();
		this.query = this.builder.createQuery(Temperatura.class);
		this.temperaturas = this.query.from(Temperatura.class);
		this.listaParametros = new ArrayList<Predicate>();
	}

	/**
	 * @see com.jhca.ardutemp.persistence.ITemperaturaJPA#getAllClient(com.jhca.ardutemp.persistence.entities.Temperatura)
	 */
	@Override
	public List<Temperatura> allClient(final Temperatura temperatura) {
		LOG.debug("Metodo getAllClient");

		//Adiciono filtros
		this.listaParametros.add(this.builder.equal(this.temperaturas.get(Temperatura_.cliente), temperatura.getCliente() ) );

		this.query.where(this.builder.and(this.listaParametros.toArray(new Predicate[this.listaParametros.size()])));

		//Ejecuto consulta
		final List<Temperatura> lista = this.getEntityManager().createQuery(this.query).getResultList();

		LOG.debug("Registros retornados [" + lista.size() + "]");

		return lista;
	}
	
	/**
	 * @see com.jhca.ardutemp.persistence.ITemperaturaJPA#getMaxMonth()
	 */
	@Override
	public Temperatura maxMonth() {
		LOG.debug("Buscando maxima temperatura");

		final Calendar ahora = Calendar.getInstance();
		final Calendar primero = (Calendar) ahora.clone();
		primero.set(Calendar.DAY_OF_MONTH, 1);

		//Adiciono filtros
		this.listaParametros.add(this.builder.between(this.temperaturas.get(Temperatura_.fecha), new Timestamp( primero.getTimeInMillis() ), new Timestamp( ahora.getTimeInMillis() ) ) );

		this.query.where(this.builder.and(this.listaParametros.toArray(new Predicate[this.listaParametros.size()])));
		this.query.orderBy( this.builder.desc( this.temperaturas.get(Temperatura_.valor) ) );

		//Ejecuto la consulta
		Temperatura temperatura = null;
		try{
			temperatura = this.entityManager.createQuery(this.query).setMaxResults(ConstantesInt._1.getValor()).getSingleResult();

			LOG.debug("TemperaturaMax Id [" + temperatura.getId() + "]");
		} catch ( NoResultException | IllegalArgumentException ex ){
			LOG.debug("No exiten resultados");
		}

		return temperatura;
	}

	/**
	 * @see com.jhca.ardutemp.persistence.ITemperaturaJPA#getMinMonth()
	 */
	@Override
	public Temperatura minMonth() {
		LOG.debug("Buscando minima temperatura");

		final Calendar ahora = Calendar.getInstance();
		final Calendar primero = (Calendar) ahora.clone();
		primero.set(Calendar.DAY_OF_MONTH, 1);

		//Adiciono filtros
		this.listaParametros.add(this.builder.between(this.temperaturas.get(Temperatura_.fecha), new Timestamp( primero.getTimeInMillis() ), new Timestamp( ahora.getTimeInMillis() ) ) );

		this.query.where(this.builder.and(this.listaParametros.toArray(new Predicate[this.listaParametros.size()])));
		this.query.orderBy( this.builder.asc( this.temperaturas.get(Temperatura_.valor) ) );

		//Ejecuto la consulta
		Temperatura temperatura = null;
		try{
			temperatura = this.entityManager.createQuery(this.query).setMaxResults(ConstantesInt._1.getValor()).getSingleResult();

			LOG.debug("TemperaturaMax Id [" + temperatura.getId() + "]");
		} catch ( NoResultException | IllegalArgumentException ex ){
			LOG.debug("No exiten resultados");
		}

		return temperatura;
	}

	/**
	 * @see com.jhca.ardutemp.persistence.ITemperaturaJPA#getTemperaturasMes(com.jhca.ardutemp.persistence.entities.Temperatura)
	 */
	@Override
	public List<Temperatura> temperaturasMes(final Temperatura temperatura) {
		LOG.debug("Buscando temperaturas del mes");

		final Calendar fecha = Calendar.getInstance();
		fecha.setTimeInMillis( temperatura.getFecha().getTime() );

		final StringBuilder sql = new StringBuilder(245);
		sql.append("SELECT id, id_cliente, ")
		.append("STR_TO_DATE( CONCAT(EXTRACT(YEAR FROM fecha), '-', EXTRACT(MONTH FROM fecha), '-', EXTRACT(DAY FROM fecha)),'%Y-%m-%d %h:%i:%s' ) fecha, ")
		.append("AVG(valor) valor ")
		.append("FROM temperatura ")
		.append("WHERE DATE_FORMAT(fecha, '%Y-%m')='").append( UtilBusiness.fechaFormateada(fecha, ConstantesString.FECHA_YYYY_MM.getValor() ) ).append('\'')
		.append("GROUP BY fecha");

		//Ejecuto consulta
		@SuppressWarnings("unchecked")
		final List<Temperatura> lista = this.entityManager.createNativeQuery(sql.toString(), Temperatura.class).getResultList();

		LOG.debug("Registros retornados [" + lista.size() + "]");

		return lista;
	}


	/**
	 * @see com.jhca.ardutemp.persistence.ITemperaturaJPA#allClientInInterval(com.jhca.ardutemp.persistence.entities.Temperatura, com.jhca.ardutemp.persistence.entities.Temperatura)
	 */
	@Override
	public List<Temperatura> allClientInInterval(final Temperatura temperaturaIni, final Temperatura temperaturaFin) {
		LOG.debug("Metodo allClientInInterval");

		//Adiciono filtros
		this.listaParametros.add(this.builder.equal(this.temperaturas.get(Temperatura_.cliente), temperaturaIni.getCliente() ) );
		this.listaParametros.add(this.builder.between(this.temperaturas.get(Temperatura_.fecha), temperaturaIni.getFecha(), temperaturaFin.getFecha() ) );

		this.query.where(this.builder.and(this.listaParametros.toArray(new Predicate[this.listaParametros.size()])));

		//Ejecuto consulta
		final List<Temperatura> lista = this.getEntityManager().createQuery(this.query).getResultList();

		LOG.debug("Registros retornados [" + lista.size() + "]");

		return lista;
	}

}
