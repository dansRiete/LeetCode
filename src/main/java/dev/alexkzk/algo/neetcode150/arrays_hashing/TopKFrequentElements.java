package dev.alexkzk.algo.neetcode150.arrays_hashing;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class TopKFrequentElements {

    public int[] topKFrequent(int[] nums, int k) {
        Map<Integer, Integer> freqMap = Arrays.stream(nums).boxed().collect(Collectors.toMap(i -> i, i -> 1, Integer::sum));
        System.out.println(freqMap);
        List<Integer> ranks = new ArrayList<>();
        List<Integer> result = new ArrayList<>();
        for(Map.Entry<Integer, Integer> e : freqMap.entrySet()) {
            ranks.add(e.getValue());
        }
        Collections.sort(ranks);
        List<Integer> ranksReversed = new ArrayList<>();
        for(int i = ranks.size() - 1, n = 0; i >=0 && n < k; i--, n++) {
            ranksReversed.add(ranks.get(i));
        }
        System.out.println(ranksReversed);
        int counter = 0;
        for(Map.Entry<Integer, Integer> e : freqMap.entrySet()) {
            if(ranksReversed.contains(e.getValue()) && counter < k){
                result.add(e.getKey());
                counter++;
            }
        }
        //System.out.println(result);
        int[] resultArr = new int[result.size()];
        for(int i = 0; i < result.size(); i++) {
            resultArr[i] = result.get(i);
        }
        return resultArr;
    }


    public static int[] topKFrequentOn(int[] nums, int k) {
        Map<Integer, Integer> freqMap = Arrays.stream(nums).boxed().collect(Collectors.toMap(n->n, n->1, Integer::sum));
        List<Integer>[] freqArr = new List[nums.length + 1];
        //  Traverse data structure from k - number, v - frequency to k - frequency, v - list of numbers of that frequency
        for (Map.Entry<Integer, Integer> e : freqMap.entrySet()) {
            int freq = e.getValue();
            if (freqArr[freq] == null) freqArr[freq] = new ArrayList<>();
            freqArr[freq].add(e.getKey());
        }
        List<Integer> result = new ArrayList<>();
        // adding ALL numbers of top frequencies to result limiting the result by k
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
