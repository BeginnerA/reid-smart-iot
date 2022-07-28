package top.reid.iot.config;

import cn.hutool.core.util.StrUtil;
import lombok.Data;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.CertificateException;

/**
 * <p>
 * MQTT 链接配置
 * </p>
 *
 * @Author REID
 * @Blog <a href="https://blog.csdn.net/qq_39035773">Blog</a>
 * @GitHub <a href="https://github.com/BeginnerA">GitHub</a>
 * @Data 2022/7/7
 * @Version V1.0
 **/
@Data
@Configuration
@ConfigurationProperties(prefix = "reid.mqtt.options")
public class ConnectOptions {
    /**
     * 客户端可以连接的 MQTT 服务器，用英文逗号间隔
     */
    private String serverUrls;
    /**
     * 链接用户名
     **/
    private String username;
    /**
     * 链接密码
     **/
    private String password;
    /**
     * 连接超时时间(单位秒)
     **/
    private int connectionTimeout = 30;
    /***
     * 是否清除 session 会话信息，当客户端 disconnect 时 cleanSession 为 true 会清除之前的所有订阅信息。<br>
     * 注意：cleanSession = true 客户端掉线后，服务器端会清除 session，客户端再次链接服务器后链接前的订阅主题将会全部失效。
     */
    private Boolean cleanSession = false;
    /**
     * 终止 executor 服务时等待的时间(以秒为单位)
     */
    private int executorServiceTimeout = 1;
    /**
     * 每隔多长时间（秒）发送一个心跳包（默认60秒）
     **/
    private int keepaliveInterval = 60;
    /**
     * 消息最大堵塞数 默认10
     */
    private int maxInflight = 10;
    /**
     * 重连接间隔毫秒数，默认为128000ms
     */
    private int maxReconnectDelay = 128000;
    /**
     * 链接安全 SSL 设置
     */
    private SSL ssl;
    /**
     * 设置连接的“最后遗嘱和遗嘱”（LWT）。<br>
     * 如果此客户端意外失去与服务器的连接，服务器将使用提供的详细信息向自己发布消息。
     */
    private Will will;


    @Data
    @Configuration
    @ConfigurationProperties(prefix = "reid.mqtt.options.ssl")
    protected class SSL {
        /**
         * 是否启用
         */
        private Boolean enable = false;
        /***
         * 客户端证书地址
         */
        private String clientKeyStore = "";

        /***
         * 客户端秘钥
         */
        private String clientKeyStorePassword = "";

        protected SocketFactory getSocketFactory() {
            return enable ? getSSLSocketFactory() : SocketFactory.getDefault();
        }

        private SSLSocketFactory getSSLSocketFactory(){
            SSLContext sslContext = null;
            try {
                InputStream keyStoreStream = Files.newInputStream(Paths.get(new URL(ssl.clientKeyStore).getPath()));
                sslContext = SSLContext.getInstance("TLS");
                TrustManagerFactory trustManagerFactory = TrustManagerFactory
                        .getInstance(TrustManagerFactory.getDefaultAlgorithm());
                KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
                keyStore.load(keyStoreStream, ssl.clientKeyStorePassword.toCharArray());
                trustManagerFactory.init(keyStore);
                sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
            } catch (NoSuchAlgorithmException | KeyStoreException | IOException | CertificateException |
                     KeyManagementException e) {
                e.printStackTrace();
            }
            return sslContext != null ? sslContext.getSocketFactory() : null;
        }
    }

    @Data
    @Configuration
    @ConfigurationProperties(prefix = "reid.mqtt.options.will")
    protected class Will {
        /***
         * 设置“遗嘱”消息的话题主题，若客户端与服务器之间的连接意外中断，服务器将发布客户端的“遗嘱”消息。
         */
        private String topic = "";

        /***
         * 设置“遗嘱”消息的内容，默认是长度为零的消息。
         */
        private String message = "";

        /***
         * 设置“遗嘱”消息的 QoS，默认为2
         */
        private int qos = 2;
        /***
         * 若想要在发布“遗嘱”消息时拥有 retain 选项，则为true。
         */
        private Boolean retained = false;
    }

    /**
     * 获取链接选项，包含控制客户端如何连接到服务器的选项集
     * @return 链接选项
     */
    public MqttConnectOptions getConnectOptions(){
        MqttConnectOptions options = new MqttConnectOptions();
        if (StrUtil.isNotEmpty(serverUrls)) {
            options.setServerURIs(serverUrls.split(","));
        }
        if (StrUtil.isNotEmpty(this.username)) {
            options.setUserName(this.username);
        }
        if (StrUtil.isNotEmpty(this.password)) {
            options.setPassword(this.password.toCharArray());
        }
        options.setCleanSession(this.cleanSession);
        options.setConnectionTimeout(this.connectionTimeout);
        options.setExecutorServiceTimeout(this.executorServiceTimeout);
        options.setKeepAliveInterval(this.keepaliveInterval);
        options.setMaxInflight(this.maxInflight);
        options.setMaxReconnectDelay(this.maxReconnectDelay);
        if (this.ssl != null) {
            options.setSocketFactory(this.ssl.getSocketFactory());
        }
        if (this.will != null) {
            options.setWill(this.will.topic, this.will.message.getBytes(), this.will.qos, this.will.retained);
        }
        return options;
    }

}
