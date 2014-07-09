package integration;

import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.MathUtils;

import java.util.Random;

public class StochasticPolynomialFunction{
    PolynomialFunction[] polyfunCoef;   // polynomial functions as the coefficients
                                        // for the stochastic parameter xi, xi ~ U[0,1]
//    double sc; // coefficients for the stochastic parameter xi, xi ~ U[0,1]
    Random random = new Random();
    StochasticPolynomialFunction(PolynomialFunction[] polyfunCoef)
            throws NullArgumentException, NoDataException {
        super();
        MathUtils.checkNotNull(polyfunCoef);
        int n = polyfunCoef.length;
        if (n == 0) {
            throw new NoDataException(LocalizedFormats.EMPTY_POLYNOMIALS_COEFFICIENTS_ARRAY);
        }
        while ((n > 1) && (polyfunCoef[n - 1].degree() == 0) && (polyfunCoef[n - 1].getCoefficients()[0] == 0)) {
            --n;
        }
        this.polyfunCoef = new PolynomialFunction[n];
        System.arraycopy(polyfunCoef, 0, this.polyfunCoef, 0, n);
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
