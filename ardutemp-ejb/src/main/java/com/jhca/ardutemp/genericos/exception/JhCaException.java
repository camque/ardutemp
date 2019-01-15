package com.jhca.ardutemp.genericos.exception;

/**
 * Excepciones del sistema
 * @author Kmi Quevedo
 */
public class JhCaException extends Exception {

	/** Serializacion */
	private static final long serialVersionUID = -2662459399997350086L;

	/**
	 * Constructor
	 */
	public JhCaException() {
		super();
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public JhCaException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public JhCaException(final String message, final Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public JhCaException(final String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public JhCaException(final Throwable cause) {
		super(cause);
	}

}
