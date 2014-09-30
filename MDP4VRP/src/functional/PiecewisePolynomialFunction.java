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
            n = 1;
            i = 1;
            j = 1;
            while (i < pieces && j < ppf.pieces) {
                if (bounds[i] < ppf.bounds[j]) tmpBounds[n++] = bounds[i++];
                else if (ppf.bounds[j] < bounds[i]) tmpBounds[n++] = ppf.bounds[j++];
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

    public PiecewisePolynomialFunction max(PiecewisePolynomialFunction ppf, int[] selectedID) {
        if (bounds[0] != ppf.bounds[0] || bounds[pieces] != ppf.bounds[ppf.pieces])
            throw new IllegalArgumentException();
        double[] newBounds;
        int newPiece;
        int n, i, j, k, l;
        List<Double> tmpBounds = new ArrayList<Double>();
//        tmpBounds.add(bounds[0]);
        n = 0; i = 0; j = 0;
        double lastBound = bounds[0], nextBound;
        double[] roots;
        double v;
        while (i < pieces && j < ppf.pieces) {
            tmpBounds.add(lastBound);
            if (bounds[i + 1] < ppf.bounds[j + 1]) { // System.out.println("<");
                nextBound = bounds[i + 1];
                roots = polyFuncs[i].subtract(ppf.getPolynomialFunction(j)).solve(tmpBounds.get(n), nextBound);
                ++i;
            }
            else if (ppf.bounds[j + 1] < bounds[i + 1]) { // System.out.println(">");
                nextBound = ppf.bounds[j + 1];
                roots = polyFuncs[i].subtract(ppf.getPolynomialFunction(j)).solve(tmpBounds.get(n), nextBound);
                ++j;
            } else { // System.out.println("=");
                nextBound = bounds[i + 1];
                roots = polyFuncs[i].subtract(ppf.getPolynomialFunction(j)).solve(tmpBounds.get(n), nextBound);
                ++i;
                ++j;
            }
            for (double r : roots) {
                if (r > lastBound && r < nextBound) {
                    tmpBounds.add(r);
                    ++n;
                }
            }
            lastBound = nextBound;
            ++n;
        }
//        for (Double b : tmpBounds) {
//            System.out.println("tmp: " + b);
//        }
//        System.out.println(n);
        newPiece = n;
        newBounds = new double[n + 1];
        newBounds[0] = tmpBounds.get(0);
        newBounds[newPiece] = bounds[pieces]; // 'cause bounds[pieces] = ppf.bounds[ppf.pieces]
        AdvancedPolynomialFunction[] pfs = new AdvancedPolynomialFunction[newPiece];
        selectedID = new int[newPiece];
        i = 0; j = 0;
        for (n = 1; n < newPiece; ++n) {
            newBounds[n] = tmpBounds.get(n);
            v = (newBounds[n - 1] + tmpBounds.get(n)) / 2;
            if (polyFuncs[i].value(v) > ppf.polyFuncs[j].value(v)) {
                pfs[n - 1] = new AdvancedPolynomialFunction(polyFuncs[i].getCoefficients());
                selectedID[n - 1] = 0;
            }
            else {
                pfs[n - 1] = new AdvancedPolynomialFunction(ppf.polyFuncs[j].getCoefficients());
                selectedID[n - 1] = 1;
            }
            if (bounds[i] >= newBounds[n]) ++i;
            if (ppf.bounds[j] >= newBounds[n]) ++j;
        }
        v = bounds[pieces] == Double.POSITIVE_INFINITY ? newBounds[newPiece - 1] + 1 : (newBounds[newPiece - 1] + bounds[pieces]) / 2;
        if (polyFuncs[i].value(v) > ppf.polyFuncs[j].value(v)) {
            pfs[newPiece - 1] = new AdvancedPolynomialFunction(polyFuncs[i].getCoefficients());
            selectedID[newPiece - 1] = 0;
        }
        else {
            pfs[newPiece - 1] = new AdvancedPolynomialFunction(ppf.polyFuncs[j].getCoefficients());
            selectedID[newPiece - 1] = 1;
        }

        return new PiecewisePolynomialFunction(pfs, newBounds);
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
//        s.deleteCharAt(s.length() - 1);
        return s.toString().trim();
    }
}
