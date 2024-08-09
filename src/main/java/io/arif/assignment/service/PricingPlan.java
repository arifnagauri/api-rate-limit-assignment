package io.arif.assignment.service;

import io.github.bucket4j.Bandwidth;

import java.time.Duration;

public enum PricingPlan {
    FREE {
        public Bandwidth getLimit() {
            return Bandwidth.builder().capacity(15).refillIntervally(15, Duration.ofMinutes(1)).build();
        }
    },

    BASIC {
        public Bandwidth getLimit() {
            return Bandwidth.builder().capacity(50).refillIntervally(50, Duration.ofMinutes(1)).build();
        }
    },

    PREMIUM {
        public Bandwidth getLimit() {
            return Bandwidth.builder().capacity(100).refillIntervally(100, Duration.ofMinutes(1)).build();
        }
    };

    static PricingPlan resolvePlanFromApiKey(String apiKey) {
        if (apiKey == null || apiKey.isEmpty()) {
            return FREE;
        } else if (apiKey.startsWith("PX001-")) {
            return PREMIUM;
        } else if (apiKey.startsWith("BX001-")) {
            return BASIC;
        }
        return FREE;
    }

    abstract public Bandwidth getLimit();
}
