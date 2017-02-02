package com.github.attatrol.preprocessing.datasource;

/**
 * A record got from some {@link DataSource}
 * @author atta_troll
 *
 * @param <V>
 */
public class Record<V> {

	/**
	 * data from data source.
	 */
	private V data;

	/**
	 * Index of current iteration of data source
	 * (index of record).
	 */
	private long index;

	/**
	 * Default ctor.
	 * @param data stored data
	 * @param index unique index
	 */
	public Record(V data, long index) {
		super();
		this.data = data;
		this.index = index;
	}

	/**
	 * @return data of the record
	 */
	public V getData() {
		return data;
	}

	/**
	 * @return index of the record
	 */
	public long getIndex() {
		return index;
	}
}
