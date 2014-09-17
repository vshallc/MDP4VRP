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

    @Override
    public State perform(State state) {
        Set<Task> newTaskSet = new HashSet<Task>(state.getTaskSet());
        if (newTaskSet.contains(task)) {
            newTaskSet.remove(task);
            return new State(state.getLocation(), newTaskSet);
        } else return state;
    }

    @Override
    public PiecewisePolynomialFunction nextValueFunc(PiecewisePolynomialFunction currentValueFunc) {
        return null;
    }
}
