package mdp;

import vrp.Edge;
import vrp.Node;
import vrp.Task;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Xiaoxi Wang on 9/3/14.
 */
public class State extends BasicState{

    private Set<Arc> incomingArcs = new HashSet<Arc>();
    private Set<Arc> outgoingArcs = new HashSet<Arc>();

    private Set<Action> possibleActions = new HashSet<Action>();
//    private boolean isActionsAssigned = false;

    public State(BasicState basicState) {
        super(basicState.getLocation(), basicState.getTaskSet());
        // Add possible actions
        // Move
        for (Edge e : getLocation().getOutgoingEdges()) possibleActions.add(new Move(e.getStartNode(), e.getEndNode()));
        // Execute
        for (Task t : getLocation().getTasks()) possibleActions.add(new Execute(t));
        // Abandon
        for (Task t : getTaskSet()) possibleActions.add(new Abandon(t));
    }
//
//    private Set<Action> assignPossibleActions() {
//        isActionsAssigned = true;
//        return possibleActions;
//    }

    protected BasicState toBasicState() {
        return new BasicState(super.getLocation(), super.getTaskSet());
    }

    public Set<Action> getPossibleActions() {
//        if (isActionsAssigned)
        return this.possibleActions;
//        else return assignPossibleActions();
    }

    public void addIncomingArc(Arc arc) {
        if (arc.getEndState().equals(this)) {
            incomingArcs.add(arc);
        }
    }

    public void addOutgoingArc(Arc arc) {
        if (arc.getStartState().equals(this)) {
            outgoingArcs.add(arc);
        }
    }

    public Set<Arc> getIncomingArcs() {
        return incomingArcs;
    }

    public Set<Arc> getOutgoingArcs() {
        return outgoingArcs;
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = result * 37 + incomingArcs.hashCode();
        result = result * 31 + outgoingArcs.hashCode();
        result = result * 31 + possibleActions.hashCode();
        result = result * 17 + super.hashCode();
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
        return getLocation().equals(other.getLocation()) && getTaskSet().equals(other.getTaskSet()) &&
                incomingArcs.equals(other.incomingArcs) && outgoingArcs.equals(other.outgoingArcs) &&
                possibleActions.equals(other.possibleActions);
    }
}
