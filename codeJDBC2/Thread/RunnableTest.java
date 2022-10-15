package Thread;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @program: codeJDBC
 * @author: Ren
 * @create: 2022-10-15 14:18
 * @description:
 **/
class RunThread1 implements Runnable {
    private static int stick = 100;
    private ReentrantLock lock = new ReentrantLock(true);  //使用公平锁保证每个线程会交替进行

    @Override
    public void run() {
        while (true) {
            //公平锁使用try-finally包围保证上锁与解锁会进行
            try {
                lock.lock();
                if (stick > 0) {
                    System.out.println(Thread.currentThread().getName() + " -->卖票，票号为：" + stick--);
                } else {
                    break;
                }
            } finally {
                lock.unlock();
            }
        }
    }
}

public class RunnableTest {
    public static void main(String[] args) {
        RunThread1 run = new RunThread1();

        Thread thread1 = new Thread(run);
        thread1.setName("线程1 ：");

        Thread thread2 = new Thread(run);
        thread2.setName("线程2 ：");

        Thread thread3 = new Thread(run);
        thread3.setName("线程3 ：");

        thread1.start();
        thread2.start();
        thread3.start();
    }
}
