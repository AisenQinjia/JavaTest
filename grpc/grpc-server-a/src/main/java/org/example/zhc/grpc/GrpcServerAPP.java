package org.example.zhc.grpc;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import io.org.example.zhc.rpc.proto.Req;
import io.org.example.zhc.rpc.proto.Rsp;
import io.org.example.zhc.rpc.proto.TestServerGrpc;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

@Slf4j
public class GrpcServerAPP {
    public static CountDownLatch countDownLatch = new CountDownLatch(1);
    private static class GrpcServerA extends TestServerGrpc.TestServerImplBase{
        private final int port;
        private final Server server;
        @Override
        public void rpcCall(Req request, StreamObserver<Rsp> responseObserver) {
            log.info("client rpcCall: {} {}",request.getSrcService(),request.getMessage());
            log.info("response");
            Rsp rsp = Rsp.newBuilder()
                    .setMessage("sever resp!")
                    .build();
            responseObserver.onNext(rsp);
            responseObserver.onCompleted();
        }

        public GrpcServerA(int port){
            this.port = port;
            server = ServerBuilder.forPort(port).addService(this).build();
        }

        public void start() throws IOException {
            server.start();
            log.info("start the server, listening on {}",this.port);
        }

        public static void main(String[] args){
            try {
                new GrpcServerA(30030).start();
                countDownLatch.await();
            }catch (Exception e){
                log.error("exception ",e);
            }
        }
    }
}
