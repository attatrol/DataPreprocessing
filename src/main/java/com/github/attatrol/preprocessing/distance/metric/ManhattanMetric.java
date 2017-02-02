package com.github.attatrol.preprocessing.distance.metric;

/**
 * Manhattan metric also known as a taxicab metric.
 * @author atta_troll
 *
 */
public class ManhattanMetric extends Metric {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double calculate(double[] differences) {
		double accumulator = 0;
		for(int i = 0; i< differences.length; i++) {
			accumulator += differences[i];
		}
		return accumulator;
	}

}
