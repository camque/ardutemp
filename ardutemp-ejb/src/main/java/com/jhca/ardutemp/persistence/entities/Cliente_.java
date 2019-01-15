package com.jhca.ardutemp.persistence.entities;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2015-12-08T09:37:52.725-0500")
@StaticMetamodel(Cliente.class)
public class Cliente_ {
	public static volatile SingularAttribute<Cliente, Integer> id;
	public static volatile SingularAttribute<Cliente, String> activo;
	public static volatile SingularAttribute<Cliente, String> nombre;
	public static volatile ListAttribute<Cliente, Temperatura> temperaturas;
	public static volatile SingularAttribute<Cliente, Usuario> usuario;
}
