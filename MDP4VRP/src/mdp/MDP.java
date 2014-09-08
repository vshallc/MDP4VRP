package mdp;

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
}
