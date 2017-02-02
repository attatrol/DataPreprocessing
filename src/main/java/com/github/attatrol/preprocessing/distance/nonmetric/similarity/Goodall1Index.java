
package com.github.attatrol.preprocessing.distance.nonmetric.similarity;

import java.util.HashMap;
import java.util.Map;

/**
 * This measure assigns higher similarity to a match if the value is infrequent than if the value is
 * frequent. Goodall’s original measure details a procedure to combine similarities in the
 * multivariate setting which takes into account dependencies between attributes. Since this
 * procedure is computationally expensive, we use a simpler version of the measure (described next
 * asGoodall1). Goodall’s original measure is not present. Also there are realized 3 other variants
 * of this measure: Goodall2, Goodall3 and Goodall4.<br/>
 * Its range is [0, 1 - 2/N/(N-1)].
 * 
 * @see <a href=
 *      "http://citeseerx.ist.psu.edu/viewdoc/download?doi=10.1.1.140.8831&rep=rep1&type=pdf">Detailed
 *      overview of all indexes</a>
 * @author atta_troll
 *
 */
public class Goodall1Index extends AbstractSimilarityIndex {

    /**
     * Collection with already calculated values
     */
    private HashMap<Long, Double>[] calculatedValues;

    /**
     * Default ctor.
     * 
     * @param occurrences
     *        number of token occurrences
     * @param tokenTotalNumber
     *        total number of tokens for each index in record
     */
    @SuppressWarnings("unchecked")
    public Goodall1Index(Map<Object, Long>[] occurrences, long[] tokenTotalNumber) {
        super(occurrences, tokenTotalNumber);
        calculatedValues = new HashMap[tokenTotalNumber.length];
        for (int i = 0; i < tokenTotalNumber.length; i++) {
            calculatedValues[i] = new HashMap<>();
        }
    }

    @Override
    public double calculate(Object[] point1, Object[] point2) {
        double result = 0.;
        for (int i = 0; i < point1.length; i++) {
            Double cashedLocalResult = null;
            double localResult = 0.;
            if (point1[i].equals(point2[i])) {
                final Long tokenOccurrence = occurrences[i].get(point1[i]);
                cashedLocalResult = calculatedValues[i].get(tokenOccurrence);
                if (cashedLocalResult != null) {
                    localResult = cashedLocalResult;
                }
                else {
                    for (Map.Entry<Object, Long> entry : occurrences[i].entrySet()) {
                        final long someTokenOccurrence = entry.getValue();
                        if (someTokenOccurrence <= tokenOccurrence) {
                            localResult += someTokenOccurrence * (someTokenOccurrence - 1);
                        }
                    }
                    localResult = tokenTotalNumber[i] > 2
                            ? 1. - localResult / tokenTotalNumber[i] / (tokenTotalNumber[i] - 1)
                            : 1.;
                    calculatedValues[i].put(tokenOccurrence, localResult);
                }
            }
            result += localResult;
        }
        return result / point1.length;
    }

    /**
     * Factory for Goodall1 index.
     * @author atta_troll
     *
     */
    public static class Factory implements SimilarityIndexFactory<Goodall1Index> {

        @Override
        public Goodall1Index getSimilarityIndex(Map<Object, Long>[] occurrences,
                long[] tokenTotalNumber) {
            return new Goodall1Index(occurrences, tokenTotalNumber);
        }
    }
}
