package io.arif.assignment.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.arif.assignment.model.Property;
import io.arif.assignment.repository.PropertyRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class PropertyService {

    @Autowired
    private PropertyRepository propertyRepository;

    public List<Property> getProperties() {
        List<Property> properties = new ArrayList<>();
        propertyRepository.findAll().forEach(properties::add);
        return properties;
    }

    public Property getProperty(Integer id) {
        return propertyRepository.findById(id).orElse(null);
    }

    /**
     * To populate DB...
     */
    @PostConstruct
    public void populateDB() {
        List<Property> properties = parseJsonFile(new File("src/main/resources/data.json"));
        propertyRepository.saveAll(properties);
    }

    private List<Property> parseJsonFile(File file) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(file, new TypeReference<>() {});
        } catch (IOException e) {
            throw new RuntimeException("Invalid Json content", e);
        }
    }

}
