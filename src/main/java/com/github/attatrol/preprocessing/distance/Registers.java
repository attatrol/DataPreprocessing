
package com.github.attatrol.preprocessing.distance;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.github.attatrol.preprocessing.datasource.parsing.TokenType;
import com.github.attatrol.preprocessing.distance.metric.ChebyshevMetric;
import com.github.attatrol.preprocessing.distance.metric.EuclideanMetric;
import com.github.attatrol.preprocessing.distance.metric.ManhattanMetric;
import com.github.attatrol.preprocessing.distance.metric.Metric;
import com.github.attatrol.preprocessing.distance.nonmetric.gower.EskinTokenSimilarityIndex;
import com.github.attatrol.preprocessing.distance.nonmetric.gower.Goodall1TokenSimilarityIndex;
import com.github.attatrol.preprocessing.distance.nonmetric.gower.Goodall2TokenSimilarityIndex;
import com.github.attatrol.preprocessing.distance.nonmetric.gower.Goodall3TokenSimilarityIndex;
import com.github.attatrol.preprocessing.distance.nonmetric.gower.Goodall4TokenSimilarityIndex;
import com.github.attatrol.preprocessing.distance.nonmetric.gower.GowerTokenSimilarityIndexFactory;
import com.github.attatrol.preprocessing.distance.nonmetric.gower.InverseOccurrenceFrequencyTokenSimilarityIndex;
import com.github.attatrol.preprocessing.distance.nonmetric.gower.JaccardBinarySimilarityIndex;
import com.github.attatrol.preprocessing.distance.nonmetric.gower.NormalizedFloatManhattanSimilarityIndex;
import com.github.attatrol.preprocessing.distance.nonmetric.gower.NormalizedIntegerManhattanSimilarityIndex;
import com.github.attatrol.preprocessing.distance.nonmetric.gower.OccurrenceFrequencyTokenSimilarityIndex;
import com.github.attatrol.preprocessing.distance.nonmetric.gower.OverlapTokenSimilarityIndex;
import com.github.attatrol.preprocessing.distance.nonmetric.similarity.AnderbergIndex;
import com.github.attatrol.preprocessing.distance.nonmetric.similarity.BurnabyIndex;
import com.github.attatrol.preprocessing.distance.nonmetric.similarity.EskinIndex;
import com.github.attatrol.preprocessing.distance.nonmetric.similarity.GambaryanIndex;
import com.github.attatrol.preprocessing.distance.nonmetric.similarity.Goodall1Index;
import com.github.attatrol.preprocessing.distance.nonmetric.similarity.Goodall2Index;
import com.github.attatrol.preprocessing.distance.nonmetric.similarity.Goodall3Index;
import com.github.attatrol.preprocessing.distance.nonmetric.similarity.Goodall4Index;
import com.github.attatrol.preprocessing.distance.nonmetric.similarity.InverseOccurrenceFrequencyIndex;
import com.github.attatrol.preprocessing.distance.nonmetric.similarity.LinIndex;
import com.github.attatrol.preprocessing.distance.nonmetric.similarity.OccurrenceFrequencyIndex;
import com.github.attatrol.preprocessing.distance.nonmetric.similarity.OverlapIndex;
import com.github.attatrol.preprocessing.distance.nonmetric.similarity.SimilarityIndexFactory;
import com.github.attatrol.preprocessing.distance.nonmetric.similarity.SmirnovIndex;

/**
 * Holds some registers of distance related objects grouped by some feature.
 * @author atta_troll
 *
 */
public class Registers {

    /**
     * Contains immutable state-less metrics.
     */
    public static final Set<Metric> SIMPLE_METRIC_REGISTER;
    static {
        Set<Metric> metrics = new HashSet<>();
        metrics.add(new ChebyshevMetric());
        metrics.add(new EuclideanMetric());
        metrics.add(new ManhattanMetric());
        SIMPLE_METRIC_REGISTER = Collections.unmodifiableSet(metrics);
    }

    public static final Set<SimilarityIndexFactory<?>> SIMILARITY_INDEX_FACTORY_REGISTER;
    static {
        Set<SimilarityIndexFactory<?>> sifRegister = new HashSet<>();
        sifRegister.add(new OverlapIndex.Factory());
        sifRegister.add(new Goodall1Index.Factory());
        sifRegister.add(new Goodall2Index.Factory());
        sifRegister.add(new Goodall3Index.Factory());
        sifRegister.add(new Goodall4Index.Factory());
        sifRegister.add(new GambaryanIndex.Factory());
        sifRegister.add(new EskinIndex.Factory());
        sifRegister.add(new InverseOccurrenceFrequencyIndex.Factory());
        sifRegister.add(new OccurrenceFrequencyIndex.Factory());
        sifRegister.add(new BurnabyIndex.Factory());
        sifRegister.add(new LinIndex.Factory());
        sifRegister.add(new SmirnovIndex.Factory());
        sifRegister.add(new AnderbergIndex.Factory());
        SIMILARITY_INDEX_FACTORY_REGISTER = Collections.unmodifiableSet(sifRegister);
    }

    /**
     * Lists token difference calculators usable for certain token types.
     */
    public static final Map<TokenType, GowerTokenSimilarityIndexFactory<?>[]> GOWER_TOKEN_SIMILARITY_INDEX_FACTORY_REGISTER;
    static {
        Map<TokenType, GowerTokenSimilarityIndexFactory<?>[]> tdsRegister = new HashMap<>();
        tdsRegister.put(TokenType.INTEGER, new GowerTokenSimilarityIndexFactory<?>[] {
            //new NormalizedIntegerEuclidSimilarityIndex.Factory(),
            new NormalizedIntegerManhattanSimilarityIndex.Factory(),
        });
        tdsRegister.put(TokenType.FLOAT, new GowerTokenSimilarityIndexFactory<?>[] {
            //new NormalizedFloatEuclidSimilarityIndex.Factory(),
            new NormalizedFloatManhattanSimilarityIndex.Factory(),
        });
        tdsRegister.put(TokenType.BINARY, new GowerTokenSimilarityIndexFactory<?>[] {
            new JaccardBinarySimilarityIndex.Factory(),
            new OverlapTokenSimilarityIndex.Factory(),
        });
        tdsRegister.put(TokenType.BINARY_DIGITAL, new GowerTokenSimilarityIndexFactory<?>[] {
            new JaccardBinarySimilarityIndex.Factory(),
            new OverlapTokenSimilarityIndex.Factory(),
        });
        tdsRegister.put(TokenType.CATEGORICAL_STRING, new GowerTokenSimilarityIndexFactory<?>[] {
            new OverlapTokenSimilarityIndex.Factory(),
            new EskinTokenSimilarityIndex.Factory(),
            new Goodall1TokenSimilarityIndex.Factory(),
            new Goodall2TokenSimilarityIndex.Factory(),
            new Goodall3TokenSimilarityIndex.Factory(),
            new Goodall4TokenSimilarityIndex.Factory(),
            new InverseOccurrenceFrequencyTokenSimilarityIndex.Factory(),
            new OccurrenceFrequencyTokenSimilarityIndex.Factory(),
        });
        GOWER_TOKEN_SIMILARITY_INDEX_FACTORY_REGISTER = Collections.unmodifiableMap(tdsRegister);
    }

}
