package com.github.attatrol.preprocessing.distance.nonmetric.similarity;

import java.util.Map;

/**
 * The Goodall3 measure assigns a high similarity if the matching
 * values are infrequent regardless of the frequencies of the other values.<br/>
 * Its range is [0, 1 - 2/N/(N-1)].
 * @see <a href=
 * "http://citeseerx.ist.psu.edu/viewdoc/download?doi=10.1.1.140.8831&rep=rep1&type=pdf">Detailed
 * overview of all indexes</a>
 * @author atta_troll
 *
 */
public class Goodall3Index extends AbstractSimilarityIndex {

    /**
     * Default ctor.
     * @param occurrences number of token occurrences
     * @param tokenTotalNumber total number of tokens for each index in record
     */
    public Goodall3Index(Map<Object, Long>[] occurrences, long[] tokenTotalNumber) {
        super(occurrences, tokenTotalNumber);
    }

    @Override
    public double calculate(Object[] point1, Object[] point2) {
        double result = 0.;
        for (int  i = 0; i < point1.length; i++) {
            if (point1[i].equals(point2[i])) {
                result += 1. - getP2(point1[i], i);
            }
        }
        return result / point1.length;
    }

    /**
     * Factory for Goodall3 index.
     * @author atta_troll
     *
     */
    public static class Factory implements SimilarityIndexFactory<Goodall3Index> {

        @Override
        public Goodall3Index getSimilarityIndex(Map<Object, Long>[] occurrences,
                long[] tokenTotalNumber) {
            return new Goodall3Index(occurrences, tokenTotalNumber);
        }
    }
}
