package com.github.attatrol.preprocessing.distance.nonmetric.similarity;

import java.util.Map;

/**
 * Factory for some {@link AbstractSimilarityIndex}
 * @author atta_troll
 *
 */
@FunctionalInterface
public interface SimilarityIndexFactory<V extends SimilarityIndex> {

    V getSimilarityIndex(Map<Object, Long>[] occurrences, long[] tokenTotalNumber);

}
