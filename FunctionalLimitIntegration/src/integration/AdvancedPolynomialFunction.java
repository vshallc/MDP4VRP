package integration;

import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.util.FastMath;

import java.util.Arrays;
import java.util.Map;

/**
 * Created by Xiaoxi Wang on 7/11/14.
 */
public class AdvancedPolynomialFunction extends PolynomialFunction {
    /**
     * Construct a polynomial with the given coefficients.  The first element
     * of the coefficients array is the constant term.  Higher degree
     * coefficients follow in sequence.  The degree of the resulting polynomial
     * is the index of the last non-null element of the array, or 0 if all elements
     * are null.
     * <p>
     * The constructor makes a copy of the input array and assigns the copy to
     * the coefficients property.</p>
     *
     * @param c Polynomial coefficients.
     * @throws org.apache.commons.math3.exception.NullArgumentException if {@code c} is {@code null}.
     * @throws org.apache.commons.math3.exception.NoDataException       if {@code c} is empty.
     */

    private boolean isExtremePointCalculated = false;
    private double[] extremePoints;

    public AdvancedPolynomialFunction(double[] c) throws NullArgumentException, NoDataException {
        super(c);
    }

    public AdvancedPolynomialFunction add(final AdvancedPolynomialFunction apf) {
        final int lowLength  = FastMath.min(this.getCoefficients().length, apf.getCoefficients().length);
        final int highLength = FastMath.max(this.getCoefficients().length, apf.getCoefficients().length);

        double[] newCoefficients = new double[highLength];
        for (int i = 0; i < lowLength; ++i) {
            newCoefficients[i] = this.getCoefficients()[i] + apf.getCoefficients()[i];
        }
        System.arraycopy((this.getCoefficients().length < apf.getCoefficients().length) ?
                        apf.getCoefficients() : this.getCoefficients(),
                lowLength,
                newCoefficients, lowLength,
                highLength - lowLength);

        return new AdvancedPolynomialFunction(newCoefficients);
    }

    public AdvancedPolynomialFunction subtract(final AdvancedPolynomialFunction apf) {
        int lowLength  = FastMath.min(this.getCoefficients().length, apf.getCoefficients().length);
        int highLength = FastMath.max(this.getCoefficients().length, apf.getCoefficients().length);

        // build the coefficients array
        double[] newCoefficients = new double[highLength];
        for (int i = 0; i < lowLength; ++i) {
            newCoefficients[i] = this.getCoefficients()[i] - apf.getCoefficients()[i];
        }
        if (this.getCoefficients().length < apf.getCoefficients().length) {
            for (int i = lowLength; i < highLength; ++i) {
                newCoefficients[i] = -apf.getCoefficients()[i];
            }
        } else {
            System.arraycopy(this.getCoefficients(), lowLength, newCoefficients, lowLength,
                    highLength - lowLength);
        }

        return new AdvancedPolynomialFunction(newCoefficients);
    }

    public AdvancedPolynomialFunction negate() {
        double[] newCoefficients = new double[this.getCoefficients().length];
        for (int i = 0; i < this.getCoefficients().length; ++i) {
            newCoefficients[i] = -this.getCoefficients()[i];
        }
        return new AdvancedPolynomialFunction(newCoefficients);
    }

    public AdvancedPolynomialFunction multiply(final AdvancedPolynomialFunction apf) {
        double[] newCoefficients = new double[this.getCoefficients().length + apf.getCoefficients().length - 1];

        for (int i = 0; i < newCoefficients.length; ++i) {
            newCoefficients[i] = 0.0;
            for (int j = FastMath.max(0, i + 1 - apf.getCoefficients().length);
                 j < FastMath.min(this.getCoefficients().length, i + 1);
                 ++j) {
                newCoefficients[i] += this.getCoefficients()[j] * apf.getCoefficients()[i-j];
            }
        }

        return new AdvancedPolynomialFunction(newCoefficients);
    }

    public AdvancedPolynomialFunction polynomialDerivative() {
        return new AdvancedPolynomialFunction(differentiate(this.getCoefficients()));
    }

    public double[] calculateExtremePoint() {
        double[] differentiatedCoefs = differentiate(this.getCoefficients());
        if (differentiatedCoefs.length == 1) {
            extremePoints = new double[0];
            isExtremePointCalculated = true;
            return extremePoints;
        }
        int deg = differentiatedCoefs.length - 1;
        extremePoints = new double[deg];
        if (deg == 1) {
            extremePoints[0] = -differentiatedCoefs[0] / differentiatedCoefs[1];
        }
        else if (deg == 2) {
            double delta = differentiatedCoefs[1] * differentiatedCoefs[1]
                    - 4 * differentiatedCoefs[2] * differentiatedCoefs[0];
            if (delta > 0) {
                extremePoints[0] = (-differentiatedCoefs[1] - Math.sqrt(delta)) / (2 * differentiatedCoefs[2]);
                extremePoints[1] = (-differentiatedCoefs[1] + Math.sqrt(delta)) / (2 * differentiatedCoefs[2]);
            } else if (delta == 0) {
                extremePoints = new double[1];
                extremePoints[0] = -differentiatedCoefs[1] / (2 * differentiatedCoefs[2]);
            } else if (delta < 0) {
                extremePoints = new double[0];
                isExtremePointCalculated = true;
                return extremePoints;
            }
        } else if (deg == 3) {
            //
        } else {
            // throw exception
        }
        isExtremePointCalculated = true;
        return extremePoints;
    }

    public double[] extremeValue(double leftBound, double rightBound) {
        double[] ext = new double[2];
        double[] differentiatedCoefs = differentiate(this.getCoefficients());
        return ext;
    }

    public StochasticPolynomialFunction compose(StochasticPolynomialFunction spf) {
        double[] nil = new double[1];
        nil[0] = 0;
        AdvancedPolynomialFunction nilPolyFunc = new AdvancedPolynomialFunction(nil);

        AdvancedPolynomialFunction[] resPolyFuncCoefs = new AdvancedPolynomialFunction[this.degree() * spf.degree() + 1];
        Arrays.fill(resPolyFuncCoefs, nilPolyFunc);
        {
            double[] tmpCoefs = new double[1];
            tmpCoefs[0] = this.getCoefficients()[0];
            resPolyFuncCoefs[0] = new AdvancedPolynomialFunction(tmpCoefs);
        }
        for (int i = 1; i <= this.degree(); ++i) {
            System.out.println("i=" + i);
            AdvancedPolynomialFunction[] multiplierPolyFuncCoefs = new AdvancedPolynomialFunction[1];
            double[] tmpCoefs = new double[1];
            tmpCoefs[0] = this.getCoefficients()[i];
            multiplierPolyFuncCoefs[0] = new AdvancedPolynomialFunction(tmpCoefs);
            System.out.println("Multiplier: " + multiplierPolyFuncCoefs[0].toString());
            AdvancedPolynomialFunction[] tmpPolyFuncCoefs = new AdvancedPolynomialFunction[spf.degree() + 1];
            Arrays.fill(tmpPolyFuncCoefs, nilPolyFunc);
            for (int d2 = 0; d2 <= spf.degree(); ++d2) {
                tmpPolyFuncCoefs[d2] = multiplierPolyFuncCoefs[0].multiply(
                        spf.getAdvancedPolynomialFunctionCoefficients()[d2]);
            }
//            for (int j = 0; j < tmpPolyFuncCoefs.length; ++j) {
//                System.out.println("tmpPolyFunc" + j + ": " + tmpPolyFuncCoefs[j].toString());
//            }
            for (int j = 1; j < i; ++j) {
                multiplierPolyFuncCoefs = tmpPolyFuncCoefs;
                tmpPolyFuncCoefs = new AdvancedPolynomialFunction[(j + 1) * spf.degree() + 1];
                Arrays.fill(tmpPolyFuncCoefs, nilPolyFunc);
                for (int d1 = 0; d1 < multiplierPolyFuncCoefs.length; ++d1) {
//                    System.out.println("tmpPolyFuncCoefs's length:" + tmpPolyFuncCoefs.length);
                    for (int d2 = 0; d2 <= spf.degree(); ++d2) {
                        System.out.println("d1=" + d1 + " d2=" + d2);
                        System.out.println("tmpPolyFunc" + (d1 + d2) + ": " + tmpPolyFuncCoefs[d1 + d2].toString());
                        tmpPolyFuncCoefs[d1 + d2] = multiplierPolyFuncCoefs[d1].multiply(
                                spf.getAdvancedPolynomialFunctionCoefficients()[d2]);
                        System.out.println("tmpPolyFunc" + (d1 + d2) + ": " + tmpPolyFuncCoefs[d1 + d2].toString());
                    }
                }
            }
            for (int j = 0; j < tmpPolyFuncCoefs.length; ++j) {
                resPolyFuncCoefs[j] = resPolyFuncCoefs[j].add(tmpPolyFuncCoefs[j]);
//                System.out.println("newpoly " + j + ":" + resPolyFuncCoefs[j].toString());
            }
        }
        return new StochasticPolynomialFunction(resPolyFuncCoefs);
    }

    public boolean equalsZero() {
        if (this.getCoefficients().length == 1 && this.getCoefficients()[0] == 0) return true;
        else return false;
    }

    public boolean equalsOne() {
        if (this.getCoefficients().length == 1 && this.getCoefficients()[0] == 1) return true;
        else return false;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(this.getCoefficients()) + this.getClass().hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof AdvancedPolynomialFunction)) {
            return false;
        }
        AdvancedPolynomialFunction other = (AdvancedPolynomialFunction) obj;
        if (!Arrays.equals(this.getCoefficients(), other.getCoefficients())) {
            return false;
        }
        return true;
    }
}
