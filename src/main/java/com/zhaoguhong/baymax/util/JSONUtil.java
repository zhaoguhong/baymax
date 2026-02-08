package com.zhaoguhong.baymax.util;

import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * json 工具类
 */
public class JSONUtil {

  private static final ObjectMapper MAPPER = buildObjectMapper();

  private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

  /**
   * 对象转json字符串
   * 
   * @param object
   * @return
   */
  public static String toJsonString(Object object) {
    try {
      return MAPPER.writeValueAsString(object);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * 对象转json字符串
   * 
   * @param object
   * @param dateFormat 时间格式
   * @return
   */
  public static String toJsonString(Object object, String dateFormat) {
    ObjectMapper mapper = buildObjectMapper();
    mapper.setDateFormat(new SimpleDateFormat(dateFormat));
    try {
      return mapper.writeValueAsString(object);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * json字符串转指定对象
   * 
   * @param json
   * @param clazz
   * @return
   */
  public static <T> T toObject(String json, Class<T> clazz) {
    try {
      return MAPPER.readValue(json, clazz);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * json字符串转指定对象
   * 
   * @param json
   * @return
   */
  public static Map<String, Object> toMap(String json) {
    try {
      JavaType javaType = MAPPER
          .getTypeFactory().constructParametrizedType(LinkedHashMap.class, Map.class,
              new Class[] {String.class, Object.class});
      return (Map<String, Object>) MAPPER.readValue(json, javaType);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static <T> List<T> toList(String json, Class<T> clazz) {
    try {
      JavaType javaType = MAPPER.getTypeFactory()
          .constructCollectionType(List.class, clazz);
      return (List<T>) MAPPER.readValue(json, javaType);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private static ObjectMapper buildObjectMapper() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.setDateFormat(new SimpleDateFormat(DEFAULT_DATE_FORMAT));
    // 当反序列化json时，未知属性会引起的反序列化被打断，这里我们禁用未知属性打断反序列化功能，
    // 因为，例如json里有10个属性，而我们的bean中只定义了2个属性，其它8个属性将被忽略
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    return mapper;
  }

}
