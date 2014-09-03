package mdp;

import vrp.Node;
import vrp.Task;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Xiaoxi Wang on 9/3/14.
 */
public class State {
    private Node location;
    private Set<Task> taskSet = new HashSet<Task>();

    public State(Node location, Set<Task> taskSet) {
        this.location = location;
        this.taskSet = taskSet;
    }

//    @Override
//    public int hashCode() {
//        return ;
//    }
}
