package com.github.attatrol.preprocessing.distance.nonmetric.similarity;

import java.util.Map;

/**
 * The occurrence frequency measure gives the opposite weighting of
 * the IOF measure for mismatches, i.e., mismatches
 * on less frequent values are assigned lower similarity
 * and mismatches on more frequent values are assigned higher
 * similarity.<br/>
 * Its range is [1 / (1 + (log(N))^2), 1 / (1 + (log(2))^2)].
 * @see <a href=
 *      "http://citeseerx.ist.psu.edu/viewdoc/download?doi=10.1.1.140.8831&rep=rep1&type=pdf">Detailed
 *      overview of all indexes</a>
 * @author atta_troll
 *
 */
public class OccurrenceFrequencyIndex extends AbstractSimilarityIndex {

    /**
     * Default ctor.
     * @param occurrences number of token occurrences
     * @param tokenTotalNumber total number of tokens for each index in record
     */
    public OccurrenceFrequencyIndex(Map<Object, Long>[] occurrences, long[] tokenTotalNumber) {
        super(occurrences, tokenTotalNumber);
    }

    @Override
    public double calculate(Object[] point1, Object[] point2) {
        double result = 0.;
        for (int  i = 0; i < point1.length; i++) {
            if (point1[i].equals(point2[i])) {
                result += 1.;
            }
            else {
                final double tokenNumber = (double) tokenTotalNumber[i];
                result += 1 / (1 + Math.log(tokenNumber / getOccurrence(point1[i], i))
                        * Math.log(tokenNumber / getOccurrence(point2[i], i)));
            }
        }
        return result / point1.length;
    }

    /**
     * Factory for Occurrence Frequency index.
     * @author atta_troll
     *
     */
    public static class Factory implements SimilarityIndexFactory<OccurrenceFrequencyIndex> {

        @Override
        public OccurrenceFrequencyIndex getSimilarityIndex(Map<Object, Long>[] occurrences,
                long[] tokenTotalNumber) {
            return new OccurrenceFrequencyIndex(occurrences, tokenTotalNumber);
        }
    }
}
