package Thread;

/**
 * @program: codeJDBC
 * @author: Ren
 * @create: 2022-10-15 13:57
 * @description:
 **/

class MyThread3 extends Thread{
    private static int stick = 100;
    @Override
    public void run() {

        synchronized (ThreadTest2.class) {
            ThreadTest2.class.notify();
            show();
            try {
                ThreadTest2.class.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void show (){
        try{
            Thread.sleep(100);
        }catch (InterruptedException e) {
            e.printStackTrace();
        }

        while (stick > 0)
        System.out.println( Thread.currentThread().getName() + " ---卖票，票号为： " + stick--);
    }
}

public class ThreadTest2 {
    public static void main(String[] args) {
        MyThread3 thread1 = new MyThread3();
        thread1.setName("窗口 1 ");

        MyThread3 thread2 = new MyThread3();
        thread2.setName("窗口 2 ");

        MyThread3 thread3 = new MyThread3();
        thread3.setName("窗口 3 ");

        thread1.start();
        thread2.start();
        thread3.start();
    }
}
