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
        System.out.println("assign done");
        System.out.println(mdp.valueFunctionToString());
    }

    public Test(int x) {
        AdvancedPolynomialFunction[] apfs = new AdvancedPolynomialFunction[2];
        double[] c = new double[1];
        c[0] = 0;
        apfs[0] = new AdvancedPolynomialFunction(c);
        c = new double[3];
        c[0] = 1;
        c[1] = 2;
        c[2] = 3;
        apfs[1] = new AdvancedPolynomialFunction(c);
//        c = new double[2];
//        c[0] = 5625;
//        c[1] = -25;
//        apfs[2] = new AdvancedPolynomialFunction(c);
//        c = new double[3];
//        c[0] = 46265.625;
//        c[1] = -343.75;
//        c[2] = 0.625;
//        apfs[3] = new AdvancedPolynomialFunction(c);
//        c = new double[1];
//        c[0] = -1000;
//        apfs[4] = new AdvancedPolynomialFunction(c);
        double[] bounds = {0.0, 230, Double.POSITIVE_INFINITY};
        PiecewisePolynomialFunction ppf = new PiecewisePolynomialFunction(apfs, bounds);

        PiecewisePolynomialFunction shiftResult = ppf.shift(40);
        System.out.println(shiftResult);
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
