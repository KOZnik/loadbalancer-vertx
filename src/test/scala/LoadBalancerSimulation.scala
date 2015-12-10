import com.typesafe.config.{Config, ConfigFactory}
import io.gatling.core.Predef._
import io.gatling.http.Predef._

class LoadBalancerSimulation extends Simulation {

  val config: Config = ConfigFactory.load()

  val httpProtocol = http
    .baseURL(config.getString("performance.host"))

  val usersFeeder = csv("users.csv").random

  val scn = scenario("Load balancing scenario")
    .feed(usersFeeder)
    .exec(http("Get group for user")
    .get("/route/${user}")
    .check(status.is(200)))

  setUp(scn.inject(rampUsers(8800) over(4))).protocols(httpProtocol)
}