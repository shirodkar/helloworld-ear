package com.example.helloworld.api;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonArrayBuilder;
import java.util.List;
import java.util.Map;

@Path("/data")
public class DataResource {

    @Inject
    private DataService dataService;

    @GET
    @Path("/single")
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject getSingleData(@QueryParam("query") String query) {
        if (query == null || query.isEmpty()) {
            query = "SELECT VERSION() AS version";
        }

        Map<String, Object> result = dataService.getDataFromDatabase(query);
        JsonObjectBuilder builder = Json.createObjectBuilder();

        result.forEach((key, value) -> {
            if (value instanceof Integer) {
                builder.add(key, (Integer) value);
            } else if (value instanceof Long) {
                builder.add(key, (Long) value);
            } else if (value instanceof Double) {
                builder.add(key, (Double) value);
            } else {
                builder.add(key, value != null ? value.toString() : "null");
            }
        });

        return builder.build();
    }

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject getAllData(@QueryParam("query") String query) {
        if (query == null || query.isEmpty()) {
            query = "SELECT 'test_data' AS name, 42 AS value";
        }

        List<Map<String, Object>> results = dataService.getAllDataFromDatabase(query);
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

        for (Map<String, Object> row : results) {
            JsonObjectBuilder rowBuilder = Json.createObjectBuilder();
            row.forEach((key, value) -> {
                if (value instanceof Integer) {
                    rowBuilder.add(key, (Integer) value);
                } else if (value instanceof Long) {
                    rowBuilder.add(key, (Long) value);
                } else if (value instanceof Double) {
                    rowBuilder.add(key, (Double) value);
                } else {
                    rowBuilder.add(key, value != null ? value.toString() : "null");
                }
            });
            arrayBuilder.add(rowBuilder.build());
        }

        return Json.createObjectBuilder()
                .add("data", arrayBuilder.build())
                .add("count", results.size())
                .build();
    }
}
