package com.jhca.ardutemp.persistence.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.log4j.Logger;

import com.jhca.ardutemp.genericos.constants.ConstantesInt;
import com.jhca.ardutemp.genericos.to.SecurityTO;
import com.jhca.ardutemp.persistence.IUsuarioJPA;
import com.jhca.ardutemp.persistence.entities.Cliente;
import com.jhca.ardutemp.persistence.entities.Cliente_;
import com.jhca.ardutemp.persistence.entities.Usuario;
import com.jhca.ardutemp.persistence.entities.Usuario_;

/**
 * Session Bean implementation class UsuarioJPA
 */
@Stateless
@LocalBean
public class UsuarioJPA extends AbstractPersistencia implements IUsuarioJPA {

	/** LOG del sistema */
	private static final Logger LOG = Logger.getLogger(UsuarioJPA.class);

	/** Criteria builder para generar consultas JPA. */
	private transient CriteriaBuilder builder;

	/** Criteria query para generar consultas JPA. */
	private transient CriteriaQuery<Usuario> query;

	/** Root para generar consultas JPA. */
	private transient Root<Usuario> usuarios;

	/** Lista de parámetros para generar consultas JPA. */
	private transient List<Predicate> listaParametros;

	/**
	 * Inicializa variables dependientes del entityManager
	 */
	@PostConstruct
	public void init(){
		// Inicializo variables de criteria
		this.builder = this.entityManager.getCriteriaBuilder();
		this.query = this.builder.createQuery(Usuario.class);
		this.usuarios = this.query.from(Usuario.class);
		this.listaParametros = new ArrayList<Predicate>();
	}

	/**
	 * @see com.jhca.ardutemp.persistence.IUsuarioJPA#loginUsuario(com.jhca.ardutemp.genericos.to.AccesoTO)
	 */
	@Override
	public Usuario loginUsuario(final SecurityTO accesoTO) {
		LOG.debug("Verificando el usuario [" + accesoTO.getUsuario() + "]");

		final Join<Usuario, Cliente> clientes = this.usuarios.join(Usuario_.cliente);

		//Adiciono filtros
		this.listaParametros.add(this.builder.equal(this.usuarios.get(Usuario_.usuario), accesoTO.getUsuario() ) );
		this.listaParametros.add(this.builder.equal(this.usuarios.get(Usuario_.clave), accesoTO.getClave() ) );
		this.listaParametros.add(this.builder.equal(this.usuarios.get(Usuario_.activo), String.valueOf( ConstantesInt._1.getValor() ) ) );
		this.listaParametros.add(this.builder.equal(clientes.get(Cliente_.activo), String.valueOf( ConstantesInt._1.getValor() ) ) );

		this.query.where(this.builder.and(this.listaParametros.toArray(new Predicate[this.listaParametros.size()])));

		//Ejecuto la consulta
		Usuario usuario = null;
		try{
			usuario = this.entityManager.createQuery(this.query).getSingleResult();

			LOG.debug("Usuario Id [" + usuario.getId() + "]");
		} catch ( NoResultException | IllegalArgumentException ex ){
			LOG.debug("No exiten resultados");
		}

		return usuario;
	}

}
