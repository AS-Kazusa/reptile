package kazusa;

import kazusa.service.app.pixiv.user;

import static kazusa.service.field.utils.ThreadPool.ThreadPoolConfig;
import static kazusa.service.field.utils.ThreadPool.threadPoolExecutor;

public class Main implements Runnable {

    public static void main(String[] args) throws Exception {
        new user().reptile();
        //new painter().reptile();
        //new painter_().reptile();
        //concurrency();
    }

    /**
     * 并发数
     */
    public static int concurrency = 3;

    public static void concurrency() {
        ThreadPoolConfig();
        for (int i = 0; i < concurrency; i++) {
            // 启动线程池使线程池创建线程执行任务
            threadPoolExecutor.execute(new Main());
        }
        // 关闭线程池
        threadPoolExecutor.shutdown();
    }

    @Override
    public void run() {
        //System.out.println(111);
    }
}
