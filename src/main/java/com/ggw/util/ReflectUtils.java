package com.ggw.util;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ggw.entity.SysRole;
import java.lang.annotation.Annotation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
/**
 * @author qizhuo
 * @date 2022/6/1 17:31
 */
@Slf4j
public class ReflectUtils {
  /**
   * 通过字段名从对象或对象的父类中得到@tableField字段的值
   * @param object 对象实例
   * @param fieldName 字段名
   * @return 字段对应的值
   * @throws Exception
   */
  public static String getValue(Object object, String fieldName)  {
    //反射获取类对象
    Class c = object.getClass();
    //获取类上面的注解 (即：@Target(ElementType.TYPE))
//    Annotation[] annotations = c.getAnnotations();
//    for (Annotation annotation : annotations) {
//
//      System.out.println(annotation);
//    }
    //获取类注解内容
    TableName tableName = (TableName) c.getAnnotation(TableName.class);
    //通过反射获取类中属性
    Field name = null;
    try {
      name = c.getDeclaredField(fieldName);
    } catch (NoSuchFieldException e) {
      e.printStackTrace();
    }
    //反射获取属性上的注解
    TableField tableField = name.getAnnotation(TableField.class);
    return tableField.value();
  }

}
