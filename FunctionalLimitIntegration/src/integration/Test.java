package integration;


/**
 * Created by Xiaoxi Wang on 7/10/14.
 */
public class Test {

    public Test() {
        AdvancedPolynomialFunction[] pf = new AdvancedPolynomialFunction[2];
        double[] c = new double[1];
        c[0] = 1;
//        c[1] = 1;
        pf[0] = new AdvancedPolynomialFunction(c);
        c = new double[1];
        c[0] = 0;
//        c[1] = 400;
        pf[1] = new AdvancedPolynomialFunction(c);

        StochasticPolynomialFunction spf = new StochasticPolynomialFunction(pf);

        c = new double[2];
        c[0] = 0;
        c[1] = 1;
//        c[2] = 3;

        AdvancedPolynomialFunction bigpf = new AdvancedPolynomialFunction(c);

        StochasticPolynomialFunction result = bigpf.compose(spf);

        System.out.println("result: (x=0, xi=0)" + result.determinedValue(0,0));
        System.out.println("result: (x=1, xi=0)" + result.determinedValue(1,0));
        System.out.println("result: (x=0, xi=1)" + result.determinedValue(0,1));
        System.out.println("result: (x=1, xi=1)" + result.determinedValue(1,1));

//        System.out.println("pf[0]: " + pf[0].value(1) + " pf[1]: " + pf[1].value(1));
//        System.out.println("sum: " + pf[0].add(pf[1]).value(1));
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
