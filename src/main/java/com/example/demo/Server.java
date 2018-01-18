package com.example.demo;

import java.io.IOException;

/**
 * Created by chenlu on 2018/1/18.
 */
public interface Server {
    public void stop();
    public void start() throws IOException;
    public void register(Class serviceInterface,Class impl);
    public boolean isRunning();
    public int getPort();
}
