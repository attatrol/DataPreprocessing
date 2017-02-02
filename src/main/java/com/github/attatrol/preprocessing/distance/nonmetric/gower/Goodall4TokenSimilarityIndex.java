package com.github.attatrol.preprocessing.distance.nonmetric.gower;

import java.io.IOException;

import com.github.attatrol.preprocessing.datasource.AbstractTokenDataSource;

/**
 * Based on {@link com.github.attatrol.preprocessing.distance.nonmetric.similarity.Goodall4Index}.
 * @author atta_troll
 *
 */
public class Goodall4TokenSimilarityIndex extends AbstractCategoricalTokenSimilarityIndex {

    /**
     * Default ctor.
     * @param data data used to initialize internal state of this index
     */
    public Goodall4TokenSimilarityIndex(CategoricalTokenSimilarityIndexData data) {
        super(data);
    }

    @Override
    public GowerTokenSimilarityIndexOutput calculate(Object coord1, Object coord2) {
        double result = 0.;
        if (coord1.equals(coord2)) {
            result = getP2(coord1);
        }
        return new GowerTokenSimilarityIndexOutput(result, 1.);
    }

    /**
     * Factory for Goodall4 token similarity index.
     * @author atta_troll
     *
     */
    public static class Factory implements GowerTokenSimilarityIndexFactory<Goodall4TokenSimilarityIndex> {

        @Override
        public Goodall4TokenSimilarityIndex getTokenDifferenceCalculator(
                AbstractTokenDataSource<?> dataSource, int index) throws IOException {
            return new Goodall4TokenSimilarityIndex(AbstractCategoricalTokenSimilarityIndex
                    .getTokenSimilarityIndexData(dataSource, index));
        }
        
    }

}
