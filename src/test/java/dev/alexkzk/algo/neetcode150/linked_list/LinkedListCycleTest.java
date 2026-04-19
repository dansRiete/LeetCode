package dev.alexkzk.algo.neetcode150.linked_list;

import dev.alexkzk.algo.neetcode150.ListNode;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class LinkedListCycleTest {
    private final LinkedListCycle sol = new LinkedListCycle();

    @Test
    void example1() {
        ListNode n1 = new ListNode(3);
        ListNode n2 = new ListNode(2);
        ListNode n3 = new ListNode(0);
        ListNode n4 = new ListNode(-4);
        n1.next = n2; n2.next = n3; n3.next = n4; n4.next = n2;
        assertTrue(sol.hasCycle(n1));
    }

    @Test
    void example2() {
        ListNode n1 = new ListNode(1);
        ListNode n2 = new ListNode(2);
        n1.next = n2; n2.next = n1;
        assertTrue(sol.hasCycle(n1));
    }

    @Test
    void edgeCase() {
        assertFalse(sol.hasCycle(new ListNode(1)));
    }
}
