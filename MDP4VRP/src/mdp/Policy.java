package mdp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Xiaoxi Wang on 10/11/14.
 */
public class Policy {
    private Action[] actions;
    private double[] bounds;
    private int pieces;

    public Policy(Action[] actions, double[] bounds) {
        this.actions = Arrays.copyOf(actions, actions.length);
        this.bounds = new double[bounds.length];
        System.arraycopy(bounds, 0, this.bounds, 0, bounds.length);
        pieces = bounds.length;
    }

    public Action[] getActions() {
        return actions.clone();
    }

    public double[] getBounds() {
        return bounds.clone();
    }

    public static Policy union(Policy p1, Policy p2, double[] bounds, int[] id) {
        List<Double> boundsList = new ArrayList<Double>();
        List<Action> actionList = new ArrayList<Action>();
        double[][] pbounds = new double[2][];
        Action[][] pActions = new Action[2][];
        pbounds[0] = p1.getBounds();
        pbounds[1] = p2.getBounds();
        pActions[0] = p1.getActions();
        pActions[1] = p2.getActions();
        int[] j = new int[2];
        j[0] = 0;
        j[1] = 0;
        int lastID = id[0];
        double lastBound = bounds[0];
        double tmpBound;
//        actionList.add(pActions[lastID][0]);
        for (int i = 1; i < id.length; ++i) {
            if (id[i] != lastID) {
                boundsList.add(lastBound);
                tmpBound = lastBound;
                lastBound = bounds[i];
//                System.out.println(lastID+" "+boundsList.get(boundsList.size()-1)+" "+lastBound);
                while (pbounds[lastID][j[id[i]] + 1] <= tmpBound) {
                    ++j[id[i]];
                }
                for (; pbounds[lastID][j[id[i]] + 1] < lastBound; ++j[id[i]]) {
                    boundsList.add(pbounds[lastID][j[id[i]] + 1]);
                    actionList.add(pActions[lastID][j[id[i]]]);
                }
                actionList.add(pActions[lastID][j[id[i]]]);
                lastID = id[i];
            }
        }
        boundsList.add(lastBound);
        boundsList.add(bounds[id.length]);
//        System.out.println("id len: " + id.length + "\npActions.len: " + pActions.length);
        lastID = id[id.length - 1];
        actionList.add(pActions[lastID][pActions[lastID].length - 1]);
//        System.out.println("debug: " + boundsList.size() + " " + actionList.size());
        Action[] newActions = new Action[actionList.size()];
        double[] newBounds = new double[boundsList.size()];
        for (int i = 0; i < newActions.length; ++i) {
            newActions[i] = actionList.get(i);
            newBounds[i] = boundsList.get(i);
        }
        newBounds[newActions.length] = boundsList.get(newActions.length);
        return new Policy(newActions, newBounds);
    }

    public static Policy SimplePolicy(Action action) {
        Action[] actions = new Action[1];
        double[] bounds = new double[2];
        actions[0] = action;
        bounds[0] = 0;
        bounds[1] = Double.POSITIVE_INFINITY;
        return new Policy(actions, bounds);
    }
}
