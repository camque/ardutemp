package com.jhca.ardutemp.business.impl;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import org.apache.log4j.Logger;

import com.jhca.ardutemp.business.ITemperaturaSrv;
import com.jhca.ardutemp.persistence.ITemperaturaJPA;
import com.jhca.ardutemp.persistence.entities.Temperatura;

/**
 * Session Bean implementation class TemperaturaSrv
 */
@Stateless
@LocalBean
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TemperaturaSrv implements ITemperaturaSrv {

	/** LOG del sistema */
	private static final Logger LOG = Logger.getLogger(TemperaturaSrv.class);

	/** Acceso a datos de temperatura */
	@EJB
	private transient ITemperaturaJPA temperaturaJPA;

	/**
	 * @see com.jhca.ardutemp.business.ITemperaturaSrv#getAll()
	 */
	@SuppressWarnings("unchecked")
	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<Temperatura> getAll() {
		LOG.debug("getAll");
		return this.temperaturaJPA.findAll(Temperatura.class);
	}

	/**
	 * @see com.jhca.ardutemp.business.ITemperaturaSrv#getAllClient(com.jhca.ardutemp.persistence.entities.Temperatura)
	 */
	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<Temperatura> allClient(final Temperatura temperatura) {
		LOG.debug("getAllClient");
		return this.temperaturaJPA.allClient(temperatura);
	}

	/**
	 * @see com.jhca.ardutemp.business.ITemperaturaSrv#create(java.lang.Object)
	 */
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void create(final Object obj) {
		LOG.debug("create");
		this.temperaturaJPA.create(obj);
	}


	/**
	 * @see com.jhca.ardutemp.business.ITemperaturaSrv#getMaxMonth()
	 */
	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Temperatura maxMonth() {
		LOG.debug("getMaxMonth");
		return this.temperaturaJPA.maxMonth();
	}

	/**
	 * @see com.jhca.ardutemp.business.ITemperaturaSrv#getMinMonth()
	 */
	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Temperatura minMonth() {
		LOG.debug("getMaxMonth");
		return this.temperaturaJPA.minMonth();
	}

	/**
	 * @see com.jhca.ardutemp.business.ITemperaturaSrv#temperaturasMes(com.jhca.ardutemp.persistence.entities.Temperatura)
	 */
	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<Temperatura> temperaturasMes(final Temperatura temperatura) {
		LOG.debug("getTemperaturasMes");
		return this.temperaturaJPA.temperaturasMes(temperatura);
	}


	/**
	 * @see com.jhca.ardutemp.business.ITemperaturaSrv#allClientInInterval(com.jhca.ardutemp.persistence.entities.Temperatura, com.jhca.ardutemp.persistence.entities.Temperatura)
	 */
	@Override
	public List<Temperatura> allClientInInterval(final Temperatura temperaturaIni, final Temperatura temperaturaFin) {
		LOG.debug("allClientInInterval");
		return this.temperaturaJPA.allClientInInterval(temperaturaIni, temperaturaFin);
	}

}
