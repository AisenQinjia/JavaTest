package org.example.zhc.validation.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

public class ClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("client close");
        ctx.close();
    }

    /**
     * @see io.netty.channel.ChannelInboundHandler#channelRead(ChannelHandlerContext, Object)
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object source) throws Exception {
        try{
            System.out.println("client received this");
        } catch (Exception e) {
            System.out.println("channelRead :" + e);
        }finally{
            ReferenceCountUtil.safeRelease(source);
        }
    }

    private void handlerObject(ChannelHandlerContext ctx, Object msg) {

//        Student student = (Student)msg;
//        System.err.println("server 获取信息："+student.getId()+student.getName());
    }


    /**
     * 数据读取完毕的处理
     * @see io.netty.channel.ChannelInboundHandler#channelReadComplete(ChannelHandlerContext)
     */
//    @Override
//    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
//        System.err.println("客户端读取数据完毕");
//    }

    /**
     * 出现异常的处理
     * @param ctx 上下文
     * @param cause 错误异常
     * @throws Exception 抛出异常
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("exceptionCaught " + cause);
        ctx.close();
    }
}
