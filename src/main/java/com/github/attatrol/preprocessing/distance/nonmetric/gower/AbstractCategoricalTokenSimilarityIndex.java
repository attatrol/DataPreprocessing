
package com.github.attatrol.preprocessing.distance.nonmetric.gower;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.github.attatrol.preprocessing.datasource.AbstractTokenDataSource;

/**
 * Performs calculation of similarity index for some pair of tokens which have the same coordinate.
 * Tokens must have the same index in record.<br/>
 * Must return values between [0, 1]
 * 
 * @author atta_troll
 *
 */
public abstract class AbstractCategoricalTokenSimilarityIndex implements GowerTokenSimilarityIndex {

    /**
     * Occurrences of a certain object (token) in whole data source.
     */
    protected Map<Object, Long> occurrences;

    /**
     * Total number of tokens which can be read from one coordinate of the record (of the same
     * index). Equals to total number of records if there is no missing tokens.
     */
    protected long tokenTotalNumber;

    /**
     * Default ctor
     * 
     * @param data
     *        data used to initialize internal state of most of categorical tokens
     */
    public AbstractCategoricalTokenSimilarityIndex(CategoricalTokenSimilarityIndexData data) {
        this(data.getOccurrences(), data.getTokenTotalNumber());
    }

    /**
     * Verbose ctor.
     * 
     * @param occurrences
     *        number of token occurrences
     * @param tokenTotalNumber
     *        total number of tokens for each index in record
     */
    public AbstractCategoricalTokenSimilarityIndex(Map<Object, Long> occurrences,
            long tokenTotalNumber) {
        this.occurrences = occurrences;
        this.tokenTotalNumber = tokenTotalNumber;
    }

    /**
     * Method used by {@link #similarityIndex}.<br/>
     * Returns occurrence number for a current token.<br/>
     * Described in <a href=
     * "http://citeseerx.ist.psu.edu/viewdoc/download?doi=10.1.1.140.8831&rep=rep1&type=pdf">Paper
     * about similarity indexes</a> on page 245 as f_k(x).
     * 
     * @param object
     *        subject token
     * @param index
     *        index of the token in record
     * @return occurrence number for a current token
     */
    protected long getOccurrence(Object object) {
        final Long result = occurrences.get(object);
        return result; // should never return null
    }

    /**
     * Method used by {@link #similarityIndex}.<br/>
     * Returns sample probability for current token.<br/>
     * Described in <a href=
     * "http://citeseerx.ist.psu.edu/viewdoc/download?doi=10.1.1.140.8831&rep=rep1&type=pdf">Paper
     * about similarity indexes</a> on page 245 as p_k(x).
     * 
     * @param object
     *        subject token
     * @param index
     *        index of the token in record
     * @return sample probability for current token
     */
    protected double getSampleProbability(Object object) {
        return tokenTotalNumber != 0 ? ((double) getOccurrence(object)) / tokenTotalNumber
                : 1.;
    }

    /**
     * Method used by {@link #similarityIndex}.<br/>
     * Returns specific p2 coefficient similar to sample probability.<br/>
     * Described in <a href=
     * "http://citeseerx.ist.psu.edu/viewdoc/download?doi=10.1.1.140.8831&rep=rep1&type=pdf">Paper
     * about similarity indexes</a> on page 245 as p^2_k(x).
     * 
     * @param object
     *        subject token
     * @param index
     *        index of the token in record
     * @return P2 index
     */
    protected double getP2(Object object) {
        final double occurrenceNumber = (double) getOccurrence(object);
        final double result;
        if (tokenTotalNumber == 0 || (tokenTotalNumber == 1 && occurrenceNumber == 1)) {
            result = 1.;
        }
        else if (tokenTotalNumber == 1 && occurrenceNumber == 0) {
            result = 0.;
        }
        else {
            result = occurrenceNumber * (occurrenceNumber - 1) / tokenTotalNumber
                    / (tokenTotalNumber - 1);
        }
        return result;
    }

    /**
     * Method used by {@link #similarityIndex}.<br/>
     * Returns token cardinality (number of unique tokens for this index).<br/>
     * Described in <a href=
     * "http://citeseerx.ist.psu.edu/viewdoc/download?doi=10.1.1.140.8831&rep=rep1&type=pdf">Paper
     * about similarity indexes</a> on page 245 as n_k.
     * 
     * @param index
     * @return
     */
    protected int getTokenCardinality() {
        return occurrences.size();
    }

    /**
     * A factory of any {@link AbstractCategoricalTokenSimilarityIndex} should use this method
     * instead of their own similar realization.
     * 
     * @param dataSource
     *        data source for which dissimilarity index created
     * @param index
     *        index of tokens in a record
     * @return dissimilarity the function
     * @throws IOException
     *         thrown on i/o error during data source analysis
     */
    public static CategoricalTokenSimilarityIndexData getTokenSimilarityIndexData(
            AbstractTokenDataSource<?> dataSource, int index) throws IOException {
        Map<Object, Long> occurrences = new HashMap<>();
        long tokenTotalNumber = 0;
        dataSource.reset();
        while (dataSource.hasNext()) {
            final Object token = dataSource.next().getData()[index];
            if (token != null) {
                tokenTotalNumber++;
                Long occurence = occurrences.get(token);
                occurence = occurence == null ? 1 : occurence + 1;
                occurrences.put(token, occurence);
            }
        }
        return new CategoricalTokenSimilarityIndexData(occurrences, tokenTotalNumber);
    }

    /**
     * POJO class, produced by
     * {@link AbstractCategoricalTokenSimilarityIndex#getTokenSimilarityIndexData(AbstractTokenDataSource, int)},
     * used to hold internal state of some realization of
     * {@link AbstractCategoricalTokenSimilarityIndex}.
     * 
     * @author atta_troll
     *
     */
    public static class CategoricalTokenSimilarityIndexData {

        private final Map<Object, Long> occurrences;

        private final long tokenTotalNumber;

        public CategoricalTokenSimilarityIndexData(Map<Object, Long> occurrences,
                long tokenTotalNumber) {
            this.occurrences = occurrences;
            this.tokenTotalNumber = tokenTotalNumber;
        }

        public Map<Object, Long> getOccurrences() {
            return occurrences;
        }

        public long getTokenTotalNumber() {
            return tokenTotalNumber;
        }
    }
}
