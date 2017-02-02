package com.github.attatrol.preprocessing.distance.nonmetric.similarity;

import java.util.Map;

/**
 * Lin describes an information-theoretic framework for
 * similarity, where he argues that when similarity is thought
 * of in terms of assumptions about the space, the similarity
 * measure naturally follows from the assumptions.  Here his framework
 * is applied to the categorical setting. The Lin measure
 * gives higher weight to matches on frequent values, and lower
 * weight to mismatches on infrequent values.<br/>
 * Its range is [0 ,1].<br/>
 * To be used rare due to heavy computational complexity.
 * @see <a href=
 * "http://citeseerx.ist.psu.edu/viewdoc/download?doi=10.1.1.140.8831&rep=rep1&type=pdf">Detailed
 * overview of all indexes</a>
 * @author atta_troll
 *
 */
public class LinIndex extends AbstractSimilarityIndex {

    /**
     * Default ctor.
     * @param occurrences number of token occurrences
     * @param tokenTotalNumber total number of tokens for each index in record
     */
    public LinIndex(Map<Object, Long>[] occurrences, long[] tokenTotalNumber) {
        super(occurrences, tokenTotalNumber);
    }

    @Override
    public double calculate(Object[] point1, Object[] point2) {
        double numerator = 0.;
        double denominator = 0.;
        for (int  i = 0; i < point1.length; i++) {
            if (point1[i].equals(point2[i])) {
                final double summand = 2 * Math.log(getSampleProbability(point1[i], i));
                numerator += summand;
                denominator += summand;
            }
            else {
                final double p1 = getSampleProbability(point1[i], i);
                final double p2 = getSampleProbability(point2[i], i);
                final double numeratorSummand = 2 * Math.log(p1 + p2);
                final double denominatorSummand = 2 * Math.log(p1) + Math.log(p2);
                numerator += numeratorSummand;
                denominator += denominatorSummand;
            }
        }
        return numerator / denominator;
    }

    /**
     * Factory for Lin index.
     * @author atta_troll
     *
     */
    public static class Factory implements SimilarityIndexFactory<LinIndex> {

        @Override
        public LinIndex getSimilarityIndex(Map<Object, Long>[] occurrences,
                long[] tokenTotalNumber) {
            return new LinIndex(occurrences, tokenTotalNumber);
        }
    }
}
