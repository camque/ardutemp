package com.jhca.ardutemp.mb;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.apache.log4j.Logger;

import com.jhca.ardutemp.genericos.constants.ConstantesInt;
import com.jhca.ardutemp.genericos.constants.ConstantesString;
import com.jhca.ardutemp.sock.impl.Sock;


/**
 *
 * @author kmilo
 */
@ManagedBean(name = "controlSocket")
@ViewScoped
public class ControlSocket extends AbstractMbExt {

	/** Log del sistema */
	private static final Logger LOG = Logger.getLogger(ControlSocket.class);
	/** Socket */
	@EJB
	private transient Sock sock;

	/**
	 * Constructor
	 */
	public ControlSocket() {
		super();
		this.verificaSesion();
	}

	/**
	 * Abre el socket
	 */
	public void abrir() {
		this.sock.openSocket();
	}

	/**
	 * Cierra el socket
	 */
	public void cerrar() {
		try {
			final Socket socket = new Socket(ConstantesString.SOCKET_HOST.getValor(), ConstantesInt.SOCKET_PORT.getValor() );
			final BufferedWriter wr = new BufferedWriter( new OutputStreamWriter( socket.getOutputStream() ) );
			wr.write( ConstantesString.SOCKET_MENSAJE_CIERRE.getValor() );
			wr.flush();
			socket.close();

		} catch (final UnknownHostException ex) {
			LOG.error("UnknownHostException ", ex);
		} catch (final IOException ex) {
			LOG.error("IOException ", ex);
		}
	}

}
