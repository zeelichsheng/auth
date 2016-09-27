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

package com.ysheng.auth.portal.filter.bastion;

import com.ysheng.auth.portal.common.HttpSessionConstant;
import com.ysheng.auth.portal.common.bastion.BastionNetworksClient;
import com.ysheng.auth.portal.common.bastion.BastionNetworksConstants;
import com.ysheng.auth.portal.common.bastion.model.BrowserPluginSpec;
import com.ysheng.auth.portal.util.CookieUtil;
import jersey.repackaged.com.google.common.cache.Cache;
import jersey.repackaged.com.google.common.cache.CacheBuilder;
import net.sf.uadetector.ReadableUserAgent;
import net.sf.uadetector.UserAgentStringParser;
import net.sf.uadetector.service.UADetectorServiceFactory;
import org.eclipse.jetty.http.HttpStatus;

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
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * Defines the filter that handles initial BBE provision.
 */
public class InitialProvisionFilter implements Filter {

  private final static String BASTION_VERSION = "BASTION_VERSION";

  private final UserAgentStringParser parser;

  private final Cache<String, ReadableUserAgent> cache;

  private ServletContext context;

  public InitialProvisionFilter() {
    parser = UADetectorServiceFactory.getCachingAndUpdatingParser();
    cache = CacheBuilder.newBuilder()
        .maximumSize(100)
        .expireAfterWrite(2, TimeUnit.HOURS)
        .build();
  }

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    context = filterConfig.getServletContext();
    context.log("InitialProvisionFilter initialized");
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

    // TODO: we use cookie hack to temporarily avoid dead looping in this page.
    Cookie cookie = CookieUtil.getCookieByName(
        httpRequest,
        BASTION_VERSION);

    if (cookie != null) {
      context.log("Bastion Networks plug-in already installed: " + cookie.getValue());
      chain.doFilter(request, response);
      return;
    }

    String version = getBastionPlugInVersion(httpRequest);

    if (version == null) {
      context.log("Bastion Networks plug-in not installed");

      request.setAttribute(
          BASTION_VERSION,
          "UNINITIALIZED");

      request.setAttribute(
          HttpSessionConstant.ATTRIBUTE_NAME_RETURN_URI,
          httpRequest.getRequestURI());

      fillHttpRequestWithUserAgentInfo(httpRequest);

      httpRequest.getRequestDispatcher("/resource/bastion/plug_in_install.jsp").forward(request, response);
    } else {
      Boolean isInstallationAgreed = Boolean.valueOf(httpRequest.getParameter(
          "agree_install"));

      if (isInstallationAgreed) {
        // TODO: we use cookie hack to temporarily avoid dead looping in this page.
        CookieUtil.addCookie(
            httpResponse,
            BASTION_VERSION,
            "hack_version",
            "/",
            60);

        context.log("User agrees to install Bastion Networks plug-in");
        try {
          BastionNetworksClient client = new BastionNetworksClient(BastionNetworksConstants.BASTION_BASE_URL);
          BrowserPluginSpec spec = new BrowserPluginSpec();
          fillBrowserPluginSpecWithUserAgentInfo(httpRequest, spec);
          Map<String, String> plugin =  client.getInitializationApi().getBrowserPluginDownloadUrl(spec);

          httpResponse.setStatus(HttpStatus.OK_200);
          httpResponse.getWriter().print("Download plugin from: " + plugin.get("url"));

          //chain.doFilter(request, response);
        } catch (Exception ex) {
          httpResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR_500);
          httpResponse.getWriter().print("Failed to get browser plugin from Bastion Networks: " + ex.getMessage());
        }
      } else {
        context.log("User refuses to install Bastion Networks plug-in");
        httpResponse.setStatus(HttpStatus.FORBIDDEN_403);
        httpResponse.getWriter().print("You need to install Bastion Networks plug-in to continue authorization");
      }
    }
  }

  @Override
  public void destroy() {
    parser.shutdown();
  }

  private String getBastionPlugInVersion(HttpServletRequest request) {
    String version = (String) request.getAttribute(BASTION_VERSION);
    if (version == null) {
      version = request.getParameter(BASTION_VERSION);
    }

    if (version == null) {
      version = request.getHeader(BASTION_VERSION);
    }

    return version;
  }

  private void fillHttpRequestWithUserAgentInfo(HttpServletRequest request) {
    fillObjectWithUserAgentInfo(
        request,
        agent -> {
          request.setAttribute(
              "OS_TYPE",
              agent.getOperatingSystem().getFamilyName());

          request.setAttribute(
              "OS_VERSION",
              agent.getOperatingSystem().getVersionNumber().toVersionString());

          request.setAttribute(
              "BROWSER_TYPE",
              agent.getFamily().getName());

          request.setAttribute(
              "BROWSER_VERSION",
              agent.getVersionNumber().toVersionString());
        });
  }

  private void fillBrowserPluginSpecWithUserAgentInfo(
      HttpServletRequest request,
      BrowserPluginSpec spec) {
    fillObjectWithUserAgentInfo(
        request,
        agent -> {
          spec.setOsType(agent.getOperatingSystem().getFamilyName());
          spec.setOsVersion(agent.getOperatingSystem().getVersionNumber().toVersionString());
          spec.setBrowserType(agent.getTypeName());
          spec.setBrowserVersion(agent.getVersionNumber().toVersionString());
        });
  }

  private void fillObjectWithUserAgentInfo(
      HttpServletRequest request,
      Consumer<ReadableUserAgent> consumer) {
    String userAgent = request.getHeader("User-Agent");
    ReadableUserAgent agent = cache.getIfPresent(userAgent);
    if (agent == null) {
      agent = parser.parse(userAgent);
      cache.put(userAgent, agent);
    }

    consumer.accept(agent);
  }
}
