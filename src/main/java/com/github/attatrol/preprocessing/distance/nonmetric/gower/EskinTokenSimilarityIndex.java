package com.github.attatrol.preprocessing.distance.nonmetric.gower;

import java.io.IOException;

import com.github.attatrol.preprocessing.datasource.AbstractTokenDataSource;
import com.github.attatrol.preprocessing.distance.nonmetric.gower.AbstractCategoricalTokenSimilarityIndex.CategoricalTokenSimilarityIndexData;

/**
 * Based on {@link com.github.attatrol.preprocessing.distance.nonmetric.similarity.EskinIndex}.
 * @author atta_troll
 *
 */
public final class EskinTokenSimilarityIndex implements GowerTokenSimilarityIndex {

    private double inequalResult;
    /**
     * Default ctor.
     * @param data data used to initialize internal state of this index
     */
    public EskinTokenSimilarityIndex(CategoricalTokenSimilarityIndexData data) {
        double tokenCardinalitySquared = data.getOccurrences().size();
        tokenCardinalitySquared *= tokenCardinalitySquared;
        inequalResult = tokenCardinalitySquared / (tokenCardinalitySquared + 2);
        // classic Eskin index has range [2/3, 1], here we map it into [0, 1]
        inequalResult = (inequalResult - 2./3.) * 3;
    }

    @Override
    public GowerTokenSimilarityIndexOutput calculate(Object coord1, Object coord2) {
        double result = coord1.equals(coord2) ? 1. : inequalResult;
        return new GowerTokenSimilarityIndexOutput(result, 1.);
    }

    /**
     * Factory for Eskin token similarity index.
     * @author atta_troll
     *
     */
    public static class Factory implements GowerTokenSimilarityIndexFactory<EskinTokenSimilarityIndex> {

        @Override
        public EskinTokenSimilarityIndex getTokenDifferenceCalculator(
                AbstractTokenDataSource<?> dataSource, int index) throws IOException {
            return new EskinTokenSimilarityIndex(AbstractCategoricalTokenSimilarityIndex
                    .getTokenSimilarityIndexData(dataSource, index));
        }
        
    }

}
