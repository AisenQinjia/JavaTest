package org.example.zhc.util.zhc.validation.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.ReferenceCountUtil;
import org.example.zhc.util.zhc.validation.TestServer;

public class TestClient {
    public static void main(String[] args) throws InterruptedException {
        String host = "127.0.0.1";
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap(); // (1)
            b.group(workerGroup); // (2)
            b.channel(NioSocketChannel.class); // (3)
            b.option(ChannelOption.SO_KEEPALIVE, true); // (4)
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) {
                    ch.pipeline().addLast(new TestClientMessageHandler());
                    ch.pipeline().addLast(new ClientHandler());
                    ch.pipeline().addLast(new SimpleChannelInboundHandler() {
                        @Override
                        protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
                            ReferenceCountUtil.safeRelease(msg);
                        }
                    });

                }});
            // Start the client.
            ChannelFuture f = b.connect(host, TestServer.port).sync(); // (5)

            // Wait until the connection is closed.
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }
}
