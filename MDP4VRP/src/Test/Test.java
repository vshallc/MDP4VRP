package Test;

import functional.AdvancedPolynomialFunction;
import functional.PiecewisePolynomialFunction;
import functional.PiecewiseStochasticPolynomialFunction;
import functional.StochasticPolynomialFunction;
import mdp.*;
import vrp.Edge;
import vrp.Node;
import vrp.Task;
import vrp.VRP;

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
        AdvancedPolynomialFunction[] apfs = new AdvancedPolynomialFunction[3];
        double[] c = new double[1];
        c[0] = 0;
        apfs[0] = new AdvancedPolynomialFunction(c);
        c = new double[2];
        c[0] = 169994.94222630217;
        c[1] = -173.60402585583728;
        apfs[1] = new AdvancedPolynomialFunction(c);
        c = new double[1];
        c[0] = -500;
        apfs[2] = new AdvancedPolynomialFunction(c);
//        c = new double[1];
//        c[0] = 0;
//        apfs[3] = new AdvancedPolynomialFunction(c);
//        c = new double[4];
//        c[0] = 3.241386965271372E8;
//        c[1] = -1171275.2074975066;
//        c[2] = 1410.8010206709682;
//        c[3] = -0.5664366231453438;
//        apfs[4] = new AdvancedPolynomialFunction(c);
//        c = new double[1];
//        c[0] = -3.590997271705419;
//        apfs[5] = new AdvancedPolynomialFunction(c);
//        c = new double[3];
//        c[0] = -2172607.78336789;
//        c[1] = 5227.982835213337;
//        c[2] = -3.145047167323087;
//        apfs[6] = new AdvancedPolynomialFunction(c);
//        c = new double[1];
//        c[0] = -128.26387411355972;
//        apfs[7] = new AdvancedPolynomialFunction(c);
//        c = new double[4];
//        c[0] = -3.3492407498029983E8;
//        c[1] = 1197160.5398220376;
//        c[2] = -1426.3325814683492;
//        c[3] = 0.5664366231453438;
//        apfs[8] = new AdvancedPolynomialFunction(c);
//        c = new double[1];
//        c[0] = -209.52998865954578;
//        apfs[9] = new AdvancedPolynomialFunction(c);
//        c = new double[3];
//        c[0] = -8688334.650372097;
//        c[1] = 20747.636565658493;
//        c[2] = -12.386513630057562;
//        apfs[10] = new AdvancedPolynomialFunction(c);
//        c = new double[2];
//        c[0] = 38274.618967141425;
//        c[1] = -45.84940075484897;
//        apfs[11] = new AdvancedPolynomialFunction(c);
//        c = new double[1];
//        c[0] = -290.4700113348663;
//        apfs[12] = new AdvancedPolynomialFunction(c);
//        c = new double[3];
//        c[0] = 8801630.172244098;
//        c[1] = -20883.068313623775;
//        c[2] = 12.386513630057562;
//        apfs[13] = new AdvancedPolynomialFunction(c);
//        c = new double[1];
//        c[0] = -371.7361256480217;
//        apfs[14] = new AdvancedPolynomialFunction(c);
//        c = new double[3];
//        c[0] = 2268268.9802467627;
//        c[1] = -5342.427400038907;
//        c[2] = 3.145047167323087;
//        apfs[15] = new AdvancedPolynomialFunction(c);
//        c = new double[4];
//        c[0] = 3.481880670811206E8;
//        c[1] = -1228517.5573635008;
//        c[2] = 1444.8640109031858;
//        c[3] = -0.5664366231453438;
//        apfs[16] = new AdvancedPolynomialFunction(c);
//        c = new double[1];
//        c[0] = -500;
//        apfs[17] = new AdvancedPolynomialFunction(c);
//        c = new double[1];
//        c[0] = -500;
//        apfs[18] = new AdvancedPolynomialFunction(c);

        double[] bounds = {0.0,
                979.2108298655924,
                982.090947406272,
                Double.POSITIVE_INFINITY};
        PiecewisePolynomialFunction ppf = new PiecewisePolynomialFunction(apfs, bounds);
        Policy policy = Policy.SimplePolicy(new DoNothing(0));
//        MDP.PiecewisePolynomialFunctionAndPolicy wait = MDP.addWait(ppf, policy);
//        System.out.println(wait.getPiecewisePolynomialFunction());

        StochasticPolynomialFunction[] spf = new StochasticPolynomialFunction[2];
        double cxi = 9.49957014836607;

        apfs = new AdvancedPolynomialFunction[2];
        c = new double[2];
        c[0] = 73.20558758547824;
        c[1] = 0.0322662432173596;
        apfs[0] = new AdvancedPolynomialFunction(c);
        apfs[1] = AdvancedPolynomialFunction.N(cxi);
        spf[0] = new StochasticPolynomialFunction(apfs);

        apfs = new AdvancedPolynomialFunction[2];
        c = new double[1];
        c[0] = 92.27223296957851;
        apfs[0] = new AdvancedPolynomialFunction(c);
        apfs[1] = AdvancedPolynomialFunction.N(cxi);
        spf[1] = new StochasticPolynomialFunction(apfs);

//        apfs = new AdvancedPolynomialFunction[2];
//        c = new double[1];
//        c[0] = 92.27223296957851;
//        apfs[0] = new AdvancedPolynomialFunction(c);
//        apfs[1] = AdvancedPolynomialFunction.N(cxi);
//        spf[2] = new StochasticPolynomialFunction(apfs);

//        apfs = new AdvancedPolynomialFunction[2];
//        c = new double[1];
//        c[0] = 76.69857769240343;
//        apfs[0] = new AdvancedPolynomialFunction(c);
//        apfs[1] = AdvancedPolynomialFunction.N(cxi);
//        spf[3] = new StochasticPolynomialFunction(apfs);

        bounds = new double[3];
        bounds[0] = 0;
        bounds[1] = 590.9161861719992;
//        bounds[2] = 1000.0;
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
        System.out.println("||||||||||||||||||End|||||||||||||");
    }

    public Test(int a, int b) {
        AdvancedPolynomialFunction[] apfs = new AdvancedPolynomialFunction[7];
        double[] c = new double[1];
        c[0] = 0;
        apfs[0] = new AdvancedPolynomialFunction(c);
        c = new double[1];
        c[0] = 1.862645149230957E-9;
        apfs[1] = new AdvancedPolynomialFunction(c);
        c = new double[3];
        c[0] = -1.2997693732736748E7;
        c[1] = 28651.754011555513;
        c[2] = -15.789782110942168;
        apfs[2] = new AdvancedPolynomialFunction(c);
        c = new double[2];
        c[0] = 113542.20575466196;
        c[1] = -124.87252276457463;
        apfs[3] = new AdvancedPolynomialFunction(c);
        c = new double[1];
        c[0] = -253.1133293658495;
        apfs[4] = new AdvancedPolynomialFunction(c);
        c = new double[3];
        c[0] = 1.322621310472797E7;
        c[1] = -28903.073744333702;
        c[2] = 15.789782110942168;
        apfs[5] = new AdvancedPolynomialFunction(c);
        c = new double[1];
        c[0] = -500;
        apfs[6] = new AdvancedPolynomialFunction(c);
        double[] bounds = {0.0,
                887.5997281333204,
                907.287821017496,
                911.2420403312013,
                911.2919044533844,
                911.2919044533905,
                915.2461237670958,
                Double.POSITIVE_INFINITY};
        PiecewisePolynomialFunction ppf1 = new PiecewisePolynomialFunction(apfs, bounds);
        Policy policy1 = Policy.SimplePolicy(new DoNothing(1));

        apfs = new AdvancedPolynomialFunction[8];
        c = new double[1];
        c[0] = 1.2969970703125E-4;
        apfs[0] = new AdvancedPolynomialFunction(c);
        c = new double[5];
        c[0] = 1.6833614323769566E10;
        c[1] = -8.461919249586436E7;
        c[2] = 159511.37113810756;
        c[3] = -133.63859517437544;
        c[4] = 0.04198589572193669;
        apfs[1] = new AdvancedPolynomialFunction(c);
        c = new double[1];
        c[0] = 1.862645149230957E-9;
        apfs[2] = new AdvancedPolynomialFunction(c);
        c = new double[3];
        c[0] = -1.2997693732736748E7;
        c[1] = 28651.754011555513;
        c[2] = -15.789782110942168;
        apfs[3] = new AdvancedPolynomialFunction(c);
        c = new double[2];
        c[0] = 113542.20575466196;
        c[1] = -124.87252276457463;
        apfs[4] = new AdvancedPolynomialFunction(c);
        c = new double[1];
        c[0] = -253.1133293658495;
        apfs[5] = new AdvancedPolynomialFunction(c);
        c = new double[3];
        c[0] = 1.322621310472797E7;
        c[1] = -28903.073744333702;
        c[2] = 15.789782110942168;
        apfs[6] = new AdvancedPolynomialFunction(c);
        c = new double[1];
        c[0] = -500;
        apfs[7] = new AdvancedPolynomialFunction(c);
        bounds = new double[]{0.0,
                795.7454365408104,
                795.7455043811476,
                907.287821017496,
                911.2420403312013,
                911.2919044533844,
                911.2919044533905,
                915.2461237670958,
                Double.POSITIVE_INFINITY};
        PiecewisePolynomialFunction ppf2 = new PiecewisePolynomialFunction(apfs, bounds);
        Policy policy2 = Policy.SimplePolicy(new DoNothing(2));

        System.out.println("ppf1:\n" + ppf1);
        System.out.println("ppf2:\n" + ppf2);

        MDP.PiecewisePolynomialFunctionAndPolicy maxResult = MDP.max(ppf1, ppf2, policy1, policy2);
        PiecewisePolynomialFunction ppf = maxResult.getPiecewisePolynomialFunction();
        ppf.simplify();
        System.out.println("max:\n" + ppf);

    }

    public Test(String x) {
//        AdvancedPolynomialFunction[] apfs = new AdvancedPolynomialFunction[3];
//        double[] c, bounds;
//        c = new double[]{500};
//        apfs[0] = new AdvancedPolynomialFunction(c);
//        c = new double[]{1000, -6, 0.01};
////        c = new double[]{300};
//        apfs[1] = new AdvancedPolynomialFunction(c);
//        c = new double[]{800};
//        apfs[2] = new AdvancedPolynomialFunction(c);
//        bounds = new double[]{0.0, 100.0, 400.0, Double.POSITIVE_INFINITY};
//        PiecewisePolynomialFunction ppf = new PiecewisePolynomialFunction(apfs, bounds);
//        Policy policy = Policy.SimplePolicy(new DoNothing(1));
//        MDP.PiecewisePolynomialFunctionAndPolicy ppfap = MDP.addWait(ppf, policy);
//        System.out.println(ppfap.getPiecewisePolynomialFunction());
//        System.out.println(ppfap.getPolicy());

//        AdvancedPolynomialFunction[] apfs = new AdvancedPolynomialFunction[11];
//        double[] bounds = new double[12];
//        double startValue = 10;
//        for (int i = 0; i < 10; ++i) {
//            apfs[i] = VRP.randomPolynomialFunction(5, i*10, (i+1) *10, startValue, 0, 1000.0);
//            bounds[i] = i * 10;
//            startValue = apfs[i].value((i + 1) * 10);
//        }
//        bounds[10] = 100;
//        bounds[11] = Double.POSITIVE_INFINITY;
//        apfs[10] = AdvancedPolynomialFunction.N(startValue);
//        PiecewisePolynomialFunction ppf = new PiecewisePolynomialFunction(apfs, bounds);
//        System.out.println(ppf);
//        System.out.println();
//        Policy policy = Policy.SimplePolicy(new DoNothing(0));
//        MDP.PiecewisePolynomialFunctionAndPolicy wait = MDP.addWait(ppf, policy);
//        System.out.println(wait.getPiecewisePolynomialFunction());

//        PiecewiseStochasticPolynomialFunction pspf = VRP.randomMovingCostFunction(4, 4, 0, 2, 100, 1000, 0, 100, 1000);
//        System.out.println(pspf);

        VRP vrp = VRP.VRPGenerator_MeshMap(3, 3, 5);
        System.out.println(vrp);
        System.out.println("============================== MDP ==============================");
        MDP mdp = new MDP(vrp,2);
        mdp.buildGraph();
        System.out.println(mdp.graphToString());
        mdp.assignValueFunction();
        System.out.println("============================== OUTPUT ==============================");
        System.out.println("MDP Graph:");
        System.out.println(mdp.valueFunctionToString());
        System.out.println("MDP Policies:");
        System.out.println(mdp.policyToString());
        System.out.println("============================== SIMULATE ==============================");
        double allres = 0;
        for (int i = 0; i < 10; ++i) {
            double result = mdp.simulate();
            allres += result;
            System.out.println("Sim " + i + ": " + result);
        }
        System.out.println("reward:" + (allres/10));
        System.out.println("pica:" + mdp.getAvgPieceNum());
        System.out.println("picm:" + mdp.getMaxPieceNum());
        System.out.println("dega:" + mdp.getAvgDegreeNum());
        System.out.println("degm:" + mdp.getMaxDegreeNum());

    }

    public static void main(String[] args) {
        for (int i=0;i<1;++i)
            new Test("");
    }
}
