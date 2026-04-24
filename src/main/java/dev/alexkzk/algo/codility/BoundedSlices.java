package dev.alexkzk.algo.codility;

public class BoundedSlices {

    /**
     * Codility — Bounded Slices
     *
     * Given an integer K and a non-empty array A of N integers, returns the number of bounded slices.
     * A slice (P, Q) is bounded if max(A[P..Q]) - min(A[P..Q]) <= K.
     * If the count exceeds 1,000,000,000, return 1,000,000,000.
     *
     * Constraints:
     *   N in [1..100,000]
     *   K in [0..1,000,000,000]
     *   each element of A in [-1,000,000,000..1,000,000,000]
     */
        public static int solution(int K, int[] A) {
            int counter = 0;
            for (int l = 0, r = 0; l < A.length; ) {
                int min = Integer.MAX_VALUE;
                int max = Integer.MIN_VALUE;
                for(int i = l; i <= r; i++) {
                    if(A[i] < min) {
                        min = A[i];
                    }
                    if (A[i] > max){
                        max = A[i];
                    }
                }
                if(max - min <= K) {
                    counter++;
                    r++;
                    if(r > A.length -1 ) {
                        l++;
                        r = l;
                    }
                } else {
                    l++;
                    r = l;
                }
                if (counter > 1_000_000_000) {
                    return 1_000_000_000;
                }
            }
            return counter;
        }
}
