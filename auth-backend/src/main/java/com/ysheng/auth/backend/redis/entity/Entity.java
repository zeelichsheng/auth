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

package com.ysheng.auth.backend.redis.entity;

import com.ysheng.auth.model.database.DatabaseObject;

import java.util.Map;

/**
 * Defines the interface that represents a Redis entity.
 *
 * @param <T> The type of the database object model.
 */
public interface Entity<T extends DatabaseObject> {

  /**
   * Returns the Redis key for the object.
   *
   * @return The key for the object.
   */
  String getKey();

  /**
   * Returns the Redis hash for the object.
   *
   * @return The hash for the object.
   */
  Map<String, String> getHash();
}
