package pl.example.loadbalancer.control;

import pl.example.loadbalancer.control.GroupsConfiguration.Group;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class ListBalancingStrategy implements BalancingStrategy {

    private int index = 0;
    final List<String> groups = new ArrayList<>();

    public ListBalancingStrategy() {
        prepareGroupList();
    }

    private void prepareGroupList() {
        for (Group group : GroupsConfiguration.INSTANCE.getGroups()) {
            IntStream.range(0, group.weight).forEach(t -> groups.add(group.name));
        }
    }

    @Override
    public String calculateNextFreeGroup() {
        if (index == groups.size()) {
            index = 0;
        }
        return groups.get(index++);
    }
}
