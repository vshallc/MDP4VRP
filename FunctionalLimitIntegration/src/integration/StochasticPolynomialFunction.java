package integration;

import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;

import java.util.Random;

public class StochasticPolynomialFunction{
    PolynomialFunction[] polyfunCoef;   // polynomial functions as the coefficients
                                        // for the stochastic parameter xi, xi ~ U[0,1]
//    double sc; // coefficients for the stochastic parameter xi, xi ~ U[0,1]
    Random random = new Random();
    StochasticPolynomialFunction(PolynomialFunction[] polyfunCoef) {
        this.polyfunCoef = polyfunCoef;
    }

    public PolynomialFunction getDeterminedPart() {
        return polyfunCoef[0];
    }

//    public double getXiCoefficient() {
//        return sc;
//    }

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
