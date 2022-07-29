package top.reid.smart.iot.entity;

import lombok.Data;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttException;

/**
 * <p>
 * MQTT 结果
 * </p>
 *
 * @Author REID
 * @Blog <a href="https://blog.csdn.net/qq_39035773">Blog</a>
 * @GitHub <a href="https://github.com/BeginnerA">GitHub</a>
 * @Data 2022/7/7
 * @Version V1.0
 **/
@Data
public class MqttResult {
    /**
     * 是否完成
     */
    private final Boolean isComplete;
    /**
     * MQTT 异常
     */
    private MqttException exception;

    public MqttResult (IMqttToken token) {
        this.isComplete = token.getMessageId() > 0;
    }

    public MqttResult(IMqttDeliveryToken token) {
        this.isComplete = token.getMessageId() > 0;
    }

    public MqttResult(MqttException mqttException) {
        this.isComplete = false;
        this.exception = mqttException;
    }

    public Boolean isComplete() {
        return isComplete;
    }
}
