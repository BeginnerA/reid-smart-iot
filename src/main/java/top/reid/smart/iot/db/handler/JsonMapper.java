package top.reid.smart.iot.db.handler;

import lombok.Data;

/**
 * <p>
 * JSON 映射器
 * </p>
 *
 * @Author REID
 * @Blog <a href="https://blog.csdn.net/qq_39035773">Blog</a>
 * @GitHub <a href="https://github.com/BeginnerA">GitHub</a>
 * @Data 2022/7/11
 * @Version V1.0
 **/
@Data
public class JsonMapper {
    private String parentName;

    private String currName;

    public JsonMapper(String parentName, String currName) {
        this.parentName = parentName;
        this.currName =currName;
    }
}
