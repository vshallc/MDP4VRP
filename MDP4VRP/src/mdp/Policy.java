package mdp;

import java.util.Arrays;

/**
 * Created by Xiaoxi Wang on 10/11/14.
 */
public class Policy {
    private Action[] actions;
    private double[] bounds;
    private int pieces;

    public Policy(Action[] actions, double[] bounds) {
        this.actions = Arrays.copyOf(actions, actions.length);
        System.arraycopy(bounds, 0, this.bounds, 0, bounds.length);
        pieces = bounds.length;
    }

    public Action[] getActions() {
        return actions;
    }
}
