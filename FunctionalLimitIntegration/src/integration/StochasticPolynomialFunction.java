package integration;

import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathUtils;

import java.util.Random;

public class StochasticPolynomialFunction{
    private final AdvancedPolynomialFunction[] polyfunCoefs;   // polynomial functions as the coefficients
                                        // for the stochastic parameter ξ, ξ ~ U[0,1]
//    double sc; // coefficients for the stochastic parameter ξ, ξ ~ U[0,1]
    private final Random random = new Random();
    StochasticPolynomialFunction(AdvancedPolynomialFunction[] polyfunCoefs)
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
        this.polyfunCoefs = new AdvancedPolynomialFunction[n];
//        System.arraycopy(polyfunCoefs, 0, this.polyfunCoefs, 0, n);   // the coefficients of AdvancedPolynomialFunction Class
//                                                                    // is set as final, so clone is unnecessary.
        for (int i = 0; i < n; i++) {
            this.polyfunCoefs[i] = new AdvancedPolynomialFunction(polyfunCoefs[i].getCoefficients());
        }
    }

    public int degree() {
        return polyfunCoefs.length - 1;
    }

    public AdvancedPolynomialFunction[] getAdvancedPolynomialFunctionCoefficients() {
        return polyfunCoefs.clone();
    }

    public AdvancedPolynomialFunction getDeterminedPart() {
        return polyfunCoefs[0];
    }

    public StochasticPolynomialFunction add(final StochasticPolynomialFunction spf) {
        final int lowLength = FastMath.min(polyfunCoefs.length, spf.getAdvancedPolynomialFunctionCoefficients().length);
        final int highLength = FastMath.max(polyfunCoefs.length, spf.getAdvancedPolynomialFunctionCoefficients().length);

        AdvancedPolynomialFunction[] newPolyFuncCoefs = new AdvancedPolynomialFunction[highLength];
        for (int i = 0; i < lowLength; ++i) {
            newPolyFuncCoefs[i] = polyfunCoefs[i].add(spf.getAdvancedPolynomialFunctionCoefficients()[i]);
        }
//        System.arraycopy((polyfunCoefs.length < spf.getAdvancedPolynomialFunctionCoefficients().length) ?
//                spf.getAdvancedPolynomialFunctionCoefficients() : polyfunCoefs,
//                lowLength, newPolyFuncCoefs, lowLength,
//                highLength - lowLength);
        if (polyfunCoefs.length < spf.getAdvancedPolynomialFunctionCoefficients().length) {
            for (int i = lowLength; i < highLength; ++i) {
                newPolyFuncCoefs[i] = new AdvancedPolynomialFunction(spf.getAdvancedPolynomialFunctionCoefficients()[i].getCoefficients());
            }
        } else {
            for (int i = lowLength; i < highLength; ++i) {
                newPolyFuncCoefs[i] = new AdvancedPolynomialFunction(polyfunCoefs[i].getCoefficients());
            }
        }

        return new StochasticPolynomialFunction(newPolyFuncCoefs);
    }

    public StochasticPolynomialFunction subtract(final StochasticPolynomialFunction spf) {
        final int lowLength = FastMath.min(polyfunCoefs.length, spf.getAdvancedPolynomialFunctionCoefficients().length);
        final int highLength = FastMath.max(polyfunCoefs.length, spf.getAdvancedPolynomialFunctionCoefficients().length);

        AdvancedPolynomialFunction[] newPolyFuncCoefs = new AdvancedPolynomialFunction[highLength];
        for (int i = 0; i < lowLength; ++i) {
            newPolyFuncCoefs[i] = polyfunCoefs[i].subtract(spf.getAdvancedPolynomialFunctionCoefficients()[i]);
        }
        if (polyfunCoefs.length < spf.getAdvancedPolynomialFunctionCoefficients().length) {
            for (int i = lowLength; i < highLength; ++i) {
                double[] pfcs = spf.getAdvancedPolynomialFunctionCoefficients()[i].getCoefficients();
                for (int j = 0; j < pfcs.length; ++j) {
                    pfcs[i] = -pfcs[j];
                }
                newPolyFuncCoefs[i] = new AdvancedPolynomialFunction(pfcs);
            }
        } else {
            for (int i = lowLength; i < highLength; ++i) {
                newPolyFuncCoefs[i] = new AdvancedPolynomialFunction(polyfunCoefs[i].getCoefficients());
            }
        }

        return new StochasticPolynomialFunction(newPolyFuncCoefs);
    }

    public StochasticPolynomialFunction negate() {
        AdvancedPolynomialFunction[] newPolyFuncCoefs = new AdvancedPolynomialFunction[polyfunCoefs.length];
        for (int i = 0; i < polyfunCoefs.length; ++i) {
            double[] pfcs = polyfunCoefs[i].getCoefficients();
            for (int j = 0; j < pfcs.length; ++j) {
                pfcs[i] = -pfcs[j];
            }
            newPolyFuncCoefs[i] = new AdvancedPolynomialFunction(pfcs);
        }
        return new StochasticPolynomialFunction(newPolyFuncCoefs);
    }

    public StochasticPolynomialFunction add(final AdvancedPolynomialFunction pf) {
        AdvancedPolynomialFunction[] newPolyFuncCoefs = new AdvancedPolynomialFunction[polyfunCoefs.length];
        newPolyFuncCoefs[0] = polyfunCoefs[0].add(pf);
        for (int i = 1; i < polyfunCoefs.length; ++i) {
            double[] pfcs = polyfunCoefs[i].getCoefficients();
            for (int j = 0; j < pfcs.length; ++j) {
                pfcs[i] = pfcs[j];
            }
            newPolyFuncCoefs[i] = new AdvancedPolynomialFunction(pfcs);
        }
        return new StochasticPolynomialFunction(newPolyFuncCoefs);
    }

    public StochasticPolynomialFunction subtract(final AdvancedPolynomialFunction pf) {
        AdvancedPolynomialFunction[] newPolyFuncCoefs = new AdvancedPolynomialFunction[polyfunCoefs.length];
        newPolyFuncCoefs[0] = polyfunCoefs[0].subtract(pf);
        for (int i = 1; i < polyfunCoefs.length; ++i) {
            double[] pfcs = polyfunCoefs[i].getCoefficients();
            for (int j = 0; j < pfcs.length; ++j) {
                pfcs[i] = pfcs[j];
            }
            newPolyFuncCoefs[i] = new AdvancedPolynomialFunction(pfcs);
        }
        return new StochasticPolynomialFunction(newPolyFuncCoefs);
    }

    public double stochasticValue(double x) {
        double xi = random.nextDouble();
        double result = 0.0;
        for (int i = 0; i < polyfunCoefs.length; ++i) {
            double value = polyfunCoefs[i].value(x);
            for (int j = 0; j < i; ++j) {
                value *= xi;
            }
            result += value;
        }
        return result;
    }

    public double determinedValue(double x, double xi) {
        double result = 0.0;
        for (int i = 0; i < polyfunCoefs.length; ++i) {
            double value = polyfunCoefs[i].value(x);
            for (int j = 0; j < i; ++j) {
                value *= xi;
            }
            result += value;
        }
        return result;
    }

    public AdvancedPolynomialFunction determinize(double xi) {
        AdvancedPolynomialFunction determinizedResult = new AdvancedPolynomialFunction(polyfunCoefs[0].getCoefficients());
        for (int i = 1; i < polyfunCoefs.length; ++i) {
            double pow = FastMath.pow(xi, i);
            double[] tmpcoefs = polyfunCoefs[i].getCoefficients();
            for (int j = 0; j < tmpcoefs.length; ++j) {
                tmpcoefs[j] *= pow;
            }
            AdvancedPolynomialFunction tmpapf = new AdvancedPolynomialFunction(tmpcoefs);
            determinizedResult = determinizedResult.add(tmpapf);
        }
        return determinizedResult;
    }

    public StochasticPolynomialFunction integrationOnXi() {
        AdvancedPolynomialFunction[] intCoefs = new AdvancedPolynomialFunction[polyfunCoefs.length + 1];
        for (int i = 1; i <= polyfunCoefs.length; ++i) {
            double[] coefsOfCoef = polyfunCoefs[i - 1].getCoefficients();
            for (int j=0;j<coefsOfCoef.length;++j) {
                coefsOfCoef[j] /= i;
            }
            intCoefs[i] = new AdvancedPolynomialFunction(coefsOfCoef);
        }
        intCoefs[0] = AdvancedPolynomialFunction.ZERO();
        return new StochasticPolynomialFunction(intCoefs);
    }

    public double[] getExtremaOnDeterminedPart(double leftBound, double rightBound) {
        return polyfunCoefs[0].extrema(leftBound, rightBound);
    }

    public double[] getStochasticRange(double leftBound, double rightBound) {
        int deg = this.degree();
        if (deg > 1) {
            throw new TooHighDegreeException(deg);
        }
        if (deg == 0) {
            double[] result = {0, 0};
            return result;
        }
        if (polyfunCoefs[1].degree() > 0) {
            throw new TooHighDegreeException(polyfunCoefs[1].degree());
        }
        double[] result = new double[2];
        result[0] = 0;
        result[1] = polyfunCoefs[1].extrema(leftBound, rightBound)[1];
        return result;
    }

    private double getRandomXi() {
        return random.nextDouble();
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        if (polyfunCoefs[0].equalsZero()) {
            if (polyfunCoefs.length == 1) return "0";
        } else {
            s.append(polyfunCoefs[0].toString());
        }
        for (int i = 1; i < polyfunCoefs.length; ++i) {
            if (!polyfunCoefs[i].equalsZero()) {
                if (s.length() > 0) {
                    s.append(" + ");
                }
                if (!polyfunCoefs[i].equalsOne()) {
                    if (polyfunCoefs[i].degree() == 0) {
                        s.append(polyfunCoefs[i].toString());
                    } else {
                        s.append("(");
                        s.append(polyfunCoefs[i].toString());
                        s.append(")");
                    }
                }
                s.append(" ");
                s.append("ξ");
                if (i > 1) {
                    s.append("^");
                    s.append(Integer.toString(i));
                }
            }
        }
        return s.toString();
    }
}
