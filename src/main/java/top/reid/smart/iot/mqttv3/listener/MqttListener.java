package top.reid.smart.iot.mqttv3.listener;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttToken;

/**
 * <p>
 * Mqtt 日志信息
 * </p>
 *
 * @Author REID
 * @Blog <a href="https://blog.csdn.net/qq_39035773">Blog</a>
 * @GitHub <a href="https://github.com/BeginnerA">GitHub</a>
 * @Data 2022/7/8
 * @Version V1.0
 **/
@Slf4j
public class MqttListener {

    /**
     * 输出特定日志信息，方便开发人员查阅
     * @param mqttToken MQTT 令牌
     */
    public void info(IMqttToken mqttToken, Throwable throwable) {
        log.info("客户端 ID：{}", mqttToken.getClient().getClientId());
        log.info("主题：{}", JSONUtil.toJsonStr(mqttToken.getTopics()));
        log.info("上下文对象：{}", mqttToken.getUserContext() != null ? mqttToken.getUserContext().toString() : "NULL");
        log.info("消息监听器类：{}",
                mqttToken.getActionCallback() != null ? mqttToken.getActionCallback().getClass().getName() : "NULL");
        if (throwable != null) {
            log.error("失败原因：", throwable);
        }
    }
}
