package mdp;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by Xiaoxi Wang on 9/5/14.
 */
public class MDP {
    private State startState;
    private State endState;
    private ConcurrentMap<State, List<Arc>> incomingArcs = new ConcurrentHashMap<State, List<Arc>>();
    private ConcurrentMap<State, List<Arc>> outgoingArcs = new ConcurrentHashMap<State, List<Arc>>();
    private ConcurrentMap<State, List<Action>> possibleActions = new ConcurrentHashMap<State, List<Action>>();

    public MDP(State startState, State endState) {
        this.startState = startState;
        this.endState = endState;
        incomingArcs.putIfAbsent(startState, new ArrayList<Arc>());
        outgoingArcs.putIfAbsent(startState, new ArrayList<Arc>());
        possibleActions.putIfAbsent(startState, new ArrayList<Action>());
        incomingArcs.putIfAbsent(endState, new ArrayList<Arc>());
        outgoingArcs.putIfAbsent(endState, new ArrayList<Arc>());
        possibleActions.putIfAbsent(endState, new ArrayList<Action>());
    }

    private void buildGraph() {
        Queue<State> checkingQueue = new LinkedList<State>();
        Set<State> checkedStates = new HashSet<State>();
        checkingQueue.add(startState);
        while (!checkingQueue.isEmpty()) {
            State currentState = checkingQueue.poll();
            possibleActions.putIfAbsent(currentState, new ArrayList<Action>());
            if (currentState.equals(endState)) continue;
            for (Action a : possibleActions.get(currentState)) {
                State nextState = a.perform(currentState);
                if (checkedStates.contains(nextState)) {
                    Arc arc = new Arc(currentState, nextState, a);
                    incomingArcs.get(nextState).add(arc);
                    outgoingArcs.get(currentState).add(arc);
                } else {
                    Arc arc = new Arc(currentState, nextState, a);
                    incomingArcs.putIfAbsent(nextState, new ArrayList<Arc>());
                    outgoingArcs.putIfAbsent(nextState, new ArrayList<Arc>());
                    incomingArcs.get(nextState).add(arc);
                    outgoingArcs.get(currentState).add(arc);
                    checkingQueue.add(nextState);
                    checkedStates.add(nextState);
                }
            }
        }
    }
}
