package pl.example.loadbalancer.boundary;

import io.vertx.ext.web.RoutingContext;
import pl.example.loadbalancer.control.GroupsConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class HashingBalancer {

    public static final String ID_PARAM = "id";
    private final List<String> groups = new ArrayList<>();

    public HashingBalancer() {
        GroupsConfiguration.INSTANCE.getGroups().forEach(g ->
                IntStream.range(0, g.getWeight()).forEach((i) -> groups.add(g.getName())));
    }

    public void handleUser(RoutingContext context) {
        final String userId = context.request().getParam(ID_PARAM);
        String group = groups.get(Math.abs(userId.hashCode() % 10));

        context.response().setStatusCode(200).end(group);
    }
}
