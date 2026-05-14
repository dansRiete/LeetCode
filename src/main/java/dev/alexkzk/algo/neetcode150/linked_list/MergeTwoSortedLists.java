package dev.alexkzk.algo.neetcode150.linked_list;

import dev.alexkzk.algo.neetcode150.ListNode;

public class MergeTwoSortedLists {
    /** LC #21 — Merge Two Sorted Lists [Easy] */
    public ListNode mergeTwoLists2(ListNode l1, ListNode l2) {
        ListNode result = new ListNode(0);
        while (l1 != null && l2 != null) {
            if (l1.val <= l2.val) {
                result.next = l1;
                l1 = l1.next;
            } else {
                result.next = l2;
                l2 = l2.next;
            }
        }
        System.out.println(result);
        return null;

    }

    /** LC #21 — Merge Two Sorted Lists [Easy] */
    public ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        ListNode dummy = new ListNode(0);
        ListNode cur = dummy;
        while (l1 != null && l2 != null) {
            System.out.println();
            if (l1.val <= l2.val) {
                cur.next = l1;
                l1 = l1.next;
            } else {
                cur.next = l2;
                l2 = l2.next;
            }
            cur = cur.next;
        }
        cur.next = l1 != null ? l1 : l2;
        return dummy.next;
    }
}
