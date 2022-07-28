package top.reid.iot.mqttv3.listener;

import top.reid.iot.mqttv3.MqttLinkComplete;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * <p>
 * MQTT 客户端消息监听器
 * </p>
 *
 * @Author REID
 * @Blog <a href="https://blog.csdn.net/qq_39035773">Blog</a>
 * @GitHub <a href="https://github.com/BeginnerA">GitHub</a>
 * @Data 2022/7/7
 * @Version V1.0
 **/
@Slf4j
public abstract class AbstractMessageListener implements IMqttMessageListener {
    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
        log.info("---------------------------------MQTT 客户端接收到消息---------------------------------");
        log.info("消息内容：{}", new String(mqttMessage.getPayload()));
        log.info("消息质量 qos：{}", mqttMessage.getQos());
        log.info("-------------------------------------------------------------------------------------");
        msgArrived(topic, mqttMessage);
        new MqttLinkComplete().persistenceData(topic, mqttMessage);
    }

    /**
     * 当消息从服务器到达时调用此方法。<br>
     * 该方法由 MQTT 客户端同步调用。在此方法信息返回之前，不会将确认发送回服务器。<br>
     * 如果此方法的实现抛出 Exception ，则客户端将被关闭。当客户端下一次重新连接时，
     * 任何 QoS 1 或 2 消息都将由服务器重新传递。<br>
     * 在此方法的实现运行时到达的任何附加消息将在内存中建立，然后将备份到网络上。<br>
     * 如果应用需要持久化数据，那么在从这个方法返回之前，应该确保数据是持久化的，
     * 因为从这个方法返回之后，消息被认为已经传递，并且不会被重现。<br>
     * 可以在此回调的实现中发送一条新消息（例如，对此消息的响应），但该实现不能断开客户端，
     * 因为不可能为正在处理的消息发送确认，并且会发生死锁。<br>
     * @param topic 消息上的主题名称
     * @param mqttMessage 实际消息
     */
    protected abstract void msgArrived(String topic, MqttMessage mqttMessage);
}
