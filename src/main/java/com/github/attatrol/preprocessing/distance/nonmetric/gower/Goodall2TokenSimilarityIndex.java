package com.github.attatrol.preprocessing.distance.nonmetric.gower;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.github.attatrol.preprocessing.datasource.AbstractTokenDataSource;

/**
 * Based on {@link com.github.attatrol.preprocessing.distance.nonmetric.similarity.Goodall2Index}.
 * @author atta_troll
 *
 */
public class Goodall2TokenSimilarityIndex extends AbstractCategoricalTokenSimilarityIndex {

    /**
     * Collection with already calculated values
     */
    private Map<Long, Double> calculatedValues = new HashMap<>();

    /**
     * Default ctor.
     * @param data data used to initialize internal state of this index
     */
    public Goodall2TokenSimilarityIndex(CategoricalTokenSimilarityIndexData data) {
        super(data);
    }

    @Override
    public GowerTokenSimilarityIndexOutput calculate(Object coord1, Object coord2) {
        double result = 0.;
        if (coord1.equals(coord2)) {
            final long tokenOccurrence = super.getOccurrence(coord1);
            final Double cachedResult = calculatedValues.get(tokenOccurrence);
            if (cachedResult != null) {
                result = cachedResult;
            }
            else {
                for (Map.Entry<Object, Long> entry : occurrences.entrySet()) {
                    final long someTokenOccurence = entry.getValue();
                    if (someTokenOccurence >= tokenOccurrence) {
                        result += someTokenOccurence * (someTokenOccurence - 1);
                    }
                }
                result = tokenTotalNumber > 2
                        ? 1. - result / tokenTotalNumber / (tokenTotalNumber - 1)
                        : 1.;
                calculatedValues.put(tokenOccurrence, result);
            }
        }
        return new GowerTokenSimilarityIndexOutput(result, 1.);
    }

    /**
     * Factory for Goodall2 token similarity index.
     * @author atta_troll
     *
     */
    public static class Factory implements GowerTokenSimilarityIndexFactory<Goodall2TokenSimilarityIndex> {

        @Override
        public Goodall2TokenSimilarityIndex getTokenDifferenceCalculator(
                AbstractTokenDataSource<?> dataSource, int index) throws IOException {
            return new Goodall2TokenSimilarityIndex(AbstractCategoricalTokenSimilarityIndex
                    .getTokenSimilarityIndexData(dataSource, index));
        }
        
    }

}

