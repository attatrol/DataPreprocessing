package com.github.attatrol.preprocessing.distance.nonmetric.similarity;

import java.util.Map;

/**
 * The Goodall4 measure assigns similarity 1 − Goodall3 for matches.<br/>
 * Its range is [2/N/(N-1), 1].
 * @see <a href=
 * "http://citeseerx.ist.psu.edu/viewdoc/download?doi=10.1.1.140.8831&rep=rep1&type=pdf">Detailed
 * overview of all indexes</a>
 * @author atta_troll
 *
 */
public class Goodall4Index extends AbstractSimilarityIndex {

    /**
     * Default ctor.
     * @param occurrences number of token occurrences
     * @param tokenTotalNumber total number of tokens for each index in record
     */
    public Goodall4Index(Map<Object, Long>[] occurrences, long[] tokenTotalNumber) {
        super(occurrences, tokenTotalNumber);
    }

    @Override
    public double calculate(Object[] point1, Object[] point2) {
        double result = 0.;
        for (int  i = 0; i < point1.length; i++) {
            if (point1[i].equals(point2[i])) {
                result += getP2(point1[i], i);
            }
        }
        return result / point1.length;
    }

    /**
     * Factory for Anderberg index.
     * @author atta_troll
     *
     */
    public static class Factory implements SimilarityIndexFactory<Goodall4Index> {

        @Override
        public Goodall4Index getSimilarityIndex(Map<Object, Long>[] occurrences,
                long[] tokenTotalNumber) {
            return new Goodall4Index(occurrences, tokenTotalNumber);
        }
    }
}
