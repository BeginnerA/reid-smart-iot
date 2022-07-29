package top.reid.smart.iot.mqttv3;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import top.reid.smart.iot.config.MqttConfig;
import top.reid.smart.iot.service.device.DeviceDataService;
import top.reid.smart.iot.service.mqtt.SubscribeService;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * <p>
 * MQTT 链接完成后需要执行的内容
 * TODO 没想到什么优雅的方式将此类交给 spring 托管
 * </p>
 *
 * @Author REID
 * @Blog <a href="https://blog.csdn.net/qq_39035773">Blog</a>
 * @GitHub <a href="https://github.com/BeginnerA">GitHub</a>
 * @Data 2022/7/11
 * @Version V1.0
 **/
@Slf4j
public class MqttLinkComplete {
    MqttConfig mqttConfig;
    SubscribeService subscribeService;
    DeviceDataService deviceDataService;

    public MqttLinkComplete() {
        mqttConfig = SpringUtil.getBean(MqttConfig.class);
        subscribeService = SpringUtil.getBean(SubscribeService.class);
        deviceDataService = SpringUtil.getBean(DeviceDataService.class);
    }

    /**
     * 链接完成后会被调用
     */
    public void append() {
        // 默认订阅主题
        if (CollUtil.isNotEmpty(mqttConfig.getSubscribeTopics())) {
            subscribeService.subscribeBatch(mqttConfig.getSubscribeTopics());
        }
    }

    /**
     * 客户端收到消息后会被调用
     * @param topic 主题
     * @param mqttMessage 消息
     */
    public void persistenceData(String topic, MqttMessage mqttMessage) {
        // 是否开启消息持久
        if (mqttConfig.getMsg() != null && mqttConfig.getMsg().isMsgPersistence()) {
            String message = new String(mqttMessage.getPayload());
            String tableName = mqttConfig.getSubscribe(topic).getTableName();
            if (StrUtil.isEmpty(tableName)) {
                tableName = StrUtil.toUnderlineCase(topic.substring(topic.lastIndexOf('/') + 1));
                String tableNamePrefix = mqttConfig.getMsg().getTableNamePrefix();
                String tableNameSuffix = mqttConfig.getMsg().getTableNameSuffix();
                if (StrUtil.isNotEmpty(tableNamePrefix)) {
                    tableName = tableNamePrefix + tableName;
                }
                if (StrUtil.isNotEmpty(tableNameSuffix)) {
                    tableName = tableName + tableNameSuffix;
                }
            }

            deviceDataService.persistenceData(tableName, JSONUtil.parseObj(message));
        }
    }
}
