package integration;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;

/**
 * Created by XiaoxiWang on 7/5/14.
 */
public class PiecewiseStochasticPolynomialFunction {
    private StochasticPolynomialFunction[] stocPolyFuncs;
    private double[] bounds;    // the overall domain is defined on [0, +inf)
    private int pieces;
    private double[] domains = new double[2];

    public PiecewiseStochasticPolynomialFunction(StochasticPolynomialFunction[] stocPolyFuncs, double[] bounds,
                                                 double leftDomain, double rightDomain) {
        if (stocPolyFuncs.length != bounds.length) {
            throw new DimensionMismatchException(bounds.length, stocPolyFuncs.length);
        }
        if (bounds.length > 1) {
            for (int i = 1; i < bounds.length; i++) {
                if (bounds[i] < bounds[i - 1]) throw new IllegalArgumentException();
            }
        }
        this.stocPolyFuncs = stocPolyFuncs;
        this.bounds = bounds;
        this.pieces = stocPolyFuncs.length;
        if (leftDomain != bounds[0] || rightDomain < bounds[bounds.length - 1]) {
            throw new MathIllegalArgumentException(LocalizedFormats.ARGUMENT_OUTSIDE_DOMAIN, bounds);
        }
        domains[0] = leftDomain;
        domains[1] = rightDomain;
    }

    public StochasticPolynomialFunction getStochasticPolynomialFunction(
            int piece) {
        return stocPolyFuncs[piece];
    }

    public double[] getBounds() {
        return this.bounds;
    }

//    public PiecewiseStochasticPolynomialFunction compose(PiecewiseStochasticPolynomialFunction pwStocPolyFunc) {
//
//        return null;
//    }

    public double[][] getDeterminedExtremaOnEachPieceFiniteDomain() {
        // we only consider g(t) within a finite domain...
        double[][] extrema = new double[pieces][2];
        for (int i = 0; i < pieces - 1; ++i) {
            extrema[i] = stocPolyFuncs[i].getExtremaOnDeterminedPart(bounds[i], bounds[i + 1]);
        }
        extrema[pieces - 1] = stocPolyFuncs[pieces - 1].getExtremaOnDeterminedPart(bounds[pieces - 1], domains[1]);
        return extrema;
    }

    public double[][] getStochasticRangeOnEachPieceFiniteDomain() {
        double[][] ranges = new double[pieces][2];
        for (int i = 0; i < pieces - 1; ++i) {
            ranges[i] = stocPolyFuncs[i].getStochasticRange(bounds[i], bounds[i + 1]);
        }
        ranges[pieces - 1] = stocPolyFuncs[pieces - 1].getStochasticRange(bounds[pieces - 1], domains[1]);
        return ranges;
    }

    public int getPieceNum() {
        return this.pieces;
    }
}
