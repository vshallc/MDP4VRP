package Test;

import mdp.Arc;
import mdp.BasicState;
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
//        A[] as = new A[2];
//        as[0] = new A(100);
//        as[1] = new A(200);
//
//        B b = new B(as);
//
//        b.printAs();
//
//        as[0].setA(1000);
//
//        b.printAs();
        Node n1 = new Node();
        Node n2 = new Node();
        Set<Task> ts1 = new HashSet<Task>();
        Set<Task> ts2 = new HashSet<Task>();
        ts2.addAll(ts1);
        System.out.println("ts: " + ts1.equals(ts2));
        System.out.println(ts1.hashCode() + " " + ts2.hashCode());
        Map<BasicState, State> basicStateStateMap = new HashMap<BasicState, State>();
        BasicState bs1 = new BasicState(n1, ts1);
        BasicState bs2 = new BasicState(n1, ts1);
        BasicState bs3 = new BasicState(n2, ts2);
        System.out.println(bs1.equals(bs2));
        State s1 = new State(bs1);
        State s2 = new State(bs2);
        State s3 = new State(bs3);
        basicStateStateMap.put(bs1, s1);
        basicStateStateMap.put(bs2, s2);
        basicStateStateMap.put(bs3, s3);
        System.out.println(basicStateStateMap.size());
        Arc arc = new Arc(s1, s2, new Move(n1, n2));
        System.out.println("hc: " + s3.hashCode());

    }

    public static void main(String[] args) {
        new Test();
    }
}
