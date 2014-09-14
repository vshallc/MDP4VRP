package mdp;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
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
        Map<BasicState, State> checkedStates = new HashMap<BasicState, State>();
        expendState(startState, endState, checkedStates);
    }

    private void expendState(State currentState, State endState, Map<BasicState, State> checkedStates) {
        checkedStates.put(new BasicState(currentState.getLocation(), currentState.getTaskSet()), currentState);
        if (currentState.toBasicState().equals(endState.toBasicState())) return;
        for (Action a : currentState.getPossibleActions()) {
            BasicState nextPossibleState = a.perform(currentState);
            if (checkedStates.containsKey(nextPossibleState)) {
                State next = checkedStates.get(nextPossibleState);
                Arc arc = new Arc(currentState, next, a);
                next.addIncomingArc(arc);
                currentState.addOutgoingArc(arc);
            } else {
                //
            }
        }
    }
}
