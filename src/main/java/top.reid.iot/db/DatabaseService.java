package top.reid.iot.db;

import cn.hutool.json.JSONObject;

import java.sql.SQLException;

/**
 * <p>
 * 数据库操作服务
 * </p>
 *
 * @Author REID
 * @Blog <a href="https://blog.csdn.net/qq_39035773">Blog</a>
 * @GitHub <a href="https://github.com/BeginnerA">GitHub</a>
 * @Data 2022/7/11
 * @Version V1.0
 **/
public interface DatabaseService {

    /**
     * 通过 JSON 对象创建表并且插入数据
     * @param tableName 表名
     * @param jsonObject JSON 对象
     * @throws SQLException SQL 异常
     */
    int createTableAndInsertData(String tableName, JSONObject jsonObject) throws SQLException;

    /**
     * 通过 JSON 对象创建表
     * @param tableName 表名
     * @param jsonObject JSON 对象
     * @throws SQLException SQL 异常
     */
    void createTableByJson(String tableName, JSONObject jsonObject) throws SQLException;

    /**
     * 解析 JSON 插入到数据库
     * @param tableName 表名
     * @param jsonObject JSON 对象
     * @return 执行结果
     * @throws SQLException SQL 异常
     */
    int insertByJson(String tableName, JSONObject jsonObject) throws SQLException;

    /**
     * 表是否存在
     * @param tableName 表名
     * @return 是否存在
     * @throws SQLException SQL 异常
     */
    boolean existsTable(String tableName) throws SQLException;
}
