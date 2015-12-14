package pl.example.loadbalancer.boundary;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

public class LoadBalancerVerticle extends AbstractVerticle {

    @Override
    public void start(Future<Void> fut) {
        HashingBalancer balancer = new HashingBalancer();

        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());
        router.route().produces("application/json");
        router.get("/route/:id").handler(balancer::handleUser);

        vertx.createHttpServer().requestHandler(router::accept).listen(8080);
    }

}