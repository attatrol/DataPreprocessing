package com.github.attatrol.preprocessing.datasource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.NoSuchElementException;

/**
 * Describes file data source where each line of text represent some record.
 * Derivatives must override {@link #parseLine(String)} to define how record will be parsed.
 * @author atta_troll
 *
 */
public class TextFileDataSource implements TitledDataSource<String> {

    /**
     * File of data source.
     */
    private File file;

    /**
     * Stream associated with data file.
     */
    private FileInputStream fis;

    /**
     * Reader used for string-by-string reading from data file.
     */
    private BufferedReader reader;

    /**
     * Flag of being closed.
     */
    private boolean isClosed;

    /**
     * Index counter for a current record.
     */
    private long indexCounter;

    /**
     * Flag of having column titles in the first row.
     * They will be omitted from iterations.
     * Titles may be recovered by method {@link #getTitles()}.
     */
    private boolean hasTitles;

    /**
     * Raw titles string.
     */
    private String rawTitlesString;

    /**
     * Default ctor.
     */
    public TextFileDataSource(File file, boolean hasTitles) {
        this.file = file;
        this.hasTitles = hasTitles;
    }

    /**
     * {@inheritDoc}
     */
	@Override
	public boolean hasNext() throws IOException {
		stateCheck();
        return fis.available() > 0 || reader.ready();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Record<String> next() throws IOException, IllegalArgumentException, NoSuchElementException {
		stateCheck();
		final String line = reader.readLine();
        if (line == null) {
            throw new NoSuchElementException("There are no records left.");
        }
		return new Record<String>(line, indexCounter++);
	}

    /**
     * {@inheritDoc}
     */
    @Override
    public void reset() throws IOException {
    	stateCheck();
        fis.getChannel().position(0L);
        reader = new BufferedReader(new InputStreamReader(fis));
        if (hasTitles && hasNext()) {
        	next();
        }
        indexCounter = 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() throws IOException {
        if (!isClosed && reader != null) {
            isClosed = true;
            reader.close();
        }
    }

    /**
     * @return raw title as a string, may return {@code null}
     * if file is empty or flag of having titles is {@code false}.
     */
    public String getTitles() {
    	return rawTitlesString;
    }

    /**
     * Checks if IO operation is aviable,
     * calls for setup when internal state is not initialized fully.
     * @throws IOException on failure to perform stream opening.
     */
    private void stateCheck() throws IOException {
        if (isClosed) {
            throw new IllegalStateException("Illegal access to closed resource");
        }
        if (reader == null) {
            setup();
        }
    }
    /**
     * Opens file for reading.
     * @throws IOException on failure to perform stream opening.
     */
    private void setup() throws IOException {
        fis = new FileInputStream(file);
        reader = new BufferedReader(new InputStreamReader(fis));
        if (hasTitles && hasNext()) {
        	rawTitlesString = next().getData();
        	indexCounter = 0;
        }
    }
}
