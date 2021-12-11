package org.example.grpc.test;

import io.grpc.Channel;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.org.example.zhc.rpc.proto.Req;
import io.org.example.zhc.rpc.proto.Rsp;
import io.org.example.zhc.rpc.proto.TestServerGrpc;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class GrpcClient {
    static final String  HOST ="localhost";
    static final int  PORT =30030;

    private ManagedChannel channel;
    private TestServerGrpc.TestServerBlockingStub blockingStub;
    private TestServerGrpc.TestServerStub asyncStub;
    public GrpcClient(){
        channel = ManagedChannelBuilder.forAddress(HOST,PORT).usePlaintext().build();
        blockingStub = TestServerGrpc.newBlockingStub(channel);
        asyncStub = TestServerGrpc.newStub(channel);
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
}
