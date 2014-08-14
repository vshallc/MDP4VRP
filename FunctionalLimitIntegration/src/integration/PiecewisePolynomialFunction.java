package integration;


import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;

import java.util.ArrayList;
import java.util.List;

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

    public PiecewisePolynomialFunction integrationOnXiOfComposition_test(PiecewisePolynomialFunction V, PiecewiseStochasticPolynomialFunction A) {
        // only for calculating: int f(x)*V(A(t)) dx; x:0~1; A(t)=t+g(t); g>0 -> t+g(t)>t
        // A(t) arrive time (start on t)
        // A'(t)>=0
        int pieceNum = V.getPieceNum();
        List<PolynomialFunction> pfsList = new ArrayList<PolynomialFunction>();
        List<Double> boundsList = new ArrayList<Double>();
        int startPiece = 0;
        int endPiece = pieceNum - 1;
        for (int i = 0; i < A.getPieceNum(); ++i) {
            for (endPiece = startPiece; endPiece < pieceNum; ++endPiece) {
                // TODO calculate when break from this loop
            }
            PolynomialFunction result = integrationForVOfAOnPieces(A.getStochasticPolynomialFunction(i), V, startPiece, endPiece);
            pfsList.add(result);
        }



        if (pieceNum == 1) {
            boundsList.add(A.getBounds()[0]);
            for (int i = 0; i < A.getPieceNum(); ++i) {
                // probability distribution f(x)=1/(1-0)=1
                // TODO compose then integration
                // TODO change bounds (append inf to the end)
                AdvancedPolynomialFunction integrationOf1 = V.getPolynomialFunction(0).compose(A.getStochasticPolynomialFunction(i)).integrationOnXi().determinize(1);
                AdvancedPolynomialFunction integrationOf0 = V.getPolynomialFunction(0).compose(A.getStochasticPolynomialFunction(i)).integrationOnXi().determinize(0);
            }
        } else if (pieceNum > 1) {
            double[][] g_ext = A.getDeterminedExtremaOnEachPiece();
            double[][] g_xi_range = A.getStochasticRangeOnEachPiece();

            boundsList.add(0.0);
            int VNextBoundID = 1;
            for (int i = 0; i < A.getPieceNum(); ++i) {
                if (g_ext[i][1] + g_xi_range[i][1] < V.getBounds()[VNextBoundID]) {
                    //
                }
            }

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
        } else {
            throw new MathIllegalArgumentException(LocalizedFormats.FUNCTION);
        }
        return null;
    }

    private PolynomialFunction integrationForVOfAOnPieces(StochasticPolynomialFunction A, PiecewisePolynomialFunction V, int startPiece, int endPiece) {
        // TODO Complete this function
        return null;
    }
}
