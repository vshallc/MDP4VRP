package mdp;

import vrp.Node;
import vrp.Task;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Xiaoxi Wang on 9/14/14.
 */
public class BasicState {
    private Node location;
    private Set<Task> taskSet = new HashSet<Task>();

    public BasicState(Node location, Set<Task> taskSet) {
        this.location = location;
        this.taskSet = taskSet;
    }

    public Node getLocation() {
        return location;
    }

    public Set<Task> getTaskSet() {
        Set<Task> result = new HashSet<Task>();
        result.addAll(taskSet);
        return result;
    }

//    @Override
//    public int hashCode() {
//        int result = 1;
//        result = result * 13 + location.hashCode();
//        result = result * 31 + taskSet.hashCode();
//        return result;
//    }
//
//    @Override
//    public boolean equals(Object obj) {
//        if (this == obj) {
//            return true;
//        }
//        if (!(obj instanceof BasicState)) {
//            return false;
//        }
//        BasicState other = (BasicState) obj;
//        return location.equals(other.location) && taskSet.equals(other.taskSet);
//    }
}
