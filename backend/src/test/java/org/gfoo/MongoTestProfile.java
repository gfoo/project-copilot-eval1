package org.gfoo;

import io.quarkus.test.junit.QuarkusTestProfile;

import java.util.Map;

public class MongoTestProfile implements QuarkusTestProfile {
    
    @Override
    public Map<String, String> getConfigOverrides() {
        return Map.of(
            "quarkus.mongodb.devservices.enabled", "true",
            "quarkus.mongodb.database", "newsunil-test"
        );
    }
}
