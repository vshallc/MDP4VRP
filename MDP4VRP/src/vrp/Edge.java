package vrp;

import functional.AdvancedPolynomialFunction;
import functional.PiecewiseStochasticPolynomialFunction;

/**
 * Created by Xiaoxi Wang on 9/1/14.
 */
public class Edge {
    private Node startNode, endNode;
    private PiecewiseStochasticPolynomialFunction timeCostFunc;
    private PiecewiseStochasticPolynomialFunction arrivalFunc;

    public Edge(Node startNode, Node endNode, PiecewiseStochasticPolynomialFunction timeCostFunc) {
        this.startNode = startNode;
        this.endNode = endNode;
        this.startNode.addOutgoingEdge(this);
        this.endNode.addIncomingEdge(this);
        this.timeCostFunc = timeCostFunc;
        double[] c = {0.0, 1.0};
        AdvancedPolynomialFunction t = new AdvancedPolynomialFunction(c);
        arrivalFunc = timeCostFunc.add(t);
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

    public PiecewiseStochasticPolynomialFunction getArrivalFunction() {
        return arrivalFunc;
    }
}
