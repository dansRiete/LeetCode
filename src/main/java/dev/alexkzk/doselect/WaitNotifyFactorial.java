package dev.alexkzk.doselect;

import java.util.Scanner;

public class WaitNotifyFactorial {
    private static final Object lock = new Object();
    private static volatile int number = 0;

    public static void main(String[] args) {

        Thread t1 = new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter an integer: ");
            int input = scanner.nextInt();

            synchronized (lock) {
                number = input;
                lock.notify();
            }
        });

        Thread t2 = new Thread(() -> {
            synchronized (lock) {
                while (number == 0) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                System.out.println("Factorial of " + number + " = " + factorial(number));
            }
        });

        t2.start();
        t1.start();
    }

    private static long factorial(int n) {
        long result = 1;
        for (int i = 2; i <= n; i++) result *= i;
        return result;
    }
}
