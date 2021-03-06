package mdp;

import functional.PiecewisePolynomialFunction;
import functional.PiecewiseStochasticPolynomialFunction;
import vrp.Task;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Xiaoxi Wang on 9/3/14.
 */
public class Execute implements Action{
    private Task task;

    public Execute(Task task) {
        this.task = task;
    }

    public String toString() {
        return "execute(" + task.getID() + ")";
    }

    @Override
    public State perform(State state) {
        Set<Task> newTaskSet = new HashSet<Task>(state.getTaskSet());
        if (newTaskSet.contains(task)) {
            newTaskSet.remove(task);
            return new State(state.getLocation(), newTaskSet);
        } else return state;
    }

    @Override
    public double timeCost(double currentTime) {
        return task.getTimeCost();
    }

    @Override
    public double reward(double currentTime) {
        return task.getReward(currentTime);
    }

    @Override
    public PiecewisePolynomialFunction preValueFunc(PiecewisePolynomialFunction currentValueFunc) {
        // V'(t) = V(t + t') + R(t)
//        System.out.println("execute: current: " + currentValueFunc);
        return (currentValueFunc.shift(task.getTimeCost())).add(task.getRewardFunc());
//        PiecewisePolynomialFunction shiftresult = currentValueFunc.shift(task.getTimeCost());
//        System.out.println("===============shift:\n" + shiftresult.toString() + "\n===================R(t):\n" + task.getRewardFunc());
//        PiecewisePolynomialFunction result = shiftresult.add(task.getRewardFunc());
//        System.out.println("result:\n" + result);
//        return result;
    }
}
