package com.example.demo;

import java.util.Date;

public class VisibilityDemo {
    static int k = 0;
//    volatile static int k = 0;

    public static void main(String[] args) {
        new Thread(()->{
            while (true){
                //加上之后k居然可见了
//                System.out.println("sout");
                if(k>=5){
                    System.out.println("______________k可见了" + new Date(System.currentTimeMillis()) +"           " +  k);
                    //拿到if外 也可见
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        new Thread(()->{
            while(true){
                k++;
                System.out.println("k++" + new Date(System.currentTimeMillis()) + "    " + k);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
