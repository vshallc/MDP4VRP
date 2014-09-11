package mdp;

import vrp.Edge;
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

//    private Set<Arc> incomingArcs = new HashSet<Arc>();
//    private Set<Arc> outgoingArcs = new HashSet<Arc>();
//
//    private Set<Action> possibleActions = new HashSet<Action>();

    public State(Node location, Set<Task> taskSet) {
        this.location = location;
        this.taskSet = taskSet;
    }

    public Set<Action> getPossibleActions() {
        Set<Action> possibleActions = new HashSet<Action>();
        // Add possible actions
        // Move
        for (Edge e : this.location.getOutgoingEdges()) possibleActions.add(new Move(e.getStartNode(), e.getEndNode()));
        // Execute
        for (Task t : this.location.getTasks()) possibleActions.add(new Execute(t));
        // Abandon
        for (Task t : this.taskSet) possibleActions.add(new Abandon(t));

        return possibleActions;
    }

//    public Set<Action> getPossibleActions() {
//        return this.possibleActions;
//    }

//    public void addIncomingArc(Arc arc) {
//        incomingArcs.add(arc);
//    }
//
//    public void addOutgoingArc(Arc arc) {
//        outgoingArcs.add(arc);
//    }
//
//    public Set<Arc> getIncomingArcs() {
//        return incomingArcs;
//    }
//
//    public Set<Arc> getOutgoingArcs() {
//        return outgoingArcs;
//    }

    public Node getLocation() {
        return location;
    }

    public Set<Task> getTaskSet() {
        Set<Task> result = new HashSet<Task>();
        result.addAll(taskSet);
        return result;
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = result * 13 + location.hashCode();
        result = result * 31 + taskSet.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof State)) {
            return false;
        }
        State other = (State) obj;
        return location.equals(other.getLocation()) && taskSet.equals(other.getTaskSet());
    }
}
