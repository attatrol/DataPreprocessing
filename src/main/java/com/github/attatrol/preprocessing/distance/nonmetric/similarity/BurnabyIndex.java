
package com.github.attatrol.preprocessing.distance.nonmetric.similarity;

import java.util.Map;

/**
 * Burnaby proposed a similarity measure using arguments from information theory. He argues that the
 * set of observed values are like a group of signals conveying information and, as in information
 * theory, attribute values that are rarely observed should be considered more informative. This
 * measure assigns low similarity to mismatches on rare values and high similarity to mismatches on
 * frequent values.<br/>
 * Its range is [N * log(1 - 1/N) / (N * log(1 - 1/N) - log(N - 1)), 1].
 * 
 * @see <a href=
 *      "http://citeseerx.ist.psu.edu/viewdoc/download?doi=10.1.1.140.8831&rep=rep1&type=pdf">Detailed
 *      overview of all indexes</a>
 * @author atta_troll
 *
 */
public class BurnabyIndex extends AbstractSimilarityIndex {

    /**
     * Sum over all tokens per index: 2 * (log(sampleFrequency(token)))
     */
    private double[] doubledSumOfProbabilityLogs;

    /**
     * Default ctor.
     * @param occurrences number of token occurrences
     * @param tokenTotalNumber total number of tokens for each index in record
     */
    public BurnabyIndex(Map<Object, Long>[] occurrences, long[] tokenTotalNumber) {
        super(occurrences, tokenTotalNumber);
        doubledSumOfProbabilityLogs = new double[tokenTotalNumber.length];
        for (int i = 0; i < tokenTotalNumber.length; i++) {
            for (Map.Entry<Object, Long> entry : occurrences[i].entrySet()) {
                doubledSumOfProbabilityLogs[i] +=
                        Math.log(1 - entry.getValue() / tokenTotalNumber[i]);
            }
            doubledSumOfProbabilityLogs[i] *= 2.;
        }
    }

    @Override
    public double calculate(Object[] point1, Object[] point2) {
        double result = 0.;
        for (int i = 0; i < point1.length; i++) {
            if (point1[i].equals(point2[i])) {
                result += 1.;
            }
            else {
                final double p1 = getSampleProbability(point1[i], i);
                final double p2 = getSampleProbability(point2[i], i);
                result += doubledSumOfProbabilityLogs[i] / (doubledSumOfProbabilityLogs[i]
                        + Math.log(p1 * p2 / (1 - p1) / (1 - p2)));
            }
        }
        return result / point1.length;
    }

    /**
     * Factory for Burnaby index.
     * @author atta_troll
     *
     */
    public static class Factory implements SimilarityIndexFactory<BurnabyIndex> {

        @Override
        public BurnabyIndex getSimilarityIndex(Map<Object, Long>[] occurrences,
                long[] tokenTotalNumber) {
            return new BurnabyIndex(occurrences, tokenTotalNumber);
        }
    }
}
