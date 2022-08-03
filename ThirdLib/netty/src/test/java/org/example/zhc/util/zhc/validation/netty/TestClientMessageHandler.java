package org.example.zhc.util.zhc.validation.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
//import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.example.zhc.util.zhc.validation.TestServer;

import java.util.List;

public class TestClientMessageHandler extends ByteToMessageDecoder {
//    @Override
//    public void channelRead(ChannelHandlerContext ctx, Object msg) {
//        ByteBuf m = (ByteBuf) msg; // (1)
//        lastBytes = lastBytes + m.readableBytes();
//        if(lastBytes < TestServer.msg_size){
//            System.out.println("not yet: " + (TestServer.msg_size - lastBytes));
//            return;
//        }
//        System.out.println("message received!");
//    }
//
//    @Override
//    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
//        cause.printStackTrace();
//        ctx.close();
//    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel closed!");
        super.channelInactive(ctx);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if(in.readableBytes() < TestServer.msg_size){
            System.out.println("not yet: " + in.readableBytes());
            return;
        }
        in.readBytes(TestServer.msg_size);
        System.out.println("message received!");
        out.add(new Object());
    }
}
