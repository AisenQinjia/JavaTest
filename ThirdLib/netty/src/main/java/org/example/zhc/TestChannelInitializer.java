package org.example.zhc;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.util.ReferenceCountUtil;


public class TestChannelInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        System.out.println("new one");
        ch.pipeline().addLast(new TestServerHandler());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in = (ByteBuf) msg;
        try {
            while (in.isReadable()) { // (1)
                System.out.print((char) in.readByte());
                System.out.flush();
            }
        } finally {
            ReferenceCountUtil.release(msg); // (2)
        }
    }
}
