package top.reid.smart.iot.db.handler;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.node.JsonNodeType;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * MySql 处理程序
 * </p>
 *
 * @Author REID
 * @Blog <a href="https://blog.csdn.net/qq_39035773">Blog</a>
 * @GitHub <a href="https://github.com/BeginnerA">GitHub</a>
 * @Data 2022/7/12
 * @Version V1.0
 **/
public class MySqlHandler extends JsonParser implements SqlHandler {
    @Override
    public String createTableSqlByJson(String tableName, JSONObject jsonObject) {
        List<JsonMapper> analysisResult = this.analysisJson(JSONUtil.toJsonStr(jsonObject));
        if (CollUtil.isNotEmpty(analysisResult)) {
            Map<String, List<String>> mapperByParentName = analysisResult.stream().
                    collect(Collectors.groupingBy(JsonMapper::getParentName,
                            Collectors.mapping(JsonMapper::getCurrName, Collectors.toList())));
            Iterator<Map.Entry<String, List<String>>> iterator = mapperByParentName.entrySet().iterator();
            StringBuilder sql = new StringBuilder();
            while (iterator.hasNext()) {
                Map.Entry<String, List<String>> entry = iterator.next();
                sql.append(createSql(tableName, entry.getValue()));
            }
            return sql.toString();
        }
        return null;
    }

    @Override
    public String existsTableSql(String tableName) {
        return "SELECT COUNT(1) FROM information_schema.tables WHERE table_name = '"+tableName+"';";
    }

    @Override
    public String createInsertSql(String tableName, List<ColumnInfo> columnInfos, JSONObject jsonObject) {
        StringBuilder insetField = new StringBuilder();
        StringBuilder insetValue = new StringBuilder();
        insetField.append("INSERT INTO ").append(tableName).append("(");
        int fieldLength = columnInfos.size();
        for (ColumnInfo columnInfo : columnInfos) {
            Object value = jsonObject.get(columnInfo.getName());
            insetField.append(columnInfo.getName()).append(',');
            // TODO 可以优化，目前只做常用几个类型
            // 字段名称（ID 和 CREATE_TIME 是每张表必须存在的字段）
            if ("ID".equals(columnInfo.getName().toUpperCase())) {
                insetValue.append("'").append(IdUtil.randomUUID()).append("',");
                continue;
            }
            if ("CREATE_TIME".equals(columnInfo.getName().toUpperCase())) {
                insetValue.append("NOW()").append(",");
                continue;
            }
            // 字段类型
            if ("INT".equals(columnInfo.getDataTypeName().toUpperCase())) {
                insetValue.append(value).append(",");
            }
            if ("CHAR".equals(columnInfo.getDataTypeName().toUpperCase())) {
                insetValue.append(value).append(",");
            }
            if ("VARCHAR".equals(columnInfo.getDataTypeName().toUpperCase())) {
                insetValue.append("'").append(value).append("',");
            }
        }
        return insetField.replace(insetField.length()-1, insetField.length(), ") VALUES(")
                .append(insetValue.replace(insetValue.length()-1, insetValue.length(), ")"))
                .toString();
    }

    /**
     * 获取创建表前缀，根据拆分表数字
     * @param tableName 表名
     * @param splitNum 拆分表数字
     * @return 建表前缀
     */
    private String getTablePrefix(String tableName, int splitNum) {
        if (splitNum > 0) {
            tableName = tableName + "_" + splitNum;
        }
        return "CREATE TABLE `" + tableName + "` ( \r\n" +
                "  `id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键ID', \r\n";
    }

    /**
     * 获取创建表后缀
     * @param tableName 表名
     * @return 建表后缀
     */
    private String getTableSuffix(String tableName) {
        return "  `create_time` datetime NULL DEFAULT NULL COMMENT '创建日期', \r\n" +
                "   PRIMARY KEY (`id`) USING BTREE \r\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=UTF8; \r\n" +
                " \r\n";
    }

    /**
     *  根据表名以及列名称创建表结构
     * @param tableName 表名
     * @param columnNames 列名
     * @return 建表结构
     */
    private String createSql(String tableName, List<String> columnNames) {
        boolean isSplit = isSplitToTables(columnNames.size());
        StringBuilder sqlAll = new StringBuilder();
        sqlAll.append(getTablePrefix(tableName, 0));
        for (int i = 0; i < columnNames.size(); i++) {
            if (columnNames.get(i).contains(":")) {
                String[] str = columnNames.get(i).split(":");
                if (!"id".equals(str[0].toLowerCase())) {
                    String columnType = buildColumnType(str[1]);
                    sqlAll.append("  `").append(str[0]).append("` ").append(columnType).append(" DEFAULT NULL, \r\n");
                }
            }else {
                if (!"id".equals(columnNames.get(i).toLowerCase())) {
                    sqlAll.append("  `").append(columnNames.get(i)).append("` LONGTEXT DEFAULT NULL, \r\n");
                }
            }
            if (isSplit) {
                if (i > 0 && i % 50 == 0) {
                    sqlAll.append(getTableSuffix(tableName));
                    sqlAll.append(getTablePrefix(tableName, i / 50));
                }
            }
        }
        sqlAll.append(getTableSuffix(tableName));
        return sqlAll.toString();
    }

    /**
     * 根据 JSON 字段类型构建数据库字段类型
     * 默认: varchar(20)
     * @param jsonType JSON 字段类型
     * @return 数据库字段类型
     */
    private String buildColumnType(String jsonType) {
        if (JsonNodeType.BOOLEAN.toString().equals(jsonType)) {
            return "CHAR(1)";
        } else if (JsonNodeType.NUMBER.toString().equals(jsonType)) {
            if (jsonType.contains(".")) {
                return "VARCHAR(20)";
            }
            return "INT(11)";
        } else if (JsonNodeType.STRING.toString().equals(jsonType)) {
            return "VARCHAR(200)";
        } else {
            return "VARCHAR(20)";
        }
    }

    /**
     * 根据表的列数判断是否需要拆分字段到多个表
     * 当字段 > 80 时，自动创建多表，多表采用单表 50个字段进行分拆
     * @param columns 列数
     * @return 是否
     */
    private boolean isSplitToTables(int columns){
        return columns >= 80;
    }
}
