package com.gateway.dashboard.leetcode;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * 1834. Single-Threaded CPU - Medium
 * https://leetcode.com/problems/single-threaded-cpu/
 * You are given n tasks labeled from 0 to n - 1 represented by a 2D integer array tasks,
 * where tasks[i] = [enqueueTime{i}, processingTime{i}] means that the i-th task will be available to process
 *  at enqueueTime{i} and will take processingTime{i} to finish processing.
 *
 * You have a single-threaded CPU that can process at most one task at a time and will act in the following way:
 *
 * If the CPU is idle and there are no available tasks to process, the CPU remains idle.
 * If the CPU is idle and there are available tasks, the CPU will choose the one with the shortest processing time.
 *  If multiple tasks have the same shortest processing time, it will choose the task with the smallest index.
 * Once a task is started, the CPU will process the entire task without stopping.
 * The CPU can finish a task then start a new one instantly.
 *
 * Return the order in which the CPU will process the tasks.
 * i.e. Return the task list in the order they will be processed.
 */
public class SingleThreadedCPU {

    public static int[] getOrder(final int[][] tasks) {
        final int length = tasks.length;

        // Store the order in which the CPU will process the tasks:
        final int[] order = new int[length];

        // Store tasks in sorted order on the basis of their queued time:
        final Task[] sortedTaskList = initTaskList(tasks, length);

        // Store tasks with minimum processing time on the top:
        final PriorityQueue<Task> minHeap = new PriorityQueue<>(
                (task1, task2) -> task1.processingTime == task2.processingTime ?
                        task1.index - task2.index : task1.processingTime - task2.processingTime
        );

        int taskIndex = 0;
        int orderIndex = 0;
        int currentTime = 0;
        while (orderIndex < length) {
            // Add all the tasks that came in while previous was getting processed
            while (taskIndex < sortedTaskList.length && sortedTaskList[taskIndex].enqueueTime <= currentTime) {
                minHeap.add(sortedTaskList[taskIndex]);
                taskIndex++;
            }
            // No tasks came in while previous was getting processed
            if (minHeap.isEmpty()) {
                currentTime = sortedTaskList[taskIndex].enqueueTime;
                continue;
            }
            final Task minTask = minHeap.poll();
            order[orderIndex++] = minTask.index;
            currentTime += minTask.processingTime;
        }
        return order;
    }

    @NotNull
    private static Task[] initTaskList(int[][] tasks, int len) {
        final Task[] sorted = new Task[len];
        for (int i = 0; i < len; i++) {
            sorted[i] = new Task(i, tasks[i][0], tasks[i][1]);
        }
        Arrays.sort(sorted, Comparator.comparingInt(task -> task.enqueueTime));
        return sorted;
    }

    static class Task {
        int index, enqueueTime, processingTime;
        public Task(int index, int enqueueTime, int processingTime) {
            this.index = index;
            this.enqueueTime = enqueueTime;
            this.processingTime = processingTime;
        }
    }

}