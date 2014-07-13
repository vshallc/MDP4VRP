package integration;

import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.util.FastMath;

import java.util.Arrays;

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

    public StochasticPolynomialFunction compose(StochasticPolynomialFunction spf) {
        double[] nil = new double[1];
        nil[0] = 0;
        AdvancedPolynomialFunction nilPolyFunc = new AdvancedPolynomialFunction(nil);

//        AdvancedPolynomialFunction[] resultPolyFuncCoefs = new AdvancedPolynomialFunction[1];
//        resultPolyFuncCoefs[0] = nilPolyFunc;
//        StochasticPolynomialFunction result = new StochasticPolynomialFunction(resultPolyFuncCoefs);

        AdvancedPolynomialFunction[] newPolyFuncCoefs = new AdvancedPolynomialFunction[this.degree() * spf.degree() + 1];
//        System.out.println("max degree of the result: " + newPolyFuncCoefs.length);
        Arrays.fill(newPolyFuncCoefs, nilPolyFunc);
        for (int i = 0; i <= this.degree(); ++i) {
//            System.out.println("i=" + i);
            AdvancedPolynomialFunction[] tmpPolyFuncCoefs = new AdvancedPolynomialFunction[i * spf.degree() + 1];
            double[] tmpcoefs = new double[1];
            tmpcoefs[0] = this.getCoefficients()[i];
            for (int j = 0; j < tmpPolyFuncCoefs.length;++j) {
                tmpPolyFuncCoefs[j] = new AdvancedPolynomialFunction(tmpcoefs);
            }
            int tmpMaxDegree = spf.degree();//System.out.println("spf degree: " + tmpMaxDegree);
            for (int j = 0; j < i; ++j) {
//                for (int k = 0; k < tmpPolyFuncCoefs.length; ++k) {
//                    System.out.println("tmppoly " + k + ":" + tmpPolyFuncCoefs[k].toString());
//                }
                for (int d1 = 0; d1 <= tmpMaxDegree; ++d1) {
//                    System.out.println("tmpPolyFuncCoefs's length:" + tmpPolyFuncCoefs.length);
                    for (int d2 = 0; d2 <= spf.degree(); ++d2) {
//                        System.out.println("d1=" + d1 + " d2=" + d2);
//                        System.out.println("tmpPolyFunc" + d1 * d2 + ": " + tmpPolyFuncCoefs[d1 * d2].toString());
                        tmpPolyFuncCoefs[d1 * d2] = tmpPolyFuncCoefs[d1].multiply(
                                spf.getAdvancedPolynomialFunctionCoefficients()[d2]);
//                        System.out.println("tmpPolyFunc" + d1 * d2 + ": " + tmpPolyFuncCoefs[d1 * d2].toString());
                    }
                }
                tmpMaxDegree += spf.degree();
            }
            for (int j = 0; j < tmpPolyFuncCoefs.length; ++j) {
                newPolyFuncCoefs[j] = newPolyFuncCoefs[j].add(tmpPolyFuncCoefs[j]);
//                System.out.println("newpoly " + j + ":" + newPolyFuncCoefs[j].toString());
            }
        }
        return new StochasticPolynomialFunction(newPolyFuncCoefs);
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
