package dev.alexkzk.algo.neetcode150.linked_list;

import dev.alexkzk.algo.neetcode150.ListNode;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class RemoveNthFromEndTest {
    private final RemoveNthFromEnd sol = new RemoveNthFromEnd();

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
        assertArrayEquals(new int[]{1, 2, 3, 5}, toArray(sol.removeNthFromEnd(build(1, 2, 3, 4, 5), 2)));
    }

    @Test
    void example2() {
        assertArrayEquals(new int[]{}, toArray(sol.removeNthFromEnd(build(1), 1)));
    }

    @Test
    void edgeCase() {
        assertArrayEquals(new int[]{2}, toArray(sol.removeNthFromEnd(build(1, 2), 2)));
    }
}
