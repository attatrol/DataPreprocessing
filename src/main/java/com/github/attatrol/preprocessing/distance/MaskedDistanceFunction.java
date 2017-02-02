package com.github.attatrol.preprocessing.distance;

/**
 * Simple holder for some {@link DistanceFunction} with associated mask.
 * @author atta_troll
 *
 */
public class MaskedDistanceFunction implements DistanceFunction {

    /**
     * Wrapped distance function.
     */
    private DistanceFunction distanceFunction;

    /**
     * Mask for tokens in use.
     */
    private int[] mask;

    /**
     * Default ctor.
     * @param distanceFunction wrapped distance function
     * @param mask mask for tokens in use
     */
    public MaskedDistanceFunction(DistanceFunction distanceFunction, int[] mask) {
        this.distanceFunction = distanceFunction;
        this.mask = mask;
    }

    /**
     * {@inheritDoc}<br/>
     * Uses {@code calculate} method from wrapped distance function with applied {@link #mask}.
     */
    @Override
    public double calculate(Object[] point1, Object[] point2) {
        return distanceFunction.calculate(point1, point2, mask);
    }

    /**
     * @return wrapped distance function
     */
    public DistanceFunction getDistanceFunction() {
        return distanceFunction;
    }

    /**
     * @return used tokens mask
     */
    public int[] getMask() {
        return mask;
    }

}
