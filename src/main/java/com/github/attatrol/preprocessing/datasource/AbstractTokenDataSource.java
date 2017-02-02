package com.github.attatrol.preprocessing.datasource;

import java.io.IOException;
import java.util.NoSuchElementException;

/**
 * Wraps around some {@link DataSource}&lt;String&gt; and parses
 * its records in an appropriate way ready to be used by distance functions.<br/>
 * This data source presents data record as an array
 * of some atomic values (tokens) in a defined order.<br/>
 * This kind of data source is used in cluster algorithm.
 * @author atta_troll
 * @param <V> record type of the wrapped data source
 */
public abstract class AbstractTokenDataSource<V> implements DataSource<Object[]> {

	/**
	 * Wrapped data source.
	 */
	private DataSource<? extends V> internalDataSource;

	/**
	 * Cardinality of each record.
	 */
	private int recordLength;

	/**
	 * Default ctor.
	 * @param internalDataSource wrapped data source
	 * @param recordLength number of tokens in a record
	 */
	protected AbstractTokenDataSource(DataSource<? extends V> internalDataSource,
			int recordLength) {
		this.internalDataSource = internalDataSource;
		this.recordLength = recordLength;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void close() throws IOException {
		internalDataSource.close();
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Record<Object[]> next() throws IOException, IllegalArgumentException, NoSuchElementException {
		final Record<? extends V> record = internalDataSource.next();
		return parseRecord(record);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasNext() throws IOException {
		return internalDataSource.hasNext();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void reset() throws IOException {
		internalDataSource.reset();		
	}

	/**
	 * @return number of tokens in a record
	 */
	public int getRecordLength() {
		return recordLength;
	}

	/**
	 * Parses a record into array of tokens with a defined order.
	 * @param record raw record from internal data source
	 * @return
	 * @throws IllegalArgumentException
	 */
	protected abstract Record<Object[]> parseRecord(Record<? extends V> record) throws IllegalArgumentException;
}
