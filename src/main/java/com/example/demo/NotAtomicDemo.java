package com.example.demo;

import java.util.concurrent.CountDownLatch;

public class NotAtomicDemo {
    volatile static int k;
//    static int k = 0;
    public static void main(String[] args) {
        int num = 100000;
        CountDownLatch countDownLatch = new CountDownLatch(num);
        for (int i = 0; i < num; i++) {
            new Thread(()->{
                k++;
                countDownLatch.countDown();
            }).start();
        }


        try {
            countDownLatch.await();
            System.out.println(k);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
