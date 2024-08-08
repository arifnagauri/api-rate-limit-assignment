package io.arif.assignment.controller;

import io.arif.assignment.model.Property;
import io.arif.assignment.service.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PropertyController {

    @Autowired
    private PropertyService propertyService;

    @GetMapping("/api/properties")
    public ResponseEntity<List<Property>> getAllProperties() {
        List<Property> properties = propertyService.getProperties();
        if (properties.isEmpty())
            return ResponseEntity.noContent().build();

        return ResponseEntity.ok(properties);
    }

    @GetMapping("/api/properties/{id}")
    public ResponseEntity<Property> getPropertyById(@PathVariable("id") Integer id) {
        Property property = propertyService.getProperty(id);
        if (property == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(property);
    }

}
