# MQTT
物联网开发，MQTT 封装
## 封装接口
* 发布消息，直接注入接口 PublisherService，调用相关成员方法。
* 订阅消息，直接注入接口 SubscribeService，调用相关成员方法。
* 取消订阅，直接注入接口 UnsubscribeService，调用相关成员方法。
## 配置说明
用户根据需要选择配置就行。
```
reid:
  # MQTT 配置
  mqtt:
    client-id: reid-1235 # 客户端 ID。默认：ip:port
    host: ws://localhost:8083/mqtt # MQTT 消息服务器地址
    reconnection-seconds: 1 # 重连间隔，单位秒，客户端断开链接后多少时间重新链接
    max-retry-times: 10 # 最大重试次数。默认：99999
    subscribe-topics: # 配置默认需要监听的主题列表
      - qos: 2 # Qos
        topic: testtopic/reid # 订阅主题
        table-name: infoTable # 开启默认消息持久化后，持久化数据库表名将读取该属性。默认：表名将从订阅主题中截取最后一个”/“后的字符串做为表名
        user-context: null # 用于将上下文传递给回调的可选对象。如果不需要，可以为 null
        message-listener: com.module.Test.MyAbstractMessageListener # 自定义 MQTT 客户端消息监听器
    options:
      server-urls: tcp://localhost:1883,ssl://localhost:8883 # 设置客户端可以连接到的一个或多个服务器的地址列表
      username: reid # 设置用于连接的用户名
      password: 123456 # 设置用于连接的密码
      connection-timeout: 60 # 连接超时时间(单位秒)。默认：30秒
      clean-session: false # 是否清除 session 会话信息，当客户端 disconnect 时 cleanSession 为 true 会清除之前的所有订阅信息。默认：false
      executor-service-timeout: 1 # 终止 executor 服务时等待的时间(以秒为单位)。默认：1秒
      keepalive-interval: 60 # 每隔多长时间（秒）发送一个心跳包。默认：60秒
      max-inflight: 10 # 消息最大堵塞数。默认：10
      max-reconnect-delay: 128000 # 重连接间隔毫秒数。默认：128000ms
      ssl: # 链接安全设置
        enable: false # 是否启用。默认：false
        client-key-store: ??? # 客户端证书地址
        client-key-store-password: ??? # 客户端秘钥
      will: # 遗嘱和遗嘱配置
        topic: ??? # 设置“遗嘱”消息的话题主题，若客户端与服务器之间的连接意外中断，服务器将发布客户端的“遗嘱”消息
        message: ??? # 设置“遗嘱”消息的内容，默认是长度为零的消息
        qos: 2 # 设置“遗嘱”消息的 QoS。默认：2
        retained: false # 若想要在发布“遗嘱”消息时拥有 retain 选项，则为true。默认：false
    msg: # 消息持久化配置
      msg-persistence: true # 是否开启默认消息持久化。默认：false
      table-name-prefix: iot_ # 持久化表名称前缀。此配置只作用于自动生成表名。默认：iot_
      table-name-suffix: table # 持久化表名后缀。此配置只作用于自动生成表名

```
