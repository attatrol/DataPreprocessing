
package com.github.attatrol.preprocessing.datasource;

import java.io.IOException;
import java.util.NoSuchElementException;

import com.github.attatrol.preprocessing.datasource.parsing.record.RecordTokenizer;

public final class TokenDataSourceUtils {

    private TokenDataSourceUtils() {
    }

    public static Object[] getMaskedTokens(Object[] originalTokens, int[] mask) {
        Object[] maskedTokens = new Object[mask.length];
        for (int i = 0; i < mask.length; i++) {
            maskedTokens[i] = originalTokens[mask[i]];
        }
        return maskedTokens;
    }

    /**
     * Finds length of the longest record in the data source
     * 
     * @param source
     *        basic data source
     * @param tokenizer
     * @return
     * @throws IOException
     */
    public static <V> int getRecordCardinality(DataSource<V> source,
            RecordTokenizer<? super V, ?> tokenizer) throws IOException {
        int cardinality = 0;
        source.reset();
        while (source.hasNext()) {
            final int tokenNumber = tokenizer.tokenize(source.next().getData()).length;
            if (tokenNumber > cardinality) {
                cardinality = tokenNumber;
            }
        }

        return cardinality;
    }

    /**
     * Simple wrapper over some {@link AbstractTokenDataSource} with appliance
     * of valid token indexes mask, so undesired tokens are thrown away from an original record.<br/>
     * Somewhat breaks Liskov substitution principle, but class is final and its ok here.
     * @author atta_troll
     *
     */
    public static final class MaskedTokenDataSource<V> extends AbstractTokenDataSource<V> {

        /**
         * Original token data source.
         */
        private final AbstractTokenDataSource<?> wrappedDataSource;

        /**
         * Mask with indexes of desired tokens.
         */
        private final int[] mask;

        public MaskedTokenDataSource(AbstractTokenDataSource<?> wrappedDataSource, int[] mask) {
            super(null, mask.length);
            this.wrappedDataSource = wrappedDataSource;
            this.mask = mask;
        }

        @Override
        public void close() throws IOException {
            wrappedDataSource.close();
        }

        @Override
        public Record<Object[]> next()
                throws IOException, IllegalArgumentException, NoSuchElementException {
            final Record<Object[]> record = wrappedDataSource.next();
            return new Record<>(getMaskedTokens(record.getData(), mask), record.getIndex());
        }

        @Override
        public boolean hasNext() throws IOException {
            return wrappedDataSource.hasNext();
        }

        @Override
        public void reset() throws IOException {
            wrappedDataSource.reset();
        }

        /**
         * Not in use.
         */
        @Override
        protected Record<Object[]> parseRecord(Record<? extends V> record)
                throws IllegalArgumentException {
            return null;
        }
        
    }
}
