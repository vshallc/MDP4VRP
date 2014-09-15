package mdp;

import java.util.*;

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
        Queue<State> checkingQueue = new LinkedList<State>();
        Map<BasicState, State> checkedStates = new HashMap<BasicState, State>();
        checkingQueue.add(startState);
        while (!checkingQueue.isEmpty()) {
            State currentState = checkingQueue.poll();
            if (currentState.toBasicState().equals(endState.toBasicState())) continue;
            for (Action a : currentState.getPossibleActions()) {
                BasicState nextPossibleState = a.perform(currentState);
                if (checkedStates.containsKey(nextPossibleState)) {
                    State nextState = checkedStates.get(nextPossibleState);
                    Arc arc = new Arc(currentState, nextState, a);
                    nextState.addIncomingArc(arc);
                    currentState.addOutgoingArc(arc);
                } else {
                    State nextState = new State(nextPossibleState);
                    Arc arc = new Arc(currentState, nextState, a);
                    nextState.addIncomingArc(arc);
                    currentState.addOutgoingArc(arc);
                    checkingQueue.add(nextState);
                    checkedStates.put(nextPossibleState, nextState);
                }
            }
        }
    }
}
