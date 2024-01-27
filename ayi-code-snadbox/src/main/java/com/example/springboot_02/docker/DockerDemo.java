package com.example.springboot_02.docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.PullImageCmd;
import com.github.dockerjava.api.command.PullImageResultCallback;
import com.github.dockerjava.api.model.PullResponseItem;
import com.github.dockerjava.core.DockerClientBuilder;

/**
 * className:DOckerDemo
 * Package:com.example.springboot_02.docker
 * Description: TODO
 *
 * @Date: 2023/12/18 23:14
 * @Author:hjy
 */
public class DOckerDemo {
    public static void main(String[] args) throws InterruptedException {
        // 获取操作docker的Java客户端
        DockerClient dockerClient = DockerClientBuilder.getInstance().build();
        // 拉取镜像
        PullImageCmd pullImageCmd = dockerClient.pullImageCmd("hello-world:latest");
        PullImageResultCallback pullImageResultCallback = new PullImageResultCallback(){
            @Override
            public void onNext(PullResponseItem item) {
                System.out.println("下载镜像：" + item.getStatus());
                super.onNext(item);
            }
        };
        pullImageCmd.exec(pullImageResultCallback).awaitCompletion();
        System.out.println("下载完成");
        // 创建容器
    }
}
