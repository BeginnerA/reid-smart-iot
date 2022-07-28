package top.reid.iot.service.mqtt;

import top.reid.iot.entity.MqttResult;
import top.reid.iot.mqttv3.listener.AbstractUnSubscribeListener;

/**
 * <p>
 * 退订服务
 * </p>
 *
 * @Author REID
 * @Blog <a href="https://blog.csdn.net/qq_39035773">Blog</a>
 * @GitHub <a href="https://github.com/BeginnerA">GitHub</a>
 * @Data 2022/7/7
 * @Version V1.0
 **/
public interface UnsubscribeService {

    /***
     * 取消订阅-同步
     * @param topicFilter 过滤主题
     * @return 取消订阅结果
     */
    MqttResult unsubscribe(String topicFilter);

    /***
     * 批量取消订阅-同步
     * @param topicFilters 过滤主题列表
     * @return 取消订阅结果
     */
    MqttResult unsubscribeBatch(String[] topicFilters);

    /**
     * 取消订阅-异步
     * @param topicFilter 过滤主题
     */
    void unsubscribeAsync(String topicFilter);

    /**
     * 取消订阅-异步
     * @param topicFilter 过滤主题
     * @param listener 异步取消订阅消息监听器
     */
    void unsubscribeAsync(String topicFilter, AbstractUnSubscribeListener listener);

    /**
     * 取消订阅-异步
     * @param topicFilter 过滤主题
     * @param userContext 额外携带的信息，异步执行结束后会传至 listener
     * @param listener 异步取消订阅消息监听器
     */
    void unsubscribeAsync(String topicFilter, Object userContext, AbstractUnSubscribeListener listener);

    /**
     * 批量取消订阅-异步
     * @param topicFilters 过滤主题列表
     */
    void unsubscribeAsyncBatch(String[] topicFilters);

    /**
     * 批量取消订阅-异步
     * @param topicFilters 过滤主题列表
     * @param listener 异步取消订阅消息监听器
     */
    void unsubscribeAsyncBatch(String[] topicFilters, AbstractUnSubscribeListener listener);

    /**
     * 批量取消订阅-异步
     * @param topicFilters 过滤主题列表
     * @param userContext 额外携带的信息，异步执行结束后会传至 listener
     * @param listener 异步取消订阅消息监听器
     */
    void unsubscribeAsyncBatch(String[] topicFilters, Object userContext, AbstractUnSubscribeListener listener);
}
