package top.reid.smart.iot.service.mqtt;

import top.reid.smart.iot.entity.MqttResult;
import top.reid.smart.iot.entity.Subscribe;
import top.reid.smart.iot.mqttv3.listener.AbstractSubscribeListener;

import java.util.List;

/**
 * <p>
 * 订阅服务
 * </p>
 *
 * @Author REID
 * @Blog <a href="https://blog.csdn.net/qq_39035773">Blog</a>
 * @GitHub <a href="https://github.com/BeginnerA">GitHub</a>
 * @Data 2022/7/7
 * @Version V1.0
 **/
public interface SubscribeService {
    /**
     * 订阅消息-同步
     * @param subscribe 订阅信息
     * @return 订阅结果
     */
    MqttResult subscribe(Subscribe subscribe);

    /***
     * 批量订阅-同步
     * @param subscribeList 订阅信息列表
     * @return 订阅结果
     */
    MqttResult subscribeBatch(List<Subscribe> subscribeList);

    /**
     * 订阅消息-异步
     * @param subscribe 订阅信息
     */
    void subscribeAsync(Subscribe subscribe);

    /**
     * 订阅消息-异步
     * @param subscribe 订阅信息
     * @param listener 异步订阅消息监听器
     */
    void subscribeAsync(Subscribe subscribe, AbstractSubscribeListener listener);

    /**
     * 批量订阅-异步
     * @param subscribeList 订阅列表
     */
    void subscribeBatchAsync(List<Subscribe> subscribeList);

    /**
     * 批量订阅-异步
     * @param subscribeList 订阅列表
     * @param listener 异步订阅消息监听器
     */
    void subscribeBatchAsync(List<Subscribe> subscribeList, AbstractSubscribeListener listener);

    /**
     * 批量订阅-异步
     * @param subscribeList 订阅列表
     * @param userContext 额外携带的信息，异步执行结束后会传至 listener
     * @param listener 异步订阅消息监听器
     */
    void subscribeBatchAsync(List<Subscribe> subscribeList, Object userContext, AbstractSubscribeListener listener);
}
