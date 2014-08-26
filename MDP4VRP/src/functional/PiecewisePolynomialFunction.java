package functional;


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

    public static PiecewisePolynomialFunction integrationOnXiOfComposition_test(PiecewisePolynomialFunction V, PiecewiseStochasticPolynomialFunction A) {
        // only for calculating: int f(x)*V(A(t)) dx; x:0~1; A(t)=t+g(t); g>0 -> t+g(t)>t
        // A(t) arrive time (start on t)
        // A'(t)>=0
//        int pieceNum = V.getPieceNum();
        List<AdvancedPolynomialFunction> pfsList = new ArrayList<AdvancedPolynomialFunction>();
        List<Double> boundsList = new ArrayList<Double>();
        int startPiece = 0;
        int endPiece; // = V.getPieceNum() - 1;
        double[][] AExtrema = A.getDeterminedExtremaOnEachPiece();
        double[][] AStocRange = A.getStochasticRangeOnEachPiece();
        for (int i = 0; i < A.getPieceNum(); ++i) {
            if (A.getStochasticPolynomialFunction(i).degree() == 1) {
                for (; startPiece < V.getPieceNum(); ++startPiece) {
                    if (V.getBounds()[startPiece] <= AExtrema[i][0] + AStocRange[i][0]) break;
                }
                for (endPiece = startPiece; endPiece < V.getPieceNum(); ++endPiece) {
                    if (V.getBounds()[endPiece] > AExtrema[i][1] + AStocRange[i][1]) break;
                }

                if (V.getBounds()[startPiece + 1] > AExtrema[i][1]) {
                    //
                } else {
                    //
                }
                double leftDomain, rightDomain;
                AdvancedPolynomialFunction result = integrationForVOfAOnPieces(V, A.getStochasticPolynomialFunction(i), leftDomain, rightDomain);
                pfsList.add(result);
            } else if (A.getStochasticPolynomialFunction(i).degree() == 0) {
                //
            } else {
                throw new TooHighDegreeException(A.getStochasticPolynomialFunction(i).degree());
            }
        }
//        if (pieceNum == 1) {
//            boundsList.add(A.getBounds()[0]);
//            for (int i = 0; i < A.getPieceNum(); ++i) {
//                // probability distribution f(x)=1/(1-0)=1
//                // TODO compose then integration
//                // TODO change bounds (append inf to the end)
//                AdvancedPolynomialFunction integrationOf1 = V.getPolynomialFunction(0).compose(A.getStochasticPolynomialFunction(i)).integrationOnXi().determinize(1);
//                AdvancedPolynomialFunction integrationOf0 = V.getPolynomialFunction(0).compose(A.getStochasticPolynomialFunction(i)).integrationOnXi().determinize(0);
//            }
//        } else if (pieceNum > 1) {
//            double[][] g_ext = A.getDeterminedExtremaOnEachPiece();
//            double[][] g_xi_range = A.getStochasticRangeOnEachPiece();
//
//            boundsList.add(0.0);
//            int VNextBoundID = 1;
//            for (int i = 0; i < A.getPieceNum(); ++i) {
//                if (g_ext[i][1] + g_xi_range[i][1] < V.getBounds()[VNextBoundID]) {
//                    //
//                }
//            }
//
//            int leftBoundID = 1;
//            for (int i = 0; i < A.getPieceNum(); ++i) {
//                double min = g_ext[i][0] + g_xi_range[i][0];
//                double max = g_ext[i][1] + g_xi_range[i][1];
//                for (int j = leftBoundID; j < bounds.length; ++j) {
//                    if (min >= bounds[j]) {
//                        --pieceNum;
//                    } else {
//                        //
//                    }
//                }
//            }
//        } else {
//            throw new MathIllegalArgumentException(LocalizedFormats.FUNCTION);
//        }
        double[] resultBounds = new double[boundsList.size()];
        for (int i = 0; i < boundsList.size(); ++i) resultBounds[i] = boundsList.get(i);
        PiecewisePolynomialFunction result = new PiecewisePolynomialFunction(pfsList.toArray(new AdvancedPolynomialFunction[pfsList.size()]), resultBounds);
        return result;
    }

    private static AdvancedPolynomialFunction integrationForVOfAOnPieces(PiecewisePolynomialFunction V, StochasticPolynomialFunction A, double leftDomain, double rightDomain) {

        // TODO Complete this function

        return null;
    }

    private static double[] separateBounds(StochasticPolynomialFunction g, double[] gExtrema, double[] gStocRanges, double[] domain, double[] range_domains) {
        // separate domain into pieces to fit range domains
//        if (bounds.length == 2) return bounds;
        List<Double> result = new ArrayList<Double>();
        double[] result_primitive = new double[result.size()];
        for (int i = 0; i < result.size(); ++i) result_primitive[i] = result.get(i);
        return result_primitive;
    }
}
