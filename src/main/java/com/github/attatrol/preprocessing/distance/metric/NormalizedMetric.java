package com.github.attatrol.preprocessing.distance.metric;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.github.attatrol.preprocessing.datasource.AbstractTokenDataSource;
import com.github.attatrol.preprocessing.distance.MaskedDistanceFunction;

/**
 * Normalized metric is a metric where each coordinate is normalized between 0 and 1.
 * It is a decorator for an ordinary {@link Metric}, it overrides {@link #calculate(Object[], Object[])}
 * method for a normalized calculation, {@link NormalizedMetric#calculate(double[])} is unchanged.
 * @author atta_troll
 *
 */
public class NormalizedMetric extends Metric {

	/**
	 * Set of ranges for every coordinate
	 */
	private double[] ranges;

	/**
	 * Wrapped metric function.
	 */
	private Metric metric;

	/**
	 * Default ctor.
	 * @param ranges ranges of coordinates
	 * @param metric wrapped metric
	 */
	private NormalizedMetric(double[] ranges, Metric metric) {
		this.ranges = ranges;
		this.metric = metric;
	}

	/**
	 * {@inheritDoc}<br/>
	 * It is unchanged call of wrapped metric method.
	 */
	@Override
	public double calculate(double[] differences) {
		return metric.calculate(differences);
	}

	/**
	 * {@inheritDoc}<br/>
	 * Calculates normalized metric.
	 */
	@Override
	public double calculate(Object[] point1, Object[] point2) {
		final double[] differences = new double[point1.length];
		for(int i = 0; i < point1.length; i++) {
			double coord1 = Metric.convert(point1[i]);
			double coord2 = Metric.convert(point2[i]);
			differences[i] = Math.abs(coord1 - coord2) / ranges[i];
		}
		return calculate(differences);
	}

	/**
	 * Factory getter for a normalized metric instance wrapped into {@link MaskedDistanceFunction}.
	 * @param dataSource token data source
	 * @param metric normalized metric instance
	 * @param mask mask of used tokens
	 * @return normalized metric instance
	 * @throws IOException on data source i/o failure
	 */
	public static MaskedDistanceFunction getNormalizedMetric(AbstractTokenDataSource<?> dataSource,
			Metric metric, int[] mask) throws IOException {
		return new MaskedDistanceFunction(new NormalizedMetric(getRanges(dataSource, mask), metric), mask);
	}

	   /**
     * Factory getter for a normalized metric instance.
     * @param dataSource token data source
     * @param metric normalized metric instance
     * @return normalized metric instance
     * @throws IOException on data source i/o failure
     */
    public static NormalizedMetric getNormalizedMetric(AbstractTokenDataSource<?> dataSource,
            Metric metric) throws IOException {
        return new NormalizedMetric(getRanges(dataSource), metric);
    }

	/**
	 * Finds a set of ranges for each coordinate of a numeric data set
	 * @param dataSource token data source
	 * @param mask mask of tokens in use
	 * @return normalized metric instance
	 * @throws IOException on data source i/o failure
	 */
	private static double[] getRanges(AbstractTokenDataSource<?> dataSource, int[] mask)
			throws IOException {
		List<Double> maxValues = new ArrayList<>(mask.length);
		List<Double> minValues = new ArrayList<>(mask.length);
		dataSource.reset();
		while (dataSource.hasNext()) {
			Object[] values = dataSource.next().getData();
			for (int i = 0; i < mask.length; i++) {
				final double doubleValue = Metric.convert(values[mask[i]]);
				if (maxValues.size() == i) {
					maxValues.add(doubleValue);
				}
				else if (maxValues.get(i) < doubleValue) {
					maxValues.set(i, doubleValue);
				}
				if (minValues.size() == i) {
					minValues.add(doubleValue);
				}
				else if (minValues.get(i) > doubleValue) {
					minValues.set(i, doubleValue);
				}
			}
		}
		final double[] ranges = new double[maxValues.size()];
		for (int i = 0; i < ranges.length; i++) {
			final double max = maxValues.get(i);
			final double min = minValues.get(i);
			ranges[i] = max != min ? max - min : 1.;
		}
		return ranges;
	}

	   /**
     * Finds a set of ranges for each coordinate of a numeric data set
     * @param dataSource token data source
     * @param mask mask of tokens in use
     * @return normalized metric instance
     * @throws IOException on data source i/o failure
     */
    private static double[] getRanges(AbstractTokenDataSource<?> dataSource)
            throws IOException {
        List<Double> maxValues = new ArrayList<>();
        List<Double> minValues = new ArrayList<>();
        dataSource.reset();
        while (dataSource.hasNext()) {
            Object[] values = dataSource.next().getData();
            for (int i = 0; i < values.length; i++) {
                final double doubleValue = Metric.convert(values[i]);
                if (maxValues.size() == i) {
                    maxValues.add(doubleValue);
                }
                else if (maxValues.get(i) < doubleValue) {
                    maxValues.set(i, doubleValue);
                }
                if (minValues.size() == i) {
                    minValues.add(doubleValue);
                }
                else if (minValues.get(i) > doubleValue) {
                    minValues.set(i, doubleValue);
                }
            }
        }
        final double[] ranges = new double[maxValues.size()];
        for (int i = 0; i < ranges.length; i++) {
            final double max = maxValues.get(i);
            final double min = minValues.get(i);
            ranges[i] = max != min ? max - min : 1.;
        }
        return ranges;
    }
}
