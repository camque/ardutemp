package com.jhca.ardutemp.mb;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.apache.log4j.Logger;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartModel;

import com.jhca.ardutemp.business.ITemperaturaSrv;
import com.jhca.ardutemp.genericos.constants.ConstantesInt;
import com.jhca.ardutemp.genericos.constants.ConstantesString;
import com.jhca.ardutemp.genericos.utils.UtilBusiness;
import com.jhca.ardutemp.persistence.entities.Temperatura;


/**
 *
 * @author kmilo
 */
@ManagedBean(name = "comparaTemp")
@ViewScoped
public class ComparaTemp extends AbstractMbExt {

	/** LOG del sistema */
	private static final Logger LOG = Logger.getLogger(ComparaTemp.class);
	/** Servicio de temperaturas */
	@EJB
	private ITemperaturaSrv temperaturaSrv;
	/** */
	private LineChartModel temperaturaGrafica;
	/** Flag para mostrar o no grafico */
	private boolean mostrarGrafica;
	/** Año serie 1 */
	private int year1;
	/** Mes serie 1 */
	private int mes1;
	/** Año serie 2 */
	private int year2;
	/** Mes serie 2 */
	private int mes2;

	/**
	 * Constructor
	 */
	public ComparaTemp() {
		super();
		this.verificaSesion();

		final Calendar mesActual = Calendar.getInstance();
		final Calendar mesAnterior = (Calendar) mesActual.clone();
		mesAnterior.add(Calendar.MONTH, -ConstantesInt._1.getValor() );

		this.year1 = mesActual.get(Calendar.YEAR);
		this.mes1 = mesActual.get(Calendar.MONTH);

		this.year2 = mesAnterior.get(Calendar.YEAR);
		this.mes2 = mesAnterior.get(Calendar.MONTH);

		this.temperaturaGrafica = new LineChartModel();
		this.mostrarGrafica = false;
	}

	/**
	 * Busca las temperaturas segun las series seleccionadas para
	 * graficar el versus
	 */
	public void consultar() {
		LOG.info("Cargando temperaturas para graficar");
		final Calendar mesSerie1 = Calendar.getInstance();
		final Calendar mesSerie2 = (Calendar) mesSerie1.clone();

		mesSerie1.set(Calendar.YEAR, this.year1);
		mesSerie1.set(Calendar.MONTH, this.mes1);

		mesSerie2.set(Calendar.YEAR, this.year2);
		mesSerie2.set(Calendar.MONTH, this.mes2);

		final Temperatura temp1 = new Temperatura();
		temp1.setFecha( new Timestamp( mesSerie1.getTimeInMillis() ) );
		final List<Temperatura> temperaturasSerie1 = this.temperaturaSrv.temperaturasMes(temp1);

		final Temperatura temp2 = new Temperatura();
		temp2.setFecha( new Timestamp( mesSerie2.getTimeInMillis() ) );
		final List<Temperatura> temperaturasSerie2 = this.temperaturaSrv.temperaturasMes(temp2);


		if ( temperaturasSerie1!=null && !temperaturasSerie1.isEmpty() && temperaturasSerie2!=null && !temperaturasSerie2.isEmpty() ){
			final Calendar tmp = Calendar.getInstance();
			//Cargamos la data de la primera serie
			final ChartSeries series1 = new ChartSeries();
			series1.setLabel( UtilBusiness.fechaFormateada(mesSerie1, ConstantesString.FECHA_YYYY_MM.getValor() ) );

			int ultimaFecha1 = 0;
			for (final Temperatura temperatura : temperaturasSerie1) {
				tmp.setTimeInMillis( temperatura.getFecha().getTime() );
				ultimaFecha1 = tmp.get(Calendar.DAY_OF_MONTH);
				series1.set(String.valueOf(ultimaFecha1), temperatura.getValor());
			}

			//Cargamos la data de la segunda serie
			final ChartSeries series2 = new ChartSeries();
			series2.setLabel( UtilBusiness.fechaFormateada(mesSerie2, ConstantesString.FECHA_YYYY_MM.getValor() ) );

			int ultimaFecha2 = 0;
			for (final Temperatura temperatura : temperaturasSerie2) {
				tmp.setTimeInMillis( temperatura.getFecha().getTime() );
				ultimaFecha2 = tmp.get(Calendar.DAY_OF_MONTH);
				series2.set(String.valueOf(ultimaFecha2), temperatura.getValor());
			}

			this.mostrarGrafica = true;
			this.temperaturaGrafica = new LineChartModel();
			this.temperaturaGrafica.addSeries(series1);
			this.temperaturaGrafica.addSeries(series2);
			this.temperaturaGrafica.setTitle( AbstractMbExt.getMensaje("lbl_promedio_temperaturas_mensuales") );
			this.temperaturaGrafica.setLegendPosition("ne");
			this.temperaturaGrafica.setShowPointLabels(true);
			this.temperaturaGrafica.setDatatipFormat("%d, %d");

			final Axis xAxis = this.temperaturaGrafica.getAxis(AxisType.X);
			xAxis.setLabel( AbstractMbExt.getMensaje("lbl_dia") );
			xAxis.setMin( ConstantesInt._0.getValor() );
			xAxis.setMax( ultimaFecha1 > ultimaFecha2 ? ultimaFecha1 + ConstantesInt._1.getValor() : ultimaFecha2 + ConstantesInt._1.getValor() );
			xAxis.setTickFormat("%d");
			xAxis.setTickInterval("1");

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
	 * @return the year1
	 */
	public int getYear1() {
		return this.year1;
	}

	/**
	 * @param year1 the year1 to set
	 */
	public void setYear1(final int year1) {
		this.year1 = year1;
	}

	/**
	 * @return the mes1
	 */
	public int getMes1() {
		return this.mes1;
	}

	/**
	 * @param mes1 the mes1 to set
	 */
	public void setMes1(final int mes1) {
		this.mes1 = mes1;
	}

	/**
	 * @return the year2
	 */
	public int getYear2() {
		return this.year2;
	}

	/**
	 * @param year2 the year2 to set
	 */
	public void setYear2(final int year2) {
		this.year2 = year2;
	}

	/**
	 * @return the mes2
	 */
	public int getMes2() {
		return this.mes2;
	}

	/**
	 * @param mes2 the mes2 to set
	 */
	public void setMes2(final int mes2) {
		this.mes2 = mes2;
	}


}
