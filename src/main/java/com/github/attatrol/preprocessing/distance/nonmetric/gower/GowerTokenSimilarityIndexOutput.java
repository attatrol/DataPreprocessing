package com.github.attatrol.preprocessing.distance.nonmetric.gower;

/**
 * POJO, output of some token similarity index, used in calculation of {@link GowerDistance}.
 * 
 * @author atta_troll
 *
 */
public final class GowerTokenSimilarityIndexOutput {

    /**
     * Summand for numerator part of generalized Gower's similarity index.
     * S[i] in Gower's distance terminology.
     */
    private final double numeratorSummand;

    /**
     * Summand for denominator part of generalized Gower's similarity index.
     * w[i] in Gower's distance terminology.
     */
    private final double denominatorSummand;

    /**
     * Default ctor.
     * @param numeratorSummand summand for numerator part of generalized Gower's similarity index
     * @param denominatorSummand summand for denominator part of generalized Gower's similarity index
     */
    public GowerTokenSimilarityIndexOutput(double numeratorSummand, double denominatorSummand) {
        this.numeratorSummand = numeratorSummand;
        this.denominatorSummand = denominatorSummand;
    }

    /**
     * @return summand for numerator part of generalized Gower's similarity index
     */
    public double getNumeratorSummand() {
        return numeratorSummand;
    }

    /**
     * @return summand for denominator part of generalized Gower's similarity index
     */
    public double getDenominatorSummand() {
        return denominatorSummand;
    }

}
