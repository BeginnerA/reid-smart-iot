package top.reid.iot.mqttv3.listener;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;

/**
 * <p>
 * MQTT 异步发布消息监听器。<br>
 * 当异步操作完成时，将通知此接口的实现者。<br>
 * 侦听器在 MqttToken 上注册，并且令牌与连接或发布等操作相关联。
 * 当与 MqttAsyncClient 上的令牌一起使用时，侦听器将在 MQTT 客户端的线程上被回调。
 * 如果操作成功或失败，将通知侦听器。监听器快速返回控制权很重要，否则 MQTT 客户端的操作将停止。
 * </p>
 *
 * @Author REID
 * @Blog <a href="https://blog.csdn.net/qq_39035773">Blog</a>
 * @GitHub <a href="https://github.com/BeginnerA">GitHub</a>
 * @Data 2022/7/7
 * @Version V1.0
 **/
@Slf4j
public abstract class AbstractPublishListener extends MqttListener implements IMqttActionListener {
    @Override
    public void onSuccess(IMqttToken mqttToken) {
        log.info("---------------------------------MQTT 异步发布消息成功---------------------------------");
        this.info(mqttToken, null);
        log.info("-------------------------------------------------------------------------------------");
        success(mqttToken);
    }

    @Override
    public void onFailure(IMqttToken mqttToken, Throwable throwable) {
        log.info("--------------------------------MQTT 异步发布消息失败----------------------------------");
        this.info(mqttToken, throwable);
        log.info("-------------------------------------------------------------------------------------");
        fail(mqttToken, throwable);
    }

    /**
     * 当操作成功完成时调用此方法
     * @param mqttToken 异步操作令牌与已完成的操作相关联
     */
    protected abstract void success(IMqttToken mqttToken);

    /**
     * 当操作失败时调用此方法。如果客户端在操作正在进行时断开连接，则将调用 onFailure。
     * 对于使用 cleanSession 设置为 false 的连接，正在传递的任何 QoS 1 和 2 消息都将在客户端下次连接时传递到请求的服务质量
     * @param mqttToken 异步操作令牌与已完成的操作相关联
     * @param throwable 由失败的操作引发
     */
    protected abstract void fail(IMqttToken mqttToken, Throwable throwable);
}
