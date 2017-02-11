package com.github.attatrol.preprocessing.distance.metric;

/**
 * Generalization of Euclid norm is a p-norm.
 * Also known as a Minkovski metric<p/>
 * f({xi}, {yi}) = (Sum(|xi - yi|^p))^(1/p).<br/>
 * Note it is much slower than euclidean or manhattan metric.
 * @author attatrol
 *
 */
public class PNorm extends Metric {

    /**
     * p-norm coefficient
     */
    private final double p;

    /**
     * inverted p-norm coefficient.
     */
    private final double oneOverP;

    /**
     * Default ctor.
     * Use {@link #getPNorm(double)} to create instance.
     * @param p p-norm coefficient
     */
    private PNorm(double p) {
        this.p = p;
        oneOverP = 1./p;
    }

    /**
     * Factory method for p-norm instances.
     * @param p p-norm coefficient
     * @return p-norm instance
     */
    public static PNorm getPNorm(double p) {
    	if (p>= 1.) {
    		return new PNorm(p);
    	}
    	else {
    		throw new IllegalArgumentException(
    				"p-norms with coefficient lesser than 1 has no triangle inequality feat");
    	}
    }

    /**
     * @return p-norm coefficient
     */
    public double getP() {
        return p;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double calculate(double[] differences) {
        double accumulator = 0.;
        for (int i = 0; i < differences.length; i++) {
            accumulator += Math.pow(differences[i], p);
        }
        return Math.pow(accumulator,  oneOverP);
    }
}