package com.gateway.dashboard.interviews.appl;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

public class ABTestingFramework {

    enum TestingGroup {
        A,
        B
    }

    /**
     * Assigns a user to a group ('A' or 'B') for a specific experiment.
     * The assignment is deterministic and provides a 50/50 split.
     *
     * @param experimentId A unique identifier for the A/B test.
     * @param userId       A unique identifier for the user.
     * @return String "A" or "B".
     */
    public TestingGroup assignGroupSimple5050(final String experimentId, final String userId) {
        // --- Input Validation ---
        if (experimentId == null || experimentId.isEmpty() || userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("Experiment ID and User ID must not be null or empty.");
        }

        // --- Deterministic Hashing ---
        // We combine experimentId and userId to ensure the user gets a different
        // assignment for a different experiment. This acts as a "salt".
        String combinedId = experimentId + ":" + userId;

        // Using hashCode() is simple but has drawbacks (e.g., potential for poor
        // distribution, changes between Java versions). For a simple interview
        // problem, it's a reasonable start. We'll improve this later.
        int hash = combinedId.hashCode();

        // --- Assignment Logic ---
        // Using Math.abs() is crucial as hashCode() can be negative.
        if (Math.abs(hash) % 2 == 0) {
            return TestingGroup.A; // "A";
        } else {
            return TestingGroup.B; // "B";
        }
    }

    /**
     * Assigns a user to a group based on specified weights (e.g., 70/20/10).
     *
     * @param experimentId A unique identifier for the A/B test.
     * @param userId       A unique identifier for the user.
     * @param groupWeights A map of Group Name -> Weight (integer percentage). Must sum to 100.
     * @return The assigned group name as a String.
     */
    public String assignWeightedGroup(String experimentId, String userId, Map<String, Integer> groupWeights) {
        // --- Input Validation ---
        if (experimentId == null || userId == null || groupWeights == null || groupWeights.isEmpty()) {
            throw new IllegalArgumentException("Inputs must not be null or empty.");
        }
        int totalWeight = groupWeights.values().stream().mapToInt(Integer::intValue).sum();
        if (totalWeight != 100) {
            throw new IllegalArgumentException("Group weights must sum to 100.");
        }

        // Using a cryptographic hash like SHA-256 provides excellent distribution.
        String combinedId = experimentId + ":" + userId;
        long hashAsLong;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(combinedId.getBytes(StandardCharsets.UTF_8));
            // We only need the first 8 bytes to create a long. This is sufficient for bucketing.
            hashAsLong = new BigInteger(1, new byte[]{hashBytes[0], hashBytes[1], hashBytes[2], hashBytes[3],
                    hashBytes[4], hashBytes[5], hashBytes[6], hashBytes[7]}).longValue();
        } catch (NoSuchAlgorithmException e) {
            // This should never happen with SHA-256
            throw new RuntimeException(e);
        }

        // --- Assignment Logic (Bucket into 0-99) ---
        int bucket = (int) (Math.abs(hashAsLong) % 100);

        int cumulativeWeight = 0;
        // Iterating over a TreeMap ensures a consistent order for assignment
        for (Map.Entry<String, Integer> entry : new java.util.TreeMap<>(groupWeights).entrySet()) {
            cumulativeWeight += entry.getValue();
            if (bucket < cumulativeWeight) {
                return entry.getKey();
            }
        }

        // Fallback, should not be reached if logic is correct
        throw new IllegalStateException("Failed to assign user to a group. Check weights and logic.");
    }

}
