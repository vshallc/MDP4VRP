package mdp;

/**
 * Created by Xiaoxi Wang on 9/10/14.
 */
public class Arc {
    private State startState, endState;
    private Action action;

    public Arc(State startState, State endState, Action action) {
        this.startState = startState;
        this.endState = endState;
        this.action = action;
    }

    public State getStartState() {
        return startState;
    }

    public State getEndState() {
        return endState;
    }

    public Action getAction() {
        return action;
    }
}
