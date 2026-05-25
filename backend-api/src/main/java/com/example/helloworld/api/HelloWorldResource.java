package com.example.helloworld.api;

import com.example.helloworld.entity.Greeting;
import jakarta.enterprise.context.RequestScoped;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonArrayBuilder;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Path("/hello")
@RequestScoped
public class HelloWorldResource {

    private static final Logger LOG = LogManager.getLogger(HelloWorldResource.class);

    @PersistenceContext(unitName = "primary")
    private EntityManager entityManager;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject sayHello() {
        LOG.info("sayHello endpoint called");
        return Json.createObjectBuilder()
            .add("message", "Hello, World!")
            .add("timestamp", System.currentTimeMillis())
            .build();
    }

    @GET
    @Path("/greetings")
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject getAllGreetings() {
        LOG.info("getAllGreetings endpoint called");
        List<Greeting> greetings = entityManager.createQuery("SELECT g FROM Greeting g", Greeting.class)
                .getResultList();
        LOG.debug("Retrieved {} greetings from database", greetings.size());

        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (Greeting greeting : greetings) {
            arrayBuilder.add(Json.createObjectBuilder()
                    .add("id", greeting.getId())
                    .add("message", greeting.getMessage())
                    .build());
        }

        return Json.createObjectBuilder()
                .add("greetings", arrayBuilder.build())
                .add("count", greetings.size())
                .build();
    }
}
