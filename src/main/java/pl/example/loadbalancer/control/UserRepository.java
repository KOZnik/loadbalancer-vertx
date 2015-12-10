package pl.example.loadbalancer.control;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import io.vertx.core.shareddata.SharedData;

import java.util.Optional;

public abstract class UserRepository {

    public static final String USERS_CACHE_NAME = "users";

    public abstract void assignUserToGroup(String userId, String group);

    public abstract Optional<String> userGroup(String userId);

    public static UserRepository produce(SharedData sharedData) {
        Optional<HazelcastInstance> instance = Hazelcast.getAllHazelcastInstances().stream().findFirst();
        if (instance.isPresent()) {
            return new ClusteredUserRepository(instance.get().getMap(USERS_CACHE_NAME));
        } else {
            return new LocalUserRepository(sharedData.getLocalMap(USERS_CACHE_NAME));
        }
    }

}
