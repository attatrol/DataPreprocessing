
package com.github.attatrol.preprocessing.distance.metric;

import com.github.attatrol.preprocessing.distance.DistanceFunction;

/**
 * Interface for some metric (distance function).<br/>
 * It is defined for any continuous multidimensional space (Rn).<br/>
 * Only used in cases when data is numerical.<br/>
 * 
 * @author atta_troll
 *
 */
public abstract class Metric implements DistanceFunction {

    /**
     * {@inheritDoc} It is presumed that all coordinates of points are either {@code Integer} or
     * {@code Double} as metric is used in continuous space, otherwise
     * {@link java.lang.IllegalArgumentException} will be thrown.
     */
    @Override
    public double calculate(Object[] point1, Object[] point2) {
        final double[] differences = new double[point1.length];
        for (int i = 0; i < point1.length; i++) {
            double coord1 = convert(point1[i]);
            double coord2 = convert(point2[i]);
            differences[i] = Math.abs(coord1 - coord2);
        }
        return calculate(differences);
    }

    /**
     * Calculates metric.
     * 
     * @param linear
     *        differences between coordinates of two points
     * @return metric value
     */
    public abstract double calculate(double[] differences);

    /**
     * Converts numeric token into double, throws {@link java.lang.IllegalArgumentException} if
     * conversion is impossible.
     * 
     * @param token
     *        value to be converted
     * @return double value
     */
    public static double convert(Object token) {
        if (token instanceof Double) {
            return (Double) token;
        }
        else if (token instanceof Integer) {
            return (Integer) token;
        }
        else {
            throw new IllegalArgumentException("Numeric coordinate expected.");
        }
    }

    /**
     * Converts array of numeric tokens into array of {@code double}.
     * 
     * @param tokens
     *        numeric tokens
     * @return converted tokens
     */
    public static double[] convert(Object[] tokens) {
        double[] converted = new double[tokens.length];
        for (int i = 0; i < tokens.length; i++) {
            converted[i] = convert(tokens[i]);
        }
        return converted;
    }

}
