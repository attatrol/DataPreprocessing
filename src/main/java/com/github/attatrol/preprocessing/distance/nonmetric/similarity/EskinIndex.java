package com.github.attatrol.preprocessing.distance.nonmetric.similarity;

import java.util.Map;

/**
 * Eskin et al. proposed a normalization
 * kernel for record-based network intrusion detection data.
 * The original measure is distance-based and assigns a weight of
 * 2/(n_k * n_k), where n_k is token cardinality (number of unique
 * values token may have in position k in record, for mismatches;
 * when adapted to similarity, this becomes a weight of
 * (n_k * n_k) / (n_k * n_k + 2).<br/>
 * This measure gives more weight to mismatches that occur on
 * attributes that take many values.<br/>
 * Its range is [2/3, 1].
 * @see <a href=
 *      "http://citeseerx.ist.psu.edu/viewdoc/download?doi=10.1.1.140.8831&rep=rep1&type=pdf">Detailed
 *      overview of all indexes</a>
 * @author atta_troll
 *
 */
public class EskinIndex implements SimilarityIndex {

    double[] inequalResults;

    /**
     * Default ctor.
     * @param occurrences number of token occurrences
     */
    public EskinIndex(Map<Object, Long>[] occurrences) {
        inequalResults = new double[occurrences.length];
        for (int i = 0; i < occurrences.length; i++) {
            int tokenCardinalitySquared = occurrences[i].size();
            tokenCardinalitySquared *= tokenCardinalitySquared;
            inequalResults[i] += ((double) tokenCardinalitySquared) / (tokenCardinalitySquared + 2);
        }
    }

    @Override
    public double calculate(Object[] point1, Object[] point2) {
        double result = 0.;
        for (int  i = 0; i < point1.length; i++) {
            if (point1[i].equals(point2[i])) {
                result += 1.;
            }
            else {
                result += inequalResults[i];
            }
        }
        return result / point1.length;
    }

    /**
     * Factory for Eskin index.
     * @author atta_troll
     *
     */
    public static class Factory implements SimilarityIndexFactory<EskinIndex> {

        @Override
        public EskinIndex getSimilarityIndex(Map<Object, Long>[] occurrences,
                long[] tokenTotalNumber) {
            return new EskinIndex(occurrences);
        }
    }
}
