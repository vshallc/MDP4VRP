package functional;


import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

/**
 * Created by Xiaoxi Wang on 7/9/14.
 */
public class PiecewisePolynomialFunction {
    AdvancedPolynomialFunction[] polyFuncs;
    private double[] bounds;    // the overall domain is defined on [0, +inf)
    private int pieces;

    public PiecewisePolynomialFunction(AdvancedPolynomialFunction[] polyFuncs, double[] bounds) {
        this.polyFuncs = polyFuncs;
        this.bounds = bounds;
        pieces = polyFuncs.length;
    }

    public AdvancedPolynomialFunction[] getPolynomialFunctions() {
        return this.polyFuncs;
    }

    public AdvancedPolynomialFunction getPolynomialFunction(int piece) {
        return this.polyFuncs[piece];
    }

    public double[] getBounds() {
        return this.bounds;
    }

    public int getPieceNum() {
        return this.polyFuncs.length;
    }

    public PiecewiseStochasticPolynomialFunction compose(PiecewiseStochasticPolynomialFunction pwStocPolyFunc) {
        return null;
    }

    public static PiecewisePolynomialFunction integrationOnXiOfComposition_test(PiecewisePolynomialFunction V, PiecewiseStochasticPolynomialFunction A) {
        // only for calculating: int f(x)*V(A(t)) dx; x:0~1; A(t)=t+g(t); g>0 -> t+g(t)>t
        // A(t) arrive time (start on t)
        // A'(t)>=0

        List<AdvancedPolynomialFunction> pfsList = new ArrayList<AdvancedPolynomialFunction>();
        List<Double> boundsList = new ArrayList<Double>();

        PolynomialFunctionPiece[] pfp = integrationForVOfAOnPieces(V, A.getStochasticPolynomialFunction(0), A.getBounds()[0], A.getBounds()[1]);
        for (PolynomialFunctionPiece p : pfp) {
            pfsList.add(p.getPolynomialFunction());
            boundsList.add(p.getBounds()[0]);
        }
        for (int i = 1; i < A.getPieceNum(); ++i) {
            pfp = integrationForVOfAOnPieces(V, A.getStochasticPolynomialFunction(i), A.getBounds()[i], A.getBounds()[i + 1]);
            int j = 0;
            if (pfp[0].getPolynomialFunction().equals(pfsList.get(pfsList.size() - 1))) {
                j = 1;
            }
            for (; j < pfp.length; ++j) {
                pfsList.add(pfp[j].getPolynomialFunction());
                boundsList.add(pfp[j].getBounds()[0]);
            }
        }
        boundsList.add(pfp[pfp.length - 1].getBounds()[1]);
        AdvancedPolynomialFunction[] apfs = new AdvancedPolynomialFunction[pfsList.size()];
        double[] bounds = new double[boundsList.size()];
        for (int i = 0; i < apfs.length; ++i) {
            apfs[i] = pfsList.get(i);
            bounds[i] = boundsList.get(i);
        }
        bounds[apfs.length] = boundsList.get(apfs.length);
        return new PiecewisePolynomialFunction(apfs, bounds);
    }

    private static PolynomialFunctionPiece[] integrationForVOfAOnPieces(PiecewisePolynomialFunction V, StochasticPolynomialFunction g, double leftDomain, double rightDomain) {
        // xi ~ [0,1]
        AdvancedPolynomialFunction gmin = g.determinize(0);
        AdvancedPolynomialFunction gmax = g.determinize(1);
        TreeSet<Double> innerBounds = new TreeSet<Double>();
        innerBounds.add(leftDomain);
        innerBounds.add(rightDomain);
        {
            double[] roots;
            for (int i = 1; i < V.getPieceNum(); ++i) {
                roots = gmin.solve(V.getBounds()[i], leftDomain, rightDomain);
                for (double r : roots) {
                    if (r > leftDomain && r < rightDomain) innerBounds.add(r);
                }
                roots = gmax.solve(V.getBounds()[i], leftDomain, rightDomain);
                for (double r : roots) {
                    if (r > leftDomain && r < rightDomain) innerBounds.add(r);
                }
            }
        }
        double[] primaryBounds = new double[innerBounds.size()];
        {
            int i = 0;
            for (double d : innerBounds) {
                primaryBounds[i] = d;
                ++i;
            }
        }
        PolynomialFunctionPiece[] results = new PolynomialFunctionPiece[primaryBounds.length - 1];
        for (int i = 0; i < primaryBounds.length - 1; ++i) {
            // g(t) must be an increasing function
            double min = g.determinedValue(primaryBounds[i], 0);
            double max = g.determinedValue(primaryBounds[i + 1], 1);
//            System.out.println("primary bound i: " + primaryBounds[i] + " primary bound i+1: " + primaryBounds[i+1] + " min: " + min + " max: " + max);
            int VStart, VEnd;
            for (VStart = 0; VStart < V.getPieceNum(); ++VStart) {
                if (min >= V.getBounds()[VStart] && min < V.getBounds()[VStart + 1]) break;
            }
            for (VEnd = V.getPieceNum() - 1; VEnd >= VStart ; --VEnd) {
                if (max > V.getBounds()[VEnd] && max <= V.getBounds()[VEnd + 1]) break;
            }
            results[i] = simpleIntegration(V, VStart, VEnd, g, primaryBounds[i], primaryBounds[i + 1]);
        }
        return results;
    }

    private static PolynomialFunctionPiece simpleIntegration(PiecewisePolynomialFunction V, int VStart, int VEnd, StochasticPolynomialFunction g, double gLeft, double gRight) {
        AdvancedPolynomialFunction apf = AdvancedPolynomialFunction.ZERO();
        AdvancedPolynomialFunction upperBound, lowerBound, uc, lc;
        lowerBound = AdvancedPolynomialFunction.ZERO();
//        System.out.println("VStart: " + VStart + "VEnd: " + VEnd);
        for (int i = VStart; i < VEnd; ++i) {
            double[] coefs = g.getDeterminedPart().getCoefficients();
            coefs[0] -= V.getBounds()[i + 1];
            for (int j = 0; j < coefs.length; ++j) {
                coefs[j] /= -g.getAdvancedPolynomialFunctionCoefficients()[1].getCoefficients()[0]; // we assume that g(t,xi) = f(t) + b*xi, and here the divider is -b;
            }
            upperBound = new AdvancedPolynomialFunction(coefs); // System.out.println("UPPER: " + upperBound);
//            apf = apf.add(V.getPolynomialFunction(i).compose(upperBound).subtract(V.getPolynomialFunction(i).compose(lowerBound)));
            uc = V.getPolynomialFunction(i).integrate().compose(upperBound);
            lc = V.getPolynomialFunction(i).integrate().compose(lowerBound);
//            System.out.println("V piece: " + i + " :: " + V.getPolynomialFunction(i).toString() + "uc: " + uc.toString() + "lc: " + lc.toString());
            apf = apf.add(uc.subtract(lc));
            lowerBound = upperBound;
        }
        upperBound = AdvancedPolynomialFunction.ONE();
        uc = V.getPolynomialFunction(VEnd).integrate().compose(upperBound);
        lc = V.getPolynomialFunction(VEnd).integrate().compose(lowerBound);
//        System.out.println("V piece: " + VEnd + " :: " + V.getPolynomialFunction(VEnd).toString() + "uc: " + uc.toString() + "lc: " + lc.toString());
        apf = apf.add(uc.subtract(lc)); // System.out.println();
        return new PolynomialFunctionPiece(apf, gLeft, gRight);
    }

    private static class PolynomialFunctionPiece {
        private AdvancedPolynomialFunction polyFunc;
        private double[] bounds =new double[2];
        public PolynomialFunctionPiece(AdvancedPolynomialFunction polyFunc, double leftBound, double rightBound) {
            this.polyFunc = polyFunc;
            this.bounds[0] = leftBound;
            this.bounds[1] = rightBound;
        }

        public AdvancedPolynomialFunction getPolynomialFunction() {
            return this.polyFunc;
        }

        public double[] getBounds() {
            return bounds;
        }
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < pieces; ++i) {
            s.append("[");
            s.append(bounds[i]);
            s.append(",");
            s.append(bounds[i + 1]);
            s.append(")\t");
            s.append(polyFuncs[i].toString());
            s.append("\n");
        }
        s.deleteCharAt(s.length() - 1);
        return s.toString();
    }
}
