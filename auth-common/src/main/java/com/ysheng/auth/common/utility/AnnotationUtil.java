/*
 * Copyright 2016 Yu Sheng. All Rights Reserved.
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
import java.util.HashMap;
import java.util.Map;

/**
 * Defines utility functions related to annotation.
 */
public class AnnotationUtil {

  /**
   * Gets the name and value of fields from an object.
   * The fields are annotated by a specific annotation type.
   *
   * @param annotatedType The class type of the annotated object.
   * @param annotationType The class type of the annotation.
   * @param annotatedObject The object whose fields are annotated.
   * @return A map of the name and value of the annotated fields from the object.
   * @throws Throwable The exception that contains detail information.
   */
  public static Map<String, Object> getAnnotatedFields(
      Class annotatedType,
      Class annotationType,
      Object annotatedObject) throws Throwable {
    Map<String, Object> result = new HashMap<>();
    for (Field field : annotatedType.getDeclaredFields()) {
      for (Annotation annotation : field.getDeclaredAnnotations()) {
        if (annotation.annotationType() == annotationType) {
          result.put(field.getName(), field.get(annotatedObject));
        }
      }
    }

    return result;
  }
}
