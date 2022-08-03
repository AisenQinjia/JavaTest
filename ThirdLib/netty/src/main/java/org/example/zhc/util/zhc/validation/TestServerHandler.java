package org.example.zhc.util.zhc.validation;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class TestServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(final ChannelHandlerContext ctx) throws InterruptedException { // (1)
        final ByteBuf time = ctx.alloc().buffer(TestServer.msg_size); // (2)
        time.writeBytes(new byte[TestServer.msg_size]);
        System.out.println("send begin! " + TestServer.msg_size);
        final ChannelFuture f = ctx.writeAndFlush(time); // (3)
//        f.addListener((ChannelFutureListener) future -> {
//            System.out.println("send complete! " + future.isSuccess());
//            future.channel().close();
//        });
        f.channel().flush();
        f.channel().close().sync();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ctx.write(msg); // (1)
        ctx.flush(); // (2)
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
