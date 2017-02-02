package com.github.attatrol.preprocessing.distance;

/**
 * The most basic interface for some distance function.
 * It must satisfy 4 conditions:<br/>
 * 1. non-negativity<br/>
 * 2. f(x, x) = 0<br/>
 * 3. symmetry, f(x, y) = f(y, x)<br/>
 * 4. triangle inequality, f(x, z) <= f(x, y) + f(y, z)<br/>
 * If any of these conditions are broken then report this fact in its javadoc.<br/>
 * It is presumed that both array parameters for {@link #calculate(Object[], Object[])}
 * have equal sizes.<br/>
 * @author atta_troll
 *
 */
public interface DistanceFunction {

    /**
     * Calculates distance between 2 points, which are a tuples of some objects.
     * @param point1 point 1
     * @param point2 point 2
     * @return distance
     */
	double calculate(Object[] point1, Object[] point2);

	/**
	 * Calculates distance exactly like {@link #calculate(Object[], Object[])},
	 * but excludes non-masked coordinates from calculation.
	 * @param point1 point 1
	 * @param point2 point 2
	 * @param mask mask of tokens in use
	 * @return distance
	 */
	default double calculate(Object[] point1, Object[] point2, int[] mask) {
        return calculate(getMaskedTokens(point1, mask), getMaskedTokens(point2, mask));
	}

     /**
     * Returns only masked tokens from a set of tokens.
     * @param tokens tokens
     * @param mask mask for tokens
     * @return masked tokens
     */
    static Object[] getMaskedTokens(Object[] tokens, int[] mask) {
        Object[] converted = new Object[mask.length];
        for (int i = 0; i < tokens.length; i++) {
            converted[i] = tokens[mask[i]];
        }
        return converted;
    }

}
