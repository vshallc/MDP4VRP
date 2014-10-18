package Test;

import functional.AdvancedPolynomialFunction;
import functional.PiecewisePolynomialFunction;
import mdp.Arc;
import mdp.MDP;
import mdp.Move;
import mdp.State;
import vrp.Node;
import vrp.Task;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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

        double[] c = new double[3];
        AdvancedPolynomialFunction[] apf = new AdvancedPolynomialFunction[1];
        double[] b = new double[2];
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

        c[0] = 5;
        c[1] = -4;
        c[2] = 1;
        apf[0] = new AdvancedPolynomialFunction(c);
        b[0] = 0;
        b[1] = Double.POSITIVE_INFINITY;
        PiecewisePolynomialFunction ppf1 = new PiecewisePolynomialFunction(apf, b);

        c[0] = 0;
        c[1] = 4;
        c[2] = -1;
        apf[0] = new AdvancedPolynomialFunction(c);
        PiecewisePolynomialFunction ppf2 = new PiecewisePolynomialFunction(apf, b);
        PiecewisePolynomialFunction result = MDP.max(ppf1, ppf2).getPiecewisePolynomialFunction();

        System.out.println("f1(x)=" + ppf1.toString());
        System.out.println("f2(x)=" + ppf2.toString());
        System.out.println(result.toString());

    }

    public static void main(String[] args) {
        new Test();
    }
}
