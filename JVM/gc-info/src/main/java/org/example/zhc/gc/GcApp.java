package org.example.zhc.gc;
import org.junit.Test;

import java.lang.management.ClassLoadingMXBean;
import java.lang.management.CompilationMXBean;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.RuntimeMXBean;
import java.lang.management.ThreadMXBean;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class GcApp {

    private static NumberFormat fmtI = new DecimalFormat("###,###", new DecimalFormatSymbols(Locale.ENGLISH));
    private static NumberFormat fmtD = new DecimalFormat("###,##0.000", new DecimalFormatSymbols(Locale.ENGLISH));

    public static void main(String[] args) {
        //运行时情况
        RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
        //操作系统情况
        OperatingSystemMXBean os = ManagementFactory.getOperatingSystemMXBean();
        //线程使用情况
        ThreadMXBean threads = ManagementFactory.getThreadMXBean();
        //堆内存使用情况
        MemoryUsage heapMemoryUsage = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage();
        //非堆内存使用情况
        MemoryUsage nonHeapMemoryUsage = ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage();
        //类加载情况
        ClassLoadingMXBean cl = ManagementFactory.getClassLoadingMXBean();
        //内存池对象
        List<MemoryPoolMXBean> pools = ManagementFactory.getMemoryPoolMXBeans();
        //编译器和编译情况
        CompilationMXBean cm = ManagementFactory.getCompilationMXBean();
        //获取GC对象（不好用）
        List<GarbageCollectorMXBean> gcmList = ManagementFactory.getGarbageCollectorMXBeans();



        //运行时情况
        System.out.printf("jvm.name (JVM名称-版本号-供应商):%s | version: %s | vendor: %s  %n", runtime.getVmName(), runtime.getVmVersion(), runtime.getVmVendor());
        System.out.printf("jvm.spec.name (JVM规范名称-版本号-供应商):%s | version: %s | vendor: %s  %n", runtime.getSpecName(), runtime.getSpecVersion(), runtime.getSpecVendor());
        System.out.printf("jvm.java.version (JVM JAVA版本):%s%n", System.getProperty("java.version"));
        System.out.printf("jvm.start.time (Java虚拟机的启动时间):%s%n", toDate(runtime.getStartTime()));
        System.out.printf("jvm.uptime (Java虚拟机的正常运行时间):%s%n", toDuration(runtime.getUptime()));

        System.out.println("------------------------------------------------------------------------------------------------------");

        //编译情况
        System.out.printf("compilation.name(编译器名称)：%s%n",cm.getName());
        System.out.printf("compilation.total.time(编译器耗时)：%d毫秒%n",cm.getTotalCompilationTime());
        boolean isSupport=cm.isCompilationTimeMonitoringSupported();
        if(isSupport){
            System.out.println("支持即时编译器编译监控");
        }else{
            System.out.println("不支持即时编译器编译监控");
        }
        System.out.printf("------------------------------------------------------------------------------------------------------");
        //JVM 线程情况
        System.out.printf("jvm.threads.total.count (总线程数(守护+非守护)):%d%n", threads.getThreadCount());
        System.out.printf("jvm.threads.daemon.count (守护进程线程数):%d%n", threads.getDaemonThreadCount());
        System.out.printf("jvm.threads.peak.count (峰值线程数):%d%n", threads.getPeakThreadCount());
        System.out.printf("jvm.threads.total.start.count(Java虚拟机启动后创建并启动的线程总数):%d%n", threads.getTotalStartedThreadCount());
        for(Long threadId : threads.getAllThreadIds()) {
            System.out.printf("threadId: %d | threadName: %s%n", threadId, threads.getThreadInfo(threadId).getThreadName());
        }
        System.out.println("------------------------------------------------------------------------------------------------------");
        //获取GC信息

        System.out.println("------------------------------------------------------------------------------------------------------");
        //堆内存情况
        System.out.printf("jvm.heap.init (初始化堆内存):%s %n",  bytesToMB(heapMemoryUsage.getInit()));
        System.out.printf("jvm.heap.used (已使用堆内存):%s %n", bytesToMB(heapMemoryUsage.getUsed()));
        System.out.printf("jvm.heap.committed (可使用堆内存):%s %n", bytesToMB(heapMemoryUsage.getCommitted()));
        System.out.printf("jvm.heap.max (最大堆内存):%s %n", bytesToMB(heapMemoryUsage.getMax()));

        System.out.println("------------------------------------------------------------------------------------------------------");

        //非堆内存使用情况
        System.out.printf("jvm.noheap.init (初始化非堆内存):%s %n",  bytesToMB(nonHeapMemoryUsage.getInit()));
        System.out.printf("jvm.noheap.used (已使用非堆内存):%s %n",  bytesToMB(nonHeapMemoryUsage.getUsed()));
        System.out.printf("jvm.noheap.committed (可使用非堆内存):%s %n",  bytesToMB(nonHeapMemoryUsage.getCommitted()));
        System.out.printf("jvm.noheap.max (最大非堆内存):%s %n", bytesToMB(nonHeapMemoryUsage.getMax()));

        System.out.println("------------------------------------------------------------------------------------------------------");

        //系统概况
        System.out.printf("os.name(操作系统名称-版本号):%s %s %s %n", os.getName(), "version", os.getVersion());
        System.out.printf("os.arch(操作系统内核):%s%n", os.getArch());
        System.out.printf("os.cores(可用的处理器数量):%s %n", os.getAvailableProcessors());
        System.out.printf("os.loadAverage(系统负载平均值):%s %n", os.getSystemLoadAverage());

        System.out.println("------------------------------------------------------------------------------------------------------");

        //类加载情况
        System.out.printf("class.current.load.count(当前加载类数量):%s %n", cl.getLoadedClassCount());
        System.out.printf("class.unload.count(未加载类数量):%s %n", cl.getUnloadedClassCount());
        System.out.printf("class.total.load.count(总加载类数量):%s %n", cl.getTotalLoadedClassCount());

        System.out.println("------------------------------------------------------------------------------------------------------");

        for(MemoryPoolMXBean pool : pools) {
            final String kind = pool.getType().name();
            final MemoryUsage usage = pool.getUsage();
            System.out.println("内存模型： " + getKindName(kind) + ", 内存空间名称： " + getPoolName(pool.getName()) + ", jvm." + pool.getName() + ".init(初始化):" + bytesToMB(usage.getInit()));
            System.out.println("内存模型： " + getKindName(kind) + ", 内存空间名称： " + getPoolName(pool.getName()) + ", jvm." + pool.getName() + ".used(已使用): " + bytesToMB(usage.getUsed()));
            System.out.println("内存模型： " + getKindName(kind) + ", 内存空间名称： " + getPoolName(pool.getName()) + ", jvm." + pool.getName()+ ".committed(可使用):" + bytesToMB(usage.getCommitted()));
            System.out.println("内存模型： " + getKindName(kind) + ", 内存空间名称： " + getPoolName(pool.getName()) + ", jvm." + pool.getName() + ".max(最大):" + bytesToMB(usage.getMax()));
            System.out.println("------------------------------------------------------------------------------------------------------");
        }

    }


    @Test
    public  void reportGc(){
        long fullCount = 0, fullTime = 0, youngCount = 0, youngTime = 0;
        List<GarbageCollectorMXBean> gcs = ManagementFactory.getGarbageCollectorMXBeans();
        for (GarbageCollectorMXBean gc : gcs) {
            System.out.println("gc : " + gc.getName());
            gc.getCollectionCount();
//            switch (GarbageCollectorName.of(gc.getName())) {
//                case MarkSweepCompact:
//                case PSMarkSweep:
//                case ConcurrentMarkSweep:
//                    fullCount += gc.getCollectionCount();
//                    fullTime += gc.getCollectionTime();
//                    break;
//                case Copy:
//                case ParNew:
//                case PSScavenge:
//                    youngCount += gc.getCollectionCount();
//                    youngTime += gc.getCollectionTime();
//                    break;
//            }
    }
    }
    protected static String getKindName(String kind) {
        if("NON_HEAP".equals(kind)) {
            return "NON_HEAP(非堆内存)";
        }else {
            return "HEAP(堆内存)";
        }
    }

    protected static String getPoolName(String poolName) {
        switch (poolName) {
            case "Code Cache":
                return poolName +"(代码缓存区)";
            case "Metaspace":
                return poolName +"(元空间)";
            case "Compressed Class Space":
                return poolName +"(类指针压缩空间)";
            case "PS Eden Space":
                return poolName +"(伊甸园区)";
            case "PS Survivor Space":
                return poolName +"(幸存者区)";
            case "PS Old Gen":
                return poolName +"(老年代)";
            default:
                return poolName;
        }
    }


    protected static String bytesToMB(long bytes) {
        return fmtI.format((long)(bytes / 1024 / 1024)) + " MB";
    }

    protected static String printSizeInKb(double size) {
        return fmtI.format((long) (size / 1024)) + " kbytes";
    }

    protected static String toDuration(double uptime) {
        uptime /= 1000;
        if (uptime < 60) {
            return fmtD.format(uptime) + " seconds";
        }
        uptime /= 60;
        if (uptime < 60) {
            long minutes = (long) uptime;
            String s = fmtI.format(minutes) + (minutes > 1 ? " minutes" : " minute");
            return s;
        }
        uptime /= 60;
        if (uptime < 24) {
            long hours = (long) uptime;
            long minutes = (long) ((uptime - hours) * 60);
            String s = fmtI.format(hours) + (hours > 1 ? " hours" : " hour");
            if (minutes != 0) {
                s += " " + fmtI.format(minutes) + (minutes > 1 ? " minutes" : " minute");
            }
            return s;
        }
        uptime /= 24;
        long days = (long) uptime;
        long hours = (long) ((uptime - days) * 24);
        String s = fmtI.format(days) + (days > 1 ? " days" : " day");
        if (hours != 0) {
            s += " " + fmtI.format(hours) + (hours > 1 ? " hours" : " hour");
        }
        return s;
    }

    protected static String toDate(long timestampMillis){
        Date date = new Date(timestampMillis);
        return date.toString();
    }
}
