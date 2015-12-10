package pl.example.loadbalancer.control;

public interface BalancingStrategy {

    String calculateNextFreeGroup();

}
