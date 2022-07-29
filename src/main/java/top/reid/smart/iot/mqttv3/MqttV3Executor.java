package top.reid.smart.iot.mqttv3;

import top.reid.smart.iot.config.MqttConfig;
import top.reid.smart.iot.entity.MqttV3Client;
import top.reid.smart.iot.entity.Subscribe;
import top.reid.smart.iot.mqttv3.listener.ConnectListener;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttAsyncClient;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * <p>
 * MQTT 执行器
 * </p>
 *
 * @Author REID
 * @Blog <a href="https://blog.csdn.net/qq_39035773">Blog</a>
 * @GitHub <a href="https://github.com/BeginnerA">GitHub</a>
 * @Data 2022/7/7
 * @Version V1.0
 **/
@Slf4j
@Component
public class MqttV3Executor implements ApplicationListener<ApplicationReadyEvent> {
    @Resource
    MqttConfig mqttConfig;
    @Resource
    private MqttV3Client mqttV3Client;
    private IMqttAsyncClient mqttAsyncClient;
    private IMqttToken connectToken;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        try {
            connect();
        } catch (MqttException e) {
            log.error("MQTT连接异常：", e);
        }
    }

    /**
     * 连接
     * @return MQTT 令牌
     * @throws MqttException 连接异常
     */
    private IMqttToken connect() throws MqttException {
        if(connectToken != null){
            return connectToken;
        }
        mqttAsyncClient = new MqttAsyncClient(mqttConfig.getHost(), mqttV3Client.getClientId());
        mqttAsyncClient.setCallback(new ConnectListener(mqttConfig.getReconnectionSeconds(),
                mqttConfig.getMaxRetryTimes(), mqttAsyncClient));
        connectToken = mqttAsyncClient.connect(mqttConfig.getConnectOptions());
        return connectToken;
    }

    /**
     * 获取 MQTT 异步客户端
     * @return MQTT 异步客户端
     * @throws MqttException 连接异常
     */
    public IMqttAsyncClient getMqttAsyncClient() throws MqttException {
        return connect().getClient();
    }

    /**
     * MQTT 客户端
     * @return MQTT 客户端
     * @throws MqttException 连接异常
     */
    public IMqttAsyncClient getMqttClient() throws MqttException {
        IMqttToken mqttToken = connect();
        mqttToken.waitForCompletion();
        return mqttToken.getClient();
    }

    /**
     * 断开
     */
    public void disconnect() {
        try {
            if (mqttAsyncClient != null) {
                mqttAsyncClient.disconnect();
            }
        } catch (MqttException e) {
            log.error("MqttV3Executor 断开 MqttException 错误：",e);
        }
    }

    /**
     * 动态订阅的主题缓存到配置文件中
     * @param subscribe 订阅主题对象
     */
    public void addSubscribe(Subscribe... subscribe) {
        mqttConfig.setSubscribe(subscribe);
    }

    /**
     * 动态取消订阅后从本地配置文件中移除对应主题
     * @param topic 订阅主题
     */
    public void removeSubscribe(String... topic) {
        mqttConfig.removeSubscribe(topic);
    }

}
