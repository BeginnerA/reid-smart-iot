package top.reid.iot.service.mqtt.impl;

import cn.hutool.core.util.ObjectUtil;
import top.reid.iot.config.MqttConfig;
import top.reid.iot.entity.MqttResult;
import top.reid.iot.entity.Publish;
import top.reid.iot.mqttv3.listener.AbstractPublishListener;
import top.reid.iot.mqttv3.MqttV3Executor;
import top.reid.iot.service.mqtt.PublisherService;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttAsyncClient;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 推送服务
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
public class PublisherServiceImpl implements PublisherService {

    @Resource
    MqttConfig mqttConfig;
    @Resource
    private MqttV3Executor mqttV3Executor;

    @Override
    public MqttResult publish(Publish publish) {
        entryCheck(publish);
        MqttMessage mqttMessage = new MqttMessage(ObjectUtil.serialize(publish.getMessage()));
        mqttMessage.setQos(publish.getQos().getValue());
        mqttMessage.setRetained(publish.getRetain());
        try {
            return new MqttResult(mqttV3Executor.getMqttClient().publish(publish.getTopic(), mqttMessage,
                    null, defaultListener()));
        } catch (MqttException e) {
            return new MqttResult(e);
        }
    }

    @Override
    public int publishBatch(List<Publish> publishList) {
        int successNum = 0;
        for (Publish publish : publishList){
            if (publish(publish).isComplete()) {
                successNum ++;
            }
        }
        return successNum;
    }

    @Override
    public void publishAsync(Publish publish) {
        entryCheck(publish);
        MqttMessage mqttMessage = new MqttMessage(ObjectUtil.serialize(publish.getMessage()));
        mqttMessage.setQos(publish.getQos().getValue());
        mqttMessage.setRetained(publish.getRetain());
        if (publish.getCallbackAsync() == null) {
            publish.setCallbackAsync(defaultListener());
        }
        try {
            IMqttAsyncClient mqttAsyncClient = mqttV3Executor.getMqttAsyncClient();
            boolean flag = true;
            synchronized (this) {
                while(flag) {
                    if (mqttConfig.getConnectOptions().getMaxInflight() > mqttAsyncClient.getPendingDeliveryTokens().length) {
                        flag = false;
                        mqttV3Executor.getMqttAsyncClient().publish(publish.getTopic(), mqttMessage,
                                publish.getUserContextAsync(), publish.getCallbackAsync());
                    }
                }
            }
        } catch (MqttException e) {
            log.error("MQTT 异步发布消息异常：", e);
        }
    }

    @Override
    public void publishBatchAsync(List<Publish> publishList) {
        for (Publish publish : publishList){
            publishAsync(publish);
        }
    }

    /**
     * 入参检查
     * @param publish 推送消息体入参
     */
    private void entryCheck(Publish publish) {
        Objects.requireNonNull(publish.getTopic(), "推送主题不能为空");
        Objects.requireNonNull(publish.getMessage(), "推送的消息报文不能为空");
    }

    /**
     * 默认 MQTT 异步发布消息监听器
     * @return MQTT 异步发布消息监听器
     */
    private AbstractPublishListener defaultListener() {
        return new AbstractPublishListener() {
            @Override
            protected void success(IMqttToken mqttToken) {
                log.info("默认 MQTT 异步发布消息监听器：MQTT 异步发布消息成功");
            }
            @Override
            protected void fail(IMqttToken mqttToken, Throwable throwable) {
                log.info("默认 MQTT 异步发布消息监听器：MQTT 异步发布消息失败");
            }
        };
    }
}
