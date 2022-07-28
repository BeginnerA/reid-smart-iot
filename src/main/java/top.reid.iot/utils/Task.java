package top.reid.iot.utils;

/**
 * <p>
 * 重试机制任务
 * </p>
 *
 * @Author REID
 * @Blog <a href="https://blog.csdn.net/qq_39035773">Blog</a>
 * @GitHub <a href="https://github.com/BeginnerA">GitHub</a>
 * @Data 2022/7/12
 * @Version V1.0
 **/
public interface Task {

    /**
     * 执行
     * @throws Exception 执行异常
     */
    void run() throws Exception;

    /**
     * 重试失败
     * @param e Throwable 类是 Java 语言中所有错误和异常的超类
     */
    default void retryFailed(Throwable e) {
    }

    /**
     * 业务快照
     * @return 业务快照
     */
    default Object snapshot() {
        return null;
    }
}
