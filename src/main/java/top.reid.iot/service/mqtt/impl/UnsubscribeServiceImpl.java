package top.reid.iot.service.mqtt.impl;

import top.reid.iot.entity.MqttResult;
import top.reid.iot.mqttv3.listener.AbstractUnSubscribeListener;
import top.reid.iot.mqttv3.MqttV3Executor;
import top.reid.iot.service.mqtt.UnsubscribeService;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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
@Slf4j
@Service
public class UnsubscribeServiceImpl implements UnsubscribeService {

    @Resource
    private MqttV3Executor mqttV3Executor;

    @Override
    public MqttResult unsubscribe(String topicFilter) {
        try {
            MqttResult mqttResult = new MqttResult(mqttV3Executor.getMqttClient().unsubscribe(topicFilter,
                    null, defaultListener()));
            mqttV3Executor.removeSubscribe(topicFilter);
            return mqttResult;
        } catch (MqttException e) {
            return new MqttResult(e);
        }
    }

    @Override
    public MqttResult unsubscribeBatch(String[] topicFilters) {
        try {
            MqttResult mqttResult = new MqttResult(mqttV3Executor.getMqttClient().unsubscribe(topicFilters,
                    null, defaultListener()));
            mqttV3Executor.removeSubscribe(topicFilters);
            return mqttResult;
        } catch (MqttException e) {
            return new MqttResult(e);
        }
    }

    @Override
    public void unsubscribeAsync(String topicFilter) {
        unsubscribeAsync(topicFilter, null, defaultListener());
    }

    @Override
    public void unsubscribeAsync(String topicFilter, AbstractUnSubscribeListener listener) {
        unsubscribeAsync(topicFilter, null, listener);
    }

    @Override
    public void unsubscribeAsync(String topicFilter, Object userContext, AbstractUnSubscribeListener listener) {
        try {
            mqttV3Executor.getMqttAsyncClient().unsubscribe(topicFilter, userContext, listener);
            mqttV3Executor.removeSubscribe(topicFilter);
        } catch (MqttException e) {
            log.error("MQTT异步取消订阅异常：", e);
        }
    }

    @Override
    public void unsubscribeAsyncBatch(String[] topicFilters) {
        unsubscribeAsyncBatch(topicFilters, null, defaultListener());
    }

    @Override
    public void unsubscribeAsyncBatch(String[] topicFilters, AbstractUnSubscribeListener listener) {
        unsubscribeAsyncBatch(topicFilters, null, listener);
    }

    @Override
    public void unsubscribeAsyncBatch(String[] topicFilters, Object userContext, AbstractUnSubscribeListener listener) {
        try {
            mqttV3Executor.getMqttAsyncClient().unsubscribe(topicFilters, userContext, listener);
            mqttV3Executor.removeSubscribe(topicFilters);
        } catch (MqttException e) {
            log.error("MQTT 异步批量取消订阅异常：", e);
        }
    }

    private AbstractUnSubscribeListener defaultListener() {
        return new AbstractUnSubscribeListener() {
            @Override
            protected void success(IMqttToken mqttToken) {
                log.info("默认 MQTT 异步取消订阅消息监听器：MQTT 异步取消订阅消息成功");
            }

            @Override
            protected void fail(IMqttToken mqttToken, Throwable throwable) {
                log.info("默认 MQTT 异步取消订阅消息监听器：MQTT 异步取消订阅消息失败");
            }
        };
    }
}
