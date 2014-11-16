package vrp;

import functional.AdvancedPolynomialFunction;
import functional.PiecewisePolynomialFunction;
import functional.PiecewiseStochasticPolynomialFunction;
import functional.StochasticPolynomialFunction;

import java.util.Random;

/**
 * Created by Xiaoxi Wang on 11/15/14.
 */
public class VRP {
    private Node[] nodes;
    private Edge[] edges;
    private Task[] tasks;
    private Node startNode, endNode;
    private PiecewisePolynomialFunction terminatedValueFunction;

    public VRP(Node[] nodes, Edge[] edges, Task[] tasks, Node startNode, Node endNode, PiecewisePolynomialFunction terminatedValueFunction) {
//        this.nodes = new Node[nodes.length];
//        this.edges = new Edge[edges.length];
//        this.tasks = new Task[tasks.length];
//        System.arraycopy(nodes, 0, this.nodes, 0, nodes.length);
//        System.arraycopy(edges, 0, this.edges, 0, edges.length);
//        System.arraycopy(tasks, 0, this.tasks, 0, tasks.length);
        this.nodes = nodes;
        this.edges = edges;
        this.tasks = tasks;
        this.startNode = startNode;
        this.endNode = endNode;
        this.terminatedValueFunction = terminatedValueFunction;
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

    public Node getStartNode() {
        return this.startNode;
    }

    public Node getEndNode() {
        return this.endNode;
    }

    public PiecewisePolynomialFunction getTerminatedValueFunction() {
        return this.terminatedValueFunction;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("number of nodes: ");
        s.append(nodes.length);
        s.append('\n');
        s.append("start node: ");
        s.append(startNode);
        s.append('\n');
        s.append("end node: ");
        s.append(endNode);
        s.append('\n');
        s.append("terminated value function:\n");
        s.append(terminatedValueFunction);
        s.append('\n');
        s.append('\n');
        for (Edge edge : edges) {
            if (edge != null) {
                s.append(edge);
                s.append('\n');
                s.append("cost function\n");
                s.append(edge.getTimeCostFunction());
                s.append("\n");
            }
        }
        s.append('\n');
        for (Task task : tasks) {
            s.append(task);
            s.append('\n');
            s.append("location ");
            s.append(task.getLocation());
            s.append('\n');
            s.append("reward function\n");
            s.append(task.getRewardFunc());
            s.append('\n');
            s.append("time cost ");
            s.append(task.getTimeCost());
            s.append('\n');
            s.append("penalty ");
            s.append(task.getPenalty());
            s.append('\n');
        }
        return s.toString();
    }

    public static VRP VRPGenerator_MeshMap(int row, int column, int taskNum) {
        int minPieces = 1, maxPieces = 3;
        int minDegree = 0, maxDegree = 1;
        double minTimeCostOnEdge = 4.0, maxTimeCostOnEdge = 14.0;
        double minStoc = 0, maxStoc = 2;
        double startTime = 0, deadline = 100;
        double deadlinePenalty = -100e8;
        double minReward = 10e8, maxReward = 10e8;
        double minTimeCostOnTask = 0.5, maxTimeCostOnTask = 1.5;
        double minPenalty = -5e8, maxPenalty = -5e8;
        int nodeNum = row * column;
        Node[] nodes = new Node[nodeNum];
        for (int i = 0; i < row; ++i) {
            for (int j = 0; j < column; ++j) {
                nodes[i * column + j] = new Node(i * column + j);
            }
        }
        Edge[] edges = new Edge[nodeNum * 4];
        int neighbour;
        for (int i = 0; i < nodes.length; ++i) {
            // UP
            neighbour = i - column;
            if (neighbour >= 0) {
                PiecewiseStochasticPolynomialFunction movingCostFunc = VRP.randomMovingCostFunction(minPieces, maxPieces, minDegree, maxDegree, minTimeCostOnEdge, maxTimeCostOnEdge, minStoc, maxStoc, deadline);
                edges[i * 4] = new Edge(nodes[i], nodes[neighbour], movingCostFunc);
            }
            // DOWN
            neighbour = i + column;
            if (neighbour < nodeNum) {
                PiecewiseStochasticPolynomialFunction movingCostFunc = VRP.randomMovingCostFunction(minPieces, maxPieces, minDegree, maxDegree, minTimeCostOnEdge, maxTimeCostOnEdge, minStoc, maxStoc, deadline);
                edges[i * 4 + 1] = new Edge(nodes[i], nodes[neighbour], movingCostFunc);
            }
            // LEFT
            neighbour = i - 1;
            if (neighbour >= (i / column) * column) {
                PiecewiseStochasticPolynomialFunction movingCostFunc = VRP.randomMovingCostFunction(minPieces, maxPieces, minDegree, maxDegree, minTimeCostOnEdge, maxTimeCostOnEdge, minStoc, maxStoc, deadline);
                edges[i * 4 + 2] = new Edge(nodes[i], nodes[neighbour], movingCostFunc);
            }
            // RIGHT
            neighbour = i + 1;
            if (neighbour < (i / column + 1) * column) {
                PiecewiseStochasticPolynomialFunction movingCostFunc = VRP.randomMovingCostFunction(minPieces, maxPieces, minDegree, maxDegree, minTimeCostOnEdge, maxTimeCostOnEdge, minStoc, maxStoc, deadline);
                edges[i * 4 + 3] = new Edge(nodes[i], nodes[neighbour], movingCostFunc);
            }
        }
        Task[] tasks = new Task[taskNum];
        Random random = new Random();
        for (int i = 0; i < taskNum; ++i) {
            tasks[i] = new Task(i,
                    nodes[random.nextInt(nodeNum)],
                    randomRewardFunction(startTime, deadline, minReward, maxReward, false),
                    random.nextDouble() * (maxTimeCostOnTask - minTimeCostOnTask) + minTimeCostOnTask,
                    random.nextDouble() * (maxPenalty - minPenalty) + minPenalty);
        }
        Node startAndEndNode = nodes[random.nextInt(nodeNum)];
        AdvancedPolynomialFunction[] apfs = new AdvancedPolynomialFunction[2];
        apfs[0] = AdvancedPolynomialFunction.ZERO();
        apfs[1] = AdvancedPolynomialFunction.N(deadlinePenalty);
        double[] bounds = new double[3];
        bounds[0] = startTime;
        bounds[1] = deadline;
        bounds[2] = Double.POSITIVE_INFINITY;
        PiecewisePolynomialFunction terminatedValueFunction = new PiecewisePolynomialFunction(apfs, bounds);
        return new VRP(nodes, edges, tasks, startAndEndNode, startAndEndNode, terminatedValueFunction);
    }

    public static PiecewiseStochasticPolynomialFunction randomMovingCostFunction(int minPiece, int maxPiece,
                                                                                 int minDegree, int maxDegree,
                                                                                 double minValue, double maxValue,
                                                                                 double minStocCoef, double maxStocCoef,
                                                                                 double timeOfConverge) {
        Random random = new Random();
        int pieces = random.nextInt(maxPiece - minPiece + 1) + minPiece + 1; // add 1 for interval after converge
        StochasticPolynomialFunction[] spfs = new StochasticPolynomialFunction[pieces];
        double xiCoef = random.nextDouble() * (maxStocCoef - minStocCoef) + minStocCoef;
        double[] bounds = new double[pieces + 1];
        bounds[0] = 0;
        for (int i = 1; i < pieces - 1; ++i) {
//            double b = random.nextDouble() * timeOfConverge;
            double b = (double)random.nextInt((int)timeOfConverge);
            for (int j = i; j > 0; --j) {
                if (b > bounds[j - 1]) {
                    System.arraycopy(bounds, j, bounds, j + 1, i - j);
                    bounds[j] = b;
                    break;
                }
            }
        }
        bounds[pieces - 1] = timeOfConverge;
        bounds[pieces] = Double.POSITIVE_INFINITY;
        AdvancedPolynomialFunction[] apfs;
        int degree;
        double startValue = random.nextDouble() * (maxValue - minValue) + minValue;
//        double endValue;
        for (int p = 0; p < pieces - 1; ++p) {
            apfs = new AdvancedPolynomialFunction[2];
            degree = random.nextInt(maxDegree - minDegree + 1);
//            endValue = degree == 0 ? startValue : random.nextDouble() * (maxValue - minValue) + minValue;
//            System.out.println("deg: " + degree + " b[" + p + "]:" + bounds[p] + " b[" + (p+1) + "]: " + bounds[p+1] + " start: " + startValue + " min: " + minValue + " max: " + maxValue);
            apfs[0] = randomPolynomialFunction(degree, bounds[p], bounds[p + 1], startValue, minValue, maxValue);
//            apfs[1] = AdvancedPolynomialFunction.N(random.nextDouble() * (maxStocCoef - minStocCoef) + minStocCoef);
            apfs[1] = AdvancedPolynomialFunction.N(xiCoef);
            spfs[p] = new StochasticPolynomialFunction(apfs);
            startValue = apfs[0].value(bounds[p + 1]);
//            System.out.println("apfs[0]: " + apfs[0]);
//            System.out.println("start value: " + startValue);
//            System.out.println();
        }
        apfs = new AdvancedPolynomialFunction[2];
        apfs[0] = AdvancedPolynomialFunction.N(startValue);
//        apfs[1] = AdvancedPolynomialFunction.N(random.nextDouble() * (maxStocCoef - minStocCoef) + minStocCoef);
        apfs[1] = AdvancedPolynomialFunction.N(xiCoef);
//        System.out.println("apfs[0]: " + apfs[0]);
//        System.out.println("start value: " + startValue);
//        System.out.println();
        spfs[pieces - 1] = new StochasticPolynomialFunction(apfs);
        return new PiecewiseStochasticPolynomialFunction(spfs, bounds);
    }

    public static AdvancedPolynomialFunction randomPolynomialFunction(int degree,
                                                                       double leftBound, double rightBound,
                                                                       double startValue,
                                                                       double minValue, double maxValue) {
        // generator a random polynomial function whose value range is within [minValue, maxValue] on domain [leftBound, rightBound]
        Random random = new Random();
        if (degree == 0) {
            return AdvancedPolynomialFunction.N(startValue);
        } else {
            double[] c = new double[degree + 1];
            for (int i = 0; i < c.length; ++i) {
                c[i] = random.nextDouble() - 0.5;
            }
            AdvancedPolynomialFunction tmp = new AdvancedPolynomialFunction(c);
//            System.out.println("c[]:" + Arrays.toString(c));
//            System.out.println("tmp: " + tmp);
            double fl = tmp.value(leftBound);
            double[] exts = tmp.extrema(leftBound, rightBound);
            double scale = random.nextDouble() * Math.min(((startValue - minValue) / (fl - exts[0])), ((maxValue - startValue) / (exts[1] - fl)));
//            System.out.println("fl: " + fl + " fr: " + tmp.value(rightBound) + " exts: " + Arrays.toString(exts) + " scale: " + scale);
            for (int i = 0; i < c.length; ++i) {
                c[i] = c[i] * scale;
            }
            c[0] = c[0] + startValue - (fl * scale);
//            System.out.println("new c[]:" + Arrays.toString(c));
            return new AdvancedPolynomialFunction(c);
        }
    }

    public static PiecewisePolynomialFunction randomRewardFunction(double leftBound, double rightBound, double minReward, double maxReward, boolean softWindow) {
        Random random = new Random();
        double startTime = random.nextDouble() * (rightBound - leftBound) + leftBound;
        double endTime = random.nextDouble() * (rightBound - leftBound) + leftBound;
        {
            if (startTime > endTime) {
                double tmp = startTime;
                startTime = endTime;
                endTime = tmp;
            }
        }
        double reward = random.nextDouble() * (maxReward - minReward) + minReward;
        if (softWindow) {
            double halfWindowSize = random.nextDouble() * (rightBound - leftBound) / 2;
            // TODO soft time window
            return null;
        } else {
            AdvancedPolynomialFunction[] apfs = new AdvancedPolynomialFunction[3];
            apfs[0] = AdvancedPolynomialFunction.N(-reward);
            apfs[1] = AdvancedPolynomialFunction.N(reward);
            apfs[2] = AdvancedPolynomialFunction.N(-reward);
            double[] bounds = new double[4];
            bounds[0] = 0;
            bounds[1] = startTime;
            bounds[2] = endTime;
            bounds[3] = Double.POSITIVE_INFINITY;
            return new PiecewisePolynomialFunction(apfs, bounds);
        }
    }
}
