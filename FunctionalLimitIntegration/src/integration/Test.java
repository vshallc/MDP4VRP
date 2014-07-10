package integration;

import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;

/**
 * Created by Xiaoxi Wang on 7/10/14.
 */
public class Test {

    public Test() {
        AdvancedPolynomialFunction[] pf = new AdvancedPolynomialFunction[2];
        double[] c = new double[2];
        c[0] = 10;
        c[1] = 200;
        pf[0] = new AdvancedPolynomialFunction(c);
        c[0] = 30;
        c[1] = 400;
        pf[1] = new AdvancedPolynomialFunction(c);

        System.out.println("pf[0]: " + pf[0].value(1) + " pf[1]: " + pf[1].value(1));
        System.out.println("sum: " + pf[0].add(pf[1]).value(1));
//
//        StochasticPolynomialFunction spf = new StochasticPolynomialFunction(pf);
//
//        System.out.println("spf x=1 xi=0: " + spf.determinedValue(1, 0));
//        System.out.println("spf x=1 xi=1: " + spf.determinedValue(1, 1));
//
//        c[0] = 50;
//        c[1] = 600;
//        pf[0] = new PolynomialFunction(c);
//
//        System.out.println("spf x=1 xi=0: " + spf.determinedValue(1, 0));
//        System.out.println("spf x=1 xi=1: " + spf.determinedValue(1, 1));
    }

    public static void main(String[] args) {
        new Test();
    }
}
