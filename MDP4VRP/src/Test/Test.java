package Test;

import functional.AdvancedPolynomialFunction;
import functional.PiecewisePolynomialFunction;
import functional.PiecewiseStochasticPolynomialFunction;
import functional.StochasticPolynomialFunction;
import mdp.*;
import vrp.Edge;
import vrp.Node;
import vrp.Task;

import java.util.*;

/**
 * Created by Xiaoxi Wang on 7/10/14.
 */
public class Test {

    public Test() {
        // VRP setup
        Node[] nodes = new Node[3];
        for (int i = 0; i < 3; ++i) {
            nodes[i] = new Node(i);
        }
        Edge[][] edges = new Edge[3][3];
        PiecewiseStochasticPolynomialFunction[][] movingTimeCost = new PiecewiseStochasticPolynomialFunction[3][3];
        StochasticPolynomialFunction[] spf = new StochasticPolynomialFunction[2];
        double[] bounds;
        AdvancedPolynomialFunction[] apfs = new AdvancedPolynomialFunction[2];
        double[] c = new double[2];
        c[0] = 10;
        c[1] = 1/3.0;
        apfs[0] = new AdvancedPolynomialFunction(c); // 1/3t + 10
        apfs[1] = AdvancedPolynomialFunction.N(20);
        spf[0] = new StochasticPolynomialFunction(apfs); // 1/3t + 10 + 20xi
        apfs = new AdvancedPolynomialFunction[2];
        apfs[0] = AdvancedPolynomialFunction.N(50);
        apfs[1] = AdvancedPolynomialFunction.N(20);
        spf[1] = new StochasticPolynomialFunction(apfs); // 50 + 20xi
        bounds = new double[3];
        bounds[0] = 0;
        bounds[1] = 120;
        bounds[2] = Double.POSITIVE_INFINITY;
        movingTimeCost[0][1] = new PiecewiseStochasticPolynomialFunction(spf, bounds);
        movingTimeCost[1][0] = new PiecewiseStochasticPolynomialFunction(spf, bounds);

        spf = new StochasticPolynomialFunction[2];
        apfs = new AdvancedPolynomialFunction[2];
        c = new double[2];
        c[0] = 50;
        c[1] = -0.25;
        apfs[0] = new AdvancedPolynomialFunction(c);
        apfs[1] = AdvancedPolynomialFunction.N(20);
        spf[0] = new StochasticPolynomialFunction(apfs); // -1/4 + 50 + 20xi
        apfs = new AdvancedPolynomialFunction[2];
        apfs[0] = AdvancedPolynomialFunction.N(25);
        apfs[1] = AdvancedPolynomialFunction.N(20);
        spf[1] = new StochasticPolynomialFunction(apfs); // 25 + 20xi
        bounds = new double[3];
        bounds[0] = 0;
        bounds[1] = 100;
        bounds[2] = Double.POSITIVE_INFINITY;
        movingTimeCost[0][2] = new PiecewiseStochasticPolynomialFunction(spf, bounds);
        movingTimeCost[2][0] = new PiecewiseStochasticPolynomialFunction(spf, bounds);

        spf = new StochasticPolynomialFunction[1];
        apfs = new AdvancedPolynomialFunction[2];
        apfs[0] = AdvancedPolynomialFunction.ZERO();
        apfs[1] = AdvancedPolynomialFunction.N(40);
        spf[0] = new StochasticPolynomialFunction(apfs);
        bounds = new double[2];
        bounds[0] = 0;
        bounds[1] = Double.POSITIVE_INFINITY;
        movingTimeCost[1][2] = new PiecewiseStochasticPolynomialFunction(spf, bounds);
        movingTimeCost[2][1] = new PiecewiseStochasticPolynomialFunction(spf, bounds);

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                if (i == j) continue;
                edges[i][j] = new Edge(nodes[i], nodes[j], movingTimeCost[i][j]);
            }
        }

        int taskNum = 1;
        Task[] tasks = new Task[taskNum];
        PiecewisePolynomialFunction[] rewards = new PiecewisePolynomialFunction[taskNum];
        apfs = new AdvancedPolynomialFunction[2];
        apfs[0] = AdvancedPolynomialFunction.N(200);
        apfs[1] = AdvancedPolynomialFunction.N(Double.NEGATIVE_INFINITY);
        bounds = new double[3];
        bounds[0] = 0;
        bounds[1] = 200;
        bounds[2] = Double.POSITIVE_INFINITY;
        rewards[0] = new PiecewisePolynomialFunction(apfs, bounds);//System.out.println("reward0 : " + rewards[0].toString());
        tasks[0] = new Task(0, nodes[1], rewards[0], 40, -100);

        // MDP setup
        Set<Task> taskSet = new HashSet<Task>(Arrays.asList(tasks));
        State startState = new State(nodes[0], taskSet);
        State endState = new State(nodes[0], new HashSet<Task>());
        apfs = new AdvancedPolynomialFunction[2];
        apfs[0] = AdvancedPolynomialFunction.ZERO();
        apfs[1] = AdvancedPolynomialFunction.N(-1000);
        bounds = new double[3];
        bounds[0] = 0;
        bounds[1] = 300;
        bounds[2] = Double.POSITIVE_INFINITY;
        PiecewisePolynomialFunction terminatedValueFunction = new PiecewisePolynomialFunction(apfs, bounds);
        MDP mdp = new MDP(startState, endState, terminatedValueFunction);
//        System.out.println("-=-=-=-=-=-=-");
//        PiecewisePolynomialFunction testppf = MDP.integrationOnXiOfComposition_test(terminatedValueFunction, edges[2][0].getArrivalFunction());
//        System.out.println("testppf: " + testppf.toString());
        System.out.println("=================");
        mdp.buildGraph();
//        System.out.println(mdp.graphToString());
        System.out.println("=================");
        mdp.assignValueFunction();
        System.out.println(mdp.valueFunctionToString());



//        Node n1 = new Node();
//        Node n2 = new Node();
//        Set<Task> ts1 = new HashSet<Task>();
//        Set<Task> ts2 = new HashSet<Task>();
//        ts2.addAll(ts1);
//        System.out.println("ts: " + ts1.equals(ts2));
//        System.out.println(ts1.hashCode() + " " + ts2.hashCode());
//        State s1 = new State(n1, ts1);
//        State s2 = new State(n1, ts1);
//        State s3 = new State(n2, ts2);
//        System.out.println("hc: " + s3.hashCode());

//        double[] c = new double[3];
//        AdvancedPolynomialFunction[] apf = new AdvancedPolynomialFunction[1];
//        double[] b = new double[2];
//        // f(x) = 5 + 4x + 3x^2 + 2x^3
//        c[0] = 5;
//        c[1] = 4;
//        c[2] = 3;
//        c[3] = 2;
//        AdvancedPolynomialFunction apf = new AdvancedPolynomialFunction(c);
//        AdvancedPolynomialFunction apf_shift = apf.shift(1);
//        double[] c2 = new double[2];
//        c2[0] = 1;
//        c2[1] = 1;
//        AdvancedPolynomialFunction s = new AdvancedPolynomialFunction(c2);
//        AdvancedPolynomialFunction s2 = apf.compose(s);
//        System.out.println(apf.value(1));
//        System.out.println(apf_shift.value(0));
//        System.out.println(s2.value(0));

//        c[0] = 5;
//        c[1] = -4;
//        c[2] = 1;
//        apf[0] = new AdvancedPolynomialFunction(c);
//        b[0] = 0;
//        b[1] = Double.POSITIVE_INFINITY;
//        PiecewisePolynomialFunction ppf1 = new PiecewisePolynomialFunction(apf, b);
//
//        c[0] = 0;
//        c[1] = 4;
//        c[2] = -1;
//        apf[0] = new AdvancedPolynomialFunction(c);
//        PiecewisePolynomialFunction ppf2 = new PiecewisePolynomialFunction(apf, b);
//        PiecewisePolynomialFunction result = MDP.max(ppf1, ppf2).getPiecewisePolynomialFunction();
//
//        System.out.println("f1(x)=" + ppf1.toString());
//        System.out.println("f2(x)=" + ppf2.toString());
//        System.out.println(result.toString());


//        Action[] actions1 = new Action[1];
//        double[] bounds1 = new double[2];
//        actions1[0] = new DoNothing(0);
////        actions1[1] = new DoNothing(1);
////        actions1[2] = new DoNothing(2);
//        bounds1[0] = 0.0;
////        bounds1[1] = 100.0;
////        bounds1[2] = 200.0;
//        bounds1[1] = Double.POSITIVE_INFINITY;
//
//        Action[] actions2 = new Action[1];
//        double[] bounds2 = new double[2];
//        actions2[0] = new DoNothing(3);
////        actions2[1] = new DoNothing(4);
//        bounds2[0] = 0;
////        bounds2[1] = 150;
//        bounds2[1] = Double.POSITIVE_INFINITY;
//
//
//        Policy p1 = new Policy(actions1, bounds1);
//        Policy p2 = new Policy(actions2, bounds2);
//        double[] ubounds = new double[3];
//        int[] id = new int[2];
//        ubounds[0] = 0;
//        ubounds[1] = 150;
//        ubounds[2] = Double.POSITIVE_INFINITY;
//        id[0] = 0;
//        id[1] = 1;
//
//        Policy pu = Policy.union(p1, p2, ubounds, id);
//        System.out.println("actions:");
//        for (Action a : pu.getActions()) {
//            System.out.print(((DoNothing) a).getId() + ", ");
//        }
//        System.out.println();
//
//        System.out.println("bounds:");
//        for (double d : pu.getBounds()) {
//            System.out.print(d + ", ");
//        }
//        System.out.println();
    }

    public Test(int x) {
        AdvancedPolynomialFunction[] apfs = new AdvancedPolynomialFunction[3];
        double[] c = new double[1];
        c[0] = 0;
        apfs[0] = new AdvancedPolynomialFunction(c);
        c = new double[2];
        c[0] = 12750;
        c[1] = -50;
        apfs[1] = new AdvancedPolynomialFunction(c);
        c = new double[1];
        c[0] = -1000;
        apfs[2] = new AdvancedPolynomialFunction(c);
        double[] bounds = {0.0, 255.0, 275.0, Double.POSITIVE_INFINITY};
        PiecewisePolynomialFunction ppf = new PiecewisePolynomialFunction(apfs, bounds);

        StochasticPolynomialFunction[] spf = new StochasticPolynomialFunction[1];
        apfs = new AdvancedPolynomialFunction[2];
        apfs[0] = AdvancedPolynomialFunction.ZERO();
        apfs[1] = AdvancedPolynomialFunction.N(40);
        spf[0] = new StochasticPolynomialFunction(apfs);
        bounds = new double[2];
        bounds[0] = 0;
        bounds[1] = Double.POSITIVE_INFINITY;
        PiecewiseStochasticPolynomialFunction g = new PiecewiseStochasticPolynomialFunction(spf, bounds);
        System.out.println("ppf:\n" + ppf.toString());
//        System.out.println("g:\n" + g.toString());
        Edge edge = new Edge(new Node(1), new Node(2), g);
        System.out.println("edge:\n" + edge.getArrivalFunction().toString());
        PiecewisePolynomialFunction result = MDP.integrationOnXiOfComposition_test(ppf, edge.getArrivalFunction());
        System.out.println("result:\n" + result);
    }

    public Test(String s) {
        Map<Integer, Double> map = new LinkedHashMap<Integer, Double>();
        for (int i = 0; i < 5; ++i) {
            map.put(i, i / 10.0);
        }
        for (int i : map.keySet()) {
            System.out.println(i + ":" + map.get(i));
        }
        Map<Integer, Double> newMap = testMap(map);
        System.out.println("========old=======");
        for (int i : map.keySet()) {
            System.out.println(i + ":" + map.get(i));
        }
        System.out.println("========new=======");
        for (int i : newMap.keySet()) {
            System.out.println(i + ":" + newMap.get(i));
        }
    }

    public static Map<Integer, Double> testMap(Map<Integer, Double> map) {
        for (Integer i : map.keySet()) {
            double d = map.get(i);
            map.put(i, d * 10);
        }
        return map;
    }

    public static void main(String[] args) {
        new Test();
    }
}
