package com.example.helloworld.api;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.json.Json;
import jakarta.json.JsonObject;

@Path("/hello")
public class HelloWorldResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject sayHello() {
        return Json.createObjectBuilder()
            .add("message", "Hello, World!")
            .add("timestamp", System.currentTimeMillis())
            .build();
    }
}
