package dev.alexkzk.algo.neetcode150.linked_list;

import dev.alexkzk.algo.neetcode150.ListNode;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class ReorderListTest {
    private final ReorderList sol = new ReorderList();

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
        ListNode head = build(1, 2, 3, 4);
        sol.reorderList(head);
        assertArrayEquals(new int[]{1, 4, 2, 3}, toArray(head));
    }

    @Test
    void example2() {
        ListNode head = build(1, 2, 3, 4, 5);
        sol.reorderList(head);
        assertArrayEquals(new int[]{1, 5, 2, 4, 3}, toArray(head));
    }

    @Test
    void edgeCase() {
        ListNode head = build(1);
        sol.reorderList(head);
        assertArrayEquals(new int[]{1}, toArray(head));
    }
}
