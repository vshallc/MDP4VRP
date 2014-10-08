package mdp;

import functional.AdvancedPolynomialFunction;
import functional.PiecewisePolynomialFunction;
import functional.PiecewiseStochasticPolynomialFunction;
import functional.StochasticPolynomialFunction;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by Xiaoxi Wang on 9/5/14.
 */
public class MDP {
    private State startState;
    private State endState;
    private PiecewisePolynomialFunction terminatedValueFunction;
    private ConcurrentMap<State, List<Arc>> incomingArcs = new ConcurrentHashMap<State, List<Arc>>();
    private ConcurrentMap<State, List<Arc>> outgoingArcs = new ConcurrentHashMap<State, List<Arc>>();
    private ConcurrentMap<State, List<Action>> possibleActions = new ConcurrentHashMap<State, List<Action>>();

    public MDP(State startState, State endState, PiecewisePolynomialFunction terminatedValueFunction) {
        this.startState = startState;
        this.endState = endState;
        this.terminatedValueFunction = terminatedValueFunction;
        incomingArcs.putIfAbsent(startState, new ArrayList<Arc>());
        outgoingArcs.putIfAbsent(startState, new ArrayList<Arc>());
        possibleActions.putIfAbsent(startState, new ArrayList<Action>());
        incomingArcs.putIfAbsent(endState, new ArrayList<Arc>());
        outgoingArcs.putIfAbsent(endState, new ArrayList<Arc>());
        possibleActions.putIfAbsent(endState, new ArrayList<Action>());
    }

    public void buildGraph() {
        Queue<State> checkingQueue = new LinkedList<State>();
        Set<State> checkedStates = new HashSet<State>();
        checkingQueue.add(startState);
        while (!checkingQueue.isEmpty()) {
            State currentState = checkingQueue.poll();
            possibleActions.putIfAbsent(currentState, new ArrayList<Action>());
            if (currentState.equals(endState)) continue;
            for (Action a : possibleActions.get(currentState)) {
                State nextState = a.perform(currentState);
                if (checkedStates.contains(nextState)) {
                    Arc arc = new Arc(currentState, nextState, a);
                    incomingArcs.get(nextState).add(arc);
                    outgoingArcs.get(currentState).add(arc);
                } else {
                    Arc arc = new Arc(currentState, nextState, a);
                    incomingArcs.putIfAbsent(nextState, new ArrayList<Arc>());
                    outgoingArcs.putIfAbsent(nextState, new ArrayList<Arc>());
                    incomingArcs.get(nextState).add(arc);
                    outgoingArcs.get(currentState).add(arc);
                    checkingQueue.add(nextState);
                    checkedStates.add(nextState);
                }
            }
        }
    }

    public void assignValueFunction() {
        Queue<State> checkingQueue = new LinkedList<State>();
        Set<State> checkedStates = new HashSet<State>();
        checkingQueue.add(endState);
        Map<State, Set<Arc>> checkedArcs = new HashMap<State, Set<Arc>>();
        Map<State, PiecewisePolynomialFunction> currentBestValueFunction = new HashMap<State, PiecewisePolynomialFunction>();
        checkedArcs.put(endState, new HashSet<Arc>());
        currentBestValueFunction.put(endState, terminatedValueFunction);

        while (!checkingQueue.isEmpty()) {
            State currentState = checkingQueue.poll();
            for (Arc arc : incomingArcs.get(currentState)) {
                State preState = arc.getStartState();

            }
        }
    }
    public static PiecewisePolynomialFunctionAndSelectedIndices max(PiecewisePolynomialFunction ppf1, PiecewisePolynomialFunction ppf2) {
        double[] bounds1 = ppf1.getBounds();
        double[] bounds2 = ppf2.getBounds();
        int pieces1 = ppf1.getPieceNum();
        int pieces2 = ppf2.getPieceNum();
        if (bounds1[0] != bounds2[0] || bounds1[pieces1] != bounds2[pieces2])
            throw new IllegalArgumentException();
        double[] newBounds;
        int newPiece;
        int n, i, j, k, l;
        List<Double> tmpBounds = new ArrayList<Double>();
//        tmpBounds.add(bounds[0]);
        n = 0; i = 0; j = 0;
        double lastBound = bounds1[0], nextBound;
        double[] roots;
        double v;
        while (i < pieces1 && j < pieces2) {
            tmpBounds.add(lastBound);
            if (bounds1[i + 1] < bounds2[j + 1]) { // System.out.println("<");
                nextBound = bounds1[i + 1];
                roots = ppf1.getPolynomialFunction(i).subtract(ppf2.getPolynomialFunction(j)).solve(tmpBounds.get(n), nextBound);
                ++i;
            }
            else if (bounds2[j + 1] < bounds1[i + 1]) { // System.out.println(">");
                nextBound = bounds2[j + 1];
                roots = ppf1.getPolynomialFunction(i).subtract(ppf2.getPolynomialFunction(j)).solve(tmpBounds.get(n), nextBound);
                ++j;
            } else { // System.out.println("=");
                nextBound = bounds1[i + 1];
                roots = ppf1.getPolynomialFunction(i).subtract(ppf2.getPolynomialFunction(j)).solve(tmpBounds.get(n), nextBound);
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
        newBounds[newPiece] = bounds1[pieces1]; // 'cause bounds[pieces] = ppf.bounds[ppf.pieces]
        AdvancedPolynomialFunction[] pfs = new AdvancedPolynomialFunction[newPiece];
        int[] selectedIDs = new int[newPiece];
        i = 0; j = 0;
        for (n = 1; n < newPiece; ++n) {
            newBounds[n] = tmpBounds.get(n);
            v = (newBounds[n - 1] + tmpBounds.get(n)) / 2;
            if (ppf1.getPolynomialFunction(i).value(v) > ppf2.getPolynomialFunction(j).value(v)) {
                pfs[n - 1] = new AdvancedPolynomialFunction(ppf1.getPolynomialFunction(i).getCoefficients());
                selectedIDs[n - 1] = 0;
            }
            else {
                pfs[n - 1] = new AdvancedPolynomialFunction(ppf2.getPolynomialFunction(j).getCoefficients());
                selectedIDs[n - 1] = 1;
            }
            if (bounds1[i] >= newBounds[n]) ++i;
            if (bounds2[j] >= newBounds[n]) ++j;
        }
        v = bounds1[pieces1] == Double.POSITIVE_INFINITY ? newBounds[newPiece - 1] + 1 : (newBounds[newPiece - 1] + bounds1[pieces1]) / 2;
        if (ppf1.getPolynomialFunction(i).value(v) > ppf2.getPolynomialFunction(j).value(v)) {
            pfs[newPiece - 1] = new AdvancedPolynomialFunction(ppf1.getPolynomialFunction(i).getCoefficients());
            selectedIDs[newPiece - 1] = 0;
        }
        else {
            pfs[newPiece - 1] = new AdvancedPolynomialFunction(ppf2.getPolynomialFunction(j).getCoefficients());
            selectedIDs[newPiece - 1] = 1;
        }
        return new PiecewisePolynomialFunctionAndSelectedIndices(new PiecewisePolynomialFunction(pfs, newBounds), selectedIDs);
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

    private static PolynomialFunctionPiece[] integrationForVOfAOnPieces(PiecewisePolynomialFunction V, StochasticPolynomialFunction a, double leftDomain, double rightDomain) {
        // xi ~ [0,1]
        AdvancedPolynomialFunction gmin = a.determinize(0);
        AdvancedPolynomialFunction gmax = a.determinize(1);
        double[] vBounds = V.getBounds();
        TreeSet<Double> innerBounds = new TreeSet<Double>();
        innerBounds.add(leftDomain);
        innerBounds.add(rightDomain);
        {
            double[] roots;
            for (int i = 1; i < V.getPieceNum(); ++i) {
                roots = gmin.solve(vBounds[i], leftDomain, rightDomain);
                for (double r : roots) {
                    if (r > leftDomain && r < rightDomain) innerBounds.add(r);
                }
                roots = gmax.solve(vBounds[i], leftDomain, rightDomain);
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
            // a(t) = t + g(t) must be an increasing function
            double min = a.determinedValue(primaryBounds[i], 0);
            double max = a.determinedValue(primaryBounds[i + 1], 1);
//            System.out.println("primary bound i: " + primaryBounds[i] + " primary bound i+1: " + primaryBounds[i+1] + " min: " + min + " max: " + max);
            int VStart, VEnd;
            for (VStart = 0; VStart < V.getPieceNum(); ++VStart) {
                if (min >= vBounds[VStart] && min < vBounds[VStart + 1]) break;
            }
            for (VEnd = V.getPieceNum() - 1; VEnd >= VStart ; --VEnd) {
                if (max > vBounds[VEnd] && max <= vBounds[VEnd + 1]) break;
            }
            results[i] = simpleIntegration(V, VStart, VEnd, a, primaryBounds[i], primaryBounds[i + 1]);
        }
        return results;
    }

    private static PolynomialFunctionPiece simpleIntegration(PiecewisePolynomialFunction V, int VStart, int VEnd, StochasticPolynomialFunction a, double gLeft, double gRight) {
        AdvancedPolynomialFunction apf = AdvancedPolynomialFunction.ZERO();
        AdvancedPolynomialFunction upperBound, lowerBound, uc, lc;
        lowerBound = AdvancedPolynomialFunction.ZERO();
        double[] vBounds = V.getBounds();
        for (int i = VStart; i < VEnd; ++i) {
            double[] coefs = a.getDeterminedPart().getCoefficients();
            coefs[0] -= vBounds[i + 1];
            for (int j = 0; j < coefs.length; ++j) {
                coefs[j] /= -a.getAdvancedPolynomialFunctionCoefficients()[1].getCoefficients()[0]; // we assume that a(t,xi) = f(t) + b*xi, and here the divider is -b;
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

    private static class PiecewisePolynomialFunctionAndSelectedIndices {
        private PiecewisePolynomialFunction ppf;
        private int[] ids;

        public PiecewisePolynomialFunctionAndSelectedIndices(PiecewisePolynomialFunction ppf, int[] selectedIDs) {
            this.ppf = ppf;
            this.ids = selectedIDs;
        }

        public PiecewisePolynomialFunction getPiecewisePolynomialFunction() {
            return ppf;
        }

        public int[] getSelectedIDs() {
            return ids;
        }
    }
}
