package com.github.attatrol.preprocessing.datasource.parsing.missing;

import java.io.IOException;

import com.github.attatrol.preprocessing.datasource.AbstractTokenDataSource;
import com.github.attatrol.preprocessing.datasource.Record;

/**
 * Simply returns expected value for any of missing token,
 * if all values are omitted, returns zero.
 * @author atta_troll
 *
 */
public class ExpectedValueFloatSubstitutor extends MissingTokenSubstitutor<Double>{

	/**
	 * Expected value for some token.
	 */
	private Double expectedValue;

	/**
	 * Defaulr ctor
	 * @param index index of missing token value
	 * @param expectedValue expected value calculated from values of all non missing tokens
	 */
	public ExpectedValueFloatSubstitutor(int index, Double expectedValue) {
		super(index);
		this.expectedValue = expectedValue;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Double substitute(Record<? extends Object[]> tokens) {
		return expectedValue;
	}

	/**
	 * Factory class for {@link ExpectedValueFloatSubstitutor}
	 * @author atta_troll
	 *
	 */
	public static class Factory implements MissingTokenSubstitutorFactory<Double> {

		/**
		 * Factory method for {@link ExpectedValueFloatSubstitutor}.
		 * {@inheritDoc}
		 * 
		 */
		@Override
		public MissingTokenSubstitutor<Double> produceSubstitutor(AbstractTokenDataSource<?> dataSource, int index)
				throws IOException, IllegalArgumentException {
			double sum = 0.;
			long count = 0;
			dataSource.reset();
			while (dataSource.hasNext()) {
				Object rawValue = dataSource.next().getData()[index];
				if (rawValue != null) {
					count++;
					sum += (Double) rawValue;
				}
			}
			return new ExpectedValueFloatSubstitutor(index, count != 0 ? sum / count : 0.);
		}

	}

}
