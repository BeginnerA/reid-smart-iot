package top.reid.smart.iot.entity;

import lombok.Getter;

/**
 * <p>
 * MQTT 客户端
 * </p>
 *
 * @Author REID
 * @Blog <a href="https://blog.csdn.net/qq_39035773">Blog</a>
 * @GitHub <a href="https://github.com/BeginnerA">GitHub</a>
 * @Data 2022/7/8
 * @Version V1.0
 **/
@Getter
public class MqttV3Client {
    /**
     * 客户端编号
     */
    private final String clientId;

    public MqttV3Client(String clientId) {
        this.clientId = clientId;
    }
}
