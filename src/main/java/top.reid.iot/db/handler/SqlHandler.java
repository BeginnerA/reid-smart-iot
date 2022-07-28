package top.reid.iot.db.handler;

import cn.hutool.json.JSONObject;

import java.util.List;

/**
 * <p>
 * SQL 处理程序服务
 * </p>
 *
 * @Author REID
 * @Blog <a href="https://blog.csdn.net/qq_39035773">Blog</a>
 * @GitHub <a href="https://github.com/BeginnerA">GitHub</a>
 * @Data 2022/7/11
 * @Version V1.0
 **/
public interface SqlHandler {

    /**
     * 通过 JSON 创建表 SQL
     * @param tableName 表名
     * @param jsonObject JSON 对象
     * @return 建表 SQL
     */
    String createTableSqlByJson(String tableName, JSONObject jsonObject);

    /**
     * 检查表是否存在 SQL
     * @param tableName 表名
     * @return 检查表是否存在 SQL
     */
    String existsTableSql(String tableName);

    /**
     * 创建 INSERT 插入语句
     * @param columnInfos 表列消息列表
     * @param tableName 表名
     * @param jsonObject JSON 对象
     * @return INSET 插入语句
     */
    String createInsertSql(String tableName, List<ColumnInfo> columnInfos, JSONObject jsonObject);

}
