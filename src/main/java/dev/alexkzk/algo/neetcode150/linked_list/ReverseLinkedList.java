package dev.alexkzk.algo.neetcode150.linked_list;

import dev.alexkzk.algo.neetcode150.ListNode;

import java.util.ArrayList;
import java.util.List;

public class ReverseLinkedList {
    /** LC #206 — Reverse Linked List [Easy] */
    public ListNode reverseList(ListNode head) {
//        System.out.println(head);
        ListNode prev = null;
        ListNode curr = head;
        while(curr != null) {
            ListNode next = curr.next;
            curr.next = prev;
            prev = curr;
            curr = next;
//            System.out.println(curr);
            System.out.println(prev);
        }
        return prev;

    }

    public static void main(String[] args) {
        ReverseLinkedList reverseLinkedList = new ReverseLinkedList();
        reverseLinkedList.reverseList(reverseLinkedList.build(1,2,3,4,5));
    }


    private ListNode build(int... vals) {
        ListNode dummy = new ListNode(0);
        ListNode cur = dummy;
        for (int v : vals) {
            cur.next = new ListNode(v);
            cur = cur.next;
        }
        return dummy.next;
    }
}
