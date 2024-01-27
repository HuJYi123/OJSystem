package com.example.springboot_02;

import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * className:MyTest
 * Package:com.example.springboot_02
 * Description: TODO
 *
 * @Date: 2023/12/21 11:03
 * @Author:hjy
 */
public class MyTest {
    public static void main(String[] args) {
        //1:创建一个线程池对象
        ThreadPoolExecutor pool = new ThreadPoolExecutor(3,10,1, TimeUnit.MINUTES,new LinkedBlockingQueue<>(15));
        //2:循环创建任务对象
        for (int i = 1; i <=20 ; i++) {
            MyTask myTask = new MyTask("客户"+i);
            Future<?> submit = pool.submit(myTask);
            pool.execute(myTask);
        }
        //3:关闭线程池
        pool.shutdown();
    }
}