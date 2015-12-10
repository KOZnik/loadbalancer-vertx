package pl.example.loadbalancer.control;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.util.List;
import java.util.stream.Collectors;

public enum GroupsConfiguration {

    INSTANCE;

    GroupsConfiguration() {
        groups = ConfigFactory.load().getConfigList("groups").stream().map(Group::new).collect(Collectors.toList());
    }

    private final List<Group> groups;

    public List<Group> getGroups() {
        return groups;
    }

    public static class Group {
        final String name;
        final int weight;

        public Group(final Config params) {
            name = params.getString("name");
            weight = params.getInt("weight");
        }

        public String getName() {
            return name;
        }

        public int getWeight() {
            return weight;
        }
    }
}
