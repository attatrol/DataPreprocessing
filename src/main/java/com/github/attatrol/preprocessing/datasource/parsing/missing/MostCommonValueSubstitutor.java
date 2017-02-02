package com.github.attatrol.preprocessing.datasource.parsing.missing;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.github.attatrol.preprocessing.datasource.AbstractTokenDataSource;
import com.github.attatrol.preprocessing.datasource.Record;

/**
 * Substitutes most common value for any missing one. If there are more than one
 * most common elements, one will be chosen randomly.
 * @author atta_troll
 *
 * @param V type of value to substitute
 */
public class MostCommonValueSubstitutor<V> extends MissingTokenSubstitutor<V> {

    /**
     * Found most common value
     */
    private V mostCommonValue;

    protected MostCommonValueSubstitutor(int index, V mostCommonValue) {
        super(index);
        this.mostCommonValue = mostCommonValue;
    }

    @Override
    public V substitute(Record<? extends Object[]> tokens) {
        return mostCommonValue;
    }

    
    /**
     * Factory class for {@link ExpectedValueFloatSubstitutor}
     * @author atta_troll
     *
     */
    public static class Factory<V> implements MissingTokenSubstitutorFactory<V> {

        /**
         * Factory method for {@link ExpectedValueFloatSubstitutor}.
         * {@inheritDoc}
         * 
         */
        @Override
        public MissingTokenSubstitutor<V> produceSubstitutor(AbstractTokenDataSource<?> dataSource, int index)
                throws IOException, IllegalArgumentException {
            Map<V, Long> occurrences = new HashMap<>();
            dataSource.reset();
            while (dataSource.hasNext()) {
                @SuppressWarnings("unchecked")
                V value = (V) (dataSource.next().getData()[index]);
                Long occurenceCount = occurrences.get(value);
                if (occurenceCount == null) {
                    occurrences.put(value, 1L);
                }
                else {
                    occurrences.put(value, occurenceCount + 1);
                }
            }
            long maxOccurrenceCount = 0L;
            V mcv = null;
            for (Map.Entry<V, Long> entry : occurrences.entrySet()) {
                if (entry.getValue() > maxOccurrenceCount) {
                    maxOccurrenceCount = entry.getValue();
                    mcv = entry.getKey();
                }
            }
            return new MostCommonValueSubstitutor<V>(index, mcv);
        }

    }
}
