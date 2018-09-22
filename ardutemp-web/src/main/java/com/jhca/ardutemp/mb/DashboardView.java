package com.jhca.ardutemp.mb;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.DashboardColumn;
import org.primefaces.model.DashboardModel;
import org.primefaces.model.DefaultDashboardColumn;
import org.primefaces.model.DefaultDashboardModel;

import com.jhca.ardutemp.business.ITemperaturaSrv;
import com.jhca.ardutemp.persistence.entities.Temperatura;


/**
 *
 * @author kmilo
 */
@ManagedBean(name = "dashboard")
@ViewScoped
public class DashboardView extends AbstractMbExt {

	/** Modelo del dashboard */
	private DashboardModel dashboardModel;
	/** Servicio de temperaturas */
	@EJB
	private ITemperaturaSrv temperaturaSrv;
	/** Temperatura maximo */
	private Temperatura temperaturaMax;
	/** Temperatura minima */
	private Temperatura temperaturaMin;

	/**
	 * Constructor
	 */
	public DashboardView() {
		super();
		this.verificaSesion();
	}

	/**
	 * Inicializacion del dash
	 */
	@PostConstruct
	public void init() {

		//Cargando el dashboard

		this.dashboardModel = new DefaultDashboardModel();
		final DashboardColumn column1 = new DefaultDashboardColumn();
		final DashboardColumn column2 = new DefaultDashboardColumn();
		final DashboardColumn column3 = new DefaultDashboardColumn();

		column1.addWidget("dash11");
		column1.addWidget("dash21");

		column2.addWidget("dash12");
		column2.addWidget("dash22");

		column3.addWidget("dash13");

		this.dashboardModel.addColumn(column1);
		this.dashboardModel.addColumn(column2);
		this.dashboardModel.addColumn(column3);

		//Se buscan las temperatura máxima y mínima
		this.temperaturaMax = this.temperaturaSrv.maxMonth();
		this.temperaturaMin = this.temperaturaSrv.minMonth();
	}


	/**
	 * @return the dashboardModel
	 */
	public DashboardModel getDashboardModel() {
		return this.dashboardModel;
	}


	/**
	 * @param dashboardModel the dashboardModel to set
	 */
	public void setDashboardModel(final DashboardModel dashboardModel) {
		this.dashboardModel = dashboardModel;
	}


	/**
	 * @return the temperaturaSrv
	 */
	public ITemperaturaSrv getTemperaturaSrv() {
		return this.temperaturaSrv;
	}


	/**
	 * @param temperaturaSrv the temperaturaSrv to set
	 */
	public void setTemperaturaSrv(final ITemperaturaSrv temperaturaSrv) {
		this.temperaturaSrv = temperaturaSrv;
	}


	/**
	 * @return the temperaturaMax
	 */
	public Temperatura getTemperaturaMax() {
		return this.temperaturaMax;
	}


	/**
	 * @param temperaturaMax the temperaturaMax to set
	 */
	public void setTemperaturaMax(final Temperatura temperaturaMax) {
		this.temperaturaMax = temperaturaMax;
	}


	/**
	 * @return the temperaturaMin
	 */
	public Temperatura getTemperaturaMin() {
		return this.temperaturaMin;
	}


	/**
	 * @param temperaturaMin the temperaturaMin to set
	 */
	public void setTemperaturaMin(final Temperatura temperaturaMin) {
		this.temperaturaMin = temperaturaMin;
	}

}
