package top.reid.iot.entity;

/**
 * <p>
 * QOS
 * </p>
 *
 * @Author REID
 * @Blog <a href="https://blog.csdn.net/qq_39035773">Blog</a>
 * @GitHub <a href="https://github.com/BeginnerA">GitHub</a>
 * @Data 2022/7/7
 * @Version V1.0
 **/
public enum QosType {
    /**
     * 尽力而为。消息发送者会想尽办法发送消息，但是遇到意外并不会重试
     */
    QOS_UNRELIABLE(0),
    /**
     * 至少一次。消息接收者如果没有知会或者知会本身丢失，消息发送者会再次发送以保证消息接收者至少会收到一次，当然可能造成重复消息
     */
    QOS_REPEAT(1),
    /**
     * 恰好一次。保证这种语义肯定会减少并发或者增加延时，不过丢失或者重复消息是不可接受的时候，级别2是最合适的
     */
    QOS_JUST(2);

    QosType(int qos) {
        this.qos = qos;
    }

    private final int qos;

    public int getValue(){
        return qos;
    }

    public static QosType getByValue(int value) {
        for (QosType qosType : values()) {
            if (qosType.getValue() == value) {
                return qosType;
            }
        }
        return null;
    }
}
