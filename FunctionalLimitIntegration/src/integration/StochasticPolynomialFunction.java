package integration;

import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;

import java.util.Random;

public class StochasticPolynomialFunction{
    PolynomialFunction polyfun;
    double sc; // coefficients for the stochastic parameter xi, xi ~ U[0,1]
    Random random = new Random();
    StochasticPolynomialFunction(double[] c, double sc) {
        polyfun = new PolynomialFunction(c);
        this.sc = sc;
    }

    public PolynomialFunction getDeterminedPart() {
        return polyfun;
    }

    public double getXiCoefficient() {
        return sc;
    }

    public StochasticPolynomialFunction add(final StochasticPolynomialFunction spf) {
        PolynomialFunction p = polyfun.add(spf.getDeterminedPart());
        return new StochasticPolynomialFunction(p.getCoefficients(), this.sc + spf.getXiCoefficient());
    }

    public StochasticPolynomialFunction subtract(final StochasticPolynomialFunction spf) {
        PolynomialFunction p = polyfun.subtract(spf.getDeterminedPart());
        return new StochasticPolynomialFunction(p.getCoefficients(), this.sc - spf.getXiCoefficient());
    }

    public double randomValue(double x) {
        return polyfun.value(x) + sc * this.getRandomXi();
    }

    private double getRandomXi() {
        return random.nextDouble();
    }

}
