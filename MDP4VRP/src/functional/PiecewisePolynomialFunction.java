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
        AdvancedPolynomialFunction[] pf = new AdvancedPolynomialFunction[newPieceNum];
        for (n = 0, i = 0, j = 0; n < newPieceNum; ++n) {
            pf[n] = polyFuncs[i].add(ppf.polyFuncs[j]);
            if (newBounds[n] >= bounds[i]) ++i;
            if (newBounds[n] >= ppf.bounds[j]) ++j;
        }
        return new PiecewisePolynomialFunction(pf, newBounds);
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
