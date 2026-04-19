package dev.alexkzk.algo.neetcode150.linked_list;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("TODO: implement solution")
class CopyListRandomPointerTest {
    private final CopyListRandomPointer sol = new CopyListRandomPointer();

    @Test
    void example1() {
        CopyListRandomPointer.Node n1 = new CopyListRandomPointer.Node(7);
        CopyListRandomPointer.Node n2 = new CopyListRandomPointer.Node(13);
        n1.next = n2;
        n2.random = n1;
        CopyListRandomPointer.Node copy = sol.copyRandomList(n1);
        assertNotSame(n1, copy);
        assertEquals(7, copy.val);
        assertEquals(13, copy.next.val);
    }

    @Test
    void example2() {
        assertNull(sol.copyRandomList(null));
    }

    @Test
    void edgeCase() {
        CopyListRandomPointer.Node n = new CopyListRandomPointer.Node(1);
        n.random = n;
        CopyListRandomPointer.Node copy = sol.copyRandomList(n);
        assertNotSame(n, copy);
        assertSame(copy, copy.random);
    }
}
