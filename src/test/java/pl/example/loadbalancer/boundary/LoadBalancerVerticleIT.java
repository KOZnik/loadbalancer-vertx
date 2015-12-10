package pl.example.loadbalancer.boundary;

import org.junit.Assert;
import org.junit.Test;
import pl.example.loadbalancer.control.GroupsConfiguration;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static javax.ws.rs.core.Response.Status.Family.SUCCESSFUL;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.closeTo;

public class LoadBalancerVerticleIT {

    static WebTarget target = ClientBuilder.newClient().target("http://localhost:8080/route");

    @Test
    public void shouldReturnSameGroupsForKnownClients() throws InterruptedException {
        //given
        List<String> clientIds = Arrays.asList("cl1", "cl2", "cl3", "cl4", "cl5", "cl6", "cl7", "cl8", "cl9", "cl10");

        //when
        Map<String, String> clientGroups = getGroupsForClients(clientIds);

        //then
        for (String clientId : clientIds) {
            Response response = target.path(clientId).request().get();

            Assert.assertThat(response.readEntity(String.class), is(clientGroups.get(clientId)));
            Assert.assertThat(response.getStatusInfo().getFamily(), is(SUCCESSFUL));
        }
    }

    @Test
    public void shouldBalanceUsersToGroupsFromConfiguration() {
        //given
        List<String> clientIds = Arrays.asList("cl1", "cl2", "cl3", "cl4", "cl5", "cl6", "cl7", "cl8", "cl9", "cl10");

        //when
        Map<String, String> clientGroups = getGroupsForClients(clientIds);

        //then
        GroupsConfiguration.INSTANCE.getGroups().forEach(g -> {
            double frequency = calculatePercentageFrequencyFor(g.getName(), clientGroups.values());
            Assert.assertThat(frequency, is(closeTo(g.getWeight() * 10, 5)));
        });
    }

    private Map<String, String> getGroupsForClients(List<String> clientIds) {
        return clientIds.stream().collect(Collectors.toMap((c) -> c, c -> {
            Response response = target.path(c).request().get();
            Assert.assertThat(response.getStatusInfo().getFamily(), is(SUCCESSFUL));
            return response.readEntity(String.class);
        }));
    }

    private double calculatePercentageFrequencyFor(String string, Collection<String> all) {
        double occurrences = all.stream().filter(s -> s.equals(string)).count();
        return occurrences / all.size() * 100;
    }

}