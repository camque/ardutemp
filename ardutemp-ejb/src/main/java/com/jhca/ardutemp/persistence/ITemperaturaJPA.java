package com.jhca.ardutemp.persistence;

import java.util.List;

import javax.ejb.Local;

import com.jhca.ardutemp.persistence.entities.Temperatura;

/**
 * Interfaz del servicio de acceso a datos de Temperaturas
 * @author Kmi Quevedo
 */
@Local
public interface ITemperaturaJPA extends IPersistencia {

	/**
	 * Obtiene todas las temperaturas de un cliente
	 * @param temperatura
	 * @return
	 */
	List<Temperatura> allClient(Temperatura temperatura);

	/**
	 * Obtiene las temperaturas de un cliente en un intervalo de fechas
	 * @param temperaturaIni
	 * @param temperaturaFin
	 * @return
	 */
	List<Temperatura> allClientInInterval(Temperatura temperaturaIni, Temperatura temperaturaFin);

	/**
	 * Obtiene la temperatura maxima del mes actual
	 * @return
	 */
	Temperatura maxMonth();

	/**
	 * Obtiene la temperatura minima del mes actual
	 * @return
	 */
	Temperatura minMonth();

	/**
	 * Retorna el listado de temperaturas del mes
	 * @param temperatura
	 * @return
	 */
	List<Temperatura> temperaturasMes(Temperatura temperatura);

}
