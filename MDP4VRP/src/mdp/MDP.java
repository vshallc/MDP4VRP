package mdp;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Xiaoxi Wang on 9/5/14.
 */
public class MDP {
    private State startState;
    private State endState;


    public MDP(State startState, State endState) {
        this.startState = startState;
        this.endState = endState;
    }

    private void buildGraph() {
        Set<State> checkedStates = new HashSet<State>();
        expendState(startState, endState, checkedStates);
    }

    private void expendState(State currentState, State endState, Set<State> checkedStates) {
        for (Action a : currentState.getPossibleActions()) {
            State nextPossibleState = a.perform(currentState);
        }
    }
}
