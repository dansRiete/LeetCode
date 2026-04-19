package dev.alexkzk.algo.neetcode150.linked_list;

import dev.alexkzk.algo.neetcode150.ListNode;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class MergeTwoSortedListsTest {
    private final MergeTwoSortedLists sol = new MergeTwoSortedLists();

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
        assertArrayEquals(new int[]{1, 1, 2, 3, 4, 4}, toArray(sol.mergeTwoLists(build(1, 2, 4), build(1, 3, 4))));
    }

    @Test
    void example2() {
        assertArrayEquals(new int[]{}, toArray(sol.mergeTwoLists(null, null)));
    }

    @Test
    void edgeCase() {
        assertArrayEquals(new int[]{0}, toArray(sol.mergeTwoLists(null, build(0))));
    }
}
