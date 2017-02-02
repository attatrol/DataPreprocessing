package com.github.attatrol.preprocessing.distance.nonmetric.similarity;

import java.util.HashMap;
import java.util.Map;
/**
 * The Goodall 2 measure is a variant of Goodall’s measure.
 * This measure assigns higher similarity if the matching values are infrequent,
 * and at the same time there are other values that are even less frequent, i.e.,
 * the similarity  is  higher  if  there  are  many  values  with approximately
 * equal frequencies, and lower if the frequency distribution is skewed.<br/>
 * Its range is [0, 1 - 2/N/(N-1)].
 * @see <a href=
 * "http://citeseerx.ist.psu.edu/viewdoc/download?doi=10.1.1.140.8831&rep=rep1&type=pdf">Detailed
 * overview of all indexes</a>
 * @author atta_troll
 *
 */
public class Goodall2Index extends AbstractSimilarityIndex {

    /**
     * Collection with already calculated values
     */
    private HashMap<Long, Double>[] calculatedValues;

    /**
     * Default ctor.
     * @param occurrences number of token occurrences
     * @param tokenTotalNumber total number of tokens for each index in record
     */
    @SuppressWarnings("unchecked")
    public Goodall2Index(Map<Object, Long>[] occurrences, long[] tokenTotalNumber) {
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
                        if (someTokenOccurrence >= tokenOccurrence) {
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
     * Factory for Goodall2 index.
     * @author atta_troll
     *
     */
    public static class Factory implements SimilarityIndexFactory<Goodall2Index> {

        @Override
        public Goodall2Index getSimilarityIndex(Map<Object, Long>[] occurrences,
                long[] tokenTotalNumber) {
            return new Goodall2Index(occurrences, tokenTotalNumber);
        }
    }
}
