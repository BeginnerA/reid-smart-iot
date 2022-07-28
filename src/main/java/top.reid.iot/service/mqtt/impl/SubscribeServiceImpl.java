package top.reid.iot.service.mqtt.impl;

import top.reid.iot.entity.MqttResult;
import top.reid.iot.entity.Subscribe;
import top.reid.iot.mqttv3.listener.AbstractSubscribeListener;
import top.reid.iot.mqttv3.MqttV3Executor;
import top.reid.iot.service.mqtt.SubscribeService;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

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
@Slf4j
@Service
public class SubscribeServiceImpl implements SubscribeService {

    @Resource
    private MqttV3Executor mqttV3Executor;

    @Override
    public MqttResult subscribe(Subscribe subscribe) {
        entryCheck(subscribe);
        try {
            MqttResult mqttResult = new MqttResult(mqttV3Executor.getMqttClient().subscribe(subscribe.getTopic(),
                    subscribe.getQos().getValue(), null, defaultListener(),
                    subscribe.getMessageListener()));
            mqttV3Executor.addSubscribe(subscribe);
            return mqttResult;
        } catch (MqttException e) {
            return new MqttResult(e);
        }
    }

    @Override
    public MqttResult subscribeBatch(List<Subscribe> subscribeList) {
        String[] topics = new String[subscribeList.size()];
        int[] qos = new int[subscribeList.size()];
        IMqttMessageListener[] mqttMessageListeners = new IMqttMessageListener[subscribeList.size()];
        buildSubscribeBatch(subscribeList, topics, qos, mqttMessageListeners);
        try {
            MqttResult mqttResult = new MqttResult(mqttV3Executor.getMqttClient()
                    .subscribe(topics, qos, null, defaultListener(), mqttMessageListeners));
            mqttV3Executor.addSubscribe(subscribeList.toArray(new Subscribe[0]));
            return mqttResult;
        } catch (MqttException e) {
            return new MqttResult(e);
        }
    }

    @Override
    public void subscribeAsync(Subscribe subscribe) {
        subscribeAsync(subscribe, defaultListener());
    }

    @Override
    public void subscribeAsync(Subscribe subscribe, AbstractSubscribeListener listener) {
        entryCheck(subscribe);
        try {
            mqttV3Executor.getMqttAsyncClient().subscribe(subscribe.getTopic(), subscribe.getQos().getValue(),
                    subscribe.getUserContext(), listener, subscribe.getMessageListener());
            mqttV3Executor.addSubscribe(subscribe);
        } catch (MqttException e) {
            log.error("MQTT异步订阅消息异常：", e);
        }
    }

    @Override
    public void subscribeBatchAsync(List<Subscribe> subscribeList) {
        subscribeBatchAsync(subscribeList, null, defaultListener());
    }

    @Override
    public void subscribeBatchAsync(List<Subscribe> subscribeList, AbstractSubscribeListener listener) {
        subscribeBatchAsync(subscribeList, null, listener);
    }

    @Override
    public void subscribeBatchAsync(List<Subscribe> subscribeList, Object userContext, AbstractSubscribeListener listener) {
        String[] topics = new String[subscribeList.size()];
        int[] qos = new int[subscribeList.size()];
        IMqttMessageListener[] iMqttMessageListeners = new IMqttMessageListener[subscribeList.size()];
        buildSubscribeBatch(subscribeList, topics, qos, iMqttMessageListeners);
        try {
            mqttV3Executor.getMqttAsyncClient().subscribe(topics, qos, userContext, listener, iMqttMessageListeners);
            mqttV3Executor.addSubscribe(subscribeList.toArray(new Subscribe[0]));
        } catch (MqttException e) {
            log.error("MQTT 异步批量订阅消息异常：", e);
        }
    }

    /**
     * 入参检查
     * @param subscribe 推送消息体入参
     */
    private void entryCheck(Subscribe subscribe) {
        Objects.requireNonNull(subscribe.getTopic(), "订阅主题不能为空");
        Objects.requireNonNull(subscribe.getQos(), "Qos 不能为空");
    }

    /**
     * 默认 MQTT 异步订阅消息监听器
     * @return MQTT 异步订阅消息监听器
     */
    private AbstractSubscribeListener defaultListener() {
        return new AbstractSubscribeListener() {
            @Override
            protected void success(IMqttToken mqttToken) {
                log.info("默认 MQTT 异步订阅消息监听器：MQTT 异步订阅消息成功");
            }
            @Override
            protected void fail(IMqttToken mqttToken, Throwable throwable) {
                log.info("默认 MQTT 异步订阅消息监听器：MQTT 异步订阅消息失败");
            }
        };
    }

    /**
     * 建立订阅批次
     * @param subscribeList 订阅信息体列表
     * @param topics 主题列表
     * @param qos Qos
     * @param mqttMessageListeners mqtt 消息监听器列表
     */
    private void buildSubscribeBatch(List<Subscribe> subscribeList, String[] topics, int[] qos,
                                     IMqttMessageListener[] mqttMessageListeners) {
        for (int i =0; i < subscribeList.size(); i++){
            entryCheck(subscribeList.get(i));
            topics[i] = subscribeList.get(i).getTopic();
            qos[i] = subscribeList.get(i).getQos().getValue();
            mqttMessageListeners[i] = subscribeList.get(i).getMessageListener();
        }
    }
}
