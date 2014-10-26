package mdp;

import functional.PiecewisePolynomialFunction;
import functional.PiecewiseStochasticPolynomialFunction;
import vrp.Task;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Xiaoxi Wang on 9/3/14.
 */
public class Abandon implements Action{
    private Task task;

    public Abandon(Task task) {
        this.task = task;
    }

    public String toString() {
        return "abandon(" + task.getID() + ")";
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
    public PiecewisePolynomialFunction preValueFunc(PiecewisePolynomialFunction currentValueFunc) {
        // V'(t) = V(t) + p, p < 0
        return currentValueFunc.add(task.getPenalty());
    }
}
