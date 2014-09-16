package mdp;

import functional.PiecewiseStochasticPolynomialFunction;
import vrp.Task;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Xiaoxi Wang on 9/3/14.
 */
public class Abandon implements Action{
    private Task task;
    private PiecewiseStochasticPolynomialFunction costFunc;

    public Abandon(Task task, PiecewiseStochasticPolynomialFunction costFunc) {
        this.task = task;
        this.costFunc = costFunc;
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
    public PiecewiseStochasticPolynomialFunction getCostFunction() {
        return costFunc;
    }
}
