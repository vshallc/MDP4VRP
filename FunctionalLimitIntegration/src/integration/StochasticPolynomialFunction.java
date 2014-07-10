package integration;

import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathUtils;

import java.util.Random;

public class StochasticPolynomialFunction{
    private final PolynomialFunction[] polyfunCoefs;   // polynomial functions as the coefficients
                                        // for the stochastic parameter xi, xi ~ U[0,1]
//    double sc; // coefficients for the stochastic parameter xi, xi ~ U[0,1]
    private final Random random = new Random();
    StochasticPolynomialFunction(PolynomialFunction[] polyfunCoefs)
            throws NullArgumentException, NoDataException {
        super();
        MathUtils.checkNotNull(polyfunCoefs);
        int n = polyfunCoefs.length;
        if (n == 0) {
            throw new NoDataException(LocalizedFormats.EMPTY_POLYNOMIALS_COEFFICIENTS_ARRAY);
        }
        while ((n > 1) && (polyfunCoefs[n - 1].degree() == 0) && (polyfunCoefs[n - 1].getCoefficients()[0] == 0)) {
            --n;
        }
        this.polyfunCoefs = new PolynomialFunction[n];
        System.arraycopy(polyfunCoefs, 0, this.polyfunCoefs, 0, n);   // the coefficients of PolynomialFunction Class
                                                                    // is set as final, so clone is unnecessary.
    }

    public PolynomialFunction[] getPolynomialFunctionCoefficients() {
        return polyfunCoefs.clone();
    }

    public PolynomialFunction getDeterminedPart() {
        return polyfunCoefs[0];
    }

//    public double getXiCoefficient() {
//        return sc;
//    }

    public StochasticPolynomialFunction add(final StochasticPolynomialFunction spf) {
        final int lowLength = FastMath.min(polyfunCoefs.length, spf.getPolynomialFunctionCoefficients().length);
        final int highLength = FastMath.max(polyfunCoefs.length, spf.getPolynomialFunctionCoefficients().length);

        PolynomialFunction[] newPolyFuncCoefs = new PolynomialFunction[highLength];
        for (int i = 0; i < lowLength; i++) {
            newPolyFuncCoefs[i] = polyfunCoefs[i].add(spf.getPolynomialFunctionCoefficients()[i]);
        }
        System.arraycopy((polyfunCoefs.length < spf.getPolynomialFunctionCoefficients().length) ?
                spf.getPolynomialFunctionCoefficients() : polyfunCoefs,
                lowLength, newPolyFuncCoefs, lowLength,
                highLength - lowLength);

        return new StochasticPolynomialFunction(newPolyFuncCoefs);
    }
//
//    public StochasticPolynomialFunction subtract(final StochasticPolynomialFunction spf) {
//        PolynomialFunction p = polyfun.subtract(spf.getDeterminedPart());
//        return new StochasticPolynomialFunction(p.getCoefficients(), this.sc - spf.getXiCoefficient());
//    }
//
    public double randomValue(double x) {
        double xi = random.nextDouble();
        double result = 0.0;
        for (int i = 0; i < polyfunCoefs.length; i++) {
            double value = polyfunCoefs[i].value(x);
            for (int j = 0; j < i; j++) {
                value *= xi;
            }
            result += value;
        }
        return result;
    }

    public double determinedValue(double x, double xi) {
        double result = 0.0;
        for (int i = 0; i < polyfunCoefs.length; i++) {
            double value = polyfunCoefs[i].value(x);
            for (int j = 0; j < i; j++) {
                value *= xi;
            }
            result += value;
        }
        return result;
    }

    private double getRandomXi() {
        return random.nextDouble();
    }

}
