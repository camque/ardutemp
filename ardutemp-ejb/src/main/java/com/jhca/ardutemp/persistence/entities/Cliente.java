package com.jhca.ardutemp.persistence.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;


/**
 * The persistent class for the cliente database table.
 *
 */
@Entity
@Table(name="cliente")
public class Cliente implements Serializable {

	private static final long serialVersionUID = 8801890497352372332L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	private String activo;

	private String nombre;

	//bi-directional many-to-one association to Temperatura
	@OneToMany(mappedBy="cliente", fetch=FetchType.LAZY)
	private List<Temperatura> temperaturas;

	//bi-directional many-to-one association to Usuario
	@OneToOne(mappedBy="cliente", fetch=FetchType.LAZY)
	private Usuario usuario;

	/**
	 * Constructor
	 */
	public Cliente() {
		super();
	}

	/**
	 * Constructor
	 * @param id
	 */
	public Cliente(final Integer id) {
		this.id = id;
	}

	public int getId() {
		return this.id;
	}

	public void setId(final int id) {
		this.id = id;
	}

	public String getActivo() {
		return this.activo;
	}

	public void setActivo(final String activo) {
		this.activo = activo;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(final String nombre) {
		this.nombre = nombre;
	}

	public List<Temperatura> getTemperaturas() {
		return this.temperaturas;
	}

	public void setTemperaturas(final List<Temperatura> temperaturas) {
		this.temperaturas = temperaturas;
	}

	public Temperatura addTemperatura(final Temperatura temperatura) {
		this.getTemperaturas().add(temperatura);
		temperatura.setCliente(this);

		return temperatura;
	}

	public Temperatura removeTemperatura(final Temperatura temperatura) {
		this.getTemperaturas().remove(temperatura);
		temperatura.setCliente(null);

		return temperatura;
	}

	public Usuario getUsuario() {
		return this.usuario;
	}

	public void setUsuario(final Usuario usuario) {
		this.usuario = usuario;
	}

}