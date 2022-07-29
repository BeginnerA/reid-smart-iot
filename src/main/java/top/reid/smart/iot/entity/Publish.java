package top.reid.smart.iot.entity;

import top.reid.smart.iot.mqttv3.listener.AbstractPublishListener;
import lombok.Data;

/**
 * <p>
 * 推送消息体
 * </p>
 *
 * @Author REID
 * @Blog <a href="https://blog.csdn.net/qq_39035773">Blog</a>
 * @GitHub <a href="https://github.com/BeginnerA">GitHub</a>
 * @Data 2022/7/7
 * @Version V1.0
 **/
@Data
public class Publish {
    /**
     * 推送的消息报文
     */
    private Object message;

    /**
     * 是否保留消息
     * 若为 true，则消息会保留在服务器上，即使 qos=2，客户端也已经消费，
     * 但当主题被重新订阅时，
     * 该消息还是会被再次消费，被消费的消息为最后一次发送的且retain=true的消息
     * 默认为false，必要情况下再设置为true，请谨慎使用
     */
    private Boolean retain = false;

    /**
     * Qos 1/2
     */
    private QosType qos = QosType.QOS_JUST;

    /**
     * 推送主题
     */
    private String  topic;

    /**
     * 额外携带的信息，异步执行结束后会传至callback
     */
    private Object userContextAsync;

    /**
     * 异步推送操作完成时，将通知此接口的实现者
     */
    private AbstractPublishListener callbackAsync;
}
