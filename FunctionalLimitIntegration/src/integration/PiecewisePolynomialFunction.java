package integration;


/**
 * Created by Xiaoxi Wang on 7/9/14.
 */
public class PiecewisePolynomialFunction {
    AdvancedPolynomialFunction[] polyFuncs;
    double[] bounds;    // the overall domain is defined on [0, +inf)

    public PiecewisePolynomialFunction(AdvancedPolynomialFunction[] polyFuncs, double[] bounds) {
        this.polyFuncs = polyFuncs;
        this.bounds = bounds;
    }

    public AdvancedPolynomialFunction[] getPolynomialFunctions() {
        return this.polyFuncs;
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

    public PiecewisePolynomialFunction integrationOnXiOfComposition_test(PiecewisePolynomialFunction V, PiecewiseStochasticPolynomialFunction A) {
        // only for calculating: int f(x)*V(A(t)) dx; x:0~1; A(t)=t+g(t); g>0 -> t+g(t)>t
        // A(t) arrive time (start on t)
        // A'(t)>=0
        double[][] g_ext = A.getDeterminedExtremaOnEachPieceFiniteDomain();
        double[][] g_xi_range = A.getStochasticRangeOnEachPieceFiniteDomain();
        int pieceNum = V.getPieceNum();
        int leftBoundID = 1;
        for (int i = 0; i < A.getPieceNum(); ++i) {
            double min = g_ext[i][0] + g_xi_range[i][0];
            double max = g_ext[i][1] + g_xi_range[i][1];
            for (int j = leftBoundID; j < bounds.length; ++j) {
                if (min >= bounds[j]) {
                    --pieceNum;
                } else {
                    //
                }
            }
        }
        return null;
    }
}
