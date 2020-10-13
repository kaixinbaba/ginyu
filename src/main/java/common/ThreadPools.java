package common;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: junjiexun
 * @date: 2020/10/13 2:44 下午
 * @description:
 */
public abstract class ThreadPools {

    private static class GinyuThreadFactory implements ThreadFactory {

        private final AtomicInteger threadNumber = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            t.setName("ginyu-thread-" + threadNumber.getAndIncrement());
            return t;
        }
    }

    // TODO RejectedExecutionHandler
    public static final ExecutorService POOL = new ThreadPoolExecutor(
            10,
            10,
            0L,
            TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(10000),
            new GinyuThreadFactory());

}
