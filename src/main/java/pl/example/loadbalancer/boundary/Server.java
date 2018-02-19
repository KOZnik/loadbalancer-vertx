package pl.example.loadbalancer.boundary;

import io.vertx.core.Vertx;

public class Server {

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(LoadBalancerVerticle.class.getName());
    }

}
