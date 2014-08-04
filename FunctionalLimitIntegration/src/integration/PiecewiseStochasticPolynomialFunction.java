package integration;

import org.apache.commons.math3.exception.DimensionMismatchException;

/**
 * Created by XiaoxiWang on 7/5/14.
 */
public class PiecewiseStochasticPolynomialFunction {
    StochasticPolynomialFunction[] stocPolyFuncs;
    double[] bounds;    // the overall domain is defined on [0, +inf)
    int pieces;

    public PiecewiseStochasticPolynomialFunction(StochasticPolynomialFunction[] stocPolyFuncs, double[] bounds) {
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

    public double[][] getExtremaOnEachPiece() {
        double[][] extrema = new double[pieces][2];
        for (int i = 0; i < pieces; ++i) {
            extrema[i] = stocPolyFuncs[i].
        }
        return extrema;
    }

    public int getPieceNum() {
        return this.pieces;
    }
}
