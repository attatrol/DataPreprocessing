package com.github.attatrol.preprocessing.distance.nonmetric.similarity;

import java.util.Map;

/**
 * The overlap measure simply counts the number of attributes
 * that match in the two data instances. The range of per-attribute similarity
 * for the overlap measure is [0,1], with a value of
 * occurring when there is no match, and a value of occurring
 * when the attribute values match.<h/>
 * This one is actually an antipode for normalized Hamming distance.
 * @author atta_troll
 *
 */
public final class OverlapIndex implements SimilarityIndex {

    @Override
    public double calculate(Object[] point1, Object[] point2) {
        double result = 0.;
        for (int  i = 0; i < point1.length; i++) {
            if (point1[i].equals(point2[i])) {
                result += 1.;
            }
        }
        return result / point1.length;
    }

    /**
     * Factory for overlap index.
     * @author atta_troll
     *
     */
    public static class Factory implements SimilarityIndexFactory<OverlapIndex> {

        @Override
        public OverlapIndex getSimilarityIndex(Map<Object, Long>[] occurrences,
                long[] tokenTotalNumber) {
            return new OverlapIndex();
        }
    }
}
