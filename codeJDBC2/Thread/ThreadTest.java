package Thread;

/**
 * @program: codeJDBC
 * @author: Ren
 * @create: 2022-10-15 13:43
 * @description:
 *
 *   使用继承Thread类的方式创建多线程
 **/

class MyThread1 extends Thread{
    @Override
    public void run() {
        for(int i = 0; i < 100; i++){
           if (i % 2 == 0){
               System.out.println(" 我是线程 1");
           }
        }
    }
}

class MyThread2 extends Thread{
    @Override
    public void run() {
        for(int i = 0; i < 100; i++){
            if (i % 2 != 0){
                System.out.println(" 我是线程 2");
            }
        }
    }
}
public class ThreadTest {
    public static void main(String[] args) {
        MyThread1 thread1 = new MyThread1();
        MyThread2 thread2 = new MyThread2();

        thread1.setName("线程1 --->");
        thread2.setName("线程2 --->");

        thread1.start();
        thread2.start();
    }
}
