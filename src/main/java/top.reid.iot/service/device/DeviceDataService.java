package top.reid.iot.service.device;

import cn.hutool.json.JSONObject;

/**
 * <p>
 * 设备数据服务
 * </p>
 *
 * @Author REID
 * @Blog <a href="https://blog.csdn.net/qq_39035773">Blog</a>
 * @GitHub <a href="https://github.com/BeginnerA">GitHub</a>
 * @Data 2022/7/11
 * @Version V1.0
 **/
public interface DeviceDataService {
    /**
     * <p>
     * 设备数据持久化。<br>
     * 当配置 mqtt.msg.msg-persistence=true 时（默认是关闭的），客户端收到信息后会被调用。<br>
     * 如果用户需要自定义消息持久化，只需要在订阅主题时设置 MQTT 客户端消息监听器即可，这样你就可以获取到所订阅主题的消息了。<br>
     * 设置消息监听器示例：<br>
     * <blockquote><pre>
     * Subscribe subscribe = new Subscribe();
     * subscribe.setQos(QosType.QOS_JUST);
     * subscribe.setTopic("a/b/cc");
     * subscribe.setMessageListener(new AbstractMessageListener() {
     *      protected void msgArrived(String topic, MqttMessage mqttMessage) {
     *          System.out.println("客户端接收到消息");
     *      }
     *  });
     * </pre></blockquote>
     * </p>
     * @param tableName 表名
     * @param jsonObject 设备 JSON 数据
     */
    void persistenceData(String tableName, JSONObject jsonObject);
}
