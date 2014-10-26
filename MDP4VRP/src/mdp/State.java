package mdp;

import vrp.Edge;
import vrp.Node;
import vrp.Task;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Xiaoxi Wang on 9/3/14.
 */
public class State {
//    private int id;
    private Node location;
    private Set<Task> taskSet = new HashSet<Task>();


//    private Set<Arc> incomingArcs = new HashSet<Arc>();
//    private Set<Arc> outgoingArcs = new HashSet<Arc>();
//    private Set<Action> possibleActions = new HashSet<Action>();
//    private boolean isActionsAssigned = false;

    public State(Node location, Set<Task> taskSet) {
//        this.id = id;
        this.location = location;
        this.taskSet.addAll(taskSet);
    }

//    public int getID() {
//        return this.id;
//    }

    public List<Action> getPossibleActions() {
        List<Action> possibleActions = new ArrayList<Action>();
        // Add possible actions
        // Move
        for (Edge e : location.getOutgoingEdges()) possibleActions.add(new Move(e));
        // Execute
        for (Task t : location.getTasks()) if (taskSet.contains(t)) possibleActions.add(new Execute(t));
        // Abandon
        for (Task t : taskSet) possibleActions.add(new Abandon(t));
        return possibleActions;
    }

    public Node getLocation() {
        return location;
    }

    public Set<Task> getTaskSet() {
        Set<Task> result = new HashSet<Task>();
        result.addAll(taskSet);
        return result;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append('(');
        s.append(this.location.getID());
        s.append(',');
        if (taskSet.isEmpty()) {
            s.append("null");
        } else {
            s.append('{');
            for (Task task : taskSet) {
                s.append(task.getID());
                s.append(',');
            }
            s.deleteCharAt(s.length() - 1);
            s.append('}');
        }
        s.append(')');
        return s.toString();
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
        return location.equals(other.location) && taskSet.equals(other.taskSet);
    }

//
//    private Set<Action> assignPossibleActions() {
//        isActionsAssigned = true;
//        return possibleActions;
//    }

//    protected BasicState toBasicState() {
//        return new BasicState(super.getLocation(), super.getTaskSet());
//    }

//    public Set<Action> getPossibleActions() {
////        if (isActionsAssigned)
//        return this.possibleActions;
////        else return assignPossibleActions();
//    }

//    public void addIncomingArc(Arc arc) {
//        if (arc.getEndState().equals(this)) {
//            incomingArcs.add(arc);
//        }
//    }
//
//    public void addOutgoingArc(Arc arc) {
//        if (arc.getStartState().equals(this)) {
//            outgoingArcs.add(arc);
//        }
//    }
//
//    public Set<Arc> getIncomingArcs() {
//        return incomingArcs;
//    }
//
//    public Set<Arc> getOutgoingArcs() {
//        return outgoingArcs;
//    }

//    @Override
//    public int hashCode() {
//        int result = 1;
//        result = result * 37 + incomingArcs.hashCode();
//        result = result * 31 + outgoingArcs.hashCode();
//        result = result * 31 + possibleActions.hashCode();
//        result = result * 17 + super.hashCode();
//        return result;
//    }
//
//    @Override
//    public boolean equals(Object obj) {
//        if (this == obj) {
//            return true;
//        }
//        if (!(obj instanceof State)) {
//            return false;
//        }
//        State other = (State) obj;
//        return getLocation().equals(other.getLocation()) && getTaskSet().equals(other.getTaskSet()) &&
//                incomingArcs.equals(other.incomingArcs) && outgoingArcs.equals(other.outgoingArcs) &&
//                possibleActions.equals(other.possibleActions);
//    }
}
