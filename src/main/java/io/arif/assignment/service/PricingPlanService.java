package io.arif.assignment.service;

import io.github.bucket4j.Bucket;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PricingPlanService {
    private final Map<String, Bucket> bucketMap = new ConcurrentHashMap<>();

    public Bucket resolveBucket(String apiKey) {
        if (apiKey == null)
            apiKey = "FREE";
        return bucketMap.computeIfAbsent(apiKey, this::newBucket);
    }

    private Bucket newBucket(String apiKey) {
        PricingPlan pricingPlan = PricingPlan.resolvePlanFromApiKey(apiKey);
        return Bucket.builder()
                .addLimit(pricingPlan.getLimit())
                .build();
    }
}
