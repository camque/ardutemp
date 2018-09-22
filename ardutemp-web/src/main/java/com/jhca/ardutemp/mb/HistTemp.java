package com.jhca.ardutemp.mb;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.apache.log4j.Logger;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.DateAxis;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;

import com.jhca.ardutemp.business.ITemperaturaSrv;
import com.jhca.ardutemp.genericos.constants.ConstantesInt;
import com.jhca.ardutemp.genericos.constants.ConstantesString;
import com.jhca.ardutemp.genericos.utils.UtilBusiness;
import com.jhca.ardutemp.persistence.entities.Temperatura;


/**
 *
 * @author kmilo
 */
@ManagedBean(name = "histTemp")
@ViewScoped
public class HistTemp extends AbstractMbExt {

	/** Log del sistema */
	private static final Logger LOG = Logger.getLogger(HistTemp.class);
	/** Servicio de temperatura */
	@EJB
	private transient ITemperaturaSrv temperaturaSrv;
	/** Listado de temperaturas */
	private List<Temperatura> temperaturas;
	/** Modelo de la grafica */
	private LineChartModel temperaturaGrafica;
	/** Flag para mostrar o no la grafica */
	private boolean mostrarGrafica;
	/** Limite inicial de la grafica */
	private Date fechaIni;
	/** Limite final de la grafica */
	private Date fechaFin;

	/**
	 * Constructor
	 */
	public HistTemp() {
		super();
		this.verificaSesion();

		this.fechaIni = Calendar.getInstance().getTime();
		this.fechaFin = Calendar.getInstance().getTime();

		this.temperaturaGrafica = new LineChartModel();
		this.mostrarGrafica = false;
	}

	/**
	 * Verifica las temperaturas del usuario
	 */
	@SuppressWarnings("deprecation")
	public void consultar() {
		LOG.info("Cargando temperaturas para graficar");

		final Temperatura temperaturaIni = new Temperatura( this.getLoggedUser().getCliente().getId() );
		this.fechaIni.setHours(0);
		this.fechaIni.setMinutes(0);
		this.fechaIni.setSeconds(1);
		temperaturaIni.setFecha( new Timestamp( this.fechaIni.getTime() ) );
		final Temperatura temperaturaFin = new Temperatura( this.getLoggedUser().getCliente().getId() );
		this.fechaFin.setHours(23);
		this.fechaFin.setMinutes(59);
		this.fechaFin.setSeconds(59);
		temperaturaFin.setFecha( new Timestamp( this.fechaFin.getTime() ) );

		this.temperaturas = this.temperaturaSrv.allClientInInterval(temperaturaIni, temperaturaFin);

		if ( this.temperaturas!=null && !this.temperaturas.isEmpty() ){
			final LineChartSeries series = new LineChartSeries();
			series.setLabel( AbstractMbExt.getMensaje("lbl_temperatura") );
			//series.setFill(true);
			series.setShowLine(true);
			series.setShowMarker(true);

			long ultimaFecha = 0;
			for (final Temperatura temperatura : this.temperaturas) {
				ultimaFecha = temperatura.getFecha().getTime();
				series.set(ultimaFecha, temperatura.getValor());
			}

			this.mostrarGrafica = true;
			this.temperaturaGrafica = new LineChartModel();
			this.temperaturaGrafica.addSeries(series);
			this.temperaturaGrafica.setTitle( AbstractMbExt.getMensaje("lbl_historico_temperaturas") );
			this.temperaturaGrafica.setShowPointLabels(true);

			final Calendar limiteMaxX = Calendar.getInstance();
			limiteMaxX.setTimeInMillis(ultimaFecha);
			limiteMaxX.add(Calendar.DATE, 1);

			final DateAxis xAxis = new DateAxis();
			xAxis.setLabel( AbstractMbExt.getMensaje("lbl_fechas") );
			xAxis.setMax( UtilBusiness.fechaFormateada(limiteMaxX, ConstantesString.FECHA_YYYY_MM_DD.getValor() ) );
			xAxis.setTickFormat("%Y-%m-%#d %H:%M");
			xAxis.setTickAngle(-45);
			this.temperaturaGrafica.getAxes().put(AxisType.X, xAxis);

			final Axis yAxis = this.temperaturaGrafica.getAxis(AxisType.Y);
			yAxis.setLabel( AbstractMbExt.getMensaje("lbl_temperatura") );
			yAxis.setMin( ConstantesInt._0.getValor() );
			yAxis.setTickFormat("%d");
			yAxis.setTickInterval("2");
		}
		else{
			this.mostrarGrafica = false;
			AbstractMbExt.mostrarMensaje(null, FacesMessage.SEVERITY_ERROR, AbstractMbExt.getMensaje("msg_datos_vacio_tit"), AbstractMbExt.getMensaje("msg_datos_vacio_des"));
		}
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
	 * @return the temperaturas
	 */
	public List<Temperatura> getTemperaturas() {
		return this.temperaturas;
	}

	/**
	 * @param temperaturas the temperaturas to set
	 */
	public void setTemperaturas(final List<Temperatura> temperaturas) {
		this.temperaturas = temperaturas;
	}

	/**
	 * @return the temperaturaGrafica
	 */
	public LineChartModel getTemperaturaGrafica() {
		return this.temperaturaGrafica;
	}

	/**
	 * @param temperaturaGrafica the temperaturaGrafica to set
	 */
	public void setTemperaturaGrafica(final LineChartModel temperaturaGrafica) {
		this.temperaturaGrafica = temperaturaGrafica;
	}

	/**
	 * @return the mostrarGrafica
	 */
	public boolean isMostrarGrafica() {
		return this.mostrarGrafica;
	}

	/**
	 * @param mostrarGrafica the mostrarGrafica to set
	 */
	public void setMostrarGrafica(final boolean mostrarGrafica) {
		this.mostrarGrafica = mostrarGrafica;
	}

	/**
	 * @return the fechaIni
	 */
	public Date getFechaIni() {
		return this.fechaIni;
	}

	/**
	 * @param fechaIni the fechaIni to set
	 */
	public void setFechaIni(final Date fechaIni) {
		this.fechaIni = fechaIni;
	}

	/**
	 * @return the fechaFin
	 */
	public Date getFechaFin() {
		return this.fechaFin;
	}

	/**
	 * @param fechaFin the fechaFin to set
	 */
	public void setFechaFin(final Date fechaFin) {
		this.fechaFin = fechaFin;
	}

}
