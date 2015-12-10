package pl.example.loadbalancer.control;

import java.util.Map;
import java.util.Optional;

public class ClusteredUserRepository extends UserRepository {

    private Map<String, String> cache;

    public ClusteredUserRepository(Map<String, String> cache) {
        this.cache = cache;
    }

    @Override
    public void assignUserToGroup(String userId, String group) {
        cache.put(userId, group);
    }

    @Override
    public Optional<String> userGroup(String userId) {
        return Optional.ofNullable(cache.get(userId));
    }
}
