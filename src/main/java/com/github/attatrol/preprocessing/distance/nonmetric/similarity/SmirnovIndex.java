package com.github.attatrol.preprocessing.distance.nonmetric.similarity;

import java.util.Map;

/**
 * Smirnov proposed a measure rooted in probability theory
 * that not only considers a given value’s frequency, but
 * also takes into account the distribution of the other
 * values taken by the same attribute. The Smirnov
 * measure is probabilistic for both matches and mismatches.
 * For  a  match, the  similarity  is  high  when  the  frequency  of  the
 * matching value is low, and the other values occur frequently.<br/>
 * <b>Important note:</b>This index is normalized to have its values in range [0, 1]
 * Normalizing denominator is 2N.
 * @see <a href=
 *      "http://citeseerx.ist.psu.edu/viewdoc/download?doi=10.1.1.140.8831&rep=rep1&type=pdf">Detailed
 *      overview of all indexes</a>
 * @author atta_troll
 *
 */
public class SmirnovIndex extends AbstractSimilarityIndex {

    /**
     * Total number of all unique tokens among all records
     */
    private double denominator;

    private double[] sums;

    /**
     * Default ctor.
     * @param occurrences number of token occurrences
     * @param tokenTotalNumber total number of tokens for each index in record
     */
    public SmirnovIndex(Map<Object, Long>[] occurrences, long[] tokenTotalNumber) {
        super(occurrences, tokenTotalNumber);
        sums = new double[tokenTotalNumber.length];
        for (int  i = 0; i < tokenTotalNumber.length; i++) {
            denominator += getTokenCardinality(i) * tokenTotalNumber[i];;
            for (Map.Entry<Object, Long> entry : occurrences[i].entrySet()) {
                sums[i] += tokenTotalNumber[i] - entry.getValue() != 0
                        ? ((double) entry.getValue())
                        / (tokenTotalNumber[i] - entry.getValue())
                        : 0.; // token has single value over whole index is a special case
            }
        }
    }

    @Override
    public double calculate(Object[] point1, Object[] point2) {
        double numerator = 0.;
        for (int  i = 0; i < point1.length; i++) {
            if (point1[i].equals(point2[i])) {
                final long tokenOccurrence = super.getOccurrence(point1[i], i);
                if (tokenOccurrence == tokenTotalNumber[i]) {
                    numerator += 2.;
                }
                else {
                    final double substrahend = ((double) tokenOccurrence)
                            / (tokenTotalNumber[i] - tokenOccurrence);
                    numerator += 2. + 1 / substrahend + sums[i] - substrahend;
                }
            }
            else {
                final double substrahend1 = ((double) getOccurrence(point1[i], i))
                        / (tokenTotalNumber[i] - getOccurrence(point1[i], i));
                final double substrahend2 = ((double) getOccurrence(point2[i], i))
                        / (tokenTotalNumber[i] - getOccurrence(point2[i], i));
                numerator += sums[i] - substrahend1 - substrahend2;
            }
        }
        return numerator / denominator;
    }

    /**
     * Factory for Smirnov index.
     * @author atta_troll
     *
     */
    public static class Factory implements SimilarityIndexFactory<SmirnovIndex> {

        @Override
        public SmirnovIndex getSimilarityIndex(Map<Object, Long>[] occurrences,
                long[] tokenTotalNumber) {
            return new SmirnovIndex(occurrences, tokenTotalNumber);
        }
    }
}
