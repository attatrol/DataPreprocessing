package com.github.attatrol.preprocessing.distance.metric;

/**
 * Chebyshev metric is a p-norm
 * @author atta_troll
 *
 */
public class ChebyshevMetric extends Metric {

	@Override
	public double calculate(double[] differences) {
		double max = 0;
		for (int i = 0; i < differences.length; i++) {
			if (max < differences[i]) {
				max = differences[i];
			}
		}
		return max;
	}

}
