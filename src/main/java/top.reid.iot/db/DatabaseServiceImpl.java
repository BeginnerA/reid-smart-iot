package top.reid.iot.db;

import cn.hutool.json.JSONObject;
import top.reid.iot.db.handler.ColumnInfo;
import top.reid.iot.db.handler.SqlHandlerFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
@Slf4j
@Service
public class DatabaseServiceImpl implements DatabaseService {

    @Resource
    DataSource dataSource;
    @Resource
    SqlHandlerFactory sqlHandlerFactory;

    /**
     * 数据库链接，永久链接不关闭
     */
    Connection connection;

    /**
     * 获取链接
     * @throws SQLException 执行异常
     */
    public void getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = dataSource.getConnection();
        }
    }

    @Override
    public int createTableAndInsertData(String tableName, JSONObject jsonObject) throws SQLException {
        createTableByJson(tableName, jsonObject);
        return insertByJson(tableName, jsonObject);
    }

    @Override
    public void createTableByJson(String tableName, JSONObject jsonObject) throws SQLException {
        getConnection();
        if (!existsTable(tableName)) {
            String tableSql = sqlHandlerFactory.createTableSqlByJson(tableName, jsonObject, getDBType());
            PreparedStatement preparedStatement = connection.prepareStatement(tableSql);
            int i = preparedStatement.executeUpdate();
            close(null, preparedStatement, null);
        }
    }

    @Override
    public int insertByJson(String tableName, JSONObject jsonObject) throws SQLException {
        getConnection();
        List<ColumnInfo> tableInfo = getTableInfo(tableName);
        String insertSql = sqlHandlerFactory.createInsertSql(tableName, getDBType(), tableInfo, jsonObject);
        log.info("持久化数据：{}", insertSql);
        PreparedStatement preparedStatement = connection.prepareStatement(insertSql);
        int i = preparedStatement.executeUpdate();
        close(null, preparedStatement, null);
        return i;
    }

    @Override
    public boolean existsTable(String tableName) throws SQLException {
        getConnection();
        String sql = sqlHandlerFactory.existsTableSql(tableName, getDBType());
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        boolean result = false;
        if (resultSet.next()) {
            result = resultSet.getInt(1) > 0;
        }
        close(resultSet, preparedStatement, null);
        return result;
    }

    /**
     * 获取表列信息列表
     * @param tableName 表名
     * @return 表列信息列表
     */
    private List<ColumnInfo> getTableInfo(String tableName) throws SQLException {
        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet resultSet = metaData.getColumns(null, null,
                tableName.toUpperCase(), null);
        List<ColumnInfo> columnInfos = new ArrayList<>();
        while(resultSet.next()){
            ColumnInfo col = new ColumnInfo();
            col.setName(resultSet.getString("COLUMN_NAME"));
            col.setDataType(resultSet.getInt("DATA_TYPE"));
            col.setDataTypeName(resultSet.getString("TYPE_NAME"));
            col.setPrecision(resultSet.getInt("COLUMN_SIZE"));
            col.setScale(resultSet.getInt("DECIMAL_DIGITS"));
            col.setIsNull(resultSet.getInt("NULLABLE"));
            col.setDefaultValue( resultSet.getString("COLUMN_DEF"));
            columnInfos.add(col);
        }
        close(resultSet, null, null);
        return columnInfos;
    }

    /**
     * 获取数据库类型
     * @return 数据库类型
     * @throws SQLException SQL 执行异常
     */
    private String getDBType() throws SQLException {
        return connection.getMetaData().getDatabaseProductName();
    }

    /**
     * 释放资源
     * @param resultSet 结果集对象
     * @param preparedStatement SQL 预编译对象
     * @param connection 数据库链接对象
     */
    private void close(ResultSet resultSet, PreparedStatement preparedStatement,
                       Connection connection) throws SQLException {
        if (resultSet != null) {
            resultSet.close();
        }
        if (preparedStatement != null) {
            preparedStatement.close();
        }
        if (connection != null) {
            connection.close();
        }
    }

}
