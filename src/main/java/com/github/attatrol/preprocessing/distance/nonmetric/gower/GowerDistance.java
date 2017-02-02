
package com.github.attatrol.preprocessing.distance.nonmetric.gower;

import java.io.IOException;

import com.github.attatrol.preprocessing.datasource.AbstractTokenDataSource;
import com.github.attatrol.preprocessing.distance.DistanceFunction;

/**
 * This is a synthetic distance function. For each pair of records (points) they are split on pairs
 * of tokens with the same index i, then they are processed by individual token similarity index
 * calculators, each returning pair of {S[i], w[i]}. Then Gower's generalized similarity index is
 * calculated as a ratio between sums of all weight[i]*S[i] and all weight[i]*w[i]. Then subtract this
 * similarity index from {@code 1} and get Gower's generalized distance.
 * 
 * @author atta_troll
 *
 */
public class GowerDistance implements DistanceFunction {

    /**
     * Difference calculators for each token. May have nulls which means the coordinate is omitted
     * from clustering.
     */
    private GowerTokenSimilarityIndex[] similarityIndexes;

    /**
     * Weights which represent importance of each coordinate. <b>Sum of all weights should be equal
     * to 1, otherwise distance function won't be normalized (have values from [0, 1] only)/b>
     */
    private double[] weights;

    /**
     * Length of a records from data set.
     */
    private int recordSize;

    /**
     * Default ctor.
     * @param similarityIndexes token similarity indexes
     * @param weights custom weights for every token, must be positive, sum must not exceed 1.
     * @param recordSize record size for current token
     */
    private GowerDistance(GowerTokenSimilarityIndex[] similarityIndexes, double[] weights,
            int recordSize) {
        super();
        this.similarityIndexes = similarityIndexes;
        this.weights = weights;
        this.recordSize = recordSize;
    }



    @Override
    public double calculate(Object[] record1, Object[] record2) {
        double numerator = 0.;
        double denominator = 0.;
        for (int i = 0; i < recordSize; i++) {
            final GowerTokenSimilarityIndexOutput output = similarityIndexes[i].calculate(record1[i], record2[i]);
            numerator += weights[i] * output.getNumeratorSummand();
            denominator += weights[i] * output.getDenominatorSummand();
        }
        final double gowerSimilarityIndex;
        if (denominator != 0.) {
            gowerSimilarityIndex = numerator / denominator;
        }
        else if (numerator == 0.) {
            gowerSimilarityIndex = 0.;
        }
        else {
            // here is illegal state of Gower similarity index, this branch should never be reached
            gowerSimilarityIndex = 1.;
        }
        return 1. - gowerSimilarityIndex;
    }

    /**
     * Factory method for a Gower's distance instance.
     * @param indexesFactories similarity indexes factories for each token
     * @param unbalancedWeights raw weights values for each token
     * @param dataSource data source
     * @return Gower's distance instance
     * @throws IOException on i/o error
     * @throws IllegalStateException on bad weights values
     */
    public static GowerDistance produceGowerDistance(
            GowerTokenSimilarityIndexFactory<?>[] indexesFactories,
            double[] unbalancedWeights, AbstractTokenDataSource<?> dataSource)
                    throws IOException, IllegalStateException {
        // balance weights
        double sum = 0.;
        for (double weight : unbalancedWeights) {
            if (weight < 0) {
                throw new IllegalStateException("Negative weight for Gower's distance.");
            }
            else {
                sum += weight;
            }
        }
        final double[] weights = new double[unbalancedWeights.length];
        if (sum != 0.) {
            for (int i = 0; i < weights.length; i++) {
                weights[i] = unbalancedWeights[i] / sum;
            }
        }
        else {
            throw new IllegalStateException("Gower's distance all weights can not be zero");
        }
        // generate token similarity indexes
        final GowerTokenSimilarityIndex[] indexes =
                new GowerTokenSimilarityIndex[indexesFactories.length];
        for (int i = 0; i < indexesFactories.length; i++) {
            indexes[i] = indexesFactories[i].getTokenDifferenceCalculator(dataSource, i);
        }
        // generating instance
        return new GowerDistance(indexes, weights, dataSource.getRecordLength());
    }
}
