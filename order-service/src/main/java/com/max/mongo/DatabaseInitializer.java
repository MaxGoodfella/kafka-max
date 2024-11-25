package com.max.mongo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Slf4j
@Component
public class DatabaseInitializer {

    @Autowired
    private MongoService mongoService;

    @PostConstruct
    public void initializeDatabase() {
        try {
            mongoService.dropDatabase();
            log.info("Database successfully dropped.");
        } catch (Exception e) {
            log.info("Failed to initialize database: {}", e.getMessage());
            e.printStackTrace();
        }
    }

}