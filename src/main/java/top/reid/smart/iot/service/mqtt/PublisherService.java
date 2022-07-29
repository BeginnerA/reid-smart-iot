package top.reid.smart.iot.service.mqtt;

import top.reid.smart.iot.mqttv3.pojo.MqttResult;
import top.reid.smart.iot.mqttv3.pojo.Publish;

import java.util.List;

/**
 * <p>
 * 推送服务
 * </p>
 *
 * @Author REID
 * @Blog <a href="https://blog.csdn.net/qq_39035773">Blog</a>
 * @GitHub <a href="https://github.com/BeginnerA">GitHub</a>
 * @Data 2022/7/7
 * @Version V1.0
 **/
public interface PublisherService {
    /***
     * 推送消息-同步
     * @param publish 推送消息
     * @return 推送结果
     */
    MqttResult publish(Publish publish);

    /***
     * 批量推送-同步
     * 返回成功条数
     * @param publishList 推送消息列表
     * @return 成功数
     */
    int publishBatch(List<Publish> publishList);

    /***
     * 推送消息-异步
     * @param publish 推送消息
     */
    void publishAsync(Publish publish);

    /***
     * 批量推送-异步
     * @param publishList 推送消息列表
     */
    void publishBatchAsync(List<Publish> publishList);
}
