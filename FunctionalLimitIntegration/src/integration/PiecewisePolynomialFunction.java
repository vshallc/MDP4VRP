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

    public PiecewiseStochasticPolynomialFunction compose(PiecewiseStochasticPolynomialFunction pwStocPolyFunc) {
        return null;
    }

    public PiecewisePolynomialFunction integrationOnXiOfComposition(PiecewisePolynomialFunction V, PiecewiseStochasticPolynomialFunction g) {
        // for calculating: int f(x)*V(t+g(t)) dx; x:0~1; g>0 -> t+g(t)>t
        double[][] g_ext = g.getDeterminedExtremaOnEachPieceFiniteDomain();
        double[][] g_xi_rng = g.getStochasticRangeOnEachPieceFiniteDomain();
        for (int i = 0; i < g.getPieceNum(); ++i) {
            double min = g_ext[i][0] + g_xi_rng[i][0];
            double max = g_ext[i][1] + g_xi_rng[i][1];
        }
        return null;
    }
}
