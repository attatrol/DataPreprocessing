
package com.github.attatrol.preprocessing.distance.nonmetric.gower;

import java.io.IOException;

import com.github.attatrol.preprocessing.datasource.AbstractTokenDataSource;

/**
 * Floats are normalized into range [0, 1], then distance is calculated.
 * @author atta_troll
 *
 */
public class NormalizedFloatManhattanSimilarityIndex implements GowerTokenSimilarityIndex {

    private double range;

    public NormalizedFloatManhattanSimilarityIndex(double range) {
        this.range = range;
    }

    @Override
    public GowerTokenSimilarityIndexOutput calculate(Object coord1, Object coord2) {
        final double numeratorSummand =
                range != 0. ? 1. - Math.abs((Double) coord1 - (Double) coord2) / range : 1.;
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
            double min = 0.;
            double max = 0.;
            if (dataSource.hasNext()) {
                min = (Double) dataSource.next().getData()[index];
                max = min;
            }
            while (dataSource.hasNext()) {
                double value = (Double) dataSource.next().getData()[index];
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
