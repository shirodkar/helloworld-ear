package com.example.helloworld.api;

import com.example.helloworld.entity.Order;
import com.example.helloworld.entity.Product;
import com.example.helloworld.entity.User;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
@Startup
public class DatabaseInitializer {

    private static final Logger logger = Logger.getLogger(DatabaseInitializer.class.getName());

    @PersistenceContext(unitName = "primary")
    private EntityManager entityManager;

    @PostConstruct
    @Transactional
    public void initializeDatabase() {
        logger.info("Starting database initialization...");

        try {
            if (isDatabaseEmpty()) {
                logger.info("Database is empty, populating with sample data...");
                populateUsers();
                populateProducts();
                populateOrders();
                logger.info("Database initialization completed successfully!");
            } else {
                logger.info("Database already contains data, skipping initialization.");
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error initializing database", e);
        }
    }

    private boolean isDatabaseEmpty() {
        Long userCount = (Long) entityManager.createQuery("SELECT COUNT(u) FROM User u").getSingleResult();
        return userCount == 0;
    }

    private void populateUsers() {
        User[] users = {
                new User("jdoe", "john.doe@example.com", "John", "Doe"),
                new User("jsmith", "jane.smith@example.com", "Jane", "Smith"),
                new User("bwilson", "bob.wilson@example.com", "Bob", "Wilson"),
                new User("agarcia", "alice.garcia@example.com", "Alice", "Garcia"),
                new User("cmiller", "charlie.miller@example.com", "Charlie", "Miller")
        };

        for (User user : users) {
            entityManager.persist(user);
        }
        entityManager.flush();
        logger.info("Inserted " + users.length + " sample users");
    }

    private void populateProducts() {
        Product[] products = {
                new Product("Laptop", "High-performance laptop with 16GB RAM", new BigDecimal("999.99"), 10),
                new Product("Mouse", "Wireless mouse with ergonomic design", new BigDecimal("29.99"), 50),
                new Product("Keyboard", "Mechanical keyboard with RGB lighting", new BigDecimal("149.99"), 25),
                new Product("Monitor", "27-inch 4K monitor", new BigDecimal("399.99"), 8),
                new Product("USB Hub", "7-port USB 3.0 hub", new BigDecimal("49.99"), 30),
                new Product("HDMI Cable", "High-speed HDMI 2.1 cable", new BigDecimal("15.99"), 100),
                new Product("Webcam", "1080p HD webcam with microphone", new BigDecimal("79.99"), 15),
                new Product("Headphones", "Noise-canceling wireless headphones", new BigDecimal("199.99"), 12)
        };

        for (Product product : products) {
            entityManager.persist(product);
        }
        entityManager.flush();
        logger.info("Inserted " + products.length + " sample products");
    }

    private void populateOrders() {
        Order[] orders = {
                new Order(1, new BigDecimal("1029.98"), "completed"),
                new Order(2, new BigDecimal("199.99"), "completed"),
                new Order(3, new BigDecimal("549.97"), "pending"),
                new Order(4, new BigDecimal("79.99"), "completed"),
                new Order(5, new BigDecimal("249.98"), "shipped")
        };

        for (Order order : orders) {
            entityManager.persist(order);
        }
        entityManager.flush();
        logger.info("Inserted " + orders.length + " sample orders");
    }
}
