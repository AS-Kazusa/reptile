package kazusa.service.field.utils;

import java.util.concurrent.*;

public class ThreadPool {

    /**
     * 默认拒绝策略:直接抛出异常阻止程序正常运行
     */
    private static ThreadPoolExecutor.AbortPolicy abortPolicy = new ThreadPoolExecutor.AbortPolicy();

    public static void ThreadPoolConfig() {
        threadPoolExecutor = CustomThreadPool
                (
                        // 配置核心线程数
                        5,
                        // 配置最大线程数
                        10,
                        // 配置空闲线程存活时间
                        2L,
                        // 配置空闲线程存活时间单位
                        TimeUnit.SECONDS,
                        // 配置线程池阻塞队列
                        new ArrayBlockingQueue<>(5),
                        // 配置默认线程工厂类
                        Executors.defaultThreadFactory(),
                        // 配置拒绝策略
                        abortPolicy
                );
    }

    /**
     * 私有化构造器防止对象调用
     */
    private ThreadPool() {}

    /**
     * 调用配置方法实例化自定义线程池对象,使用自定义线程池对象操作
     */
    public static ThreadPoolExecutor threadPoolExecutor;

    /**
     * @param corePoolSize 配置核心线程数
     * @param maximumPoolSize 配置最大线程数
     * @param keepAliveTime 配置空闲线程存活时间
     * @param unit 配置空闲线程存活时间单位
     * @param workQueue 配置线程池阻塞队列
     * @param threadFactory 配置线程工厂类
     * @param handler 配置拒绝策略
     * @return 返回自定义线程池对象
     */
    public static ThreadPoolExecutor CustomThreadPool
    (
            int corePoolSize,
            int maximumPoolSize,
            long keepAliveTime,
            TimeUnit unit,
            BlockingQueue<Runnable> workQueue,
            ThreadFactory threadFactory,
            RejectedExecutionHandler handler
    )
    {
        return new ThreadPoolExecutor
                (
                        corePoolSize,
                        maximumPoolSize,
                        keepAliveTime,
                        unit,
                        workQueue,
                        threadFactory,
                        handler
                );
    }
}
