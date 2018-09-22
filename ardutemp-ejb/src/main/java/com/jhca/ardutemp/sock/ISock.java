package com.jhca.ardutemp.sock;

import java.io.IOException;

import javax.ejb.Local;

/**
 * @author Kmi Quevedo
 */
@Local
public interface ISock {

	/**
	 * Abre un socket en un puerto preconfigurado
	 * @author Kmi Quevedo
	 */
	void openSocket();

	/**
	 * Cierra un socket previamente abierto
	 * @author Kmi Quevedo
	 * @throws IOException
	 */
	void closeSocket() throws IOException;

}
