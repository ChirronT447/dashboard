package com.gateway.dashboard.interviews.anthpc;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * A system for assigning annotation tasks to workers based on skills and workload.
 */
public class TaskAssignmentSystem {

    // Using an enum for type-safe skill definitions
    public enum Skill { LANGUAGE_SPANISH, IMAGE_SEGMENTATION, SENTIMENT_ANALYSIS }

    // Records for concise, immutable data objects
    public record Worker(String id, Set<Skill> skills, int currentTaskCount) {}
    public record AnnotationTask(String id, Set<Skill> requiredSkills) {}

    public static class TaskAssignor {

        /**
         * Assigns a task to the best available worker.
         * "Best" is the qualified worker with the minimum current task count.
         *
         * @param task The task to be assigned.
         * @param availableWorkers A list of currently available workers.
         * @return An Optional containing the best worker, or empty if no suitable worker is found.
         */
        public Optional<Worker> assignTask(AnnotationTask task, List<Worker> availableWorkers) {
            return availableWorkers.stream()
                    // 1. Filter: Find all workers who have all the required skills.
                    .filter(worker ->
                            worker.skills().containsAll(task.requiredSkills())
                    )
                    // 2. Find Minimum: Select the one with the lowest task count.
                    .min(Comparator.comparingInt(Worker::currentTaskCount));
        }
    }

    // --- Demo Usage ---
    public static void main(String[] args) {
        List<Worker> workers = List.of(
                new Worker("Alice", Set.of(Skill.LANGUAGE_SPANISH, Skill.SENTIMENT_ANALYSIS), 2),
                new Worker("Bob", Set.of(Skill.IMAGE_SEGMENTATION), 5),
                new Worker("Charlie", Set.of(Skill.LANGUAGE_SPANISH, Skill.SENTIMENT_ANALYSIS), 1)
        );

        TaskAssignor assignor = new TaskAssignor();

        // Task 1: Requires Spanish sentiment analysis
        AnnotationTask spanishTask = new AnnotationTask("task-001", Set.of(Skill.LANGUAGE_SPANISH, Skill.SENTIMENT_ANALYSIS));
        assignor.assignTask(spanishTask, workers)
                .ifPresentOrElse(
                        worker -> System.out.printf("Assigned Spanish task to: %s (Tasks: %d)%n", worker.id(), worker.currentTaskCount()),
                        () -> System.out.println("No suitable worker for Spanish task.")
                ); // Expects Charlie (fewer tasks than Alice)

        // Task 2: Requires image segmentation
        AnnotationTask imageTask = new AnnotationTask("task-002", Set.of(Skill.IMAGE_SEGMENTATION));
        assignor.assignTask(imageTask, workers)
                .ifPresentOrElse(
                        worker -> System.out.printf("Assigned Image task to: %s (Tasks: %d)%n", worker.id(), worker.currentTaskCount()),
                        () -> System.out.println("No suitable worker for Image task.")
                ); // Expects Bob

        // Task 3: Requires skills nobody has
        AnnotationTask impossibleTask = new AnnotationTask("task-003", Set.of(Skill.LANGUAGE_SPANISH, Skill.IMAGE_SEGMENTATION));
        assignor.assignTask(impossibleTask, workers)
                .ifPresentOrElse(
                        worker -> System.out.printf("Assigned Impossible task to: %s%n", worker.id()),
                        () -> System.out.println("No suitable worker for Impossible task.")
                ); // Expects no worker
    }
}