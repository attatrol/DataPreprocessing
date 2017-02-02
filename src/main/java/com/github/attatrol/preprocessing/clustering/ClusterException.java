package com.github.attatrol.preprocessing.clustering;

/**
 * Thrown on internal cluster processor error.
 * @author atta_troll
 *
 */
public class ClusterException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7169983416686849342L;

	/**
	 * Default ctor.
	 * @param message error message
	 */
	public ClusterException(String message) {
		super(message);
	}

}
