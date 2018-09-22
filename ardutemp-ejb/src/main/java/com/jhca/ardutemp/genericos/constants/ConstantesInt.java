package com.jhca.ardutemp.genericos.constants;

/**
 * Contantes de tipo entero
 * @author Kmi Quevedo
 *
 */
public enum ConstantesInt {

	/** Puerto del socket */
	SOCKET_PORT(6969),

	/** Numeros */
	_0(0),
	_1(1),
	_2(2),
	_3(3),
	_4(4),
	_5(5),
	_6(6),
	_7(7),
	_8(8),
	_9(9);

	private int valor;

	/**
	 * @param valor
	 */
	private ConstantesInt(final int valor) {
		this.valor = valor;
	}

	/**
	 * @return the valor
	 */
	public int getValor() {
		return this.valor;
	}

	/**
	 * @param valor the valor to set
	 */
	public void setValor(final int valor) {
		this.valor = valor;
	}

}
