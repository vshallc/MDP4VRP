package vrp;

import functional.PiecewisePolynomialFunction;

/**
 * Created by Xiaoxi Wang on 9/3/14.
 */
public class Task {
    private int id;
    private Node location;
    private PiecewisePolynomialFunction rewardFunc;
    private double timeCost;
    private double penalty; // less than 0

    public Task(int id, Node location, PiecewisePolynomialFunction rewardFunc, double timeCost, double penalty) {
        this.id = id;
        this.location = location;
        this.location.assignTask(this);
        this.rewardFunc = rewardFunc;
        this.timeCost = timeCost;
        this.penalty = penalty;
    }

    public int getID() {
        return this.id;
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

    public double getReward(double time) {
        return rewardFunc.value(time);
    }

    public double getPenalty() {
        return penalty;
    }

    @Override
    public String toString() {
        return "task(" + id + ")";
    }
}
