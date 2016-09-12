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
import org.eclipse.jetty.http.HttpStatus;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Defines the grant permission filter.
 */
public class GrantPermissionFilter implements Filter {

  private ServletContext context;

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    context = filterConfig.getServletContext();
    context.log("GrantPermissionFilter initialized");
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
    String permissionState = getPermissionState(httpRequest);

    if (permissionState == null) {
      context.log("Permission not granted: " + httpRequest.getRequestURI());
      request.setAttribute(
          HttpSessionConstant.ATTRIBUTE_NAME_PERMISSION_STATE,
          "NOT_GRANTED");
      request.setAttribute(
          HttpSessionConstant.ATTRIBUTE_NAME_RETURN_URI,
          httpRequest.getRequestURI());
      httpRequest.getRequestDispatcher("/resource/grant_permission.jsp").forward(request, response);
    } else {

      Boolean isPermissionGranted = Boolean.valueOf(httpRequest.getParameter(
          HttpSessionConstant.PARAM_NAME_GRANT_PERMISSION));

      if (isPermissionGranted) {
        context.log("Permission granted: " + httpRequest.getRequestURI());
        request.setAttribute(
            HttpSessionConstant.ATTRIBUTE_NAME_PERMISSION_STATE,
            "GRANTED");

        chain.doFilter(request, response);
      } else {
        context.log("Permission denied: " + httpRequest.getRequestURI());
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setStatus(HttpStatus.FORBIDDEN_403);
        httpResponse.getWriter().print("User denied permission.");
      }
    }
  }

  @Override
  public void destroy() {
  }

  private String getPermissionState(HttpServletRequest request) {
    String permissionState = (String) request.getAttribute(HttpSessionConstant.ATTRIBUTE_NAME_PERMISSION_STATE);
    if (permissionState == null) {
      permissionState = request.getParameter(HttpSessionConstant.ATTRIBUTE_NAME_PERMISSION_STATE);
    }

    return permissionState;
  }
}
