package com.jhca.ardutemp.mb;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.jhca.ardutemp.persistence.entities.Usuario;
import com.jhca.ardutemp.persistence.impl.UsuarioJPA;

/**
 * Metodos para extender a los managed bean
 * @author Kmi Quevedo
 */
public abstract class AbstractMbExt {

	/** LOG del sistema */
	private static final Logger LOG = Logger.getLogger(AbstractMbExt.class);

	/** Usuario logeado */
	@EJB
	private transient UsuarioJPA usuarioJPA;

	/**
	 * Verifica que sea una sesion valida
	 */
	protected void verificaSesion() {
		/*HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		if (session==null || session.getAttribute("loggedUser")==null
				|| ( (Usuario) session.getAttribute("loggedUser") ).getId()==null
				|| ( (Usuario) session.getAttribute("loggedUser") ).getId().equals( Integer.valueOf(0) ) ) {

			LOG.error( this.getMensaje("msg_sesion_invalida") );
			try {
				FacesContext.getCurrentInstance().getExternalContext().redirect("./index.jsf");
			} catch (IOException ex) {
				LOG.error(ex);
			}
			FacesContext.getCurrentInstance().responseComplete();
		}*/
		LOG.debug("verificaSesion");
	}

	/**
	 * Cierra la sesion
	 */
	public void logout() {
		try {
			LOG.debug("Cerrando Sesion");

			final HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
			session.setAttribute("loggedUser", null);
			session.invalidate();
			FacesContext.getCurrentInstance().getExternalContext().redirect("./index.jsf");
			FacesContext.getCurrentInstance().responseComplete();
		} catch (final IOException ex) {
			LOG.error(ex);
		}
	}

	/**
	 * Destruye el bean
	 */
	protected void destroyBean() {
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove( this.getClass().getName() );
	}

	/**
	 * Retorna el usuario que esta en el sistema actualmente
	 * @return
	 */
	protected Usuario getLoggedUser(){
		/*HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		return ( (Usuario) session.getAttribute("loggedUser") );*/
		return (Usuario) this.usuarioJPA.findById(Usuario.class, 1);
	}

	/**
	 * Retorna un listado de los meses del año
	 * @return
	 */
	public List<SelectItem> obtenerListaMeses(){
		final List<SelectItem> listaMeses = new ArrayList<SelectItem>();
		int i = -1;
		listaMeses.add( new SelectItem(++i, AbstractMbExt.getMensaje("lbl_enero") ) );
		listaMeses.add( new SelectItem(++i, AbstractMbExt.getMensaje("lbl_febrero") ) );
		listaMeses.add( new SelectItem(++i, AbstractMbExt.getMensaje("lbl_marzo") ) );
		listaMeses.add( new SelectItem(++i, AbstractMbExt.getMensaje("lbl_abril") ) );
		listaMeses.add( new SelectItem(++i, AbstractMbExt.getMensaje("lbl_mayo") ) );
		listaMeses.add( new SelectItem(++i, AbstractMbExt.getMensaje("lbl_junio") ) );
		listaMeses.add( new SelectItem(++i, AbstractMbExt.getMensaje("lbl_julio") ) );
		listaMeses.add( new SelectItem(++i, AbstractMbExt.getMensaje("lbl_agosto") ) );
		listaMeses.add( new SelectItem(++i, AbstractMbExt.getMensaje("lbl_septiembre") ) );
		listaMeses.add( new SelectItem(++i, AbstractMbExt.getMensaje("lbl_octubre") ) );
		listaMeses.add( new SelectItem(++i, AbstractMbExt.getMensaje("lbl_noviembre") ) );
		listaMeses.add( new SelectItem(++i, AbstractMbExt.getMensaje("lbl_diciembre") ) );

		return listaMeses;
	}

	/**
	 * Retorna un listado de los ultimos 10 años
	 * @return
	 */
	public List<SelectItem> obtenerListaYears(){
		final List<SelectItem> listaYears = new ArrayList<SelectItem>();

		final int yearActual = Calendar.getInstance().get(Calendar.YEAR);

		for(int i=yearActual; i>yearActual-10; i--){
			listaYears.add( new SelectItem(i, String.valueOf(i) ) );
		}

		return listaYears;
	}

	/**
	 * Busca un mensaje en el bundle
	 * @param resourceBundleKey
	 * @return
	 */
	public static String getMensaje(final String resourceBundleKey) {
		final FacesContext facesContext = FacesContext.getCurrentInstance();
		final ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "bundle");
		return bundle.getString(resourceBundleKey);
	}

	/**
	 * Despliega un mensaje por pantalla
	 * @param clientId
	 * @param severity
	 * @param titulo
	 * @param mensaje
	 */
	public static void mostrarMensaje(final String clientId, FacesMessage.Severity severity, String titulo, String descripcion){

		if ( severity == null ){
			severity = FacesMessage.SEVERITY_INFO;
		}

		if ( titulo == null ){
			titulo = "";
		}

		if ( descripcion == null ){
			descripcion = "";
		}

		FacesContext.getCurrentInstance().addMessage(clientId, new FacesMessage(severity, titulo,  descripcion) );
	}
}
