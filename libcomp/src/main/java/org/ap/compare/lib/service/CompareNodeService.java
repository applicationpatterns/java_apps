package org.ap.compare.lib.service;

import org.ap.compare.lib.annotations.PrimaryKey;
import org.ap.compare.lib.model.ComparableNode;
import org.ap.compare.lib.model.ComparableNodeResult;
import org.ap.compare.lib.model.ComparePair;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.MessageFormat;
import java.util.*;

// this is a low level service that compares nodes
public class CompareNodeService {
  ComparableNodeResult compareNode(ComparePair comparePair) {
    return comparePair.getCurrentNode().compare(comparePair.getBaseNode());
  }

  void compareNodeList() {}

  /**
   * this method converts the object to a ComparableNode. the object must be annotated
   * with @ComparableNode at least one field must be annotated as @PrimaryKey
   */
  public ComparableNode toComparableNode(Object obj) {
    if (Objects.isNull(obj)) {
      return null;
    }

    Class<?> clazz = obj.getClass();
    if (!clazz.isAnnotationPresent(org.ap.compare.lib.annotations.ComparableNode.class)) {
      throw new RuntimeException(
          "cannot convert to ComparableNode since the class "
              + clazz.getName()
              + " is not annotated as @ComparableNode");
    }
    System.out.println("printing fields of class " + clazz.getName());

    Map<String, Object> fields = new HashMap<>();
    Map<String, Object> primaryKeys = new HashMap<>();

    for (Field field : clazz.getDeclaredFields()) {
      String fieldName = field.getName();
      String fieldType = field.getType().getName();
      boolean isPrimaryKey = field.isAnnotationPresent(PrimaryKey.class);
      Type type = field.getGenericType();
      String pTypeString = "";
      // TODO clean this up!!
      boolean isList = false;
      boolean isListOfComparableNode = false;
      if (type instanceof ParameterizedType) {
        ParameterizedType pType = (ParameterizedType)type;
        pTypeString = "parameter type: " + pType.getRawType() + "-" + pType.getActualTypeArguments()[0];
        if (pType.getRawType().getTypeName().equals("java.util.List")) {
          isList = true;
          System.out.println("           GOT THIS " + pType.getActualTypeArguments()[0].getTypeName());
          if (pType.getActualTypeArguments()[0] instanceof Class) {
            Class nestedClazz = (Class)(pType.getActualTypeArguments()[0]);
            boolean boo = nestedClazz.isAnnotationPresent(org.ap.compare.lib.annotations.ComparableNode.class);
            System.out.println("           GOT THIS ComparableNode " +boo);
            Object values = getFieldValue(field, obj);
            System.out.println("           GOT THIS with value " +values);
            if (values instanceof List<?>) {
              List<?> list = (List<?>)values;
              System.out.println("           GOT THIS with list value " +list);
            }
            isListOfComparableNode = boo;
          }
        }
      }
      System.out.println(
          MessageFormat.format(
              "  fieldInfo: {0} of type {1} pk={2} {3}", fieldName, fieldType, isPrimaryKey, pTypeString));

      if (isList && isListOfComparableNode) {
        // create a ComparableNode from value
        List<ComparableNode> nestedNodeList = new ArrayList<>();
        Object listObjectValue = getFieldValue(field, obj);
        if (listObjectValue instanceof List<?>) {
          List<?> list = (List<?>)listObjectValue;
          Iterator iterator = list.iterator();
          while (iterator.hasNext()) {
            Object elem = iterator.next();
            ComparableNode comparableNode = toComparableNode(elem);
            nestedNodeList.add(comparableNode);
          }
        }
        fields.put(field.getName(), nestedNodeList);
      } else {
        fields.put(field.getName(), getFieldValue(field, obj));
      }
      if (field.isAnnotationPresent(PrimaryKey.class)) {
        primaryKeys.put(field.getName(), getFieldValue(field, obj));
      }
    }

    //    Map<String, Object> fields = new HashMap<>();
    //    Map<String, Object> primaryKeys = new HashMap<>();

    for (Field field : clazz.getDeclaredFields()) {
      //      fields.put(field.getName(), getFieldValue(field, obj));
      //      if (field.isAnnotationPresent(PrimaryKey.class)) {
      //        primaryKeys.put(field.getName(), getFieldValue(field, obj));
      //      }
    }
    ComparableNode comparableNode =
        ComparableNode.builder().fields(fields).primaryKeys(primaryKeys).build();
    return comparableNode;
  }

  private Object getFieldValue(Field field, Object obj) {
    try {
      field.setAccessible(true);
      return field.get(obj);
    } catch (IllegalAccessException e) {
      String msg =
          MessageFormat.format("Unable to access field {0} of object {1}", field.getName(), obj);
      throw new RuntimeException(msg, e);
    }
  }
}
