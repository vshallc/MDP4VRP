package mdp;

import vrp.Task;

/**
 * Created by Xiaoxi Wang on 9/3/14.
 */
public class Execute implements Action{
    private Task task;

    public Execute(Task task) {
        this.task = task;
    }

    @Override
    public BasicState perform(BasicState state) {
        return null;
    }
}
