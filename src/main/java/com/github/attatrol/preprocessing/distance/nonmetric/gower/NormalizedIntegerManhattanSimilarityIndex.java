
package com.github.attatrol.preprocessing.distance.nonmetric.gower;

import java.io.IOException;

import com.github.attatrol.preprocessing.datasource.AbstractTokenDataSource;

/**
 * Calculates difference between 2 integers, linearly mapped into [0, 1].
 * 
 * @author atta_troll
 *
 */
public class NormalizedIntegerManhattanSimilarityIndex implements GowerTokenSimilarityIndex {

    private int range;

    public NormalizedIntegerManhattanSimilarityIndex(int range) {
        this.range = range;
    }

    @Override
    public GowerTokenSimilarityIndexOutput calculate(Object coord1, Object coord2) {
        final double numeratorSummand =
                range != 0. ? 1. - ((double) Math.abs((Integer) coord1 - (Integer) coord2)) / range : 1.;
        return new GowerTokenSimilarityIndexOutput(numeratorSummand, 1.);
    }

    /**
     * Factory class for current distance calculator.
     * 
     * @author atta_troll
     *
     */
    public static class Factory
            implements GowerTokenSimilarityIndexFactory<NormalizedFloatManhattanSimilarityIndex> {
        public NormalizedFloatManhattanSimilarityIndex getTokenDifferenceCalculator(
                AbstractTokenDataSource<?> dataSource, int index) throws IOException {
            dataSource.reset();
            int min = 0;
            int max = 0;
            if (dataSource.hasNext()) {
                min = (Integer) dataSource.next().getData()[index];
                max = min;
            }
            while (dataSource.hasNext()) {
                int value = (Integer) dataSource.next().getData()[index];
                if (value > max) {
                    max = value;
                }
                else if (value < min) {
                    min = value;
                }
            }
            return new NormalizedFloatManhattanSimilarityIndex(max - min);
        }
    }
}
