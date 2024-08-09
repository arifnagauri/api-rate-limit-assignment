package io.arif.assignment;

import io.arif.assignment.model.Property;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class ApiConsumerApp {

    /** Cached Properties */
    private static final Map<Integer, Property> propertiesCache = new HashMap<>();

    private static final String URL = "http://localhost:8080/api/properties/";

    /** If opted for priced plan, we can call api more than 15/minute.
     *  Change API_KEY to FX001-UNIQUE-CODE for free api calls
     */
    private static final String API_KEY = "PX001-UNIQUE-CODE";

    private static final RestTemplate restTemplate = new RestTemplate();

    public static void main(String[] args) {

        //### Un-comment any of the function according to desired result:
//        callApiWithCaching();
        callApiWithoutCaching();

        System.out.println("After 15th Api request:");
        // the next call... (will work if cached or with priced plan otherwise show friendly message of limit-exceeded)
        Property property = getProperty(3);
        System.out.println(property);

        /* ### We can make large number of calls to API if we have caching solution in place, which reduces the number of
         calls we make to the API ### */
    }

    private static Property callApiWithCaching() {
        Property property = null;

        for (int i=1; i<=15; ++i) {
            property = getProperty(i);
            System.out.println(property);
        }

        return property;
    }

    private static Property callApiWithoutCaching() {
        Property property = null;

        for (int i=1; i<=15; ++i) {
            property = makeApiRequest(i);
            System.out.println(property);
        }

        return property;
    }

    private static Property getProperty(Integer id) {
        if (propertiesCache.containsKey(id))
            return propertiesCache.get(id);

        Property property = null;
        try {
            property = makeApiRequest(id);
            propertiesCache.put(id, property);
        } catch (RestClientException restClientException) {
            System.out.println("You have exceeded api rate limits, please wait for a minute to refill...");
        }
        return property;
    }

    private static Property makeApiRequest(Integer id) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-api-key", API_KEY);
        RequestEntity<Void> requestEntity =
                new RequestEntity<>(headers, HttpMethod.GET, URI.create(URL + id));
        return restTemplate.exchange(requestEntity, Property.class).getBody();
    }

}
