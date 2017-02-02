package com.github.attatrol.preprocessing.distance.nonmetric.gower;

import java.io.IOException;

import com.github.attatrol.preprocessing.datasource.AbstractTokenDataSource;

/**
 * Factory for {@link TokenDiffernceCslculator}.<br/>
 * For convenience every difference calculator should implement this one as {@code Factory} inner static class.
 * @author atta_troll
 *
 */
@FunctionalInterface
public interface GowerTokenSimilarityIndexFactory<V extends GowerTokenSimilarityIndex> {

    /**
     * Factory method for this token difference calculator.
     * 
     * @param dataSource data source
     * @param index index of this coordinate
     * @return difference calculator instance
     * @throws IOException on data source 
     */
    V getTokenDifferenceCalculator(AbstractTokenDataSource<?> dataSource, int index)
        throws IOException;

}
