package pl.example.loadbalancer.boundary;

import io.vertx.core.shareddata.SharedData;
import io.vertx.ext.web.RoutingContext;
import pl.example.loadbalancer.control.BalancingStrategy;
import pl.example.loadbalancer.control.UserRepository;

public class Balancer {

    public static final String ID_PARAM = "id";
    private final UserRepository repository;
    private final BalancingStrategy strategy;

    public Balancer(SharedData sharedData, BalancingStrategy strategy) {
        this.strategy = strategy;
        repository = UserRepository.produce(sharedData);
    }

    public void handleUser(RoutingContext context) {
        final String userId = context.request().getParam(ID_PARAM);
        final String group = repository.userGroup(userId).orElseGet(() -> {
            String calculated = strategy.calculateNextFreeGroup();
            repository.assignUserToGroup(userId, calculated);
            return calculated;
        });
        context.response().setStatusCode(200).end(group);
    }

}
