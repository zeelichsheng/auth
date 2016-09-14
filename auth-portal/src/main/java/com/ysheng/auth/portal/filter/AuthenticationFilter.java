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

package com.ysheng.auth.portal.filter;

import com.ysheng.auth.portal.common.HttpSessionConstant;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.UUID;

/**
 * Defines the authentication filter.
 */
public class AuthenticationFilter implements Filter {

  private final static String COOKIE_AUTHENTICATED_USER = "authenticated_user";

  private ServletContext context;

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    context = filterConfig.getServletContext();
    context.log("AuthenticationFilter initialized");
  }

  @Override
  public void doFilter(
      ServletRequest request,
      ServletResponse response,
      FilterChain chain) throws IOException, ServletException {
    if (!(request instanceof HttpServletRequest) ||
        !(response instanceof HttpServletResponse)) {
      chain.doFilter(request, response);
      return;
    }

    HttpServletRequest httpRequest = (HttpServletRequest) request;
    HttpServletResponse httpResponse = (HttpServletResponse) response;

    Cookie[] cookies = httpRequest.getCookies();
    if (cookies != null) {
      for (Cookie cookie : cookies) {
        if (cookie.getName().equals(COOKIE_AUTHENTICATED_USER)) {
          // TODO: add a user look-up and sanity check before proceeding.
          context.log("User has already been authenticated: " + httpRequest.getRequestURI());
          chain.doFilter(request, response);
          return;
        }
      }
    }

    String authState = getAuthState(httpRequest);

    if (authState == null) {
      context.log("Unauthenticated access request: " + httpRequest.getRequestURI());
      request.setAttribute(
          HttpSessionConstant.ATTRIBUTE_NAME_AUTH_STATE,
          "INITIALIZED");
      request.setAttribute(
          HttpSessionConstant.ATTRIBUTE_NAME_RETURN_URI,
          httpRequest.getRequestURI());
      httpRequest.getRequestDispatcher("/resource/login.jsp").forward(request, response);
    } else {
      // TODO: add the real authentication logic here
      String username = httpRequest.getParameter("j_username");
      String password = httpRequest.getParameter("j_password");
      Boolean remember = "true".equalsIgnoreCase(httpRequest.getParameter("j_remember"));

      context.log("Access authenticated: " + httpRequest.getRequestURI());

      // TODO: other than saving the cookie, we need to save the relattion between
      // the UUID saved in the cookie and the real user credentials, such that
      // when we read the cookie above, we can validate it with real user credentials.
      String cookieValue = remember ? UUID.randomUUID().toString() : null;
      Cookie cookie = new Cookie(COOKIE_AUTHENTICATED_USER, cookieValue);
      cookie.setMaxAge(60);
      cookie.setPath("/");
      httpResponse.addCookie(cookie);

      chain.doFilter(request, response);
    }
  }

  @Override
  public void destroy() {
  }

  private String getAuthState(HttpServletRequest request) {
    String authState = (String) request.getAttribute(HttpSessionConstant.ATTRIBUTE_NAME_AUTH_STATE);
    if (authState == null) {
      authState = request.getParameter(HttpSessionConstant.ATTRIBUTE_NAME_AUTH_STATE);
    }

    return authState;
  }
}
