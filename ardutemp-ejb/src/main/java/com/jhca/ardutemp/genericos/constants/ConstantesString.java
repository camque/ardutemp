package com.jhca.ardutemp.genericos.constants;

/**
 * Constantes de tipo String
 * @author Kmi Quevedo
 */
public enum ConstantesString {

	/** Host del socket */
	SOCKET_HOST("localhost"),
	/** Mensaje de cierre */
	SOCKET_MENSAJE_CIERRE("CIERRE EL PUTO SOCKET"),
	/** Mensaje de exito de recepcion de los datos */
	SOCKET_MENSAJE_EXITO("SE HAN RECIBIDO LOS DATOS CON EXITO."),


	/** FORMATO_FECHA */
	FECHA_YYYY_MM_DD("yyyy-MM-dd"),
	FECHA_YYYY_MM("yyyy-MM");

	private String valor;

	/**
	 * @param valor
	 */
	private ConstantesString(final String valor) {
		this.valor = valor;
	}

	/**
	 * @return the valor
	 */
	public String getValor() {
		return this.valor;
	}

	/**
	 * @param valor the valor to set
	 */
	public void setValor(final String valor) {
		this.valor = valor;
	}
}
