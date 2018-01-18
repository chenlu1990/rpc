package com.example.demo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by chenlu on 2018/1/18.
 */
public class RpcClient<T> {
    public static <T> T getRemoteProxyObj(final Class<?> serviceInterface, final InetSocketAddress addr) {
        //1、将本地接口地调用转换成JDK动态代理，在动态代理中实现接口类的远程调用
        return (T) Proxy.newProxyInstance(serviceInterface.getClassLoader(),
                new Class<?>[]{serviceInterface},
                new InvocationHandler() {

                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) {

                        Socket socket = null;
                        ObjectInputStream inputStream = null;
                        ObjectOutputStream outputStream = null;
                        try {
                            //2.创建客户端，根据指定地址链接远程服务提供者
                            socket = new Socket();
                            socket.connect(addr);
                            //3、将远程服务调用所需的接口类、方法名、参数列表等编码后发送给服务提供者
                            outputStream = new ObjectOutputStream(socket.getOutputStream());
                            outputStream.writeUTF(serviceInterface.getName());
                            outputStream.writeUTF(method.getName());
                            outputStream.writeObject(method.getParameterTypes());
                            outputStream.writeObject(args);

                            //4、同步阻塞等待服务器返回应答，获取应答返回
                            inputStream = new ObjectInputStream(socket.getInputStream());
                            return inputStream.readObject();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                if (socket != null) {
                                    socket.close();
                                }
                                if (inputStream != null) {
                                    inputStream.close();
                                }
                                if (outputStream != null) {
                                    outputStream.close();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        return null;
                    }
                });
    }
}
