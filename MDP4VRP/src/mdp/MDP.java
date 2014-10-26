package mdp;

import functional.AdvancedPolynomialFunction;
import functional.PiecewisePolynomialFunction;
import functional.PiecewiseStochasticPolynomialFunction;
import functional.StochasticPolynomialFunction;
import vrp.Task;

import java.awt.*;
import java.util.*;
import java.util.List;
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
    private Set<State> stateSet = new LinkedHashSet<State>();
    private List<ConcurrentMap<Set<Task>, Set<State>>> moduleMapList = new ArrayList<ConcurrentMap<Set<Task>, Set<State>>>();
    private ConcurrentMap<State, PiecewisePolynomialFunction> valueFuncMap = new ConcurrentHashMap<State, PiecewisePolynomialFunction>();
    private ConcurrentMap<State, Policy> policyMap = new ConcurrentHashMap<State, Policy>();

    public MDP(State startState, State endState, PiecewisePolynomialFunction terminatedValueFunction) {
        this.startState = startState;
        this.endState = endState;
        this.terminatedValueFunction = terminatedValueFunction;
        incomingArcs.putIfAbsent(startState, new ArrayList<Arc>());
        outgoingArcs.putIfAbsent(startState, new ArrayList<Arc>());
        incomingArcs.putIfAbsent(endState, new ArrayList<Arc>());
        outgoingArcs.putIfAbsent(endState, new ArrayList<Arc>());
        stateSet.add(startState);
        stateSet.add(endState);
        for (int i = 0; i <= startState.getTaskSet().size(); ++i) {
            moduleMapList.add(new ConcurrentHashMap<Set<Task>, Set<State>>());
        }
    }

    public void buildGraph() {
        Queue<State> checkingQueue = new LinkedList<State>();
        Set<State> checkedStates = new HashSet<State>();
        checkingQueue.add(startState);
        checkedStates.add(startState);
        checkedStates.add(endState);
        moduleMapList.get(startState.getTaskSet().size()).putIfAbsent(startState.getTaskSet(), new LinkedHashSet<State>());
        moduleMapList.get(startState.getTaskSet().size()).get(startState.getTaskSet()).add(startState);
        while (!checkingQueue.isEmpty()) {
            State currentState = checkingQueue.poll();
            possibleActions.putIfAbsent(currentState, currentState.getPossibleActions());
//            System.out.println(currentState.toString()+":"+possibleActions.get(currentState).size());
//            if (currentState.equals(endState)) continue;
            for (Action a : possibleActions.get(currentState)) {
                State nextState = a.perform(currentState); // System.out.println("next state: " + nextState.toString());
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
                    stateSet.add(nextState);
                    moduleMapList.get(nextState.getTaskSet().size()).putIfAbsent(nextState.getTaskSet(), new LinkedHashSet<State>());
                    moduleMapList.get(nextState.getTaskSet().size()).get(nextState.getTaskSet()).add(nextState);
                }
            }
        }
//        System.out.println("size: " + moduleMapList.get(startState.getTaskSet().size()).size());
    }

    public String graphToString() {
        StringBuilder s = new StringBuilder();
//        System.out.println(stateSet.size());
//        for (State state : stateSet) {
//            System.out.println(state.toString());
//        }
        for (State state : stateSet) {
//            System.out.println("state: " + state.toString() + " outgoing arc: " + outgoingArcs.get(state).size());
            for (Arc arc : outgoingArcs.get(state)) {
                s.append(state.toString());
                s.append(new char[]{'-', '>'});
                s.append(arc.getEndState().toString());
                s.append(':');
                s.append(arc.getAction().toString());
                s.append('\n');
            }
        }
        return s.toString();
    }

    public void assignValueFunction() {
        valueFuncMap.put(endState, terminatedValueFunction);
        System.out.println("++endstate: " + endState + "\nppf:\n" + valueFuncMap.get(endState));
        for (Arc arc : incomingArcs.get(endState)) {
            if (arc.getAction() instanceof Move) {
                valueFuncMap.put(arc.getStartState(), arc.getAction().preValueFunc(valueFuncMap.get(endState)));
                policyMap.put(arc.getStartState(), Policy.SimplePolicy(arc.getAction()));
                System.out.println("++state: " + arc.getStartState() + "\nppf:\n" + valueFuncMap.get(arc.getStartState()));
            }
        }
        for (int level = 0; level < moduleMapList.size(); ++level) {
            ConcurrentMap<Set<Task>, Set<State>> moduleTaskMap = moduleMapList.get(level);
            for (Set<Task> set : moduleTaskMap.keySet()) {
                MDP.moduleSolver(moduleTaskMap.get(set), valueFuncMap, policyMap, incomingArcs, level);
            }
        }


//        LinkedList<Arc> checkingList = new LinkedList<Arc>();
//        for (Arc arc : incomingArcs.get(endState)) {
//            checkingList.addLast(arc);
//        }
////        Stack<State> checkingStack = new Stack<State>();
////        checkingStack.push(endState);
//        Set<State> checkedStates = new HashSet<State>();
//        Map<State, Set<Arc>> unCheckedArcs = new HashMap<State, Set<Arc>>(); // Save arcs that have not been moved to checkingList
//        unCheckedArcs.put(endState, new HashSet<Arc>()); // 'Cause arcs to endState have been moved to checkingList
//
//        Map<State, PiecewisePolynomialFunction> currentBestValueFunction = new HashMap<State, PiecewisePolynomialFunction>();
//        Map<State, Policy> currentBestPolicy = new HashMap<State, Policy>();
//        currentBestValueFunction.put(endState, terminatedValueFunction);
//        currentBestPolicy.put(endState, null);
//
//        while (!checkingList.isEmpty()) {
//            Arc arc = checkingList.pollLast();
////            State currentState = checkingStack.pop();
////            for (Arc arc : incomingArcs.get(currentState)) {
//            State preState = arc.getStartState();
//            PiecewisePolynomialFunction preValueFunc = arc.getAction().preValueFunc(currentBestValueFunction.get(arc.getEndState()));
//            Policy prePolicy = Policy.SimplePolicy(arc.getAction());
//            if (currentBestValueFunction.containsKey(preState)) {
//                PiecewisePolynomialFunctionAndPolicy ppfap = max(preValueFunc, currentBestValueFunction.get(preState), prePolicy, currentBestPolicy.get(preState));
//                currentBestValueFunction.put(preState, ppfap.getPiecewisePolynomialFunction());
//                currentBestPolicy.put(preState, ppfap.getPolicy());
//            } else {
//                currentBestValueFunction.put(preState, preValueFunc);
//                currentBestPolicy.put(preState, prePolicy);
//            }
////            }
//        }
    }

    public String valueFunctionToString() {
        StringBuilder s = new StringBuilder();
        for (State state : stateSet) {
            s.append(state.toString());
            s.append('\n');
            s.append(valueFuncMap.get(state).toString());
        }
        return s.toString();
    }

    public static void moduleSolver(Set<State> module,
                                    ConcurrentMap<State, PiecewisePolynomialFunction> valueFuncMap,
                                    ConcurrentMap<State, Policy> policyMap,
                                    ConcurrentMap<State, List<Arc>> incomingArcs,
                                    int level) {
//        State[] states = module.keySet().toArray(new State[module.size()]);
        LinkedHashSet<State> iteratorSet = new LinkedHashSet<State>();
        LinkedHashSet<State> nextIteratorSet = new LinkedHashSet<State>();
        iteratorSet.addAll(module);
        while (!iteratorSet.isEmpty()) {
            for (State currentState : iteratorSet) {
                for (Arc arc : incomingArcs.get(currentState)) {
                    State preState = arc.getStartState();
                    PiecewisePolynomialFunction preValueFunc = valueFuncMap.get(preState);
                    Policy prePolicy = policyMap.get(preState);
                    PiecewisePolynomialFunction newPreValueFunc = arc.getAction().preValueFunc(valueFuncMap.get(currentState));
                    System.out.println("***");
                    System.out.println("prestate: " + preState.toString() + " currentstate: " + currentState);
                    System.out.println("***");
                    PiecewisePolynomialFunctionAndPolicy maxResult = MDP.max(preValueFunc, newPreValueFunc, policyMap.get(preState), Policy.SimplePolicy(arc.getAction()));
                    if (!maxResult.getPiecewisePolynomialFunction().equals(preValueFunc) || !maxResult.getPolicy().equals(prePolicy)) {
                        valueFuncMap.put(preState, maxResult.getPiecewisePolynomialFunction());
                        policyMap.put(preState, maxResult.getPolicy());
                        if (preState.getTaskSet().size() == level) nextIteratorSet.add(preState);
                    }
                }
            }
            iteratorSet = nextIteratorSet;
            nextIteratorSet = new LinkedHashSet<State>();
        }
    }

    public static PiecewisePolynomialFunctionAndPolicy max(PiecewisePolynomialFunctionAndPolicy ppfap1, PiecewisePolynomialFunctionAndPolicy ppfap2) {
        return MDP.max(ppfap1.getPiecewisePolynomialFunction(), ppfap2.getPiecewisePolynomialFunction(), ppfap1.getPolicy(), ppfap2.getPolicy());
    }

    public static PiecewisePolynomialFunctionAndPolicy max(PiecewisePolynomialFunction ppf1, PiecewisePolynomialFunction ppf2, Policy policy1, Policy policy2) {
//        if (ppf1 == null) {
//            return new PiecewisePolynomialFunctionAndPolicy(ppf2, policy2);
//        }
//        if (ppf2 == null) {
//            return new PiecewisePolynomialFunctionAndPolicy(ppf1, policy1);
//        }
        double[] bounds1 = ppf1.getBounds();
        double[] bounds2 = ppf2.getBounds();
        int pieces1 = ppf1.getPieceNum();
        int pieces2 = ppf2.getPieceNum();
        if (bounds1[0] != bounds2[0] || bounds1[pieces1] != bounds2[pieces2]) {
            System.out.println("***");
            System.out.println("pff1:\n" + ppf1.toString() + "ppf2:\n" + ppf2.toString());
            System.out.println("***");
            throw new IllegalArgumentException();
        }
        double[] newBounds;
        int newPiece;
        int n, i, j;
        List<Double> tmpBounds = new ArrayList<Double>();
        n = 0;
        i = 0;
        j = 0;
        double lastBound = bounds1[0], nextBound;
        double[] roots;
        double v;
        while (i < pieces1 && j < pieces2) {
            tmpBounds.add(lastBound);
            if (bounds1[i + 1] < bounds2[j + 1]) { // System.out.println("<");
                nextBound = bounds1[i + 1];
                roots = ppf1.getPolynomialFunction(i).subtract(ppf2.getPolynomialFunction(j)).solve(tmpBounds.get(n), nextBound);
                ++i;
            } else if (bounds2[j + 1] < bounds1[i + 1]) { // System.out.println(">");
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
        newPiece = n;
        newBounds = new double[n + 1];
        newBounds[0] = tmpBounds.get(0);
        newBounds[newPiece] = bounds1[pieces1]; // 'cause bounds[pieces] = ppf.bounds[ppf.pieces]
        AdvancedPolynomialFunction[] pfs = new AdvancedPolynomialFunction[newPiece];
        int[] selectedIDs = new int[newPiece];
        i = 0;
        j = 0;
        for (n = 1; n < newPiece; ++n) {
            newBounds[n] = tmpBounds.get(n);
            v = (newBounds[n - 1] + tmpBounds.get(n)) / 2;
            if (ppf1.getPolynomialFunction(i).value(v) > ppf2.getPolynomialFunction(j).value(v)) {
                pfs[n - 1] = new AdvancedPolynomialFunction(ppf1.getPolynomialFunction(i).getCoefficients());
                selectedIDs[n - 1] = 0;
            } else {
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
        } else {
            pfs[newPiece - 1] = new AdvancedPolynomialFunction(ppf2.getPolynomialFunction(j).getCoefficients());
            selectedIDs[newPiece - 1] = 1;
        }
        return new PiecewisePolynomialFunctionAndPolicy(new PiecewisePolynomialFunction(pfs, newBounds), Policy.union(policy1, policy2, newBounds, selectedIDs));
    }

    public static PiecewisePolynomialFunction integrationOnXiOfComposition_test(PiecewisePolynomialFunction V, PiecewiseStochasticPolynomialFunction A) {
        // only for calculating: Int f(x)*V(A(t)) dx; x:0~1; A(t)=t+g(t); g>0 -> t+g(t)>t
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
            System.out.println("i: " + i + " pfp: " + pfp.length);
            for (; j < pfp.length; ++j) {
                pfsList.add(pfp[j].getPolynomialFunction());
                boundsList.add(pfp[j].getBounds()[0]);
            }
        }
        boundsList.add(pfp[pfp.length - 1].getBounds()[1]);
        System.out.println("bounds list: ");
        for (Double d : boundsList) System.out.print(d + ", ");
        System.out.println();
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

    private static class PiecewisePolynomialFunctionAndPolicy {
        private PiecewisePolynomialFunction ppf;
        private Policy policy;
//        private int[] ids;

        public PiecewisePolynomialFunctionAndPolicy(PiecewisePolynomialFunction ppf, Policy policy) {
            this.ppf = ppf;
            this.policy = policy;
        }

        public PiecewisePolynomialFunction getPiecewisePolynomialFunction() {
            return ppf;
        }

        public Policy getPolicy() {
            return policy;
        }

//        public int[] getSelectedIDs() {
//            return ids;
//        }
    }
}
