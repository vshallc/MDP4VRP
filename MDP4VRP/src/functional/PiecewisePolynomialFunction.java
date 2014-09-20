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

    public PiecewisePolynomialFunction add(PiecewisePolynomialFunction ppf) {
        double[] newBounds;
        int newPieceNum;
        int n, i, j;
        {
            double[] tmpBounds = new double[pieces + ppf.pieces - 1];
            tmpBounds[0] = bounds[0];
            n = 0;
            i = 1;
            j = 1;
            while (i < pieces && j < ppf.pieces) {
                if (bounds[i] < ppf.bounds[j]) tmpBounds[n++] = bounds[i++];
                else if (ppf.bounds[j] < bounds[i]) tmpBounds[n++] = bounds[i++];
                else {
                    tmpBounds[n++] = bounds[i++];
                    ++j;
                }
            }
            newBounds = new double[n];
            System.arraycopy(tmpBounds, 0, newBounds, 0, n);
            newPieceNum = n - 1; // bounds [0, +inf)
        }
        AdvancedPolynomialFunction[] pfs = new AdvancedPolynomialFunction[newPieceNum];
        for (n = 0, i = 0, j = 0; n < newPieceNum; ++n) {
            pfs[n] = polyFuncs[i].add(ppf.polyFuncs[j]);
            if (newBounds[n] >= bounds[i]) ++i;
            if (newBounds[n] >= ppf.bounds[j]) ++j;
        }
        return new PiecewisePolynomialFunction(pfs, newBounds);
    }

    public PiecewisePolynomialFunction add(final double c) {
        AdvancedPolynomialFunction[] pfs = new AdvancedPolynomialFunction[pieces];
        for (int i = 0; i < pieces; ++i) {
            pfs[i] = polyFuncs[i].add(c);
        }
        return new PiecewisePolynomialFunction(pfs, bounds.clone());
    }

    public PiecewisePolynomialFunction shift(final double t) {
        // V(t) -> V(t + t')
        AdvancedPolynomialFunction[] pfs = new AdvancedPolynomialFunction[pieces];
        for (int i = 0; i < pieces; ++i) {
            pfs[i] = polyFuncs[i].shift(t);
        }
        return new PiecewisePolynomialFunction(pfs, bounds.clone());
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
