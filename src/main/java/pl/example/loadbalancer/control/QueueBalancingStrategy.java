package pl.example.loadbalancer.control;

import pl.example.loadbalancer.control.GroupsConfiguration.Group;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.IntStream;

public class QueueBalancingStrategy implements BalancingStrategy {

    public static final int QUEUE_SIZE = 10000;

    final Queue<String> queue = new LinkedList<>();
    final List<String> seed = new ArrayList<>();

    public QueueBalancingStrategy() {
        prepareSeed();
        fillQueue();
    }

    private void prepareSeed() {
        for (Group group : GroupsConfiguration.INSTANCE.getGroups()) {
            IntStream.range(0, group.weight).forEach(t -> seed.add(group.name));
        }
    }

    private void fillQueue() {
        IntStream.range(0, QUEUE_SIZE).forEach(t -> queue.addAll(seed));
    }

    @Override
    public String calculateNextFreeGroup() {
        if (queue.size() == 0) {
            fillQueue();
        }
        return queue.remove();
    }
}
