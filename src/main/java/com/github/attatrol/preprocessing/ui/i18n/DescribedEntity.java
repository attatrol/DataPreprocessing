package com.github.attatrol.preprocessing.ui.i18n;

/**
 * Simple immutable bean wrapper
 * used to implement i18n into ui.
 * @author atta_troll
 *
 * @param <V>
 */
public class DescribedEntity<V> {

	/**
	 * Entity wrapped.
	 */
	private final V value;

	/**
	 * Full text description of the entity.
	 */
	private final String description;

	/**
	 * Short description, used in {@link #toString()}.
	 */
	private final  String shortDescription;

	public DescribedEntity(V value, String description, String shortDescription) {
		this.value = value;
		this.description = description;
		this.shortDescription = shortDescription;
	}

	public V get() {
		return value;
	}

	public String getDescription() {
		return description;
	}

	@Override
	public String toString() {
		return shortDescription;
	}

}
