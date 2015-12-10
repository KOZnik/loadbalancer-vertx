package pl.example.loadbalancer.control;

import io.vertx.core.shareddata.LocalMap;

import java.util.Optional;

public class LocalUserRepository extends UserRepository {

    private LocalMap<String, String> cache;

    public LocalUserRepository(LocalMap<String, String> cache) {
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
