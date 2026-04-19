package dev.alexkzk.algo.neetcode150.linked_list;

import dev.alexkzk.algo.neetcode150.ListNode;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class AddTwoNumbersTest {
    private final AddTwoNumbers sol = new AddTwoNumbers();

    private ListNode build(int... vals) {
        ListNode dummy = new ListNode(0);
        ListNode cur = dummy;
        for (int v : vals) { cur.next = new ListNode(v); cur = cur.next; }
        return dummy.next;
    }

    private int[] toArray(ListNode head) {
        java.util.List<Integer> list = new java.util.ArrayList<>();
        while (head != null) { list.add(head.val); head = head.next; }
        return list.stream().mapToInt(Integer::intValue).toArray();
    }

    @Test
    void example1() {
        // 342 + 465 = 807
        assertArrayEquals(new int[]{7, 0, 8}, toArray(sol.addTwoNumbers(build(2, 4, 3), build(5, 6, 4))));
    }

    @Test
    void example2() {
        assertArrayEquals(new int[]{0}, toArray(sol.addTwoNumbers(build(0), build(0))));
    }

    @Test
    void edgeCase() {
        // 9999999 + 9999 = 10009998
        assertArrayEquals(new int[]{8, 9, 9, 9, 0, 0, 0, 1}, toArray(sol.addTwoNumbers(build(9, 9, 9, 9, 9, 9, 9), build(9, 9, 9, 9))));
    }
}
