package Test;

/**
 * Created by Xiaoxi Wang on 7/10/14.
 */
public class B {
    A[] as;

    public B(A[] as) {
        this.as = new A[as.length];
        // System.arraycopy(as, 0, this.as, 0, as.length);
        for (int i = 0; i < as.length; i++) {
            this.as[i] = (A)as[i].clone();
        }
    }

    public void printAs() {
        for (A a : as) {
            System.out.print(a.getA() + ", ");
        }
        System.out.println();
    }
}
