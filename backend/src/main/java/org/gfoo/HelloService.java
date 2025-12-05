package org.gfoo;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class HelloService {

    public String getGreeting(String name) {
        if (name == null || name.isBlank()) {
            return "Hello from Quarkus REST";
        }
        return "Hello " + name + " from Quarkus REST";
    }
}
