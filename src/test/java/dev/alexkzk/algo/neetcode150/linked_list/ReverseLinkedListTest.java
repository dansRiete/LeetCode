package dev.alexkzk.algo.neetcode150.linked_list;

import dev.alexkzk.algo.neetcode150.ListNode;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class ReverseLinkedListTest {
    private final ReverseLinkedList sol = new ReverseLinkedList();

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
        assertArrayEquals(new int[]{5, 4, 3, 2, 1}, toArray(sol.reverseList(build(1, 2, 3, 4, 5))));
    }

    @Test
    void example2() {
        assertArrayEquals(new int[]{2, 1}, toArray(sol.reverseList(build(1, 2))));
    }

    @Test
    void edgeCase() {
        assertNull(sol.reverseList(null));
    }
}
