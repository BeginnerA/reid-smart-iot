package top.reid.smart.iot.config.mqtt;

import cn.hutool.core.util.StrUtil;
import top.reid.smart.iot.mqttv3.pojo.MqttV3Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * <p>
 * MQTT 初始化客户端配置
 * </p>
 *
 * @Author REID
 * @Blog <a href="https://blog.csdn.net/qq_39035773">Blog</a>
 * @GitHub <a href="https://github.com/BeginnerA">GitHub</a>
 * @Data 2022/7/8
 * @Version V1.0
 **/
@Configuration
public class MqttInitConfig {
    /**
     * spring 服务端口
     */
    @Value("${server.port}")
    private String port;

    /**
     * 客户端自定义 ID<br>
     * 如果没有配置，默认 MQTT 客户端 ID 为 ip:port
     */
    @Value("${reid.mqtt.client-id:}")
    private String clientId;

    @Bean
    @ConditionalOnMissingBean(value = {MqttV3Client.class})
    public MqttV3Client mqttV3Client() {
        try {
            if (StrUtil.isEmpty(clientId)) {
                InetAddress localHost = Inet4Address.getLocalHost();
                String ip = localHost.getHostAddress();
                clientId = ip + ":" + port;
            }
            return new MqttV3Client(clientId);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return new MqttV3Client("unknown");
        }
    }
}
