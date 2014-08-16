package Test;

/**
 * Created by Xiaoxi Wang on 7/10/14.
 */
public class Test {

    public Test() {
        A[] as = new A[2];
        as[0] = new A(100);
        as[1] = new A(200);

        B b = new B(as);

        b.printAs();

        as[0].setA(1000);

        b.printAs();
    }

    public static void main(String[] args) {
        new Test();
    }
}
