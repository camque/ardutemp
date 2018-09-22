package com.jhca.ardutemp.mb;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.jhca.ardutemp.business.IUsuarioSrv;
import com.jhca.ardutemp.genericos.to.SecurityTO;


/**
 *
 * @author kmilo
 */
@ManagedBean(name = "security")
@ViewScoped
public class Security extends AbstractMbExt {

	/** Log del sistema */
	private static final Logger LOG = Logger.getLogger(Security.class);
	/** Usuario logeado */
	@EJB
	private transient IUsuarioSrv usuarioSrv;
	/** Transfer objeto con datos de usuario logeado */
	private SecurityTO accesoTO;

	/**
	 * Constructor
	 */
	public Security() {
		super();
		this.accesoTO = new SecurityTO();
	}

	/**
	 * Vacia los objetos
	 */
	public void limpiar(){
		this.accesoTO = new SecurityTO();
	}

	/**
	 * Verifica credenciales de usuario
	 * @return
	 */
	public String acceder(){
		LOG.debug( "VERIFICANDO CREDENCIALES" );

		String accion = null;

		final HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);

		if ( this.usuarioSrv.loginUsuario(this.accesoTO) ){
			LOG.debug( "ACCESO CONCEDIDO" );
			session.setAttribute("loggedUser", this.usuarioSrv.getUsuario() );
			this.destroyBean();
			accion = "dashboard";
		}
		else{
			LOG.debug( "ACCESO FALLIDO" );
			session.setAttribute("loggedUser", null);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error",  "Credenciales erroneas, intente de nuevo") );
		}

		return accion;
	}

	/**
	 * @return the accesoTO
	 */
	public SecurityTO getAccesoTO() {
		return this.accesoTO;
	}

	/**
	 * @param accesoTO the accesoTO to set
	 */
	public void setAccesoTO(final SecurityTO accesoTO) {
		this.accesoTO = accesoTO;
	}

}
