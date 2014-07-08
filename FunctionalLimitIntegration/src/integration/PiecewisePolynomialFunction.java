package integration;

import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;

/**
 * Created by Xiaoxi Wang on 7/9/14.
 */
public class PiecewisePolynomialFunction {
    PolynomialFunction[] polyFuncs;
    double[] bounds;    // the overall domain is defined on [0, +inf)

    public PiecewisePolynomialFunction(PolynomialFunction[] polyFuncs, double[] bounds) {
        this.polyFuncs = polyFuncs;
        this.bounds = bounds;
    }

    public PolynomialFunction[] getPolynomialFunctions() {
        return this.polyFuncs;
    }

    public double[] getBounds() {
        return this.bounds;
    }

    public PiecewiseStochasticPolynomialFunction compose(PiecewiseStochasticPolynomialFunction pwStocPolyFunc) {
        return null;
    }
}
