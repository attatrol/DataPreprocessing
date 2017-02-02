
package com.github.attatrol.preprocessing.distance.nonmetric.similarity;

import java.util.Map;

/**
 * Performs actual calculations of some similarity index which are listed in <a href=
 * "http://citeseerx.ist.psu.edu/viewdoc/download?doi=10.1.1.140.8831&rep=rep1&type=pdf">Detailed
 * overview of all indexes</a>.<br/>
 * Used in calculation of certain similarity function as its parameter.
 * 
 * @author atta_troll
 *
 */
public abstract class AbstractSimilarityIndex implements SimilarityIndex {

    /**
     * Occurrences of a certain object (token) in whole data source.
     */
    protected Map<Object, Long>[] occurrences;

    /**
     * Total number of tokens which can be read from one coordinate
     * of the record (of the same index).
     * Equals to total number of records if there is no missing tokens.
     */
    protected long[] tokenTotalNumber;

    /**
     * Default ctor.
     * @param occurrences number of token occurrences
     * @param tokenTotalNumber total number of tokens for each index in record
     */
    public AbstractSimilarityIndex(Map<Object, Long>[] occurrences, long[] tokenTotalNumber) {
        super();
        this.occurrences = occurrences;
        this.tokenTotalNumber = tokenTotalNumber;
    }

    /**
     * Method used by {@link #similarityIndex}.<br/>
     * Returns occurrence number for a current token.<br/>
     * Described in <a href=
     * "http://citeseerx.ist.psu.edu/viewdoc/download?doi=10.1.1.140.8831&rep=rep1&type=pdf">Paper
     * about similarity indexes</a> on page 245 as f_k(x).
     * @param object subject token
     * @param index index of the token in record
     * @return occurrence number for a current token
     */
    protected long getOccurrence(Object object, int index) {
        final Long result = occurrences[index].get(object);
        return result; // should never return null
    }

    /**
     * Method used by {@link #similarityIndex}.<br/>
     * Returns sample probability for current token.<br/>
     * Described in <a href=
     * "http://citeseerx.ist.psu.edu/viewdoc/download?doi=10.1.1.140.8831&rep=rep1&type=pdf">Paper
     * about similarity indexes</a> on page 245 as p_k(x).
     * @param object subject token
     * @param index index of the token in record
     * @return sample probability for current token
     */
    protected double getSampleProbability(Object object, int index) {
        return tokenTotalNumber[index] != 0
                ? ((double) getOccurrence(object, index)) / tokenTotalNumber[index]
                : 1.;
    }

    /**
     * Method used by {@link #similarityIndex}.<br/>
     * Returns specific p2 coefficient similar to sample probability.<br/>
     * Described in <a href=
     * "http://citeseerx.ist.psu.edu/viewdoc/download?doi=10.1.1.140.8831&rep=rep1&type=pdf">Paper
     * about similarity indexes</a> on page 245 as p^2_k(x).
     * @param object subject token
     * @param index index of the token in record
     * @return P2 index
     */
    protected double getP2(Object object, int index) {
        final double occurrenceNumber = (double) getOccurrence(object, index);
        final long tokenNumber = tokenTotalNumber[index];
        final double result;
        if (tokenNumber == 0 || (tokenNumber == 1 && occurrenceNumber == 1)) {
            result = 1.;
        }
        else if (tokenNumber == 1 && occurrenceNumber == 0) {
            result = 0.;
        }
        else {
            result = occurrenceNumber * (occurrenceNumber - 1) / tokenNumber / (tokenNumber - 1);
        }
        return result;
    }

    /**
     * Method used by {@link #similarityIndex}.<br/>
     * Returns token cardinality (number of unique tokens for this index).<br/>
     * Described in <a href=
     * "http://citeseerx.ist.psu.edu/viewdoc/download?doi=10.1.1.140.8831&rep=rep1&type=pdf">Paper
     * about similarity indexes</a> on page 245 as n_k.
     * @param index
     * @return
     */
    protected int getTokenCardinality(int index) {
        return occurrences[index].size();
    }
}
