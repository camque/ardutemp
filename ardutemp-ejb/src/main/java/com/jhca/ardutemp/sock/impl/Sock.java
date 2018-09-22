package com.jhca.ardutemp.sock.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import org.apache.log4j.Logger;

import com.jhca.ardutemp.business.ITemperaturaSrv;
import com.jhca.ardutemp.genericos.constants.ConstantesInt;
import com.jhca.ardutemp.genericos.constants.ConstantesString;
import com.jhca.ardutemp.genericos.utils.UtilBusiness;
import com.jhca.ardutemp.persistence.entities.Temperatura;
import com.jhca.ardutemp.sock.ISock;

/**
 * Session Bean implementation class Sock
 */
@Singleton
@LocalBean
@TransactionManagement(TransactionManagementType.CONTAINER)
public class Sock implements ISock {

	/** LOG del sistema */
	private static final Logger LOG = Logger.getLogger(Sock.class);
	/** Servidor */
	private transient ServerSocket server; //Servidor
	/** Socket */
	private transient Socket socket; //Socket
	/** Flag de estado del socket */
	private transient boolean activo;
	/** Flujo de salida del socket */
	private transient BufferedWriter salida;
	/** Flujo de entrada del socket */
	private transient BufferedReader entrada;

	/** Servicio de temperaturas */
	@EJB
	private transient ITemperaturaSrv temperaturaSrv;

	/**
	 * Constructor
	 */
	public Sock() {
		this.activo = true;
	}

	/**
	 * @see com.jhca.ardutemp.sock.ISock#openSocket()
	 */
	@Asynchronous
	@Override
	@TransactionAttribute(TransactionAttributeType.NEVER)
	public void openSocket() {
		LOG.debug("Metodo openSocket");
		this.activo = true;
		try{
			this.server = new ServerSocket( ConstantesInt.SOCKET_PORT.getValor() );
			while ( this.activo ) {
				this.waitConnection();
				this.getFlow();
				this.sendData(ConstantesString.SOCKET_MENSAJE_EXITO.getValor());
				this.closeConnection();
			}
		} catch (final IOException ex){
			LOG.error("IO1 ", ex);
		} finally {
			try {
				this.closeSocket();
			} catch (final IOException ex) {
				LOG.error("IO2 ", ex);
			}
		}
	}

	/**
	 * @see com.jhca.ardutemp.sock.ISock#closeSocket()
	 */
	@Asynchronous
	@Override
	@TransactionAttribute(TransactionAttributeType.NEVER)
	public void closeSocket() throws IOException {
		LOG.debug("Metodo closeSocket");
		if ( this.server != null ){
			this.server.close();
		}
		this.activo = false;
	}

	/**
	 * Espera la conexion al socket
	 * @throws IOException
	 */
	@TransactionAttribute(TransactionAttributeType.NEVER)
	private void waitConnection() throws IOException {
		LOG.debug("Metodo waitConnection");
		this.socket = this.server.accept(); // Permitir al servidor aceptar la conexión
	}

	/**
	 * Recibe el flujo que envia el cliente
	 * @author Kmi Quevedo
	 * @throws IOException
	 */
	@TransactionAttribute(TransactionAttributeType.NEVER)
	private void getFlow() throws IOException {
		LOG.debug("Metodo getFlow");
		// establecer flujo de salida para los objetos
		this.entrada = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
		String linea;
		while ( (linea=this.entrada.readLine())!=null && this.activo ) {
			if ( linea.equals( ConstantesString.SOCKET_MENSAJE_CIERRE.getValor() ) ){
				this.activo = false;
			}
			else{
				this.salvarLinea( linea );
			}
		}
	}

	/**
	 * Guarda un nuevo registro
	 * @param linea
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void salvarLinea(final String linea) {
		LOG.debug("Metodo salvarLinea");

		final List<String> lista = UtilBusiness.separatedListToList(linea);

		final Calendar fecha = UtilBusiness.fechaFormateada(lista.get(0), "-");

		final Temperatura temperatura = new Temperatura( Integer.parseInt( lista.get(1) ) );
		temperatura.setValor( Double.parseDouble( lista.get(2) ) );
		temperatura.setFecha( new Timestamp( fecha.getTimeInMillis() ) );

		this.temperaturaSrv.create(temperatura);
	}

	/**
	 * Le envia respuesta al cliente
	 * @author Kmi Quevedo
	 * @param mensaje
	 * @throws IOException
	 */
	@TransactionAttribute(TransactionAttributeType.NEVER)
	private void sendData(final String mensaje) throws IOException {
		LOG.debug("Metodo sendData");
		// Enviar objeto al cliente
		this.salida = new BufferedWriter( new OutputStreamWriter( this.socket.getOutputStream() ) );
		this.salida.write(mensaje);
		this.salida.flush();
	}

	/**
	 * Cierra las conexiones abiertas
	 * @author Kmi Quevedo
	 * @throws IOException
	 */
	@TransactionAttribute(TransactionAttributeType.NEVER)
	private void closeConnection() throws IOException{
		LOG.debug("Metodo closeConnection");
		if ( this.salida != null ){
			this.salida.close();
		}
		if ( this.entrada != null ){
			this.entrada.close();
		}
		if ( this.socket != null ){
			this.socket.close();
		}
	}
}
