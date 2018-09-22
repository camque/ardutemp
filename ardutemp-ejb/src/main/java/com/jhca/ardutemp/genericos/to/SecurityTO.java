package com.jhca.ardutemp.genericos.to;

/**
 * Transfer Object de datos de seguridad
 * @author arleyquevedo
 */
public class SecurityTO {

	/** Nombre de usuario */
	private String usuario;
	/** Password */
	private String clave;

	/**
	 * @return the usuario
	 */
	public String getUsuario() {
		return this.usuario;
	}

	/**
	 * @param usuario the usuario to set
	 */
	public void setUsuario(final String usuario) {
		this.usuario = usuario;
	}

	/**
	 * @return the clave
	 */
	public String getClave() {
		return this.clave;
	}

	/**
	 * @param clave the clave to set
	 */
	public void setClave(final String clave) {
		this.clave = clave;
	}

}
