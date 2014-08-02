package integration;


/**
 * Created by Xiaoxi Wang on 7/10/14.
 */
public class Test {

    public Test() {
        AdvancedPolynomialFunction[] pf = new AdvancedPolynomialFunction[3];
        double[] c = new double[2];
        c[0] = 7;
        c[1] = 1;
        pf[0] = new AdvancedPolynomialFunction(c);  // f1(x)=0
        System.out.println("f1(x) = " + pf[0].toString());
        c = new double[1];
        c[0] = 0;
//        c[1] = 4;
        pf[1] = new AdvancedPolynomialFunction(c);  // f2(x)=0
        System.out.println("f2(x) = " + pf[1].toString());
        c = new double[2];
        c[0] = 5;
        c[1] = 4;
        pf[2] = new AdvancedPolynomialFunction(c);  // f3(x)=1
        System.out.println("f3(x) = " + pf[2].toString());

        StochasticPolynomialFunction spf = new StochasticPolynomialFunction(pf);    // g(x,ξ)=ξ
        System.out.println("g(x,ξ) = " + spf.toString());

        c = new double[3];
        c[0] = 3;
        c[1] = -(2.0/3.0);
        c[2] = 1;

        AdvancedPolynomialFunction bigpf = new AdvancedPolynomialFunction(c);   // f(x)=x
        System.out.println("f(x) = " + bigpf.toString());

        System.out.println("f'(x) = " + bigpf.polynomialDerivative().toString());

//        double min = bigpf.NewtonsMethod(bigpf, bigpf.polynomialDerivative(), bigpf.polynomialDerivative().polynomialDerivative(), -100, 100);
//        System.out.println("min = " + min);

//        StochasticPolynomialFunction result = bigpf.compose(spf);   // f(g(x,ξ)) should be ξ
//
//        System.out.println("f(g(x,ξ)) = " + result.toString());
//        System.out.println("result: (x=0, xi=0)" + result.determinedValue(0,0));
//        System.out.println("result: (x=1, xi=0)" + result.determinedValue(1,0));
//        System.out.println("result: (x=0, xi=1)" + result.determinedValue(0,1));
//        System.out.println("result: (x=1, xi=1)" + result.determinedValue(1,1));

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
