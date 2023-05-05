# 设置基础镜像
FROM default.registry.tke-syyx.com/syyx-tpf/tpf-java-base-8:1.0.14.wuhuids1

# 将 WHBuild.tar.gz 文件复制到镜像的 /tmp 目录中, 该文件路径可配置
COPY WHBuild.tar.gz /tmp/

# 设置工作目录
WORKDIR /tmp

# 解压缩 WHBuild.tar.gz 文件
RUN tar -xzf WHBuild.tar.gz && \
    rm WHBuild.tar.gz

# 设置工作目录为 WHBuild 文件夹
WORKDIR /tmp/WHBuild

# 启动 WuHuiDSServer 程序
CMD ["./WuHuiDSServer"]
