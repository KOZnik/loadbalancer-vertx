package pl.example.loadbalancer.boundary;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import java.util.UUID;

@BenchmarkMode(Mode.Throughput)
public class ThroughputTest {

    @State(Scope.Thread)
    public static class Context {
        WebTarget target;

        @Setup
        public void initClient() {
            target = ClientBuilder.newClient().target("http://localhost:8080/route");
        }
    }

    @Benchmark
    public String executeRequest(Context context) {
        return context.target.path(UUID.randomUUID().toString()).request().get(String.class);
    }

    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
                .include(ThroughputTest.class.getSimpleName())
                .warmupIterations(5)
                .forks(1)
                .threads(10)
                .shouldFailOnError(true).shouldDoGC(true).build();
        new Runner(options).run();

    }

}
