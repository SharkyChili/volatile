git地址：https://github.com/fw1036994377/volatile.git
### 不具有原子性
``` java
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
```
``` java
99994
显然volatile不具有原子性，这个不用多说
Process finished with exit code 0
```
### 具有可见性
``` java
public class VisibilityDemo {
    static int k = 0;
//    volatile static int k = 0;

    public static void main(String[] args) {
        new Thread(()->{
            while (true){
                if(k>=5){
                    System.out.println("______________k可见了" + new Date(System.currentTimeMillis()) +"           " +  k);
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
```
``` java
k++Thu Dec 05 18:12:34 CST 2019    1
k++Thu Dec 05 18:12:35 CST 2019    2
k++Thu Dec 05 18:12:36 CST 2019    3
k++Thu Dec 05 18:12:37 CST 2019    4
k++Thu Dec 05 18:12:38 CST 2019    5
k++Thu Dec 05 18:12:39 CST 2019    6
k++Thu Dec 05 18:12:40 CST 2019    7
k++Thu Dec 05 18:12:41 CST 2019    8
k++Thu Dec 05 18:12:42 CST 2019    9
k++Thu Dec 05 18:12:43 CST 2019    10
```
说明第一个线程没有意识到k已经>=5了，而且过了很久都没有意识到，多次实验均如此，

加上volatile之后
``` java
//    static int k = 0;
    volatile static int k = 0;
```
``` java
k++Thu Dec 05 18:15:07 CST 2019    1
k++Thu Dec 05 18:15:08 CST 2019    2
k++Thu Dec 05 18:15:09 CST 2019    3
k++Thu Dec 05 18:15:10 CST 2019    4
k++Thu Dec 05 18:15:11 CST 2019    5
______________k可见了Thu Dec 05 18:15:11 CST 2019           5
______________k可见了Thu Dec 05 18:15:12 CST 2019           6
k++Thu Dec 05 18:15:12 CST 2019    6
______________k可见了Thu Dec 05 18:15:13 CST 2019           7
k++Thu Dec 05 18:15:13 CST 2019    7
```
第一个线程可以意识到k>5了，多次试验均是如此。

### 还有一些没想明白的问题
``` java
            while (true){
                //加上之后k居然可见了
                System.out.println("sout");
                if(k>=5){
```
在while和if之间加上一句之后，在五秒后，第一个线程就可以看到k更新了，，多次实验均如此
``` java
                if(k>=5){
                    System.out.println("______________k可见了" + new Date(System.currentTimeMillis()) +"           " +  k);
                    //拿到if外 也可见

                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
```
或者把sleep放到if外，五秒后，第一个线程也可以看到k更新，多次实验均如此

这就有点想不通了。

### 结论
虽然有想不通的地方，不过在原子性，可见性上还是有一些收获的，volatile可以立即看到新的值（可见性），但是不具有原子性。

