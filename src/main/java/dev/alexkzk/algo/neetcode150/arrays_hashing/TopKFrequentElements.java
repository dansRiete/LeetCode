package dev.alexkzk.algo.neetcode150.arrays_hashing;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class TopKFrequentElements {
    /** LC #347 — Top K Frequent Elements [Medium] */

    public static int[] topKFrequent2(int[] nums, int k) {
        Map<Integer, Integer> freqMap = Arrays.stream(nums).boxed().collect(Collectors.toMap(n->n, n->1, Integer::sum));
        int[][] freqArr = new int[nums.length + 1][];
        for(int i = 0; i < nums.length + 1; i++) {
            final int i2 = i;
            freqArr[i] = freqMap.entrySet().stream().filter(e -> e.getValue() == i2).map(e -> e.getKey()).mapToInt(e -> e).toArray();

        }
        List<Integer> result = new ArrayList<>();
        for(int i = freqArr.length - 1, n = 0; i >= 0; i--) {
            if (freqArr[i].length > 0) {
                List<Integer> addedList = Arrays.stream(freqArr[i]).boxed().toList();
                result.addAll(addedList);
                n += addedList.size();
                if(n == k) {
                    break;
                }
            }
        }

        return result.stream().mapToInt(i -> i).toArray();
    }

    public static int[] topKFrequent(int[] nums, int k) {
        Map<Integer, Integer> freqMap = Arrays.stream(nums).boxed().collect(Collectors.toMap(n->n, n->1, Integer::sum));
        List<Integer>[] freqArr = new List[nums.length + 1];
        for (Map.Entry<Integer, Integer> e : freqMap.entrySet()) {
            int freq = e.getValue();
            if (freqArr[freq] == null) freqArr[freq] = new ArrayList<>();
            freqArr[freq].add(e.getKey());
        }
        List<Integer> result = new ArrayList<>();
        for (int i = freqArr.length - 1; i >= 0 && result.size() < k; i--) {
            if (freqArr[i] != null) result.addAll(freqArr[i]);
        }
        return result.stream().mapToInt(i -> i).toArray();
    }

    /** O(n log k) time, O(k) extra space — min-heap of size k */
    public static int[] topKFrequentV3(int[] nums, int k) {
        Map<Integer, Integer> freqMap = Arrays.stream(nums).boxed().collect(Collectors.toMap(n->n, n->1, Integer::sum));
        PriorityQueue<Map.Entry<Integer, Integer>> minHeap = new PriorityQueue<>(Comparator.comparingInt(Map.Entry::getValue));
        for (Map.Entry<Integer, Integer> e : freqMap.entrySet()) {
            minHeap.offer(e);
            if (minHeap.size() > k) minHeap.poll();
        }
        return minHeap.stream().mapToInt(Map.Entry::getKey).toArray();
    }

    public static void main(String[] args) {
//        System.out.println(Arrays.toString(topKFrequent(new int[]{1,1,2,2,2,3,3,6,6,6}, 2)));
//        System.out.println(Arrays.toString(topKFrequent(new int[]{7,7}, 1)));
//        System.out.println(Arrays.toString(topKFrequent(new int[]{1}, 1)));
        System.out.println(Arrays.toString(topKFrequent(new int[]{4,1,-1,2,-1,2,3}, 2)));
    }
}
