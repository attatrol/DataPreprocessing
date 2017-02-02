package com.github.attatrol.preprocessing.distance.nonmetric.gower;

import java.io.IOException;

import com.github.attatrol.preprocessing.datasource.AbstractTokenDataSource;

public class JaccardBinarySimilarityIndex implements GowerTokenSimilarityIndex {

    @Override
    public GowerTokenSimilarityIndexOutput calculate(Object coord1, Object coord2) {
        final boolean c1 = (Boolean) coord1;
        final boolean c2 = (Boolean) coord2;
        return new GowerTokenSimilarityIndexOutput(c1 && c2 ? 1. : 0.,
                c1 || c2 ? 1. : 0.);
    }

    public static class Factory
            implements GowerTokenSimilarityIndexFactory<JaccardBinarySimilarityIndex> {
        public JaccardBinarySimilarityIndex getTokenDifferenceCalculator(
                AbstractTokenDataSource<?> dataSource, int index) throws IOException {
            return new JaccardBinarySimilarityIndex();
        }
    }

}
