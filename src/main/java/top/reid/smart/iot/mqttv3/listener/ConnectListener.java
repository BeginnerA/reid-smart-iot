package top.reid.smart.iot.mqttv3.listener;

import cn.hutool.json.JSONUtil;
import top.reid.smart.iot.mqttv3.MqttLinkComplete;
import top.reid.smart.iot.utils.RetryTools;
import top.reid.smart.iot.utils.Task;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;

/**
 * <p>
 * 自定义连监听器，不使用异步客户端 MqttAsyncClient 自带的连接监听器
 * </p>
 *
 * @Author REID
 * @Blog <a href="https://blog.csdn.net/qq_39035773">Blog</a>
 * @GitHub <a href="https://github.com/BeginnerA">GitHub</a>
 * @Data 2022/7/8
 * @Version V1.0
 **/
@Slf4j
public class ConnectListener implements MqttCallbackExtended {
    private final IMqttAsyncClient mqttClient;
    /**
     * 多少秒重新连接
     */
    private final int reconnectionSeconds;
    /**
     * 最大重试次数
     */
    private final int maxRetryTimes;
    /**
     * 锁
     */
    private static final Object LOCK = new Object();

    public ConnectListener(int reconnectionSeconds, int maxRetryTimes, IMqttAsyncClient mqttClient) {
        this.reconnectionSeconds = reconnectionSeconds;
        this.maxRetryTimes = maxRetryTimes;
        this.mqttClient = mqttClient;
    }

    @Override
    public void connectComplete(boolean reconnect, String serverUrl) {
        log.info("==============================================================");
        log.info("MQTT 连接成功，客户端 ID：{}，服务器 URI：{}", mqttClient.getClientId(), serverUrl);
        log.info("==============================================================");
        new MqttLinkComplete().append();
    }

    @Override
    public void connectionLost(Throwable throwable) {
        log.error("MQTT 连接断开，开始尝试重连", throwable);
        synchronized (LOCK) {
            new RetryTools().doRetry(maxRetryTimes, new int[]{reconnectionSeconds}, new Task() {
                @Override
                public void run() throws Exception {
                    try {
                        if (!mqttClient.isConnected()) {
                            log.info("MQTT 开始重连，客户端 ID：{}，每隔{}秒重连一次",
                                    mqttClient.getClientId(), reconnectionSeconds);
                            mqttClient.reconnect();
                        }
                    } catch (MqttException e) {
                        // 已连接
                        if (e.getReasonCode() == MqttException.REASON_CODE_CLIENT_CONNECTED) {
                            log.info("MQTT 已连接，客户端 ID：{}，不需要再重新连接",
                                    mqttClient.getClientId());
                            return;
                        }
                        // 连接已关闭
                        if (e.getReasonCode() == MqttException.REASON_CODE_CLIENT_CLOSED) {
                            log.info("MQTT 连接已关闭，客户端 ID：{}，不需要再重新连接",
                                    mqttClient.getClientId());
                            return;
                        }
                        // 正在连接
                        if (e.getReasonCode() == MqttException.REASON_CODE_CONNECT_IN_PROGRESS) {
                            log.info("MQTT 正在连接，客户端 ID：{}，不需要再重新连接",
                                    mqttClient.getClientId());
                            return;
                        }
                        // 正在断开连接
                        if (e.getReasonCode() == MqttException.REASON_CODE_CLIENT_DISCONNECTING) {
                            log.info("MQTT 正在断开连接，客户端 ID：{}，不需要再重新连接",
                                    mqttClient.getClientId());
                            return;
                        }
                        log.error("MQTT 重连异常，客户端 ID：{}，", mqttClient.getClientId(), e);
                        throw e;
                    }
                }
            });
        }
    }

    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
        log.info("MQTT 消息已到达。客户端ID：{}，消息 ID：{}，消息内容：{}，topic：{}", mqttClient.getClientId(),
                mqttMessage.getId(), new String(mqttMessage.getPayload()), topic);
        new MqttLinkComplete().persistenceData(topic, mqttMessage);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        try {
            log.info("MQTT 消息传递完成。" +
                            "消息内容（可能为空，QOS 为0时非 NULL，为1、2时为 NULL）：{}，" +
                            "topics：{}，" +
                            "上下文对象：{}，" +
                            "回调类：{}",
                    token.getMessage() != null ? new String(token.getMessage().getPayload()) : "NULL",
                    JSONUtil.toJsonStr(token.getTopics()),
                    token.getUserContext() != null ? token.getUserContext().toString() : "NULL",
                    token.getActionCallback() != null ? token.getActionCallback().getClass().getName() : "NULL");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
