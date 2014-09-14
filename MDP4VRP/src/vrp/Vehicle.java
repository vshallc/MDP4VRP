package vrp;

import mas.Agent;
import mdp.State;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Xiaoxi Wang on 9/3/14.
 */
public class Vehicle implements Agent{
    private TrafficMap map;
    private Set<Task> taskSet;
    private State state;
    private double currentTime;

    public Vehicle(TrafficMap map, Set<Task> taskSet) {
        this.map = map;
        this.taskSet = taskSet;
    }

    public void init() {
//        state = new State(map.getStartNode(), taskSet);
        currentTime = 0;
    }

    public void setTrafficMap(TrafficMap map) {
        this.map = map;
    }


}
