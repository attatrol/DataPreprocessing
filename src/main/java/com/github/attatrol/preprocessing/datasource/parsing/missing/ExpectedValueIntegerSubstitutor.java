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
public class ExpectedValueIntegerSubstitutor extends MissingTokenSubstitutor<Long> {

	/**
	 * Expected value
	 */
	private Long expectedValue;

	public ExpectedValueIntegerSubstitutor(int index, Long expectedValue) {
		super(index);
		this.expectedValue = expectedValue;
	}

	@Override
	public Long substitute(Record<? extends Object[]> tokens) {
		return expectedValue;
	}

	/**
	 * Factory class for {@link ExpectedIntegerFloatSubstitutor}
	 * @author atta_troll
	 *
	 */
	public static class Factory implements MissingTokenSubstitutorFactory<Long> {

		/**
		 * Factory method for {@link ExpectedValueIntegerSubstitutor}.
		 * {@inheritDoc}
		 * 
		 */
		@Override
		public MissingTokenSubstitutor<Long> produceSubstitutor(AbstractTokenDataSource<?> dataSource, int index)
				throws IOException, IllegalArgumentException {
			double sum = 0.;
			long count = 0;
			dataSource.reset();
			while (dataSource.hasNext()) {
				Double value = (Double) dataSource.next().getData()[index];
				if (value != null) {
					count++;
					sum += value;
				}
			}
			return new ExpectedValueIntegerSubstitutor(
					index, count != 0 ? Math.round(sum / count) : 0);
		}
	}

}
