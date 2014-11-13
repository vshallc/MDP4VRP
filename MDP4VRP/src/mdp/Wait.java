package mdp;

import functional.PiecewisePolynomialFunction;

/**
 * Created by Xiaoxi Wang on 11/12/14.
 */
public class Wait implements Action {
    private double wakeUpTime;

    public Wait(double wakeUpTime) {
        this.wakeUpTime = wakeUpTime;
    }

    @Override
    public State perform(State state) {
        return state;
    }

    @Override
    public double timeCost(double currentTime) {
        double timeCost = wakeUpTime - currentTime;
        return timeCost >= 0 ? wakeUpTime - currentTime : 0;
    }

    @Override
    public PiecewisePolynomialFunction preValueFunc(PiecewisePolynomialFunction currentValueFunc) {
        return currentValueFunc;
    }
}
