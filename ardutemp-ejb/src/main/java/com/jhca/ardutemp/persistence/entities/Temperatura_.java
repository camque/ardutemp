package com.jhca.ardutemp.persistence.entities;

import java.sql.Timestamp;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2015-12-08T09:37:52.954-0500")
@StaticMetamodel(Temperatura.class)
public class Temperatura_ {
	public static volatile SingularAttribute<Temperatura, Integer> id;
	public static volatile SingularAttribute<Temperatura, Timestamp> fecha;
	public static volatile SingularAttribute<Temperatura, Double> valor;
	public static volatile SingularAttribute<Temperatura, Cliente> cliente;
}
