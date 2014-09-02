package vrp;

import java.util.HashSet;

/**
 * Created by Xiaoxi Wang on 9/3/14.
 */
public class TrafficMap {
    private Node[] nodes;
    private int startNodeID, endNodeID;

    public TrafficMap(int nodeSize, int startNodeID, int endNodeID) {
        nodes = new Node[nodeSize];
        this.startNodeID = startNodeID;
        this.endNodeID = endNodeID;
    }

    public Node getStartNode() {
        return nodes[startNodeID];
    }

    public Node getEndNode() {
        return nodes[endNodeID];
    }
}
