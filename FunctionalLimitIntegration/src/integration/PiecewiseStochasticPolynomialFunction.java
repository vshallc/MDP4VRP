package integration;

import org.apache.commons.math3.exception.DimensionMismatchException;

/**
 * Created by XiaoxiWang on 7/5/14.
 */
public class PiecewiseStochasticPolynomialFunction {
    StochasticPolynomialFunction[] stocPolyFuncs;
    double[] bounds;    // the overall domain is defined on [0, +inf)

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
}
