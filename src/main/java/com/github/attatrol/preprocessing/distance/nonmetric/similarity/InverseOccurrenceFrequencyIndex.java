package com.github.attatrol.preprocessing.distance.nonmetric.similarity;

import java.util.Map;

/**
 * The inverse occurrence frequency measure assigns lower
 * similarity to mismatches on more frequent values.
 * The IOF measure is related to the concept of inverse
 * document frequency which comes from information retrieval,
 * where it is used to signify the relative number of documents
 * that contain a specific  word.
 * A key difference is that inverse document frequency is computed
 * on a term-document matrix which is usually binary, while the
 * IOF measure is defined for categorical data.<br/>
 * Its range is [ 1 / (1 + (log(N/2))^2), 1].
 * @see <a href=
 *      "http://citeseerx.ist.psu.edu/viewdoc/download?doi=10.1.1.140.8831&rep=rep1&type=pdf">Detailed
 *      overview of all indexes</a>
 * @author atta_troll
 *
 */
public final class InverseOccurrenceFrequencyIndex extends AbstractSimilarityIndex {

    /**
     * Default ctor.
     * @param occurrences number of token occurrences
     * @param tokenTotalNumber total number of tokens for each index in record
     */
    public InverseOccurrenceFrequencyIndex(Map<Object, Long>[] occurrences,
            long[] tokenTotalNumber) {
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
                result += 1 / (1 + Math.log(getOccurrence(point1[i], i)
                        * Math.log(getOccurrence(point2[i], i))));
            }
        }
        return result / point1.length;
    }

    /**
     * Factory for Inverse Occurrence Frequency index.
     * @author atta_troll
     *
     */
    public static class Factory implements SimilarityIndexFactory<InverseOccurrenceFrequencyIndex> {

        @Override
        public InverseOccurrenceFrequencyIndex getSimilarityIndex(Map<Object, Long>[] occurrences,
                long[] tokenTotalNumber) {
            return new InverseOccurrenceFrequencyIndex(occurrences, tokenTotalNumber);
        }
    }
}
