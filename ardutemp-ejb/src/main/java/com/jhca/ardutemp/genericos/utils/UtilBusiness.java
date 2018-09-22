package com.jhca.ardutemp.genericos.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.log4j.Logger;

import com.jhca.ardutemp.genericos.exception.JhCaException;

/**
 * Clase de utilidades
 * @author Kmi Quevedo
 */
public final class UtilBusiness {

	/** Log del sistema */
	private static final Logger LOG = Logger.getLogger(UtilBusiness.class);

	/**
	 * Constructor
	 */
	private UtilBusiness() {
	}

	/**
	 * Obtiene un Map con los parametros que han sido pasados al comando que se desea ejecutar.
	 * La lista de parametros son parejas param1=valor1;param2=valor2 ...
	 * @param args
	 * @return Map<String, String>
	 * @author kmilo
	 */
	public static Map<String, String> getParams(final String args) {
		final Map<String, String> map = new ConcurrentHashMap<String, String>();
		if (args != null && !args.trim().isEmpty()) {
			final StringTokenizer strTokenizer = new StringTokenizer(args, ";");
			StringTokenizer tkTokenizer;
			String token;
			String parmName;
			String parmValue;
			while ( strTokenizer.hasMoreTokens() ) {
				token = strTokenizer.nextToken();
				tkTokenizer = new StringTokenizer(token, "=");
				if (tkTokenizer.hasMoreTokens()) {
					parmName = tkTokenizer.nextToken();
				}
				else {
					parmName = "";
				}

				if (tkTokenizer.hasMoreTokens()) {
					parmValue = tkTokenizer.nextToken();
				}
				else {
					parmValue = "";
				}

				map.put(parmName, parmValue);
			}
		}
		return map;
	}

	/**
	 * Lee un archivo de properties del sistema
	 * @param archivo
	 * @return
	 */
	public static Properties readProperties(final String archivo){
		InputStream inputStream = null;
		Properties properties = null;
		try {
			properties = new Properties();
			inputStream = new FileInputStream(archivo);
			properties.load(inputStream);
		} catch (final FileNotFoundException ex) {
			LOG.info("No existe el archivo de propiedades.");
			LOG.error("", ex);
		} catch (final IOException ex) {
			LOG.info("Error al leer archivo de propiedades.");
			LOG.error("", ex);
		} finally {
			try {
				inputStream.close();
			} catch (final IOException ex) {
				LOG.info("Error al cerrar el archivo.");
				LOG.error("", ex);
			}
		}

		return properties;
	}

	/**
	 * Busca una propiedad en un properties y retorna el valor asociado
	 * @param properties
	 * @param propiedad
	 * @return
	 * @throws PropertyNotExistException
	 */
	public static String obtenerPropiedad(final Properties properties, final String propiedad) throws JhCaException {
		final String valor = properties.getProperty(propiedad);
		if ( valor == null ){
			throw new JhCaException("Error al obtener propiedad " + propiedad + ".");
		}

		return valor;
	}

	/**
	 * Salva un archivo properties
	 * @param properties
	 * @param ruta
	 */
	public static void saveProperties(final Properties properties, final String fileName) throws FileNotFoundException, IOException {
		final OutputStream outputStream = new FileOutputStream( new File(fileName) );
		properties.store( new BufferedOutputStream( outputStream ), null );
		outputStream.close();
	}

	/**
	 * Da formato a una fecha segun un patron
	 * @param fecha
	 * @param pattern
	 * @return
	 */
	public static String fechaFormateada(final Calendar fecha, final String pattern ){
		final SimpleDateFormat dateFormat = new SimpleDateFormat(pattern, Locale.getDefault() );
		return dateFormat.format( fecha.getTime() );
	}

	/**
	 * Convierte una fecha en string a forma texto
	 * @param string
	 * @return
	 */
	public static Calendar fechaFormateada(final String string, final String separador){
		final String[] tmp = string.split(" ");

		final Calendar fecha = Calendar.getInstance();
		//Fecha
		final String[] token1 = tmp[0].split(separador);
		final String[] token2 = tmp[1].split(":");
		fecha.set( Integer.parseInt(token1[0]), Integer.parseInt(token1[1])-1, Integer.parseInt(token1[2]), Integer.parseInt(token2[0]), Integer.parseInt(token2[1]), Integer.parseInt(token2[2]) );

		return fecha;
	}

	/**
	 * Obtiene el un campo de una lista separada por comas
	 * @param message
	 * @param campo
	 * @return
	 */
	public static String getCampoLista(final String message, final String campo){
		String retorno = "";
		final int posicion = message.indexOf(campo+"=");
		if ( posicion != -1 ){
			try{
				final String cadena = message.substring(posicion);
				final int posI = cadena.indexOf('=');
				int posF = cadena.indexOf(';');
				if ( posF == -1 ){
					posF = cadena.length();
				}
				retorno = cadena.substring(posI+1, posF);
			} catch (final IndexOutOfBoundsException ex){
				LOG.error("Imposible obtener " + campo, ex);
			}
		}
		return retorno;
	}

	/**
	 * Retorna la representacion en String de un objeto
	 * @param object
	 * @return String
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static String toString(final Object object) {
		final Class clase = object.getClass();
		final StringBuilder cadena = new StringBuilder(clase.getName());
		cadena.append('\n');

		final Field fieldlist[] = clase.getDeclaredFields();
		for (final Field field : fieldlist) {
			if (!field.getName().equals("serialVersionUID")) {
				try {
					cadena.append(field.getName()).append(" = ");//.append( field.get(object) ).append( "\n" );

					final String tmp = "get" + field.getName().toString().substring(0, 1).toUpperCase() + field.getName().toString().substring(1);
					final Method setTmpMethod = clase.getMethod(tmp);
					final Object obj = setTmpMethod.invoke(object);
					cadena.append( obj!=null ? obj.toString() : "" ).append('\n');

				} catch (final IllegalArgumentException ex) { LOG.error("IllegalArgumentException");
				} catch (final NoSuchMethodException ex) { LOG.error("NoSuchMethodException");
				} catch (final IllegalAccessException ex) { LOG.error("IllegalAccessException");
				} catch (final InvocationTargetException ex) { LOG.error("InvocationTargetException");
				}
			}
		}

		return cadena.toString();
	}

	/**
	 * Verifica si una fecha es valida en el formato YYYY-MM-DD HH:MM:SS
	 * @param formato
	 * @param fecha
	 * @return
	 */
	public static boolean validarFormatoFechaMySQL(final String fecha){
		final String datePattern = "(?<Year>(?:19|20)\\d{2})\\-(?<Month>0[1-9]|1[0-2])\\-(?<Day>0[1-9]|[1-2]\\d|3[0-1])\\s(?<Hour>[0-1]\\d|2[0-3])\\:(?<Minute>[0-5]\\d)\\:(?<Second>[0-5]\\d)";
		return fecha.matches(datePattern);
	}

	/**
	 * Verifica si una fecha es valida en el formato YYYY/MM/DD HH:MM:SS
	 * @param formato
	 * @param fecha
	 * @return
	 */
	public static boolean validarFormatoFechaOracle(final String fecha){
		final String datePattern = "(?<Year>(?:19|20)\\d{2})\\/(?<Month>0[1-9]|1[0-2])\\/(?<Day>0[1-9]|[1-2]\\d|3[0-1])\\s(?<Hour>[0-1]\\d|2[0-3])\\:(?<Minute>[0-5]\\d)\\:(?<Second>[0-5]\\d)";
		return fecha.matches(datePattern);
	}

	/**
	 * Convierte un calendar en una fecha en formato long, son los segundo que han pasado
	 * @param date
	 * @return
	 */
	public static Long calendarToLong(final Calendar date){
		return date.getTimeInMillis() / 1000;
	}

	/**
	 * Al enviar los segundos los convierte en un calendar
	 * @param numero
	 * @return
	 */
	public static Calendar longToCalendar(final Long numero){
		final Calendar fecha = Calendar.getInstance();
		fecha.setTime( new Date(numero * 1000) );
		return fecha;
	}

	/**
	 * Retorna el siguiente cuarto de hora al cual pertenece una fecha
	 * @param fecha formata MySQL
	 * @return
	 */
	public static Calendar fechaCuartos(final String fecha){
		final Calendar date = fechaFormateada(fecha, "-");
		date.set(Calendar.SECOND, 0);
		date.set(Calendar.MILLISECOND, 0);

		boolean sumarHora = false;

		int minuto = date.get(Calendar.MINUTE);
		if ( minuto>0 && minuto<=15 ){
			minuto = 15;
		}
		else{
			if ( minuto>15 && minuto<=30 ){
				minuto = 30;
			}
			else{
				if ( minuto>30 && minuto<=45 ){
					minuto = 45;
				}
				else{
					if ( minuto>45 || minuto==0 ){
						sumarHora = true;
						minuto = 0;
					}
				}
			}
		}

		date.set(Calendar.MINUTE, minuto);
		if ( sumarHora ){
			date.add(Calendar.HOUR_OF_DAY, 1);
		}

		return date;
	}

	/**
	 * Retorna el siguiente cuarto de hora al cual pertenece una fecha
	 * @param fecha formata MySQL
	 * @return
	 */
	public static Calendar fechaCuartos(final Calendar date){
		date.set(Calendar.SECOND, 0);
		date.set(Calendar.MILLISECOND, 0);

		boolean sumarHora = false;

		int minuto = date.get(Calendar.MINUTE);
		if ( minuto!=0 ){
			if ( minuto>0 && minuto<=15 ){
				minuto = 15;
			}
			else{
				if ( minuto>15 && minuto<=30 ){
					minuto = 30;
				}
				else{
					if ( minuto>30 && minuto<=45 ){
						minuto = 45;
					}
					else{
						if ( minuto>45 ){
							sumarHora = true;
							minuto = 0;
						}
					}
				}
			}
		}

		date.set(Calendar.MINUTE, minuto);
		if ( sumarHora ){
			date.add(Calendar.HOUR_OF_DAY, 1);
		}

		return date;
	}

	/**
	 * Convierte una cadena separa por | (pipe) en un lista
	 * @param cadena
	 * @return
	 */
	public static List<String> separatedListToList(final String cadena){
		final List<String> list = new ArrayList<String>();

		final StringTokenizer tokens = new StringTokenizer( cadena, "|" );
		while( tokens.hasMoreTokens() ){
			list.add( tokens.nextToken().trim() );
		}

		return list;
	}

	/**
	 * Elimina los archivos contenidos en un directorio
	 * @param ruta
	 */
	public static void deleteContents(final String ruta) {
		final File dir = new File( ruta );
		if ( dir.exists() ) {
			final File[] ficheros = dir.listFiles();
			for (final File file : ficheros) {
				if ( !file.getName().equals(".") && !file.getName().equals("..") ){
					final File dirInt = new File(ruta + "/" + file.getName() );
					if ( dirInt.isDirectory() ){
						try {
							UtilBusiness.deleteContentsRecursive( dirInt );
						} catch (final IOException ex) {
							LOG.info(ex.getMessage());
							LOG.error("", ex);
						}
					}
					dirInt.delete();
				}
			}
		}
	}

	/**
	 * Borra recursivamente recursos de un fichero
	 * @param file
	 * @throws IOException
	 */
	private static void deleteContentsRecursive(final File file) throws IOException {
		final File[] files = file.listFiles();
		for (final File child : files) {
			if (child.isDirectory()) {
				deleteContentsRecursive(child);
			}
			if (!child.delete()) {
				throw new IOException("Unable to delete " + child.getPath());
			}
		}
	}

	/**
	 * Cambia una fecha que estaba en formato 2012/4/5 a 2012/04/05
	 * @param fecha
	 * @return string
	 */
	public static String fechaMesDia2Digitos(String fecha){
		for (int i=1; i<=9; i++){
			fecha = fecha.replace("/"+i+"/", "/0"+i+"/").replace("/"+i+" ", "/0"+i+" ");
		}

		return fecha;
	}

	/**
	 * Convierte un java.util.date a XMLGregorianCalendar
	 * @param date
	 * @return
	 */
	public static XMLGregorianCalendar dateToXMLGregorianCalendar(final Date date){
		XMLGregorianCalendar gregorianCalendar = null;
		try {
			final GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(date);
			gregorianCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar( gc );
		} catch (final DatatypeConfigurationException ex) {
			LOG.error("Error al convertir fecha", ex);
		}

		return gregorianCalendar;
	}

	/**
	 * Convierte un XMLGregorianCalendar a java.util.date
	 * @param xMLGregorianCalendar
	 * @return
	 */
	public static Date xmlGregorianCalendarToDate(final XMLGregorianCalendar xMLGregorianCalendar){
		return xMLGregorianCalendar.toGregorianCalendar().getTime();
	}

	/**
	 * Quita caracteres no permitidos en una cadena de texto
	 * @param str
	 * @return
	 */
	public static String removeSpecialCharacters(final String str) {
		final StringBuilder builder = new StringBuilder();

		for (int i=0; i<str.length(); i++){
			final String c = String.valueOf( str.charAt(i) );
			if ( c.matches("[0-9a-zA-Z:._=,;\\-\\s|]") ){
				builder.append(c);
			}
		}
		return builder.toString();
	}
}