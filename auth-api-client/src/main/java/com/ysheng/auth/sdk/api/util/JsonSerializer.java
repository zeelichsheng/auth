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

package com.ysheng.auth.sdk.api.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;

import java.io.IOException;

/**
 * Defines a utility class that performs serialization operations
 * specifically for API client purposes.
 */
public class JsonSerializer {

  // The object mapper.
  private static final ObjectMapper objectMapper = new ObjectMapper();

  /**
   * Serializes an object to JSON string.
   *
   * @param object The object to be serialized.
   * @return A JSON string that contains the serialized object.
   * @throws JsonProcessingException The error that contains detail information.
   */
  public static HttpEntity serialize(Object object) throws JsonProcessingException {
    String serialized = objectMapper.writeValueAsString(object);

    return new StringEntity(serialized, ContentType.APPLICATION_JSON);
  }

  /**
   * Deserializes a JSON string to an object.
   *
   * @param serialized The JSON string that contains the serialized object.
   * @param typeReference The type reference of the object.
   * @param <T> The type of the object.
   * @return An object deserialized from the string.
   * @throws IOException The error that contains detail information.
   */
  public static <T> T deserialize(
      HttpEntity serialized,
      TypeReference<T> typeReference) throws IOException {
    if (typeReference.getType() == Void.TYPE) {
      return null;
    }

    return objectMapper.readValue(serialized.getContent(), typeReference);
  }
}
