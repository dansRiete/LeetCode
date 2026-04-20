package dev.alexkzk.algo.neetcode150.arrays_hashing;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class TopKFrequentElements {

    public int[] topKFrequent(int[] nums, int k) {
        Map<Integer, Integer> freqMap = Arrays.stream(nums).boxed().collect(Collectors.toMap(n -> n, n -> 1, Integer::sum));
        //System.out.println("freqMap: " + freqMap);
        List<Integer>[] freqList = new ArrayList[nums.length + 1];
        for(Map.Entry<Integer, Integer> entry : freqMap.entrySet()) {
            int value = entry.getValue();
            if(freqList[value] == null) freqList[value] = new ArrayList<>();
            freqList[value].add(entry.getKey());
        }
        //System.out.println("freqList: " + freqList);
        List<Integer> result = new ArrayList();
        for(int i = nums.length, n = 0; i >= 0 && n < k; i--) {
            if(i < freqList.length && freqList[i] != null && freqList[i].size() != 0) {
                result.addAll(freqList[i]);
                n += freqList[i].size();
            }
        }
        return result.stream().mapToInt(i -> i).toArray();
    }


    public static int[] topKFrequentOn(int[] nums, int k) {
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
//        System.out.println(Arrays.toString(topKFrequent(new int[]{4,1,-1,2,-1,2,3}, 2)));
    }
}
