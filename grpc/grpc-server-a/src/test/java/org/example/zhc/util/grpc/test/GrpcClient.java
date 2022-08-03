package org.example.zhc.util.grpc.test;

import io.grpc.Channel;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import io.org.example.zhc.rpc.proto.Req;
import io.org.example.zhc.rpc.proto.Rsp;
import io.org.example.zhc.rpc.proto.TestServerGrpc;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@Slf4j
public class GrpcClient {
    static final String  HOST ="localhost";
    static final int  PORT =30030;
    static CountDownLatch countDownLatch = new CountDownLatch(1);
    private ManagedChannel channel;
    private TestServerGrpc.TestServerBlockingStub blockingStub;
    private TestServerGrpc.TestServerStub asyncStub;
    public GrpcClient(){
        channel = ManagedChannelBuilder.forAddress(HOST,PORT).usePlaintext().build();
        blockingStub = TestServerGrpc.newBlockingStub(channel);
        asyncStub = TestServerGrpc.newStub(channel);
    }
    @Before
    public void before(){

    }
    @Test
    public void rpcCall(){
        Req req = Req.newBuilder()
                .setSrcService("client")
                .setMessage("hello server")
                .build();
        Rsp rsp = blockingStub.rpcCall(req);
        log.info("receive server message: {}",rsp.getMessage());
    }

    @Test
    public void serverStreamResponse(){
        Req req = Req.newBuilder().build();
        Iterator<Rsp> rsp = blockingStub.serverStreamCall(req);
        while (rsp.hasNext()){
            log.info("rsp");
            rsp.next();
        }
    }

    @Test
    public void clientStreamCall(){
         StreamObserver<Req> reqStreamObserver = asyncStub.clientStreamCall(new StreamObserver<Rsp>() {
            @Override
            public void onNext(Rsp rsp) {
                log.info("server onNext");
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onCompleted() {
                log.info("server onCompleted");
            }
        });
         Req req = Req.newBuilder().build();
        reqStreamObserver.onNext(req);
        reqStreamObserver.onNext(req);
        reqStreamObserver.onNext(req);
        reqStreamObserver.onCompleted();
    }
    @Test
    public void biStreamCall(){
        StreamObserver<Req> req = asyncStub.biStreamCall(new StreamObserver<Rsp>() {
            @Override
            public void onNext(Rsp rsp) {
                log.info("onNext rsp");
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onCompleted() {
                log.info("onCompleted");
            }
        });
        req.onNext(Req.newBuilder().build());
        req.onNext(Req.newBuilder().build());
        req.onNext(Req.newBuilder().build());
    }
    @After
    public void after() throws InterruptedException {
        countDownLatch.await();
    }

}
