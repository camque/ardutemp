package com.jhca.ardutemp.business.impl;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateful;

import org.apache.log4j.Logger;

import com.jhca.ardutemp.business.IUsuarioSrv;
import com.jhca.ardutemp.genericos.constants.ConstantesInt;
import com.jhca.ardutemp.genericos.to.SecurityTO;
import com.jhca.ardutemp.persistence.IUsuarioJPA;
import com.jhca.ardutemp.persistence.entities.Usuario;

/**
 * Session Bean implementation class UsuarioSrv
 */
@Stateful
@LocalBean
public class UsuarioSrv implements IUsuarioSrv {

	/** LOG del sistema */
	private static final Logger LOG = Logger.getLogger(UsuarioSrv.class);
	/** Objeto de usuario */
	private transient Usuario usuario;
	/** Acceso a datos de usuario */
	@EJB
	private transient IUsuarioJPA usuarioJPA;

	/**
	 * @see com.jhca.ardutemp.business.IUsuarioSrv#loginUsuario(com.jhca.ardutemp.genericos.to.AccesoTO)
	 */
	@Override
	public boolean loginUsuario(final SecurityTO accesoTO) {
		try {
			final MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			final byte[] digest = messageDigest.digest( accesoTO.getClave().getBytes() );
			final BigInteger number = new BigInteger(ConstantesInt._1.getValor(), digest);
			final StringBuilder hashtext = new StringBuilder( number.toString(16) );
			while (hashtext.length() < 32) {
				hashtext.insert(ConstantesInt._0.getValor(), "0");
			}
			accesoTO.setClave( hashtext.toString() ) ;
		} catch (final NoSuchAlgorithmException ex) {
			LOG.debug("No se encontro el algortimo", ex);
		}

		this.usuario = this.usuarioJPA.loginUsuario(accesoTO);

		return ( this.usuario!=null && this.usuario.getId()!=null ? true : false );
	}

	/**
	 * @see com.jhca.ardutemp.business.IUsuarioSrv#getUsuario()
	 */
	@Override
	public Usuario getUsuario() {
		return this.usuario;
	}

}
