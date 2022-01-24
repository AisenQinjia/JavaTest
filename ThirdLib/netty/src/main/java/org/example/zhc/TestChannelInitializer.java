package org.example.zhc;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;


public class TestChannelInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        System.out.println("new one");
        ch.pipeline().addLast(new TestServerHandler());
    }

}
