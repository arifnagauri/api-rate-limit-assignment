package io.arif.assignment.controller;

import io.arif.assignment.model.Property;
import io.arif.assignment.service.PropertyService;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@RestController
public class PropertyController {

    @Autowired
    private PropertyService propertyService;

    private final Bucket bucket;
    private AtomicBoolean blockRequest = new AtomicBoolean(false);

    PropertyController() {
        Bandwidth limit = Bandwidth.builder().capacity(15).refillIntervally(15, Duration.ofMinutes(1)).build();
        this.bucket = Bucket.builder()
                .addLimit(limit)
                .build();
    }

    @GetMapping("/api/properties")
    public ResponseEntity<List<Property>> getAllProperties() {
        if (blockRequest.get())
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();

        if (bucket.tryConsume(1)) {
            List<Property> properties = propertyService.getProperties();
            if (properties.isEmpty())
                return ResponseEntity.noContent().build();

            return ResponseEntity.ok(properties);
        }

        blockRequestForMinute();

        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
    }

    @GetMapping("/api/properties/{id}")
    public ResponseEntity<Property> getPropertyById(@PathVariable("id") Integer id) {
        if (blockRequest.get())
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();

        if (bucket.tryConsume(1)) {
            Property property = propertyService.getProperty(id);
            if (property == null)
                return ResponseEntity.notFound().build();

            return ResponseEntity.ok(property);
        }

        blockRequestForMinute();

        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
    }

    private void blockRequestForMinute() {
        new Thread(() -> {
            blockRequest.set(true);
            try {
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            blockRequest.set(false);
        }).start();
    }

}
