package Test;

import functional.AdvancedPolynomialFunction;
import functional.PiecewisePolynomialFunction;
import mdp.*;
import vrp.Node;
import vrp.Task;

import java.util.*;

/**
 * Created by Xiaoxi Wang on 7/10/14.
 */
public class Test {

    public Test() {
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


        Action[] actions1 = new Action[1];
        double[] bounds1 = new double[2];
        actions1[0] = new DoNothing(0);
//        actions1[1] = new DoNothing(1);
//        actions1[2] = new DoNothing(2);
        bounds1[0] = 0.0;
//        bounds1[1] = 100.0;
//        bounds1[2] = 200.0;
        bounds1[1] = Double.POSITIVE_INFINITY;

        Action[] actions2 = new Action[1];
        double[] bounds2 = new double[2];
        actions2[0] = new DoNothing(3);
//        actions2[1] = new DoNothing(4);
        bounds2[0] = 0;
//        bounds2[1] = 150;
        bounds2[1] = Double.POSITIVE_INFINITY;


        Policy p1 = new Policy(actions1, bounds1);
        Policy p2 = new Policy(actions2, bounds2);
        double[] ubounds = new double[3];
        int[] id = new int[2];
        ubounds[0] = 0;
        ubounds[1] = 150;
        ubounds[2] = Double.POSITIVE_INFINITY;
        id[0] = 0;
        id[1] = 1;

        Policy pu = Policy.union(p1, p2, ubounds, id);
        System.out.println("actions:");
        for (Action a : pu.getActions()) {
            System.out.print(((DoNothing) a).getId() + ", ");
        }
        System.out.println();

        System.out.println("bounds:");
        for (double d : pu.getBounds()) {
            System.out.print(d + ", ");
        }
        System.out.println();
    }

    public Test(int x) {
        LinkedHashSet<Integer> integers = new LinkedHashSet<Integer>();
        LinkedHashSet<Integer> integers2 = new LinkedHashSet<Integer>();
        for (int i = 0; i < 5; ++i) {
            integers.add(i * x);
            integers2.add(i * x * 2);
        }

        for (int i : integers) {
            System.out.println(i);
        }
        System.out.println("==============");

        integers = integers2;
        integers2 = new LinkedHashSet<Integer>();
        for (int i = 10; i < 15; ++i) {
            integers2.add(i * x * 2);
        }
        for (int i : integers) {
            System.out.println(i);
        }
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
        new Test("x");
    }
}
