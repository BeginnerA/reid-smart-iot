package top.reid.smart.iot.config.mqtt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * MQTT 消息持久化配置
 * </p>
 *
 * @Author REID
 * @Blog <a href="https://blog.csdn.net/qq_39035773">Blog</a>
 * @GitHub <a href="https://github.com/BeginnerA">GitHub</a>
 * @Data 2022/7/12
 * @Version V1.0
 **/
@Data
@Configuration
@ConfigurationProperties(prefix = "reid.mqtt.msg")
public class MessagePersistenceConfig {
    /**
     * 是否开启默认消息持久
     */
    private boolean msgPersistence = false;
    /**
     * 持久化表名称前缀。此配置只作用于自动生成表名
     */
    private String tableNamePrefix = "iot_";
    /**
     * 持久化表名后缀。此配置只作用于自动生成表名
     */
    private String tableNameSuffix;
}
