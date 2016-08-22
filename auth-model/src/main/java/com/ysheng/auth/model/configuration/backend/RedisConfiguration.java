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

package com.ysheng.auth.model.configuration.backend;

import java.util.Set;

/**
 * Defines configuration related to Redis.
 */
public class RedisConfiguration {

  // The address of the master node.
  private String masterAddress;

  // The number of maximum connections.
  private int maxConnections;

  // The list of sentinels.
  private Set<String> sentinels;

  // Whether to block when connection is exhausted.
  private boolean blockWhenExhausted;

  ///
  /// Getters and Setters.
  ///

  public String getMasterAddress() {
    return masterAddress;
  }

  public void setMasterAddress(String masterAddress) {
    this.masterAddress = masterAddress;
  }

  public int getMaxConnections() {
    return maxConnections;
  }

  public void setMaxConnections(int maxConnections) {
    this.maxConnections = maxConnections;
  }

  public Set<String> getSentinels() {
    return sentinels;
  }

  public void setSentinels(Set<String> sentinels) {
    this.sentinels = sentinels;
  }

  public boolean isBlockWhenExhausted() {
    return blockWhenExhausted;
  }

  public void setBlockWhenExhausted(boolean blockWhenExhausted) {
    this.blockWhenExhausted = blockWhenExhausted;
  }
}
