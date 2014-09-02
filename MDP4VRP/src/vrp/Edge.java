package vrp;

import functional.PiecewiseStochasticPolynomialFunction;

/**
 * Created by Xiaoxi Wang on 9/1/14.
 */
public class Edge {
    private Node startNode, endNode;
    private PiecewiseStochasticPolynomialFunction timeCostFunc;

    public Edge(Node startNode, Node endNode, PiecewiseStochasticPolynomialFunction timeCostFunc) {
        this.startNode = startNode;
        this.endNode = endNode;
        this.startNode.addOutgoingEdge(this);
        this.endNode.addIncomingEdge(this);
        this.timeCostFunc = timeCostFunc;
    }

    public Node getStartNode() {
        return startNode;
    }

    public Node getEndNode() {
        return endNode;
    }

    public PiecewiseStochasticPolynomialFunction getTimeCostFunction() {
        return timeCostFunc;
    }
}
