
package com.github.attatrol.preprocessing.distance.nonmetric.similarity;

import java.util.Map;

/**
 * In his book on cluster analysis, Anderberg presents an approach to handle similarity between
 * categorical attributes. He argues that rare matches indicate a strong association and should be
 * given a very high weight, and that mismatches on rare values should be treated as being
 * distinctive and should also be given special importance. In accordance with these arguments,
 * the Anderberg measure assigns higher similarity to rare matches, and lower similarity to rare
 * mismatches.<br/>
 * The range of the Anderberg measure is [0, 1]; the minimum value is attained
 * when there are no matches, and the maximum value is attained when all attributes match
 * @see <a href=
 *      "http://citeseerx.ist.psu.edu/viewdoc/download?doi=10.1.1.140.8831&rep=rep1&type=pdf">Detailed
 *      overview of all indexes</a>
 * @author atta_troll
 *
 */
public class AnderbergIndex extends AbstractSimilarityIndex {

    /**
     * Default ctor.
     * 
     * @param occurrences
     *        number of token occurrences
     * @param tokenTotalNumber
     *        total number of tokens for each index in record
     */
    public AnderbergIndex(Map<Object, Long>[] occurrences, long[] tokenTotalNumber) {
        super(occurrences, tokenTotalNumber);
    }

    @Override
    public double calculate(Object[] point1, Object[] point2) {
        double similarTokenSum = 0.;
        double dissimilarTokenSum = 0.;
        for (int i = 0; i < point1.length; i++) {
            final double p1 = getSampleProbability(point1[i], i);
            final double product = 2. / tokenTotalNumber[i] / (tokenTotalNumber[i] + 1);
            if (point1[i].equals(point2[i])) {
                similarTokenSum += product / (p1 * p1);
            }
            else {
                final double p2 = getSampleProbability(point2[i], i);
                dissimilarTokenSum += product / (2. * p1 * p2);
            }
        }
        return similarTokenSum / (similarTokenSum + dissimilarTokenSum);
    }

    /**
     * Factory for Anderberg index.
     * @author atta_troll
     *
     */
    public static class Factory implements SimilarityIndexFactory<AnderbergIndex> {

        @Override
        public AnderbergIndex getSimilarityIndex(Map<Object, Long>[] occurrences,
                long[] tokenTotalNumber) {
            return new AnderbergIndex(occurrences, tokenTotalNumber);
        }
    }
}
