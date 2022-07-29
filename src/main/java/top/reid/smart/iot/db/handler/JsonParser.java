package top.reid.smart.iot.db.handler;

import cn.hutool.core.collection.CollUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * Json 解析器
 * </p>
 *
 * @Author REID
 * @Blog <a href="https://blog.csdn.net/qq_39035773">Blog</a>
 * @GitHub <a href="https://github.com/BeginnerA">GitHub</a>
 * @Data 2022/7/12
 * @Version V1.0
 **/
@Slf4j
public class JsonParser {
    /**
     * 解析 JSON
     * @param jsonParam JSON 字符串
     * @return JSON 映射器列表
     */
    protected List<JsonMapper> analysisJson(String jsonParam) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNodeRoot = mapper.readTree(jsonParam);
            List<JsonMapper> tableMapper = new ArrayList<>();
            List<JsonMapper> fieldMapper = new ArrayList<>();
            recursiveAnalysis(tableMapper, fieldMapper, "rootJson", jsonNodeRoot);

            // 表与表之间
            if (CollUtil.isNotEmpty(tableMapper)) {
                // 合并
                fieldMapper.addAll(tableMapper);
            }

            // 属性与表之间
            if (CollUtil.isNotEmpty(fieldMapper)) {

                // 去重
                List<JsonMapper> fieldMapperAfter = fieldMapper.stream().collect(
                        Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(
                                Comparator.comparing(o -> o.getParentName() + ";" + o.getCurrName()))), ArrayList::new)
                );

                // 排序
                fieldMapperAfter.sort(new Comparator<JsonMapper>() {
                    public int compare(JsonMapper o1, JsonMapper o2) {
                        // 按照父节点升序
                        return o1.getParentName().compareTo(o2.getParentName());
                    }
                });
                return fieldMapperAfter;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }



    /**
     *  递归解析子节点
     * @param tableMapper 表与表之间关联映射
     * @param fieldMapper 字段属性与表之间关联映射
     * @param parentNodeName 父节点名称
     * @param jsonNode 当前节点
     */
    private void recursiveAnalysis(List<JsonMapper> tableMapper, List<JsonMapper> fieldMapper, String parentNodeName,
                                  JsonNode jsonNode) {
        if (jsonNode.isContainerNode()){
            if (JsonNodeType.OBJECT == jsonNode.getNodeType()) {
                Iterator<String> fieldNames = jsonNode.fieldNames();
                while (fieldNames.hasNext()) {
                    String fieldName = fieldNames.next();
                    if (jsonNode.get(fieldName).isContainerNode()){
                        tableMapper.add(new JsonMapper(parentNodeName, fieldName));
                        recursiveAnalysis(tableMapper, fieldMapper, fieldName, jsonNode.get(fieldName));
                    } else {
                        fieldMapper.add(new JsonMapper(parentNodeName, fieldName + ":" + jsonNode.get(fieldName).getNodeType()));
                    }
                }
            } else if (JsonNodeType.ARRAY == jsonNode.getNodeType()) {
                for (JsonNode jsonNodeElement : jsonNode) {
                    recursiveAnalysis(tableMapper, fieldMapper, parentNodeName, jsonNodeElement);
                }
            }
        }
    }

}
