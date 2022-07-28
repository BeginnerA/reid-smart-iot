package top.reid.iot.db.handler;

import cn.hutool.json.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.List;

/**
 * <p>
 * SQL 处理程序工厂
 * TODO 可以扩展为抽象工厂
 * </p>
 *
 * @Author REID
 * @Blog <a href="https://blog.csdn.net/qq_39035773">Blog</a>
 * @GitHub <a href="https://github.com/BeginnerA">GitHub</a>
 * @Data 2022/7/12
 * @Version V1.0
 **/
@Slf4j
@Component
public class SqlHandlerFactory {

    final String MYSQL = "MYSQL";
    final String ORACLE = "ORACLE";

    /**
     * 通过 JSON 创建表 SQL
     * @param tableName 表名
     * @param jsonObject JSON 对象
     * @param databaseProductName 数据库产品名称
     * @return 建表 SQL
     */
    public String createTableSqlByJson(String tableName, JSONObject jsonObject, String databaseProductName) throws SQLException {
        String tableSql = null;
        switch (databaseProductName.toUpperCase()) {
            case MYSQL:
                tableSql = new MySqlHandler().createTableSqlByJson(tableName, jsonObject);
                break;
            case ORACLE:
                tableSql = new OracleHandler().createTableSqlByJson(tableName, jsonObject);
                break;
            default:
                log.error("获取建表 SQL 失败，未匹配对应数据库类型：[{}]", databaseProductName);
        }
        return tableSql;
    }

    /**
     * 判断表是否存在 SQL
     * @param tableName 表名
     * @param databaseProductName 数据库产品名称
     * @return 判断表是否存在 SQL
     */
    public String existsTableSql(String tableName, String databaseProductName) {
        String sql = null;
        switch (databaseProductName.toUpperCase()) {
            case MYSQL:
                sql = new MySqlHandler().existsTableSql(tableName);
                break;
            case ORACLE:
                sql = new OracleHandler().existsTableSql(tableName);
                break;
            default:
                log.error("获取检查表是否存在 SQL 失败，未匹配对应数据库类型：[{}]", databaseProductName);
        }
        return sql;
    }

    /**
     * 判断表是否存在 SQL
     * @param tableName 表名
     * @param databaseProductName 数据库产品名称
     * @param columnInfos 表列消息列表
     * @param jsonObject JSON 对象
     * @return 判断表是否存在 SQL
     */
    public String createInsertSql(String tableName, String databaseProductName, List<ColumnInfo> columnInfos,
                                  JSONObject jsonObject) {
        String sql = null;
        switch (databaseProductName.toUpperCase()) {
            case MYSQL:
                sql = new MySqlHandler().createInsertSql(tableName, columnInfos, jsonObject);
                break;
            case ORACLE:
                sql = new OracleHandler().createInsertSql(tableName, columnInfos, jsonObject);
                break;
            default:
                log.error("获取 INSERT SQL 失败，未匹配对应数据库类型：[{}]", databaseProductName);
        }
        return sql;
    }
}
