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

package com.ysheng.auth.core.util;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Defines utility functions related to URI.
 */
public class UriUtil {

  /**
   * Determines whether the given URI is in valid format.
   *
   * @param uri The URI string to be validated.
   * @return True if the give URI is valid. False otherwise.
   */
  public static boolean isValidUri(String uri) {
    try {
      new URL(uri);
      return true;
    } catch (MalformedURLException ex) {
      return false;
    }
  }
}
