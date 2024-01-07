package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class GreetingService {

    public String greetingByName(String name){
        return "Ciao " + name;
    }
}
