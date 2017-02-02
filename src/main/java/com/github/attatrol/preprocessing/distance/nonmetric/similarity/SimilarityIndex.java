package com.github.attatrol.preprocessing.distance.nonmetric.similarity;

/**
 * Performs actual calculations of some similarity index which are listed in <a href=
 * "http://citeseerx.ist.psu.edu/viewdoc/download?doi=10.1.1.140.8831&rep=rep1&type=pdf">Detailed
 * overview of all indexes</a>.<br/>
 * Used in calculation of certain similarity function as its parameter.
 * @author atta_troll
 *
 */
public interface SimilarityIndex {

    /**
     * Calculates similarity index.
     * @param point1
     * @param point2
     * @return
     */
    double calculate(Object[] point1, Object[] point2);
}
