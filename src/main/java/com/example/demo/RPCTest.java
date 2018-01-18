package com.example.demo;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Created by chenlu on 2018/1/18.
 */
public class RPCTest {
    public static void main(String[] args) throws InterruptedException {
        new Thread(new Runnable() {
            public void run() {
                try {
                    Server serviceServer = new ServerCenter(8088);
                    serviceServer.register(HelloService.class, HelloServiceImpl.class);
                    serviceServer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        HelloService service = RpcClient.getRemoteProxyObj(HelloService.class,
                new InetSocketAddress("localhost", 8088));
        System.out.println(service.sayHi("test"));
    }
}
