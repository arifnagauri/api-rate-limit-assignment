package io.arif.assignment.controller;

import io.arif.assignment.model.Property;
import io.arif.assignment.service.PricingPlanService;
import io.arif.assignment.service.PropertyService;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PropertyController {

    @Autowired
    private PropertyService propertyService;
    @Autowired
    private PricingPlanService pricingPlanService;

    @GetMapping("/api/properties")
    public ResponseEntity<List<Property>> getAllProperties(@RequestHeader(value = "X-api-key", required = false) String apiKey) {
        Bucket bucket = pricingPlanService.resolveBucket(apiKey);
        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);

        if (probe.isConsumed()) {
            List<Property> properties = propertyService.getProperties();
            if (properties.isEmpty())
                return ResponseEntity.noContent().build();

            return ResponseEntity.ok(properties);
        }

        long waitForRefill = probe.getNanosToWaitForRefill()/(10^9);
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                .header("X-rate-limit-retry-after-seconds", String.valueOf(waitForRefill))
                .build();
    }

    @GetMapping("/api/properties/{id}")
    public ResponseEntity<Property> getPropertyById(@RequestHeader(value = "X-api-key", required = false) String apiKey,
                                                    @PathVariable("id") Integer id) {
        Bucket bucket = pricingPlanService.resolveBucket(apiKey);
        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);

        if (probe.isConsumed()) {
            Property property = propertyService.getProperty(id);
            if (property == null)
                return ResponseEntity.notFound().build();

            return ResponseEntity.ok(property);
        }

        long waitForRefill = probe.getNanosToWaitForRefill()/(10^9);
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                .header("X-rate-limit-retry-after-seconds", String.valueOf(waitForRefill))
                .build();
    }

}
