package org.example.zhc;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class TestServer {
    private int port;
    public TestServer(int port){
        this.port = port;
    }

    public void run() throws Exception {
        //boss group 用于接收连接
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        //worker group 用于处理建立好的连接
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap(); // sets up a server
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class) // instantiate to accept incoming connections
                    .childHandler(new TestChannelInitializer()) // 处理新接收的channel
                    .option(ChannelOption.SO_BACKLOG, 128) // channel options for channel
                    .childOption(ChannelOption.SO_KEEPALIVE, true); // channel options for childChanel

            // Bind and start to accept incoming connections.
            ChannelFuture f = b.bind(port).sync(); //

            // Wait until the server socket is closed.
            // In this example, this does not happen, but you can do that to gracefully
            // shut down your server.
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        int port = 34511;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }

        new TestServer(port).run();
    }
}
