package com.github.attatrol.preprocessing.distance.metric;

/**
 * Euclidean metric, the one we use everyday.
 * @author atta_troll
 *
 */
public class EuclideanMetric extends Metric {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double calculate(double[] differences) {
		double accumulator = 0;
		for(int i = 0; i< differences.length; i++) {
			accumulator += differences[i] * differences[i];
		}
		return Math.pow(accumulator, .5);
	}

}
