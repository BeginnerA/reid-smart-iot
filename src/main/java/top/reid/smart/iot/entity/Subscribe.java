package top.reid.smart.iot.entity;

import cn.hutool.core.util.ReflectUtil;
import top.reid.smart.iot.mqttv3.listener.AbstractMessageListener;
import lombok.Data;

/**
 * <p>
 * 订阅信息体
 * </p>
 *
 * @Author REID
 * @Blog <a href="https://blog.csdn.net/qq_39035773">Blog</a>
 * @GitHub <a href="https://github.com/BeginnerA">GitHub</a>
 * @Data 2022/7/7
 * @Version V1.0
 **/
@Data
public class Subscribe {
    /**
     * Qos 0/1/2
     */
    private QosType qos = QosType.QOS_JUST;
    /**
     * 订阅主题
     */
    private String topic;
    /**
     * 订阅主题对应持久化数据库表名，默认会自动根据消息创建持久化表。
     * 如果订阅主题采用默认持久化引擎，最好配置该属性，
     * 否则表名将从订阅主题中截取最后一个”/“后的字符串做为表名<br>
     * 注意：主题第一次持久化时如果数据库中存在同名不同字段的表时，消息会持久化失败。
     */
    private String tableName;
    /**
     * 消息监听器中收到的上下文对象。<br>
     * 用于将上下文传递给回调的可选对象。如果不需要，可以为 null
     */
    private Object userContext;
    /**
     * MQTT 客户端消息监听器，在此处编写消息处理逻辑
     */
    private AbstractMessageListener messageListener;

    /**
     * 设置自定义消息 MQTT 客户端消息监听器。<br>
     * 配置示例：<br>
     * <blockquote><pre>
     * # spring 配置
     * mqtt:
     *   subscribe-topics:
     *     - topic: testtopic/test
     *       qos: 2
     *       message-listener: com.module.Test.MyAbstractMessageListener
     * </pre></blockquote>
     * 注意：自定义消息监听器是需要继承 {@link AbstractMessageListener} 类并实现对应方法。
     * @param messageListener 自定义消息 MQTT 客户端消息监听器 Class
     */
    public void setMessageListener(Class<? extends AbstractMessageListener> messageListener) {
        this.messageListener = ReflectUtil.newInstance(messageListener);
    }

    /**
     * 设置自定义消息 MQTT 客户端消息监听器。<br>
     * 配置示例：<br>
     * <blockquote><pre>
     * Subscribe subscribe = new Subscribe();
     * subscribe.setQos(QosType.QOS_JUST);
     * subscribe.setTopic("a/b/cc");
     * subscribe.setMyMessageListener(new AbstractMessageListener() {
     *      protected void msgArrived(String topic, MqttMessage mqttMessage) {
     *          System.out.println("客户端接收到消息");
     *      }
     *  });
     * </pre></blockquote>
     * 注意：自定义消息监听器是需要继承 {@link AbstractMessageListener} 类并实现对应方法。
     * @param messageListener 自定义消息 MQTT 客户端消息监听器 Class
     */
    public void setMyMessageListener(AbstractMessageListener messageListener) {
        this.messageListener = messageListener;
    }
}
