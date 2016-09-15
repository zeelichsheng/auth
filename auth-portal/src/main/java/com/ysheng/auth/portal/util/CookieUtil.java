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

package com.ysheng.auth.portal.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Defines a class that contains utility functions related to Cookie.
 */
public class CookieUtil {

  /**
   * Gets a cookie from the HTTP request by cookie name.
   *
   * @param request The HTTP request object.
   * @param cookieName The cookie name.
   * @return The cookie object that matches the name.
   */
  public static Cookie getCookieByName(
      HttpServletRequest request,
      String cookieName) {

    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
      for (Cookie cookie : cookies) {
        if (cookie.getName().equals(cookieName)) {
          return cookie;
        }
      }
    }

    return null;
  }

  /**
   * Adds a cookie to the HTTP response.
   *
   * @param response The HTTP response object.
   * @param cookieName The cookie name.
   * @param cookieValue The cookie value.
   * @param cookiePath The cookie path.
   * @param cookieMaxAge The cookie max live age.
   */
  public static void addCookie(
      HttpServletResponse response,
      String cookieName,
      String cookieValue,
      String cookiePath,
      int cookieMaxAge) {
    Cookie cookie = new Cookie(cookieName, cookieValue);
    cookie.setMaxAge(cookieMaxAge);
    cookie.setPath(cookiePath);
    response.addCookie(cookie);
  }

  /**
   * Removes a cookie from the HTTP response.
   *
   * @param response The HTTP response object.
   * @param cookieName The cookie name.
   */
  public static void removeCookie(
      HttpServletResponse response,
      String cookieName) {
    addCookie(response, cookieName, null, "/", 0);
  }
}
