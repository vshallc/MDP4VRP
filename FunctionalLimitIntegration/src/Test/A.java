package Test;


/**
 * Created by Xiaoxi Wang on 7/10/14.
 */
public class A implements Cloneable{
    private double a;

    public A(double a) {
        this.a = a;
    }

    public void setA(double a) {
        this.a = a;
    }

    public double getA() {
        return this.a;
    }

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }
}
