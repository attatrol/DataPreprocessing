
package com.github.attatrol.preprocessing.distance.nonmetric.similarity;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.github.attatrol.preprocessing.datasource.AbstractTokenDataSource;
import com.github.attatrol.preprocessing.distance.DistanceFunction;

/**
 * Dissimilarity function is a kind of distance function used exclusively for pure categorical data.
 * This is a general realization of any similarity coefficient and must be parameterized by
 * {@link AbstractSimilarityIndex} where particular coefficient is realized.<br/>
 * Works well with data with missing (null) tokens, however some {@link AbstractSimilarityIndex} may or may
 * not support missing (null) tokens.
 * 
 * @author atta_troll
 *
 */
public class DissimilarityFunction implements DistanceFunction {

    /**
     * Similarity index used in to distance function calculation.
     */
    private SimilarityIndex similarityIndex;

    /**
     * Private ctor, use
     * {@link #produceDissimilarityFunction(AbstractTokenDataSource, AbstractSimilarityIndex)}.
     * 
     * @param occurrences
     *        occurrences of tokens in data source
     * @param tokenTotalNumber
     *        total number of tokens of the same index
     * @param similarityIndex
     */
    private DissimilarityFunction(SimilarityIndex similarityIndex) {
        this.similarityIndex = similarityIndex;
    }

    /**
     * {@inheritDoc}<br/>
     * Substracts value of similarity index from {@code 1.}.
     */
    @Override
    public double calculate(Object[] point1, Object[] point2) {
        final double similarity = similarityIndex.calculate(point1, point2);
        return 1. - similarity;
    }

    /**
     * Factory method for any dissimilarity index. Analyzes data source, then creates dissimilarity
     * index.
     * 
     * @param dataSource
     *        data source for which dissimilarity index created
     * @param similarityIndexFactory
     *        factory for similarity index used for actual calculations <b> must return value
     *        betweeen 0 and 1</b>
     * @return dissimilarity the function
     * @throws IOException
     *         thrown on i/o error during data source analysis
     */
    public static DissimilarityFunction produceDissimilarityFunction(
            AbstractTokenDataSource<?> dataSource, SimilarityIndexFactory<?> similarityIndexFactory)
            throws IOException {
        final int recordLength = dataSource.getRecordLength();
        @SuppressWarnings("unchecked")
        Map<Object, Long>[] occurrences = new Map[recordLength];
        for (int i = 0; i < recordLength; i++) {
            occurrences[i] = new HashMap<Object, Long>();
        }
        long[] tokenTotalNumber = new long[recordLength];
        dataSource.reset();
        while (dataSource.hasNext()) {
            final Object[] tokens = dataSource.next().getData();
            for (int i = 0; i < recordLength; i++) {
                if (tokens[i] != null) {
                    tokenTotalNumber[i]++;
                    Long occurence = occurrences[i].get(tokens[i]);
                    occurence = occurence == null ? 1 : occurence + 1;
                    occurrences[i].put(tokens[i], occurence);
                }
            }
        }
        return new DissimilarityFunction(
                similarityIndexFactory.getSimilarityIndex(occurrences, tokenTotalNumber));
    }
}
