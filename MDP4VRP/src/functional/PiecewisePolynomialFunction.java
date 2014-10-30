package functional;


import java.util.ArrayList;
import java.util.Arrays;
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
        this.polyFuncs = Arrays.copyOf(polyFuncs, polyFuncs.length);
        this.bounds = bounds.clone();
//        System.arraycopy(bounds, 0, this.bounds, 0, bounds.length);
        pieces = polyFuncs.length;
    }

    public void simplify() {
        AdvancedPolynomialFunction[] tmpPolyFuncs = new AdvancedPolynomialFunction[polyFuncs.length];
        double[] tmpBounds = new double[bounds.length];
        double[] lastCoefs = null, currentCoefs;
        int n = 0;
        for (int i = 0; i < polyFuncs.length; ++i) {
            currentCoefs = polyFuncs[i].getCoefficients();
//            System.out.println("last: " + Arrays.toString(lastCoefs) + " current: " + Arrays.toString(currentCoefs));
//            System.out.println("is eq: " + Arrays.equals(currentCoefs, lastCoefs));
            if (!Arrays.equals(currentCoefs, lastCoefs)){
                tmpPolyFuncs[n] = polyFuncs[i];
                tmpBounds[n] = bounds[i];
                ++n;
                lastCoefs = currentCoefs;
            }
        }
        tmpBounds[n] = bounds[bounds.length - 1];
        polyFuncs = new AdvancedPolynomialFunction[n];
        bounds = new double[n + 1];
        for (int i = 0; i < n; ++i) {
            polyFuncs[i] = tmpPolyFuncs[i];
            bounds[i] = tmpBounds[i];
        }
        bounds[n] = tmpBounds[n];
        pieces = n;
    }

    public PiecewisePolynomialFunction add(PiecewisePolynomialFunction ppf) {
        double[] newBounds;
        int newPieceNum;
        int n, i, j;
        {
            double[] tmpBounds = new double[pieces + ppf.pieces];
            tmpBounds[0] = bounds[0];
            n = 1;
            i = 1;
            j = 1;
            while (i <= pieces && j <= ppf.pieces) {
                if (bounds[i] < ppf.bounds[j]) tmpBounds[n++] = bounds[i++];
                else if (ppf.bounds[j] < bounds[i]) tmpBounds[n++] = ppf.bounds[j++];
                else {
                    tmpBounds[n++] = bounds[i++];
                    ++j;
                }
            }
            newBounds = new double[n];
            System.arraycopy(tmpBounds, 0, newBounds, 0, n);
//            System.out.println("bounds: " + Arrays.toString(bounds));
//            System.out.println("ppf.bounds: " + Arrays.toString(ppf.bounds));
//            System.out.println("tmp bounds: " + Arrays.toString(tmpBounds));
//            System.out.println("new bounds: " + Arrays.toString(newBounds));
            newPieceNum = n - 1; // bounds [0, +inf)
        }
        AdvancedPolynomialFunction[] pfs = new AdvancedPolynomialFunction[newPieceNum];
        for (n = 0, i = 0, j = 0; n < newPieceNum; ++n) {
            pfs[n] = polyFuncs[i].add(ppf.polyFuncs[j]);
            if (newBounds[n+1] >= bounds[i+1]) ++i;
            if (newBounds[n+1] >= ppf.bounds[j+1]) ++j;
        }
        PiecewisePolynomialFunction result = new PiecewisePolynomialFunction(pfs, newBounds);
        result.simplify();
        return result;
    }

    public PiecewisePolynomialFunction add(final double c) {
        AdvancedPolynomialFunction[] pfs = new AdvancedPolynomialFunction[pieces];
        for (int i = 0; i < pieces; ++i) {
            pfs[i] = polyFuncs[i].add(c);
        }
        PiecewisePolynomialFunction result = new PiecewisePolynomialFunction(pfs, bounds.clone());
        result.simplify();
        return result;
    }

    public PiecewisePolynomialFunction shift(final double t) {
        // V(t) -> V(t + t')
        AdvancedPolynomialFunction[] pfs = new AdvancedPolynomialFunction[pieces];
        for (int i = 0; i < pieces; ++i) {
            pfs[i] = polyFuncs[i].shift(t);
        }
        PiecewisePolynomialFunction result = new PiecewisePolynomialFunction(pfs, bounds.clone());
        result.simplify();
        return result;
    }


    public AdvancedPolynomialFunction[] getPolynomialFunctions() {
        return this.polyFuncs;
    }

    public AdvancedPolynomialFunction getPolynomialFunction(int piece) {
        return this.polyFuncs[piece];
    }

    public double[] getBounds() {
        return this.bounds.clone();
    }

    public int getPieceNum() {
        return this.polyFuncs.length;
    }

    @Override
    public int hashCode() {
        final int prime = 17;
        int result = 1;
        result = prime * result + Arrays.hashCode(bounds);
        result = prime * result + Arrays.hashCode(polyFuncs);
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof PiecewisePolynomialFunction)) return false;
        PiecewisePolynomialFunction other = (PiecewisePolynomialFunction) obj;
        return Arrays.equals(this.getPolynomialFunctions(), other.getPolynomialFunctions()) && Arrays.equals(this.getBounds(), other.getBounds());
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
//        s.deleteCharAt(s.length() - 1);
        return s.toString().trim();
    }
}
