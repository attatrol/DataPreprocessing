package com.github.attatrol.preprocessing.distance.nonmetric.gower;

/**
 * Data set records are tuples of tokens.
 * We have to calculate quantitative difference between tokens on
 * the same positions for 2 different tuples.<br/>
 * It must satisfy 2 conditions:<br/>
 * 1. f(x, x) = 1 = max(f)<br/>
 * 2. f(x, y) = f(y, x)<br/>
 * 3. f >= 0 <br/>
 * 4. triangle inequality is not required but strongly advised.
 * * @author atta_troll
 *
 */
public interface GowerTokenSimilarityIndex {

    /**
	 * 
	 * @param coord1 token from first record
	 * @param coord2 token from second record
	 * @return similarity index value 
	 */
	GowerTokenSimilarityIndexOutput calculate(Object coord1, Object coord2);

}
