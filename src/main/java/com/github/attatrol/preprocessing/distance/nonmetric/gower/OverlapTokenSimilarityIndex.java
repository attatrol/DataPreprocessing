
package com.github.attatrol.preprocessing.distance.nonmetric.gower;

import java.io.IOException;

import com.github.attatrol.preprocessing.datasource.AbstractTokenDataSource;

public class OverlapTokenSimilarityIndex implements GowerTokenSimilarityIndex {

    @Override
    public GowerTokenSimilarityIndexOutput calculate(Object coord1, Object coord2) {
        return new GowerTokenSimilarityIndexOutput(coord1.equals(coord2) ? 1. : 0., 1.);
    }

    public static class Factory
            implements GowerTokenSimilarityIndexFactory<OverlapTokenSimilarityIndex> {
        public OverlapTokenSimilarityIndex getTokenDifferenceCalculator(
                AbstractTokenDataSource<?> dataSource, int index) throws IOException {
            return new OverlapTokenSimilarityIndex();
        }
    }

}
