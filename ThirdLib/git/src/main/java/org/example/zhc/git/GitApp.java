package org.example.zhc.git;

import com.jcraft.jsch.Session;
import org.eclipse.jgit.api.LsRemoteCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.transport.JschConfigSessionFactory;
import org.eclipse.jgit.transport.OpenSshConfig;
import org.eclipse.jgit.transport.SshSessionFactory;
import org.eclipse.jgit.transport.SshTransport;

import java.util.Collection;

public class GitApp {
    public static void main(String[] args) {
        String remoteUrl = "git@192.168.10.51:tpf-service-group/tpf-mail.git";  // 把这个替换成你的git仓库地址
//        String remoteUrl = "git@192.168.10.51:shitou/tpf-service.git";  // 把这个替换成你的git仓库地址
//        String shortCommitId = "844ca59";  // 把这个替换成你的提交号或者tag的简短形式
        String shortCommitId = "3.5.1.0";  // 把这个替换成你的提交号或者tag的简短形式
        String sshPrivateKeyPath = "/path/to/your/private/key";  // SSH私钥的路径

        SshSessionFactory sshSessionFactory = new JschConfigSessionFactory() {
            @Override
            protected void configure(OpenSshConfig.Host hc, Session session) {
                // 不做Host Key检查
                session.setConfig("StrictHostKeyChecking", "no");
            }

//            @Override
//            protected JSch getJSch(OpenSshConfig.Host hc, FS fs) throws JSchException {
//                JSch defaultJSch = super.getJSch(hc, fs);
//                defaultJSch.removeAllIdentity();  // 移除所有已知的身份认证
//
//                try {
//                    Path privateKeyPath = Paths.get(sshPrivateKeyPath);
//                    byte[] privateKey = Files.readAllBytes(privateKeyPath);
//                    defaultJSch.addIdentity("Identity", privateKey, null, null);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//                return defaultJSch;
//            }
        };

        try {
            LsRemoteCommand lsRemoteCommand = new LsRemoteCommand(null);
            lsRemoteCommand.setRemote(remoteUrl);
            lsRemoteCommand.setTransportConfigCallback(transport -> {
                SshTransport sshTransport = (SshTransport) transport;
                sshTransport.setSshSessionFactory(sshSessionFactory);
            });
            Collection<Ref> refs = lsRemoteCommand.call();

            for (Ref ref : refs) {
                if (ref.getName().contains(shortCommitId) ||
                        ref.getObjectId().getName().startsWith(shortCommitId)) {
                    System.out.println("Full commit hash: " + ref.getObjectId().getName());
                }
            }
        } catch (GitAPIException e) {
            e.printStackTrace();
        }
    }
}


