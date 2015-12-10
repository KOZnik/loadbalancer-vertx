package pl.example.loadbalancer.boundary;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import pl.example.loadbalancer.control.QueueBalancingStrategy;

public class LoadBalancerVerticle extends AbstractVerticle {

    @Override
    public void start(Future<Void> fut) {
        Balancer balancer = new Balancer(vertx.sharedData(), new QueueBalancingStrategy());

        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());
        router.route().produces("application/json");
        router.get("/route/:id").handler(balancer::handleUser);

        vertx.createHttpServer().requestHandler(router::accept).listen(8080);
    }

}