package top.reid.iot.utils;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * <p>
 * 重试机制
 * </p>
 *
 * @Author REID
 * @Blog <a href="https://blog.csdn.net/qq_39035773">Blog</a>
 * @GitHub <a href="https://github.com/BeginnerA">GitHub</a>
 * @Data 2022/7/12
 * @Version V1.0
 **/
@Slf4j
public class RetryTools {
    /**
     * 默认次数
     */
    private final static int DEFAULT_RETRY_TIMES = 6;
    /**
     * 默认间隔
     */
    private final static int[] DEFAULT_DELAY_SECONDS = {3, 30, 180, 600, 1800, 3600};
    /**
     * 任务队列
     */
    private static final Queue<RetryRunnable> TASK_QUEUE = new ConcurrentLinkedQueue<>();
    /**
     * 执行线程池
     */
    public static ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    /**
     * 调度器
     */
    public static ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();

    static {
        // 配置核心线程数
        executor.setCorePoolSize(10);
        // 配置最大线程数
        executor.setMaxPoolSize(20);
        // 配置队列大小
        executor.setQueueCapacity(1000);
        // 配置线程池中的线程的名称前缀
        executor.setThreadNamePrefix("async_");
        // rejection-policy：当 pool 已经达到 max size 的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 执行初始化
        executor.initialize();

        scheduler.setThreadNamePrefix("scheduler_");
        scheduler.setPoolSize(5);
        scheduler.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        scheduler.initialize();
    }

    public RetryTools() {
        // 每秒检查一次：遍历任务队列，如需执行，线程池调度执行
        scheduler.scheduleAtFixedRate(() -> {
            for (RetryRunnable task : TASK_QUEUE) {
                long nextRetryMillis = task.nextRetryMillis;
                if (nextRetryMillis != -1 && nextRetryMillis <= System.currentTimeMillis()) {
                    task.nextRetryMillis = -1;
                    executor.execute(task);
                }
            }
        }, 1000);
    }

    /**
     * 重试
     * @param task 任务
     */
    public void doRetry(Task task) {
        doRetry(DEFAULT_RETRY_TIMES, DEFAULT_DELAY_SECONDS, task);
    }

    /**
     * 重试
     * @param maxRetryTime 最大重试次数
     * @param task 任务
     */
    public void doRetry(int maxRetryTime, Task task) {
        doRetry(maxRetryTime, DEFAULT_DELAY_SECONDS, task);
    }

    /**
     * 重试
     * @param retryDelaySeconds 重试间隔时间数组
     * @param task 任务
     */
    public void doRetry(int[] retryDelaySeconds, Task task) {
        doRetry(retryDelaySeconds.length, retryDelaySeconds, task);
    }

    /**
     * 重试
     * @param maxRetryTime 最大重试次数
     * @param retryDelaySeconds 重试间隔时间数组
     * @param task 任务
     */
    public void doRetry(final int maxRetryTime, final int[] retryDelaySeconds, final Task task) {
        Runnable runnable = new RetryRunnable(maxRetryTime, retryDelaySeconds, task);
        executor.execute(runnable);
    }

    /**
     * 自定义线程类
     */
    private static class RetryRunnable implements Runnable {
        /**
         * 任务
         */
        private final Task task;
        /**
         * 最大重试次数
         */
        private final int maxRetryTimes;
        /**
         * 重试间隔时间数组
         */
        private final int[] retryDelaySeconds;
        /**
         * 重试次数
         */
        private int retryTimes;
        /**
         * 下一个重试时间（ms）
         */
        private volatile long nextRetryMillis;

        /**
         * 构造函数
         * @param maxRetryTimes 最大重试次数
         * @param retryDelaySeconds 重试间隔时间数组
         * @param task 重试任务
         */
        public RetryRunnable(final int maxRetryTimes, final int[] retryDelaySeconds, final Task task) {
            this.task = task;
            if (maxRetryTimes <= 0) {
                this.maxRetryTimes = DEFAULT_RETRY_TIMES;
            } else {
                this.maxRetryTimes = maxRetryTimes;
            }
            if (retryDelaySeconds == null || retryDelaySeconds.length == 0) {
                this.retryDelaySeconds = DEFAULT_DELAY_SECONDS;
            } else {
                this.retryDelaySeconds = retryDelaySeconds;
            }
        }

        @Override
        public void run() {
            try {
                task.run();
            } catch (Throwable e) {
                int sleepSeconds = retryTimes < retryDelaySeconds.length ?
                        retryDelaySeconds[retryTimes] : retryDelaySeconds[retryDelaySeconds.length - 1];

                if (retryTimes < maxRetryTimes) {
                    if (retryTimes == 0) {
                        TASK_QUEUE.add(this);
                        log.error("任务执行错误, " + sleepSeconds + " 秒后尝试重试... ", e);
                    } else {
                        log.error("重试 " + retryTimes + " 次错误, " + sleepSeconds + " 秒后尝试重试... ", e);
                    }
                    nextRetryMillis = System.currentTimeMillis() + sleepSeconds * 1000L;
                } else {
                    log.error("重试 " + retryTimes + " times error", e);
                    log.error("重试任务快照: {}", JSONUtil.toJsonStr(task.snapshot()));

                    TASK_QUEUE.remove(this);
                    task.retryFailed(e);

                }
                retryTimes++;
            }
        }
    }
}
