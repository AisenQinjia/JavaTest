package org.example.zhc;

import org.junit.Test;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryManagerMXBean;

import java.util.List;

public class MemoryTest {
    public static void main(String[] args){
        System.out.println("hello world");
    }

    @Test
    public void jvmMemoryManagers(){
        List<MemoryManagerMXBean> memBeans = ManagementFactory.getMemoryManagerMXBeans();
    }
}
