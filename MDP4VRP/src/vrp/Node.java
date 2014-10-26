package vrp;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Xiaoxi Wang on 9/1/14.
 */
public class Node {
    private int id;
    private Set<Edge> incomingEdges = new HashSet<Edge>();
    private Set<Edge> outgoingEdges = new HashSet<Edge>();
    private Set<Task> tasks = new HashSet<Task>();

    public Node(int id) {
        this.id = id;
    }

    public int getID() {
        return this.id;
    }

    public void addIncomingEdge(Edge edge) {
        incomingEdges.add(edge);
    }

    public Set<Edge> getIncomingEdges() {
        return incomingEdges;
    }

    public void addOutgoingEdge(Edge edge) {
        outgoingEdges.add(edge);
    }

    public Set<Edge> getOutgoingEdges() {
        return outgoingEdges;
    }

    public void assignTask(Task task) {
        tasks.add(task);
    }

    public Set<Task> getTasks() {
        return tasks;
    }
}
