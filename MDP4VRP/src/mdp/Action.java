package mdp;

import functional.PiecewiseStochasticPolynomialFunction;

/**
 * Created by Xiaoxi Wang on 9/3/14.
 */
public interface Action {

    public State perform(State state);

    public PiecewiseStochasticPolynomialFunction getCostFunction();
}
