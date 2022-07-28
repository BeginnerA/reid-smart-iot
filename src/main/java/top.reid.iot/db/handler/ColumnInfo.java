package top.reid.iot.db.handler;

import lombok.Data;

/**
 * <p>
 * 数据库表列消息
 * </p>
 *
 * @Author REID
 * @Blog <a href="https://blog.csdn.net/qq_39035773">Blog</a>
 * @GitHub <a href="https://github.com/BeginnerA">GitHub</a>
 * @Data 2022/7/13
 * @Version V1.0
 **/
@Data
public class ColumnInfo {
    /**
     * 主键标识
     */
    private boolean isKey;
    /**
     * 列名称
     */
    private String name;
    /**
     * 数据类型
     */
    private int dataType;
    /**
     * 数据类型名称
     */
    private String dataTypeName;
    /**
     * 自增标识
     */
    private boolean isAutoIncrement;
    /**
     * 精度
     */
    private int precision;
    /**
     * 是否为空
     */
    private int isNull;
    /**
     * 小数位数
     */
    private int scale;
    /**
     * 默认值
     */
    private String defaultValue;
}
