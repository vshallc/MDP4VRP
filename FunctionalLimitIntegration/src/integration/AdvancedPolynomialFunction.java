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
        return null;
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
