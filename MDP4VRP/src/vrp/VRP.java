package vrp;

import java.util.Arrays;

/**
 * Created by Xiaoxi Wang on 11/15/14.
 */
public class VRP {
    private Node[] nodes;
    private Edge[] edges;
    private Task[] tasks;

    public VRP(Node[] nodes, Edge[] edges, Task[] tasks) {
        this.nodes = new Node[nodes.length];
        this.edges = new Edge[edges.length];
        this.tasks = new Task[tasks.length];
        System.arraycopy(nodes, 0, this.nodes, 0, nodes.length);
        System.arraycopy(edges, 0, this.edges, 0, edges.length);
        System.arraycopy(tasks, 0, this.tasks, 0, tasks.length);
    }

    public Node[] getNodes() {
        return this.nodes;
    }

    public Edge[] getEdges() {
        return this.edges;
    }

    public Task[] getTasks() {
        return this.tasks;
    }
}
