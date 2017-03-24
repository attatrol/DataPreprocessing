package com.github.attatrol.preprocessing.distance.nonmetric.similarity;

import java.util.Map;

/**
 * Gambaryan proposed a measure that gives more weight to matches where the
 * matching value occurs in about half the data set, i.e., in between being
 * frequent and rare. The Gambaryan measure for a single attribute match
 * is closely related to the Shannon entropy from information theory,
 * as can be seen from its formula.<br/>
 * Its range is [0, 1].
 * @see <a href=
 *      "http://citeseerx.ist.psu.edu/viewdoc/download?doi=10.1.1.140.8831&rep=rep1&type=pdf">Detailed
 *      overview of all indexes</a>
 * @author atta_troll
 *
 */
public class GambaryanIndex extends AbstractSimilarityIndex {

    private static final double LOG2 = Math.log(2.);

    /**
     * Total number of all unique tokens among all records
     */
    private double denominator;

    /**
     * Default ctor.
     * @param occurrences number of token occurrences
     * @param tokenTotalNumber total number of tokens for each index in record
     */
    public GambaryanIndex(Map<Object, Long>[] occurrences, long[] tokenTotalNumber) {
        super(occurrences, tokenTotalNumber);
        for (int  i = 0; i < tokenTotalNumber.length; i++) {
            denominator += getTokenCardinality(i);
        }
    }

    @Override
    public double calculate(Object[] point1, Object[] point2) {
        double result = 0.;
        for (int  i = 0; i < point1.length; i++) {
            if (point1[i].equals(point2[i])) {
                final double probability = getSampleProbability(point1[i], i);
                result -= probability != 1 ? (probability * Math.log(probability)
                        + (1 - probability) * Math.log(1 - probability)) / LOG2
                        : 0.;
            }
        }
        return result / denominator;
    }

    /**
     * Factory for Gambaryan index.
     * @author atta_troll
     *
     */
    public static class Factory implements SimilarityIndexFactory<GambaryanIndex> {

        @Override
        public GambaryanIndex getSimilarityIndex(Map<Object, Long>[] occurrences,
                long[] tokenTotalNumber) {
            return new GambaryanIndex(occurrences, tokenTotalNumber);
        }
    }
}
