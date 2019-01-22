package cn.com.play.controllers.util;

import cn.com.play.controllers.config.redis.PagedListVO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class JsonUtil {
    private static Logger logger = LoggerFactory.getLogger(JsonUtil.class);

    public static String getValueAsText(JsonNode jsonNode, String key) {
        JsonNode subJsonNode = jsonNode.get(key);
        if (subJsonNode == null || subJsonNode.isNull()) {
            return null;
        }
        return subJsonNode.asText();
    }

    public static BigDecimal getValueAsBigDecimal(JsonNode jsonNode, String key) {
        return getValueAsBigDecimal(jsonNode, key, null);
    }

    public static BigDecimal getValueAsBigDecimal(JsonNode jsonNode, String key, BigDecimal defaultValue) {
        JsonNode subJsonNode = jsonNode.get(key);
        if (subJsonNode == null || subJsonNode.isNull()) {
            return defaultValue;
        }
        String value = subJsonNode.asText();
        if (isBlank(value)) {
            return defaultValue;
        }
        return new BigDecimal(value);
    }

    public static <T extends Enum<T>> T getValueAsEnum(JsonNode jsonNode, String key, Class<T> enumType) {
        JsonNode subJsonNode = jsonNode.get(key);
        if (subJsonNode == null || subJsonNode.isNull()) {
            return null;
        }
        String value = subJsonNode.asText();
        if (isBlank(value)) {
            return null;
        }

        return Enum.valueOf(enumType, value);
    }

    public static Long getValueAsLong(JsonNode jsonNode, String key) {
        JsonNode subJsonNode = jsonNode.get(key);
        if (subJsonNode == null || subJsonNode.isNull()) {
            return null;
        }
        return subJsonNode.asLong();
    }

    public static Integer getValueAsInteger(JsonNode jsonNode, String key) {
        return getValueAsInteger(jsonNode, key, null);
    }

    public static Integer getValueAsInteger(JsonNode jsonNode, String key, Integer defaultValue) {
        JsonNode subJsonNode = jsonNode.get(key);
        if (subJsonNode == null || subJsonNode.isNull()) {
            return defaultValue;
        }
        return subJsonNode.asInt();
    }

    public static <T> List<T> getValueAsList(JsonNode jsonNode, Class<T> clazz) throws IOException {
        return getValueAsList(jsonNode.toString(), clazz);
    }

    public static <T> List<T> getValueAsList(String jsonNode, Class<T> clazz) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, clazz);
        return mapper.readValue(jsonNode, javaType);
    }

    public static <T> Set<T> getValueAsSet(String jsonNode, Class<T> clazz) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructParametricType(Set.class, clazz);
        return mapper.readValue(jsonNode, javaType);
    }

    public static <T> PagedListVO<T> getPagedListVOFromResult(String data, Class<T> clazz) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructParametricType(PagedListVO.class, clazz);
        return mapper.readValue(data, javaType);
    }

    public static <T> T getValueFromResult(String data, Class<T> clazz) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(data, clazz);
        } catch (Exception e) {
            logger.error("Json read error", e);
            return null;
        }
    }

    public static <T> T getValueFromResult(JsonNode data, Class<T> clazz) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(data.toString(), clazz);
    }

    public static <T> T getTypeFromResult(String data, TypeReference<T> typeReference) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(data, typeReference);
        } catch (Exception e) {
            logger.error("Json read error", e);
            return null;
        }
    }
}
