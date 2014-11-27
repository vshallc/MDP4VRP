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
        pieces = actions.length;
    }

    public Action getCurrentAction(double time) {
        for (int i = 0; i < pieces; ++i) {
            if (time >= bounds[i] && time < bounds[i + 1]) {
                return actions[i];
            }
        }
        return actions[pieces - 1];
    }

    public Action[] getActions() {
        return actions.clone();
    }

    public double[] getBounds() {
        return bounds.clone();
    }

    public void removeTrivial(double threshold) {
        if (pieces > 3) {
            for (int i = 1; i < pieces - 1; ++i) {
                if (bounds[i - 1] - bounds[i] < threshold) {
                    if (actions[i - 1].toString().equals(actions[i + 1].toString())) {
                        actions[i] = actions[i - 1];
                    }
                }
            }
        }
    }

    public void simplify() {
        Action[] tmpActions = new Action[actions.length];
        double[] tmpBounds = new double[bounds.length];
        String lastAction = "", currentAction;
        int n = 0;
        for (int i = 0; i < actions.length; ++i) {
            currentAction = actions[i].toString();
            if (!currentAction.equals(lastAction)) {
                tmpActions[n] = actions[i];
                tmpBounds[n] = bounds[i];
                ++n;
                lastAction = currentAction;
            }
        }
        tmpBounds[n] = bounds[bounds.length - 1];
        actions = new Action[n];
        bounds = new double[n + 1];
        for (int i = 0; i < n; ++i) {
            actions[i] = tmpActions[i];
            bounds[i] = tmpBounds[i];
        }
        bounds[n] = tmpBounds[n];
        pieces = n;
    }

    public Policy replace(Action action, double leftBound, double rightBound) {
        int i, start = 0, end = 0;
        for (i = 1; i < pieces + 1; ++i) {
            if (leftBound < bounds[i]) {
                start = i - 1;
                break;
            }
        }
        for (; i < pieces + 1; ++i) {
            if (rightBound < bounds[i]) {
                end = i - 1;
                break;
            }
        }
        int newPieces = start + pieces - end + (leftBound == bounds[start] ? 0 : 1) + (rightBound == bounds[end + 1] ? 0 : 1);
        Action[] newActions = new Action[newPieces];
        double[] newBounds = new double[newPieces + 1];
        for (i = 0; i < start; ++i) {
            newActions[i] = actions[i];
            newBounds[i] = bounds[i];
        }
//        System.out.println(this);
//        System.out.println(pieces + " " + newPieces + " " + " start: " +start + " end: " + end + " left: " + leftBound + " right: " + rightBound + " " + i);
        if (bounds[start] < leftBound) {
            newActions[i] = actions[start];
            newBounds[i] = bounds[start];
            ++i;
        }
        newActions[i] = action;
        newBounds[i] = leftBound;
        ++i;
        if (bounds[end + 1] > rightBound) {
            newActions[i] = actions[end];
            newBounds[i] = rightBound;
            ++i;
        }
        for (; i < newPieces; ++i) {
            newActions[i] = actions[pieces - newPieces + i];
            newBounds[i] = bounds[pieces - newPieces + i];
        }
        newBounds[newPieces] = bounds[pieces];
        return new Policy(newActions, newBounds);
    }

    public static Policy union(Policy p1, Policy p2, double[] bounds, int[] id) {
//        System.out.println("p1:\n" + p1);
//        System.out.println("p2:\n" + p2);
//        System.out.println("bounds:" + Arrays.toString(bounds));
//        System.out.println("id:" + Arrays.toString(id));
//        double[] tmpBounds = new double[bounds.length];
        List<Double> boundsList = new ArrayList<Double>();
        List<Action> actionList = new ArrayList<Action>();
        double[][] pBounds = new double[2][];
        Action[][] pActions = new Action[2][];
        pBounds[0] = p1.getBounds();
        pBounds[1] = p2.getBounds();
        pActions[0] = p1.getActions();
        pActions[1] = p2.getActions();
        int[] pos = {0, 0};

        for (int i = 0; i < id.length; ++i) {
            int select = id[i];
            boundsList.add(bounds[i]);
//            System.out.println("se" + pos[select]);
            while (pBounds[select][pos[select] + 1] < bounds[i]) ++pos[select];
            actionList.add(pActions[select][pos[select]]);
            while (pBounds[select][pos[select] + 1] < bounds[i + 1]) {
                ++pos[select];
                actionList.add(pActions[select][pos[select]]);
                boundsList.add(pBounds[select][pos[select]]);
            }
        }
        boundsList.add(bounds[bounds.length - 1]);
//        int[] j = {0, 0};
//        int lastID = id[0];
//        double lastBound = bounds[0];
//        double tmpBound;
//        Action tmpAction;
//        for (int i = 1; i < id.length; ++i) {
//            if (id[i] != lastID) {
//                boundsList.add(lastBound);
//                tmpBound = lastBound;
//                lastBound = bounds[i];
//                while (pBounds[lastID][j[id[i]] + 1] <= tmpBound) {
//                    ++j[id[i]];
//                }
//                for (; pBounds[lastID][j[id[i]] + 1] < lastBound; ++j[id[i]]) {
//                    boundsList.add(pBounds[lastID][j[id[i]] + 1]);
//                    tmpAction = pActions[lastID][j[id[i]]];
//                    actionList.add(tmpAction);
//                }
//                actionList.add(pActions[lastID][j[id[i]]]);
//                lastID = id[i];
//            }
//        }
//        boundsList.add(lastBound);
//        boundsList.add(bounds[id.length]);
////        System.out.println("id len: " + id.length + "\npActions.len: " + pActions.length);
//        lastID = id[id.length - 1];
//        actionList.add(pActions[lastID][pActions[lastID].length - 1]);
////        System.out.println("debug: " + boundsList.size() + " " + actionList.size());
        Action[] newActions = new Action[actionList.size()];
        double[] newBounds = new double[boundsList.size()];
        for (int i = 0; i < newActions.length; ++i) {
            newActions[i] = actionList.get(i);
            newBounds[i] = boundsList.get(i);
        }
        newBounds[newActions.length] = boundsList.get(newActions.length);
//        System.out.println(Arrays.toString(newActions));
//        System.out.println(Arrays.toString(newBounds));
//        System.out.println("---------");
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

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < pieces; ++i) {
            s.append("[");
            s.append(bounds[i]);
            s.append(",");
            s.append(bounds[i + 1]);
            s.append(")\t");
            s.append(actions[i].toString());
            s.append("\n");
        }
//        s.deleteCharAt(s.length() - 1);
        return s.toString().trim();
    }
}
