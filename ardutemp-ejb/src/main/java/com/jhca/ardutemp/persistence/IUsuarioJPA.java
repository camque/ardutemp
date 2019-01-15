package com.jhca.ardutemp.persistence;

import javax.ejb.Local;

import com.jhca.ardutemp.genericos.to.SecurityTO;
import com.jhca.ardutemp.persistence.entities.Usuario;

/**
 * Interfaz del servicio de acceso a datos de Temperaturas
 * @author Kmi Quevedo
 */
@Local
public interface IUsuarioJPA {

	/**
	 * Verifica las credenciales de usuario
	 * @param accesoTO
	 * @return
	 */
	Usuario loginUsuario(SecurityTO accesoTO);

}
