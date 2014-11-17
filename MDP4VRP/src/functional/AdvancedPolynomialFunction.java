package functional;

import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
import org.apache.commons.math3.analysis.solvers.NewtonRaphsonSolver;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.TooManyEvaluationsException;
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

    private static final double DEFAULT_ABSOLUTE_ACCURACY = 1e-8;
    private static final int DEFAULT_MAX_EVAL = 100;

//    private boolean isExtremePointCalculated = false;
//    private double[] extremePoints;

    public AdvancedPolynomialFunction(double[] c) throws NullArgumentException, NoDataException {
        super(c[0] == Double.POSITIVE_INFINITY ? new double[]{Double.POSITIVE_INFINITY} : c[0] == Double.NEGATIVE_INFINITY ? new double[]{Double.NEGATIVE_INFINITY} : c);
    }

    public AdvancedPolynomialFunction add(final AdvancedPolynomialFunction apf) {
        double[] c = this.getCoefficients();
        double[] apfc = apf.getCoefficients();
        final int lowLength  = FastMath.min(c.length, apfc.length);
        final int highLength = FastMath.max(c.length, apfc.length);

        double[] newCoefficients = new double[highLength];
        for (int i = 0; i < lowLength; ++i) {
            newCoefficients[i] = c[i] + apfc[i];
        }
        System.arraycopy((c.length < apfc.length) ? apfc : c,
                lowLength,
                newCoefficients, lowLength,
                highLength - lowLength);

        return new AdvancedPolynomialFunction(newCoefficients);
    }

    public AdvancedPolynomialFunction add(final double c) {
        double[] newCoefficients = this.getCoefficients();
        newCoefficients[0] += c;
        return new AdvancedPolynomialFunction(newCoefficients);
    }

    public AdvancedPolynomialFunction subtract(final AdvancedPolynomialFunction apf) {
        double[] c = this.getCoefficients();
        double[] apfc = apf.getCoefficients();
        int lowLength  = FastMath.min(c.length, apfc.length);
        int highLength = FastMath.max(c.length, apfc.length);

        // build the coefficients array
        double[] newCoefficients = new double[highLength];
        for (int i = 0; i < lowLength; ++i) {
            newCoefficients[i] = c[i] - apfc[i];
        }
        if (c.length < apfc.length) {
            for (int i = lowLength; i < highLength; ++i) {
                newCoefficients[i] = -apfc[i];
            }
        } else {
            System.arraycopy(c, lowLength, newCoefficients, lowLength,
                    highLength - lowLength);
        }

        return new AdvancedPolynomialFunction(newCoefficients);
    }

    public AdvancedPolynomialFunction subtract(final double c) {
        double[] newCoefficients = this.getCoefficients();
        newCoefficients[0] -= c;
        return new AdvancedPolynomialFunction(newCoefficients);
    }

    public AdvancedPolynomialFunction negate() {
        double[] newCoefficients = this.getCoefficients();
        for (int i = 0; i < newCoefficients.length; ++i) {
            newCoefficients[i] = -newCoefficients[i];
        }
        return new AdvancedPolynomialFunction(newCoefficients);
    }

    public AdvancedPolynomialFunction multiply(final AdvancedPolynomialFunction apf) {
        double[] thisCoefficients = this.getCoefficients();
        double[] newCoefficients = new double[thisCoefficients.length + apf.getCoefficients().length - 1];
        for (int i = 0; i < newCoefficients.length; ++i) {
            newCoefficients[i] = 0.0;
            for (int j = FastMath.max(0, i + 1 - apf.getCoefficients().length);
                 j < FastMath.min(thisCoefficients.length, i + 1);
                 ++j) {
                newCoefficients[i] += thisCoefficients[j] * apf.getCoefficients()[i-j];
            }
        }
        return new AdvancedPolynomialFunction(newCoefficients);
    }

    public AdvancedPolynomialFunction multiply(final double c) {
        double[] newCoefficients = this.getCoefficients();
        for (int i = 0; i < newCoefficients.length; ++i) {
            newCoefficients[i] *= c;
        }
        return new AdvancedPolynomialFunction(newCoefficients);
    }

    public AdvancedPolynomialFunction shift(final double t) {
        // p(t) -> p(t + t')
        double[] c = this.getCoefficients();
        double[] newCoefficients = new double[c.length];
        Arrays.fill(newCoefficients, 0);
        newCoefficients[0] = c[0];
        for (int i = 1; i < c.length; ++i) {
            double[] p = new double[i + 1];
            Arrays.fill(p, 0);
            p[0] = 1;
            for (int j = 0; j < i; ++j) {
                for (int k = i; k > 0; --k) {
                    p[k] += p[k - 1];
                    p[k - 1] = p[k - 1] * t;
                }
            }
            for (int j = 0; j < i + 1; ++j) {
                newCoefficients[j] += p[j] * c[i];
            }
        }
        return new AdvancedPolynomialFunction(newCoefficients);
    }

    public AdvancedPolynomialFunction integrate() {
        double[] c = this.getCoefficients();
        double[] newCoefficients = new double[c.length + 1];
        newCoefficients[0] = 0;
        System.arraycopy(c, 0, newCoefficients, 1, c.length);
        for (int i = 1; i < newCoefficients.length; ++i) {
            newCoefficients[i] /= i;
        }
        return new AdvancedPolynomialFunction(newCoefficients);
    }

    public AdvancedPolynomialFunction polynomialDerivative() {
        return new AdvancedPolynomialFunction(differentiate(this.getCoefficients()));
    }

    public double[] extrema(double leftBound, double rightBound) {
        int degree = this.degree();
        double[] coefs = this.getCoefficients();
        if (degree == 0) {
            double c0 = coefs[0];
            return new double[]{c0, c0};
        } else if (degree == 1) {
            if (coefs[1] > 0) {
                return new double[]{this.value(leftBound), this.value(rightBound)};
            } else {
                return new double[]{this.value(rightBound), this.value(leftBound)};
            }
        } else if (degree == 2) {
            double extPoint = -coefs[1] / (2 * coefs[2]);
            if (extPoint < leftBound) {
                if (coefs[2] > 0) {
                    return new double[]{this.value(leftBound), this.value(rightBound)};
                } else {
                    return new double[]{this.value(rightBound), this.value(leftBound)};
                }
            } else if (extPoint > rightBound) {
                if (coefs[2] > 0) {
                    return new double[]{this.value(rightBound), this.value(leftBound)};
                } else {
                    return new double[]{this.value(leftBound), this.value(rightBound)};
                }
            } else {
                if (coefs[2] > 0) {
                    return new double[]{this.value(extPoint), Math.max(this.value(leftBound), this.value(rightBound))};
                } else {
                    return new double[]{Math.min(this.value(leftBound), this.value(rightBound)), this.value(extPoint)};
                }
            }
        } else {
            AdvancedPolynomialFunction derApf = new AdvancedPolynomialFunction(differentiate(coefs));
            double[] extPoints = derApf.solve(leftBound, rightBound);
            double[] extrema = new double[2];
            {
                double leftValue = this.value(leftBound);
                double rightValue = this.value(rightBound);
                if (leftValue < rightValue) {
                    extrema[0] = leftValue;
                    extrema[1] = rightValue;
                } else {
                    extrema[0] = rightValue;
                    extrema[1] = leftValue;
                }
            }
            int i = 0;
            while(i < extPoints.length && extPoints[i] < leftBound) ++i;
            for (; i < extPoints.length; ++i) {
                if (extPoints[i] > rightBound) break;
                double v = this.value(extPoints[i]);
                if (v < extrema[0]) extrema[0] = v;
                if (v > extrema[1]) extrema[1] = v;
            }
            return extrema;
        }
    }

    public double[] extremePoints(double leftBound, double rightBound) {
        int degree = this.degree();
        double[] coefs = this.getCoefficients();
        if (degree == 0 || degree == 1) {
            return new double[]{leftBound, rightBound};
        } else if (degree == 2) {
            double extPoint = -coefs[1] / (2 * coefs[2]);
            if (extPoint < leftBound || extPoint > rightBound) {
                return new double[]{leftBound, rightBound};
            } else {
                return new double[]{leftBound, extPoint, rightBound};
            }
        } else {
            AdvancedPolynomialFunction derApf = new AdvancedPolynomialFunction(differentiate(coefs));
            double[] roots = derApf.solve(leftBound, rightBound);
            int i=0, start, end;
            for (; i < roots.length; ++i) {
                if (roots[i] > leftBound) break;
            }
            start = i;
            for (; i < roots.length; ++i) {
                if (roots[i] > rightBound) break;
            }
            end = i;
            double[] extPoints = new double[end - start + 2];
            extPoints[0] = leftBound;
            extPoints[extPoints.length - 1] = rightBound;
            System.arraycopy(roots, start, extPoints, 1, end - start);
            return extPoints;
        }
    }

    public double[] solve(final double min, final double max) {
        // Solve roots for f(x)=0
        // the roots are sorted, but may be out of [min, max]
        double[] c = this.getCoefficients();
        int degree = this.degree();
        if (degree == 0) return new double[0];
        else if (degree == 1) {
            double[] roots = new double[1];
            roots[0] = -c[0] / c[1];
            return roots;
        } else if (degree == 2) {
            double delta = c[1] * c[1] - 4 * c[2] * c[0];
            if (delta > 0) {
                double[] roots = new double[2];
                if (c[2] > 0) {
                    roots[0] = (-c[1] - Math.sqrt(delta)) / (2 * c[2]);
                    roots[1] = (-c[1] + Math.sqrt(delta)) / (2 * c[2]);
                } else {
                    roots[0] = (-c[1] + Math.sqrt(delta)) / (2 * c[2]);
                    roots[1] = (-c[1] - Math.sqrt(delta)) / (2 * c[2]);
                }
                return roots;
            } else if (delta == 0) {
                double[] roots = new double[1];
                roots[0] = (-c[1]) / (2 * c[2]);
                return roots;
            } else {
                return new double[0];
            }
        } else if (degree > 2) {
            double[] roots;// = new double[degree];
            if (min > max) throw new NumberIsTooLargeException(min, max, true);
            if (this.value(min) == 0) {
                if (max != min && this.value(max) == 0) {
                    roots = new double[2];
                    roots[0] = min;
                    roots[1] = max;
                } else {
                    roots = new double[1];
                    roots[0] = min;
                }
            } else if (this.value(max) == 0) {
                roots = new double[1];
                roots[0] = max;
            } else roots = new double[0];
            NewtonRaphsonSolver nrs = new NewtonRaphsonSolver(DEFAULT_ABSOLUTE_ACCURACY);
            roots = NewtonRaphsonSolverIterator(nrs, min, max, roots);
            return roots;
        } else throw new IllegalArgumentException();
    }

    public double[] solve(double value, final double min, final double max) {
        double[] c = this.getCoefficients();
        c[0] -= value;
        return new AdvancedPolynomialFunction(c).solve(min, max);
    }

    public double[] solveInRange(final double leftBound, final double rightBound) {
        double[] roots = solve(leftBound, rightBound);
        int i=0, start, end;
        for (; i < roots.length; ++i) {
            if (roots[i] >= leftBound) break;
        }
        start = i;
        for (; i < roots.length; ++i) {
            if (roots[i] > rightBound) break;
        }
        end = i;
        double[] rootsInRange = new double[end - start];
        System.arraycopy(roots, start, rootsInRange, 0, end - start);
        return rootsInRange;
    }

    public double[] solveInRange(double value, final double leftBound, final double rightBound) {
        double[] roots = solve(value, leftBound, rightBound);
        int i=0, start, end;
        for (; i < roots.length; ++i) {
            if (roots[i] > leftBound) break;
        }
        start = i;
        for (; i < roots.length; ++i) {
            if (roots[i] > rightBound) break;
        }
        end = i;
        double[] rootsInRange = new double[end - start];
        System.arraycopy(roots, start, rootsInRange, 0, end - start);
        return rootsInRange;
    }

    private double[] NewtonRaphsonSolverIterator(NewtonRaphsonSolver nrs, double min, double max, double[] roots) {
        try {
            double newRoot = nrs.solve(DEFAULT_MAX_EVAL, this, min, max);
            int id = roots.length;
            for (int j = 0; j < roots.length; ++j) {
                if (Math.abs(newRoot - roots[j]) <= DEFAULT_ABSOLUTE_ACCURACY) return roots; // duplicated root
                else if (newRoot < roots[j]) {
                    id = j;
                    break;
                }
            }
            double[] tmp = new double[roots.length + 1];
            System.arraycopy(roots, 0, tmp, 0, id);
            tmp[id] = newRoot;
            System.arraycopy(roots, id, tmp, id + 1, roots.length - id);
            if (newRoot > min && newRoot < max) {
                tmp = NewtonRaphsonSolverIterator(nrs, min, newRoot, tmp);
                tmp = NewtonRaphsonSolverIterator(nrs, newRoot, max, tmp);
            }
            roots = tmp;
        } catch (TooManyEvaluationsException e) {
            return roots;
        }
        return roots;
    }

    public AdvancedPolynomialFunction compose(AdvancedPolynomialFunction apf) {
        double[] c = this.getCoefficients();
        double[] apfc = apf.getCoefficients();
        double[] newCoefficients = new double[this.degree() * apf.degree() + 1];
        Arrays.fill(newCoefficients, 0);
        newCoefficients[0] = c[0];
        for (int i = 1; i < c.length; ++i) {
            double[] m = apfc.clone(), p;
            for (int j = 1; j < i; ++j) {
                p = new double[apfc.length + m.length - 1];
                Arrays.fill(p, 0);
                for (int k = 0; k < m.length; ++k) {
                    for (int l = 0; l < apfc.length; ++l) {
                        p[k + l] += m[k] * apfc[l];
                    }
                }
                m = p.clone();
            }
            for (int j = 0; j < m.length; ++j) {
                newCoefficients[j] += m[j] * c[i];
            }
        }
        return new AdvancedPolynomialFunction(newCoefficients);
    }

    public StochasticPolynomialFunction compose(StochasticPolynomialFunction spf) {
        StochasticPolynomialFunction result = new StochasticPolynomialFunction(
                new AdvancedPolynomialFunction[]{AdvancedPolynomialFunction.N(this.getCoefficients()[0])});
        StochasticPolynomialFunction multiplier = new StochasticPolynomialFunction(
                new AdvancedPolynomialFunction[]{AdvancedPolynomialFunction.ONE()});
        for (int i = 1; i <= this.degree(); ++i) {
            multiplier = multiplier.multiply(spf);
            result = result.add(multiplier.multiply(this.getCoefficients()[i]));
        }
        return result;
//
////        double[] c = this.getCoefficients();
//        AdvancedPolynomialFunction nilPolyFunc = AdvancedPolynomialFunction.ZERO();
//        AdvancedPolynomialFunction[] resPolyFuncCoefs = new AdvancedPolynomialFunction[this.degree() * spf.degree() + 1];
//        Arrays.fill(resPolyFuncCoefs, nilPolyFunc);
//        resPolyFuncCoefs[0] = AdvancedPolynomialFunction.N(this.getCoefficients()[0]);
//        for (int i = 1; i <= this.degree(); ++i) {
//            AdvancedPolynomialFunction[] multiplierPolyFuncCoefs = new AdvancedPolynomialFunction[1];
//            double[] tmpCoefs = new double[1];
////            tmpCoefs[0] = c[i];
//            multiplierPolyFuncCoefs[0] = new AdvancedPolynomialFunction(tmpCoefs);
////            System.out.println("Multiplier: " + multiplierPolyFuncCoefs[0].toString());
//            AdvancedPolynomialFunction[] tmpPolyFuncCoefs = new AdvancedPolynomialFunction[spf.degree() + 1];
//            Arrays.fill(tmpPolyFuncCoefs, nilPolyFunc);
//            for (int d2 = 0; d2 <= spf.degree(); ++d2) {
//                tmpPolyFuncCoefs[d2] = multiplierPolyFuncCoefs[0].multiply(
//                        spf.getAdvancedPolynomialFunctionCoefficients()[d2]);
//            }
//            for (int j = 1; j < i; ++j) {
//                multiplierPolyFuncCoefs = tmpPolyFuncCoefs;
//                tmpPolyFuncCoefs = new AdvancedPolynomialFunction[(j + 1) * spf.degree() + 1];
//                Arrays.fill(tmpPolyFuncCoefs, nilPolyFunc);
//                for (int d1 = 0; d1 < multiplierPolyFuncCoefs.length; ++d1) {
//                    for (int d2 = 0; d2 <= spf.degree(); ++d2) {
//                        tmpPolyFuncCoefs[d1 + d2] = multiplierPolyFuncCoefs[d1].multiply(
//                                spf.getAdvancedPolynomialFunctionCoefficients()[d2]);
//                    }
//                }
//            }
//            for (int j = 0; j < tmpPolyFuncCoefs.length; ++j) {
//                resPolyFuncCoefs[j] = resPolyFuncCoefs[j].add(tmpPolyFuncCoefs[j]);
//            }
//        }
//        return new StochasticPolynomialFunction(resPolyFuncCoefs);
    }

    public boolean equalsZero() {
        return this.getCoefficients().length == 1 && this.getCoefficients()[0] == 0;
    }

    public boolean equalsOne() {
        return this.getCoefficients().length == 1 && this.getCoefficients()[0] == 1;
    }

    public static AdvancedPolynomialFunction ZERO() {
        return new AdvancedPolynomialFunction(new double[]{0});
    }

    public static AdvancedPolynomialFunction ONE() {
        return new AdvancedPolynomialFunction(new double[]{1});
    }

    public static AdvancedPolynomialFunction N(double n) {
        return new AdvancedPolynomialFunction(new double[]{n});
    }

    public static AdvancedPolynomialFunction Linear(double point1, double value1, double point2, double value2) {
        if (value1 == value2) return AdvancedPolynomialFunction.N(value1);
        double[] c = new double[2];
        c[0] = (value2 * point1 - value1 * point2) / (point1 - point2);
        c[1] = (value1 - value2) / (point1 - point2);
        return new AdvancedPolynomialFunction(c);
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
        if (this == obj) return true;
        if (!(obj instanceof AdvancedPolynomialFunction)) return false;
        AdvancedPolynomialFunction other = (AdvancedPolynomialFunction) obj;
        return Arrays.equals(this.getCoefficients(), other.getCoefficients());
    }
}
