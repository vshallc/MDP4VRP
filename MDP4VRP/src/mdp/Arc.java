package mdp;

import java.util.Collections;

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

    @Override
    public int hashCode() {
        int result = 1;
        result = result * 31 + startState.hashCode();
        result = result * 31 + endState.hashCode();
        result = result * 31 + action.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Arc)) {
            return false;
        }
        Arc other = (Arc) obj;
        return startState.equals(other.startState) && endState.equals(other.endState) && action.equals(other.action);
    }
}
