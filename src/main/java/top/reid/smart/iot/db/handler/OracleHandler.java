package top.reid.smart.iot.db.handler;

import cn.hutool.json.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * <p>
 * Oracle 处理程序
 * </p>
 *
 * @Author REID
 * @Blog <a href="https://blog.csdn.net/qq_39035773">Blog</a>
 * @GitHub <a href="https://github.com/BeginnerA">GitHub</a>
 * @Data 2022/7/12
 * @Version V1.0
 **/
@Slf4j
public class OracleHandler implements SqlHandler {
    @Override
    public String createTableSqlByJson(String tableName, JSONObject jsonObject) {
        log.info("待实现数据库类型[ORACLE]处理程序");
        return null;
    }

    @Override
    public String existsTableSql(String tableName) {
        return "SELECT COUNT(1) FROM user_objects WHERE object_name = '"+tableName+"';";
    }

    @Override
    public String createInsertSql(String tableName, List<ColumnInfo> columnInfos, JSONObject jsonObject) {
        return null;
    }
}
