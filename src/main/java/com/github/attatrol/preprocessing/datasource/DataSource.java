package com.github.attatrol.preprocessing.datasource;

import java.io.Closeable;
import java.io.IOException;
import java.util.NoSuchElementException;

/**
 * Almost identical to {@link java.util.Iterator}
 * but throws {@link java.io.IOException} on failure to access external data source properly.<br/>
 * Record type is undefined as it is very general interface for sequential input of external data.
 * Also may be reset into initial state with {@link #reset()}.<br/>
 * As an additional restriction records should follow in a static order, so
 * we can index them, indexed records are easily mapped with least possible memory consumption.
 * @author atta_troll
 * @param <V> record type of data source
 */
public interface DataSource<V> extends Closeable {

	/**
     * Returns the next record from iterative data source.
     * @return  the next record
     * @throws IOException on failure to access external data source
     * @throws IllegalArgumentException if parsing of the record (dividing it into array of atomic features) failed
     * @throws NoSuchElementException if there is no more records left
     */
	Record<V> next() throws IOException, IllegalArgumentException, NoSuchElementException;

    /**
     * @return {@code true} if the iteration has records left
     * @throws IOException on external data source error
     */
	boolean hasNext() throws IOException;

	/**
    * Resets iterator, places it before the first element
    * for another cycle of iteration
    * @throws TransactionSourceException on data source error
    */
	void reset() throws IOException;
	

}
