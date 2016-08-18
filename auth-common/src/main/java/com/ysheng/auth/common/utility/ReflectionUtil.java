/*
 * Copyright 2016 VMware, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy of
 * the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS, without warranties or
 * conditions of any kind, EITHER EXPRESS OR IMPLIED.  See the License for the
 * specific language governing permissions and limitations under the License.
 */

package com.ysheng.auth.common.utility;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Defines utility functions related to object reflection.
 */
public class ReflectionUtil {

  /**
   * Gets the names and values of the fields from an object.
   * The fields are annotated by a specific annotation type.
   *
   * @param annotatedType The class type of the annotated object.
   * @param annotationType The class type of the annotation.
   * @param annotatedObject The object whose fields are annotated.
   * @return A map of the names and values of the annotated fields from the object.
   * @throws Throwable The exception that contains detail information.
   */
  public static Map<String, Object> getAnnotatedFieldNamesAndValues(
      Class annotatedType,
      Class annotationType,
      Object annotatedObject) throws Throwable {
    Map<String, Object> result = new HashMap<>();
    for (Field field : annotatedType.getDeclaredFields()) {
      for (Annotation annotation : field.getDeclaredAnnotations()) {
        if (annotation.annotationType() == annotationType) {
          Object value = field.get(annotatedObject);
          if (value != null) {
            result.put(field.getName(), field.get(annotatedObject));
          }
        }
      }
    }

    return result;
  }

  /**
   * Sets the values of the fields for an object.
   * The names and values of the fields are specified in the map.
   *
   * @param objectType The class type of the database object.
   * @param object The database object.
   * @param fieldNamesAndValues A map of names and values of the fields to be set.
   * @throws Throwable The exception that contains detail information.
   */
  public static void setFieldValues(
      Class objectType,
      Object object,
      Map<String, String> fieldNamesAndValues) throws Throwable {
    for (Field field : objectType.getDeclaredFields()) {
      String fieldName = field.getName();
      if (fieldNamesAndValues.containsKey(fieldName)) {
        String fieldValueStr = fieldNamesAndValues.get(fieldName);
        Object fieldValue = fieldValueStr;
        if (fieldValueStr == null) {
          continue;
        }

        boolean fieldAccessable = field.isAccessible();
        Class fieldType = field.getType();

        if (!fieldAccessable) {
          field.setAccessible(true);
        }

        if (Enum.class == fieldType) {
          fieldValue = Enum.valueOf(fieldType, fieldValueStr);
        } else if (Integer.class == fieldType) {
          fieldValue = Integer.parseInt(fieldValueStr);
        } else if (Long.class == fieldType) {
          fieldValue = Long.parseLong(fieldValueStr);
        }

        field.set(object, fieldValue);
        if (!fieldAccessable) {
          field.setAccessible(false);
        }
      }
    }
  }
}
