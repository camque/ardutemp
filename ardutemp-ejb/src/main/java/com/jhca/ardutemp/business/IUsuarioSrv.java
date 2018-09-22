package com.jhca.ardutemp.business;

import javax.ejb.Local;

import com.jhca.ardutemp.genericos.to.SecurityTO;
import com.jhca.ardutemp.persistence.entities.Usuario;

/**
 * Interfaz del servicio de usuarios
 * @author Kmi Quevedo
 */
@Local
public interface IUsuarioSrv {

	/**
	 * Verifica las credenciales de usuario
	 * @param accesoTO
	 * @return
	 */
	boolean loginUsuario(SecurityTO accesoTO);

	/**
	 * Retorna el usuario
	 * @return
	 */
	Usuario getUsuario();

}
