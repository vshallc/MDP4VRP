package functional;

import org.apache.commons.math3.exception.DimensionMismatchException;

/**
 * Created by XiaoxiWang on 7/5/14.
 */
public class PiecewiseStochasticPolynomialFunction {
    private StochasticPolynomialFunction[] stocPolyFuncs;
    private double[] bounds;    // the overall domain is defined on [0, +inf)
    private int pieces;
//    private double[] domains = new double[2];

    public PiecewiseStochasticPolynomialFunction(StochasticPolynomialFunction[] stocPolyFuncs, double[] bounds) {
        if (stocPolyFuncs.length != bounds.length - 1) {
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
//        if (leftDomain != bounds[0] || rightDomain < bounds[bounds.length - 1]) {
//            throw new MathIllegalArgumentException(LocalizedFormats.ARGUMENT_OUTSIDE_DOMAIN, bounds);
//        }
//        domains[0] = leftDomain;
//        domains[1] = rightDomain;
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

    public double[][] getDeterminedExtremaOnEachPiece() {
        // we only consider g(t) within a finite domain...
        double[][] extrema = new double[pieces][2];
        for (int i = 0; i < pieces; ++i) {
            extrema[i] = stocPolyFuncs[i].getDeterminedPart().extrema(bounds[i], bounds[i + 1]);
        }
        return extrema;
    }

    public double[][] getStochasticRangeOnEachPiece() {
        double[][] ranges = new double[pieces][2];
        for (int i = 0; i < pieces; ++i) {
            int degree = stocPolyFuncs[i].degree();
            if (degree == 0) {
                ranges[i][0] = 0;
                ranges[i][1] = 0;
            } else if (degree == 1) {
                if (stocPolyFuncs[i].getAdvancedPolynomialFunctionCoefficients()[1].degree() > 0) {
                    throw new TooHighDegreeException(degree);
                } else {
                    ranges[i][0] = 0;
                    ranges[i][1] = stocPolyFuncs[i].getAdvancedPolynomialFunctionCoefficients()[1].extrema(bounds[i], bounds[i + 1])[1];
                }
            } else {
                throw new TooHighDegreeException(degree);
            }
//            ranges[i] = stocPolyFuncs[i].getStochasticRange(bounds[i], bounds[i + 1]);
        }
//        ranges[pieces - 1] = stocPolyFuncs[pieces - 1].getStochasticRange(bounds[pieces - 1], domains[1]);
        return ranges;
    }

    public int getPieceNum() {
        return this.pieces;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < pieces; ++i) {
            s.append("[");
            s.append(bounds[i]);
            s.append(",");
            s.append(bounds[i + 1]);
            s.append(")\t");
            s.append(stocPolyFuncs[i].toString());
            s.append("\n");
        }
        s.deleteCharAt(s.length() - 1);
        return s.toString();
    }
}
