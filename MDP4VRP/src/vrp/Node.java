package vrp;

import java.util.HashSet;

/**
 * Created by Xiaoxi Wang on 9/1/14.
 */
public class Node {
    private HashSet<Edge> incomingEdges = new HashSet<Edge>();
    private HashSet<Edge> outgoingEdges = new HashSet<Edge>();
    private HashSet<Task> tasks = new HashSet<Task>();

    public Node() {
    }

    public void addIncomingEdge(Edge edge) {
        incomingEdges.add(edge);
    }

    public void addOutgoingEdge(Edge edge) {
        outgoingEdges.add(edge);
    }

    public void assignTask(Task task) {
        tasks.add(task);
    }
}
