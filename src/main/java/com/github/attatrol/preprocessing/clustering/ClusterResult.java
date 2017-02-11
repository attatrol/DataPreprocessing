
package com.github.attatrol.preprocessing.clustering;

import com.github.attatrol.preprocessing.datasource.Record;

/**
 * Result of some cluster process.
 * 
 * @author atta_troll
 *
 */
public interface ClusterResult {

    /**
     * Returns index of a cluster, to which current record belongs.
     * 
     * @param record
     *        index from
     *        {@link com.github.attatrol.preprocessing.datasource.AbstractTokenDataSource}
     * @return index of a cluster
     */
    int getClusterIndex(Record<? extends Object[]> recordIndex);

    /**
     * @return total number of clusters, empty cluster are allowed but undesirable
     */
    int getClusterNumber();

}
