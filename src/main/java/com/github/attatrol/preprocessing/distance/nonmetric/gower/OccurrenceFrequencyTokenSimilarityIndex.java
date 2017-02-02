package com.github.attatrol.preprocessing.distance.nonmetric.gower;

import java.io.IOException;

import com.github.attatrol.preprocessing.datasource.AbstractTokenDataSource;

/**
 * Based on {@link com.github.attatrol.preprocessing.distance.nonmetric.similarity.OccurrenceFrequencyIndex}.
 * @author atta_troll
 *
 */
public class OccurrenceFrequencyTokenSimilarityIndex extends AbstractCategoricalTokenSimilarityIndex {

    /**
     * Default ctor.
     * 
     * @param data
     *        data used to initialize internal state of this index
     */
    public OccurrenceFrequencyTokenSimilarityIndex(
            CategoricalTokenSimilarityIndexData data) {
        super(data);
    }

    @Override
    public GowerTokenSimilarityIndexOutput calculate(Object coord1, Object coord2) {
        double result = coord1.equals(coord2) ? 1.
                : 1 / (1. + Math.log(tokenTotalNumber / getOccurrence(coord1))
                * Math.log(tokenTotalNumber / getOccurrence(coord2)));
        return new GowerTokenSimilarityIndexOutput(result, 1.);
    }

    /**
     * Factory for Goodall3 token similarity index.
     * 
     * @author atta_troll
     *
     */
    public static class Factory implements
            GowerTokenSimilarityIndexFactory<OccurrenceFrequencyTokenSimilarityIndex> {

        @Override
        public OccurrenceFrequencyTokenSimilarityIndex getTokenDifferenceCalculator(
                AbstractTokenDataSource<?> dataSource, int index) throws IOException {
            return new OccurrenceFrequencyTokenSimilarityIndex(
                    AbstractCategoricalTokenSimilarityIndex.getTokenSimilarityIndexData(dataSource,
                            index));
        }

    }

}
