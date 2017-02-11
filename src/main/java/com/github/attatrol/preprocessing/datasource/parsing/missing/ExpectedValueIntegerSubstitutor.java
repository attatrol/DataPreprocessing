package com.github.attatrol.preprocessing.datasource.parsing.missing;

import java.io.IOException;

import com.github.attatrol.preprocessing.datasource.AbstractTokenDataSource;
import com.github.attatrol.preprocessing.datasource.Record;

/**
 * Very basic integer value substitutor that returns expected value for any omitted integer,
 * if all values are omitted, returns zero.
 * @author atta_troll
 *
 */
public class ExpectedValueIntegerSubstitutor extends MissingTokenSubstitutor<Integer> {

	/**
	 * Expected value
	 */
	private Integer expectedValue;

	public ExpectedValueIntegerSubstitutor(int index, Integer expectedValue) {
		super(index);
		this.expectedValue = expectedValue;
	}

	@Override
	public Integer substitute(Record<? extends Object[]> tokens) {
		return expectedValue;
	}

	/**
	 * Factory class for {@link ExpectedIntegerFloatSubstitutor}
	 * @author atta_troll
	 *
	 */
	public static class Factory implements MissingTokenSubstitutorFactory<Integer> {

		/**
		 * Factory method for {@link ExpectedValueIntegerSubstitutor}.
		 * {@inheritDoc}
		 * 
		 */
		@Override
		public MissingTokenSubstitutor<Integer> produceSubstitutor(AbstractTokenDataSource<?> dataSource, int index)
				throws IOException, IllegalArgumentException {
			double sum = 0.;
			long count = 0;
			dataSource.reset();
			while (dataSource.hasNext()) {
				Object rawValue = dataSource.next().getData()[index];
				if (rawValue != null) {
					count++;
					sum += (Integer) rawValue;
				}
			}
			Integer calculatedValue = count != 0 ? ((int) Math.round(sum / count)) : 0;
			return new ExpectedValueIntegerSubstitutor(
					index, calculatedValue);
		}
	}

}
