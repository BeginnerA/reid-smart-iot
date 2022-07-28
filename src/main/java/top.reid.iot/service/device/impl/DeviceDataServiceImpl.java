package top.reid.iot.service.device.impl;

import cn.hutool.json.JSONObject;
import top.reid.iot.db.DatabaseService;
import top.reid.iot.service.device.DeviceDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.SQLException;

/**
 * <p>
 * 设备数据服务。<br>
 * 结构化数据库：设备地址类(地点、GIS信息)、设备名称编号(资产属性相关)、设备相关的标签类、设备的规格，
 * 静态数据多以结构性、关系型数据库存储。如：mysql、oracle等<br>
 * 时间数据库：诊断信号类(如温湿度、压力状态)、设备自身状态数据(如电池电量)，每个数据都与时间有对应关系，
 * 通常采用时序数据库方式存储。如：MongoDB、CouchDB、HBase等<br>
 * 非结构化数据：动态数据也包含图片、文本、语音和视频等非结构化数据。如：对象存储、hdfs等<br>
 * 或者直接采用大数据引擎。如：TDengine
 * </p>
 *
 * @Author REID
 * @Blog <a href="https://blog.csdn.net/qq_39035773">Blog</a>
 * @GitHub <a href="https://github.com/BeginnerA">GitHub</a>
 * @Data 2022/7/11
 * @Version V1.0
 **/
@Slf4j
@Service
public class DeviceDataServiceImpl implements DeviceDataService {

    @Resource
    DatabaseService databaseService;

    @Override
    public void persistenceData(String tableName, JSONObject jsonObject) {
        try {
            databaseService.createTableAndInsertData(tableName, jsonObject);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
