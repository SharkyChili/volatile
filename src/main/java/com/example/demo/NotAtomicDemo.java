package com.example.demo;

import java.util.concurrent.CountDownLatch;

public class NotAtomicDemo {
    volatile static int k;
    public static void main(String[] args) {
        int num = 100;
        CountDownLatch countDownLatch = new CountDownLatch(num);
        for (int i = 0; i < num; i++) {
            new Thread(()->{
                k++;
            }).start();
            countDownLatch.countDown();
        }

        try {
            countDownLatch.await();
            System.out.println(k);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
