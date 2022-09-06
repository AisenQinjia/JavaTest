package org.example.zhc;

import com.google.protobuf.InvalidProtocolBufferException;
import io.org.example.zhc.rpc.proto.Resp;
import io.org.example.zhc.rpc.proto.SizeProto;

public class ProtoTestApp {
    static public void main(String[] args) throws InvalidProtocolBufferException {
        SizeProto proto = SizeProto.newBuilder().setLongFiled(1).build();
        byte[] bytes = proto.toByteArray();
        System.out.println(bytes.length);
        byte[] a = new byte[0];
        Resp build = Resp.newBuilder().setErrorCode(Resp.EnumType.ERROR).build();
        Resp build2 = Resp.newBuilder().build();
        Resp resp = Resp.parseFrom(build.toByteArray());
        Resp resp2 = Resp.parseFrom(build2.toByteArray());
    }
}
