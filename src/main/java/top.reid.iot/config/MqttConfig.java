package top.reid.iot.config;

import top.reid.iot.entity.Subscribe;
import lombok.Data;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * MQTT 配置
 * </p>
 *
 * @Author REID
 * @Blog <a href="https://blog.csdn.net/qq_39035773">Blog</a>
 * @GitHub <a href="https://github.com/BeginnerA">GitHub</a>
 * @Data 2022/7/7
 * @Version V1.0
 **/
@Data
@Configuration
@ConfigurationProperties(prefix = "reid.mqtt")
public class MqttConfig {
    /**
     * 客户端自定义 ID<br>
     * 如果没有配置，默认 MQTT 客户端 ID 为 ip:port
     */
    private String clientId;
    /***
     * MQTT 服务地址
     **/
    private String host;
    /**
     * 重连间隔，单位秒
     */
    private int reconnectionSeconds = 1;
    /**
     * 最大重试次数
     */
    private int maxRetryTimes = 99999;
    /**
     * 默认订阅主题列表
     */
    protected List<Subscribe> subscribeTopics = new ArrayList<>();
    /**
     * MQTT 链接配置
     */
    private ConnectOptions options = new ConnectOptions();
    /**
     * 消息持久化配置
     */
    private MessagePersistenceConfig msg = new MessagePersistenceConfig();

    /**
     * 获取连接选项，包含控制客户端如何连接到服务器的选项集
     * @return 连接选项
     */
    public MqttConnectOptions getConnectOptions(){
        return options.getConnectOptions();
    }

    /**
     * 设置订阅主题
     * @param subscribes 订阅主题
     */
    public void setSubscribe(Subscribe... subscribes) {
        for (Subscribe subscribe : subscribes) {
            if (getSubscribe(subscribe.getTopic()) != null) {
                removeSubscribe(subscribe.getTopic());
            }
            subscribeTopics.add(subscribe);
        }
    }

    /**
     * 从主题列表中获取指定订阅主题消息
     * @return 订阅主题消息
     */
    public Subscribe getSubscribe(String topic) {
        Map<String, List<Subscribe>> topicMap = subscribeTopics.stream()
                .collect(Collectors.groupingBy(Subscribe::getTopic));
        return topicMap.containsKey(topic) ? topicMap.get(topic).get(0) : null;
    }

    /**
     * 移除对应订阅主题
     * @param topic 需要移除的订阅主题
     */
    public void removeSubscribe(String... topic) {
        subscribeTopics.removeIf(subscribe -> Arrays.asList(topic).contains(subscribe.getTopic()));
    }
}
