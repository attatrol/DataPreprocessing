package com.github.attatrol.preprocessing.clustering;

import java.io.IOException;

import com.github.attatrol.preprocessing.datasource.AbstractTokenDataSource;
import com.github.attatrol.preprocessing.distance.DistanceFunction;

/**
 * Clusters data from {@link AbstractTokenDataSource}.
 * @author atta_troll
 *
 */
public interface ClusterProcessor {

	ClusterResult cluster(AbstractTokenDataSource<?> dataSource,
	        DistanceFunction distanceFunction, int initialNumberOfClusters)
	        throws ClusterException, IOException;

}
