package dev.alexkzk.algo;

import java.util.HashMap;
import java.util.Map;

public class TaskScheduler {

    /**
     * Returns the minimum total time required to execute all tasks in the given order.
     *
     * <p><b>Problem:</b> You are given an array {@code tasks} where each element is a I mean,
     * and an integer {@code k} representing the cooldown period. Tasks must be executed in the
     * exact order they appear in the array. After executing a task with a given ID, the same ID
     * cannot be executed again until at least {@code k} units of time have passed. Each task takes
     * exactly 1 unit of time. If the next task in the array is still on cooldown, you must wait
     * (idle) — each idle unit also counts as 1 unit of time.
     *
     * <p><b>Examples:</b>
     * <pre>
     *   tasks = [1, 2, 3, 1],  k = 2  →  4
     *     t=1 exec 1, t=2 exec 2, t=3 exec 3, t=4 exec 1
     *     (2 tasks between the two 1s satisfies the cooldown of 2 — no waiting needed)
     *
     *   tasks = [1, 1],  k = 2  →  4
     *     t=1 exec 1, t=2 idle, t=3 idle, t=4 exec 1
     *     (no tasks in between, must wait 2 units before repeating task 1)
     *
     *   tasks = [1, 2, 1],  k = 2  →  4
     *     t=1 exec 1, t=2 exec 2, t=3 idle, t=4 exec 1
     *     (only 1 task between the two 1s, need 1 more idle unit to satisfy k=2)
     *
     *   tasks = [1, 1, 1],  k = 2  →  7
     *     t=1 exec 1, t=2 idle, t=3 idle, t=4 exec 1, t=5 idle, t=6 idle, t=7 exec 1
     * </pre>
     *
     * <p><b>Constraints:</b>
     * <ul>
     *   <li>{@code 0 <= tasks.length <= 10^4}</li>
     *   <li>{@code 0 <= k <= 100}</li>
     *   <li>Task IDs are positive integers.</li>
     * </ul>
     *
     * @param tasks array of task IDs to execute in order
     * @param k     cooldown period — minimum units of time that must pass before the same task ID
     *              can be executed again
     * @return minimum total time (including any idle periods) to complete all tasks
     */
    public static int minTime2(int[] tasks, int k) {
        if (tasks == null || tasks.length == 0) {
            return 0;
        }
        if (k == 0) {
            return tasks.length;
        }
        Map<Integer, Integer> cooldownMap = new HashMap<>();
        int time = 0;
        for (int i = 0; i < tasks.length; i++) {
            int currentTask = tasks[i];
            System.out.println("step " + i + ", processing " + tasks[i]);
            time++;
            int cooldown = 0;
            if (cooldownMap.get(currentTask) == null || cooldownMap.get(currentTask) == 0) {
                cooldownMap.put(currentTask, k);
            } else {
                cooldown = cooldownMap.get(currentTask);
                time += cooldown;
            }
            for(int key : cooldownMap.keySet()) {
                if (key != currentTask) {
                    int reducedCooldown = cooldownMap.get(key) - 1 - cooldown;
                    cooldownMap.put(key, reducedCooldown < 0 ? 0 : reducedCooldown);
                } else {
                    cooldownMap.put(currentTask, k);
                }
            }
            System.out.println("cooldown: " + cooldown);
            System.out.println("put3 " + cooldownMap);
        }

        return time;
    }

    /**
     * O(n) solution using last-execution timestamps instead of decrementing cooldown counters.
     *
     * <p>For each task, the earliest it can run is either right after the previous task
     * ({@code time + 1}), or {@code k + 1} slots after it last ran — whichever is later.
     * A single {@code max()} replaces all the per-key decrement bookkeeping.
     *
     * @see #minTime(int[], int)
     */
    public static int minTime/*OFromN*/(int[] tasks, int k) {
        if (tasks == null || tasks.length == 0) return 0;
        Map<Integer, Integer> lastExec = new HashMap<>();
        int time = 0;
        for (int task : tasks) {
            time = Math.max(time + 1, lastExec.getOrDefault(task, -k) + k + 1);
            lastExec.put(task, time);
        }
        return time;
    }

    public static void main(String[] args) {
//        System.out.println(minTime(new int[]{1, 1, 3, 4, 5}, 3));   //  8
//        System.out.println(minTime(new int[]{1, 1, 1, 1, 1}, 3));   //  17
//        System.out.println(minTime(new int[]{1, 1, 1}, 2));   //  7
//        System.out.println(minTime(new int[]{1, 1, 1}, 100));   //  7
//        System.out.println(minTime(new int[]{1, 2, 1532, 1}, 2));   //  4
        System.out.println(minTime(new int[]{1, 2, 3, 1}, 0));   //  4
//        System.out.println(minTime(new int[]{}, 0));   //  0
//        System.out.println(minTime(null, 0));   //  0
//        System.out.println(minTime(null, 100));   //  0
//        System.out.println(minTime(new int[]{1, 2, 1, 2}, 2));   //  6
//        System.out.println(minTime(new int[]{1, 2, 3, 1, 2, 3}, 3));   //  7 THAT WAS FAILING! PAY ATTENTION THAT COOLDOWN IS REDUCED DURING WAIT TIME AS WELL!

    }
}
