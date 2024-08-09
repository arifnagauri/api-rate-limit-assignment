package io.arif.assignment;

import io.arif.assignment.model.Property;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

public class ApiConsumerApp {

    /** Cached Properties */
    private static final Map<Integer, Property> propertiesCache = new HashMap<>();

    public static void main(String[] args) {

        RestTemplate restTemplate = new RestTemplate();

        //### Un-comment any of the function according to desired result:

        // these functions always call Api within safe limit...
        callApiWithCaching(restTemplate);
//        callApiWithoutCaching(restTemplate);

        // ...may be, until the next call
        Property property = getProperty(restTemplate, 3);
        System.out.println(property);

        /* ### We can make large number of calls to API if we have caching solution in place, which reduces the number of
         calls we make to the API ### */
    }

    private static void callApiWithCaching(RestTemplate restTemplate) {
        Property property;

        for (int i=1; i<=15; ++i) {
            if (propertiesCache.containsKey(i)) {
                property = propertiesCache.get(i);
            } else {
                property = restTemplate.getForObject("http://localhost:8080/api/properties/" + i, Property.class);
                propertiesCache.put(i, property);
            }

            System.out.println(property);
        }
    }

    private static void callApiWithoutCaching(RestTemplate restTemplate) {
        Property property = null;

        for (int i=1; i<=15; ++i) {
            property = restTemplate.getForObject("http://localhost:8080/api/properties/" + i, Property.class);
            System.out.println(property);
        }
    }

    private static Property getProperty(RestTemplate restTemplate, Integer id) {
        if (propertiesCache.containsKey(id))
            return propertiesCache.get(id);

        Property property = null;
        try {
            property = restTemplate.getForObject("http://localhost:8080/api/properties/" + id, Property.class);
            propertiesCache.put(id, property);
        } catch (RestClientException restClientException) {
            System.out.println("You have exceeded api rate limits, please wait for a minute to refill...");
        }
        return property;
    }

}
