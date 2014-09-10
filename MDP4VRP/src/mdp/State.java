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

    private Set<Arc> incomingArcs = new HashSet<Arc>();
    private Set<Arc> outgoingArcs = new HashSet<Arc>();

    private Set<Action> possibleActions = new HashSet<Action>();

    public State(Node location, Set<Task> taskSet) {
        this.location = location;
        this.taskSet = taskSet;
        // Add possible actions
        // Move
        for (Edge e : this.location.getOutgoingEdges()) possibleActions.add(new Move(e.getStartNode(), e.getEndNode()));
        // Execute
        for (Task t : this.location.getTasks()) possibleActions.add(new Execute(t));
        // Abandon
        for (Task t : this.taskSet) possibleActions.add(new Abandon(t));
    }

    public Set<Action> getPossibleActions() {
        return this.possibleActions;
    }

    public void addIncomingArc(Arc arc) {
        incomingArcs.add(arc);
    }

    public void addOutgoingArc(Arc arc) {
        outgoingArcs.add(arc);
    }

    public Set<Arc> getIncomingArcs() {
        return incomingArcs;
    }

    public Set<Arc> getOutgoingArcs() {
        return outgoingArcs;
    }

    public Node getLocation() {
        return location;
    }

    public Set<Task> getTaskSet() {
        return taskSet;
    }
}
