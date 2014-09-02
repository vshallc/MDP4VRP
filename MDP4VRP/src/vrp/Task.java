package vrp;

import functional.PiecewisePolynomialFunction;

/**
 * Created by Xiaoxi Wang on 9/3/14.
 */
public class Task {
    private Node location;
    private PiecewisePolynomialFunction rewardFunc;
    private double timeCost;
    private double penalty;

    public Task(Node location, PiecewisePolynomialFunction rewardFunc, double timeCost, double penalty) {
        this.location = location;
        this.location.assignTask(this);
        this.rewardFunc = rewardFunc;
        this.timeCost = timeCost;
        this.penalty = penalty;
    }

    public Node getLocation() {
        return location;
    }

    public PiecewisePolynomialFunction getRewardFunc() {
        return rewardFunc;
    }

    public double getTimeCost() {
        return timeCost;
    }

    public double getPenalty() {
        return penalty;
    }
}
