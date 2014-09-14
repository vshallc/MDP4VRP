package mdp;

import vrp.Task;

/**
 * Created by Xiaoxi Wang on 9/3/14.
 */
public class Abandon implements Action{
    private Task task;

    public Abandon(Task task) {
        this.task = task;
    }

    @Override
    public BasicState perform(BasicState state) {
        return null;
    }
}
