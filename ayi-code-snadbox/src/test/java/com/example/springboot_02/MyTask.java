package com.example.springboot_02;

/**
 * className:MyTask
 * Package:com.example.springboot_02
 * Description: TODO
 *
 * @Date: 2023/12/21 11:03
 * @Author:hjy
 */
public class MyTask implements Runnable {
    //设计一个变量,用于表示商品的数量
    private static int id = 10;
    //表示客户名称的变量
    private String userName;

    public MyTask(String userName) {
        this.userName = userName;
    }

    @Override
    public void run() {
        String name = Thread.currentThread().getName();
        System.err.println(userName+"正在使用"+name+"参与秒杀任务...");
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        synchronized (MyTask.class){
            if(id>0){
                System.out.println(userName+"使用"+name+"秒杀:"+id-- +"号商品成功啦!");
            }else {
                System.err.println(userName+"使用"+name+"秒杀失败啦!");
            }
        }
    }
}
