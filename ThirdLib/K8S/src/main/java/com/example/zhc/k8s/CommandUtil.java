package com.example.zhc.k8s;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.List;

@Slf4j
public class CommandUtil {

    private static final boolean isWin = System.getProperty("os.name").toLowerCase().contains("win");

    private CommandUtil() {

    }
    private static String getShellCommand() {
        if (isWin) {
            return "cmd.exe";
        }
        return "/bin/bash";
    }

    private static String getEncoding() {
        if (isWin) {
            // windows 下是 GBK
            return "GBK";
        }
        return "UTF-8";
    }

    public static boolean executeCommand(List<String> commands, List<String> result) {
        Runtime run = Runtime.getRuntime();
        try {
            Process proc = run.exec(getShellCommand(), null, null);
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream(), getEncoding()));
            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(proc.getOutputStream())), true);
            for (String line : commands) {
                out.println(line);
            }
            out.println("exit");// 这个命令必须执行，否则in流不结束。
            StringBuilder resultBuilder = new StringBuilder();
            String rspLine = "";
            while ((rspLine = in.readLine()) != null) {
                log.info(rspLine);
                resultBuilder.append(rspLine);
            }
            proc.waitFor();
            in.close();
            out.close();
            int exitValue = proc.exitValue();
            proc.destroy();
            if (exitValue == 0) {
                result.add(resultBuilder.toString());
            }
            return exitValue == 0;
        } catch (IOException | InterruptedException e1) {
            log.info("exec command error ", e1);
            return false;
        }
    }
}
