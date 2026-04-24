package dev.alexkzk.algo.neetcode150.linked_list;

import dev.alexkzk.algo.neetcode150.ListNode;

import java.util.ArrayList;
import java.util.List;

public class ReverseLinkedList {
    /** LC #206 — Reverse Linked List [Easy] */
    public ListNode reverseListExplained(ListNode head) {

        ListNode prev = null;
        ListNode curr = head;
        ListNode next = null;
        System.out.println("i:" + "start");
        System.out.println("prev: " + prev);
        System.out.println("curr: " + curr);
        System.out.println("next: " + next);

        // i1
        next = curr.next;
        curr.next = prev;
        prev = curr;
        curr = next;
        System.out.println("i:" + 1);
        System.out.println("prev: " + prev);
        System.out.println("curr: " + curr);
        System.out.println("next: " + next);

        // i2
        next = curr.next;
        curr.next = prev;
        prev = curr;
        curr = next;
        System.out.println("i:" + 2);
        System.out.println("prev: " + prev);
        System.out.println("curr: " + curr);
        System.out.println("next: " + next);

        // i3
        next = curr.next;
        curr.next = prev;
        prev = curr;
        curr = next;
        System.out.println("i:" + 3);
        System.out.println("prev: " + prev);
        System.out.println("curr: " + curr);
        System.out.println("next: " + next);

        // i4
        next = curr.next;
        curr.next = prev;
        prev = curr;
        curr = next;
        System.out.println("i:" + 4);
        System.out.println("prev: " + prev);
        System.out.println("curr: " + curr);
        System.out.println("next: " + next);

        // i5
        next = curr.next;
        curr.next = prev;
        prev = curr;
        curr = next;
        System.out.println("i:" + 5);
        System.out.println("prev: " + prev);
        System.out.println("curr: " + curr);
        System.out.println("next: " + next);

        return prev;

    }

    public ListNode reverseList(ListNode head) {
        ListNode prev = null;
        ListNode curr = head;
        ListNode next = null;
        int i = 0;

        while (curr != null) {
            System.out.println("i:" + i);
            System.out.println("prev: " + prev);
            System.out.println("curr: " + curr);
            System.out.println("next: " + next);
            i++;
            next = curr.next;
            curr.next = prev;
            prev = curr;
            curr = next;
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
