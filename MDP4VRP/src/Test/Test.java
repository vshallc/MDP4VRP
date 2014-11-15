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
        rewards[0] = new PiecewisePolynomialFunction(apfs, bounds); //System.out.println("reward0 : " + rewards[0].toString());
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

        System.out.println("============================== INPUT ==============================");
        System.out.println("Graph {(0,1),(0,2),(1,2)}");
        for (int i = 0; i < 2; ++i) {
            for (int j = i + 1; j < 3; j++) {
                System.out.println("Edge: (" + i + "," + j + ") time cost function: \n" +edges[i][j].getTimeCostFunction() + "\n");
            }
        }
        System.out.println("Tasks:");
        for (Task task : tasks) {
            System.out.println("Location: " + task.getLocation().getID());
            System.out.println("Reward Function: \n" + task.getRewardFunc());
            System.out.println("Penalty: " + task.getPenalty());
            System.out.println();
        }
        System.out.println("============================== OUTPUT ==============================");
        System.out.println("MDP Graph:");
        System.out.println(mdp.valueFunctionToString());
        System.out.println("MDP Policies:");
        System.out.println(mdp.policyToString());
    }

    public Test(int x) {
        AdvancedPolynomialFunction[] apfs = new AdvancedPolynomialFunction[10];
        double[] c = new double[1];
        c[0] = 200;
        apfs[0] = new AdvancedPolynomialFunction(c);
        c = new double[3];
        c[0] = -17800;
        c[1] = 300;
        c[2] = -1.25;
        apfs[1] = new AdvancedPolynomialFunction(c);
        c = new double[3];
        c[0] = -8034.375;
        c[1] = 143.75;
        c[2] = -0.625;
        apfs[2] = new AdvancedPolynomialFunction(c);
        c = new double[2];
        c[0] = 2046.25;
        c[1] = -15;
        apfs[3] = new AdvancedPolynomialFunction(c);
        c = new double[4];
        c[0] = -22336.08333333333;
        c[1] = 528.8125;
        c[2] = -4.09375;
        c[3] = 0.01041666666666667;
        apfs[4] = new AdvancedPolynomialFunction(c);
        c = new double[4];
        c[0] = -10893.374999999996;
        c[1] = 264.75;
        c[2] = -2.0625;
        c[3] = 0.005208333333333335;
        apfs[5] = new AdvancedPolynomialFunction(c);
        c = new double[2];
        c[0] = 1085.625;
        c[1] = -7.5;
        apfs[6] = new AdvancedPolynomialFunction(c);
        c = new double[3];
        c[0] = 5197.251302083334;
        c[1] = -63.03125;
        c[2] = 0.1875;
        apfs[7] = new AdvancedPolynomialFunction(c);
        c = new double[1];
        c[0] = -100;
        apfs[8] = new AdvancedPolynomialFunction(c);
        c = new double[1];
        c[0] = -1100;
        apfs[9] = new AdvancedPolynomialFunction(c);
        double[] bounds = {0.0, 120.0, 125.0, 127.0, 128.12819439667624, 130.0, 132.0, 148.08333333333334, 168.08333333333334, 300, Double.POSITIVE_INFINITY};
        PiecewisePolynomialFunction ppf = new PiecewisePolynomialFunction(apfs, bounds);

        StochasticPolynomialFunction[] spf = new StochasticPolynomialFunction[2];
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
        PiecewiseStochasticPolynomialFunction move = new PiecewiseStochasticPolynomialFunction(spf, bounds);
        Edge edge = new Edge(new Node(2), new Node(0), move);
        System.out.println("||||||||||||||||||Start|||||||||||||");
        PiecewisePolynomialFunction result = MDP.integrationOnXiOfComposition_test(ppf, edge.getArrivalFunction());
        System.out.println();
        System.out.println(ppf);
        System.out.println();
        System.out.println(edge.getArrivalFunction());
        System.out.println();
        System.out.println(result);
        System.out.println();
//        System.out.println(ppf.getPolynomialFunction(1).value(106.66666666666667));
//        System.out.println(result.getPolynomialFunction(1).value(70.66666666666667));
        System.out.println("||||||||||||||||||End|||||||||||||");
    }

    public Test(String x) {
        AdvancedPolynomialFunction[] apfs = new AdvancedPolynomialFunction[3];
        double[] c, bounds;
        c = new double[]{500};
        apfs[0] = new AdvancedPolynomialFunction(c);
        c = new double[]{1000, -6, 0.01};
//        c = new double[]{300};
        apfs[1] = new AdvancedPolynomialFunction(c);
        c = new double[]{800};
        apfs[2] = new AdvancedPolynomialFunction(c);
        bounds = new double[]{0.0, 100.0, 400.0, Double.POSITIVE_INFINITY};
        PiecewisePolynomialFunction ppf = new PiecewisePolynomialFunction(apfs, bounds);
        Policy policy = Policy.SimplePolicy(new DoNothing(1));
        MDP.PiecewisePolynomialFunctionAndPolicy ppfap = MDP.addWait(ppf, policy);
        System.out.println(ppfap.getPiecewisePolynomialFunction());
        System.out.println(ppfap.getPolicy());

    }

    public static void main(String[] args) {
        new Test();
    }
}
