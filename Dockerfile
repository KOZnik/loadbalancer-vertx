# Extend vert.x image
FROM vertx/vertx3

#
ENV VERTICLE_NAME pl.example.loadbalancer.boundary.LoadBalancerVerticle
ENV VERTICLE_FILE target/LoadBalancer-1.0-SNAPSHOT.jar

# Set the location of the verticles
ENV VERTICLE_HOME /usr/verticles

EXPOSE 8080

# Copy your verticle to the container
COPY $VERTICLE_FILE $VERTICLE_HOME/

# Launch the verticle
WORKDIR $VERTICLE_HOME
ENTRYPOINT ["sh", "-c"]
CMD ["vertx run $VERTICLE_NAME -cp $VERTICLE_HOME/*"]