<%--
  ~ Copyright 2016 Yu Sheng. All Rights Reserved.
  ~
  ~ Licensed under the Apache License, Version 2.0 (the "License"); you may not
  ~ use this file except in compliance with the License.  You may obtain a copy of
  ~ the License at http://www.apache.org/licenses/LICENSE-2.0
  ~
  ~ Unless required by applicable law or agreed to in writing, software distributed
  ~ under the License is distributed on an "AS IS" BASIS, without warranties or
  ~ conditions of any kind, EITHER EXPRESS OR IMPLIED.  See the License for the
  ~ specific language governing permissions and limitations under the License.
  --%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title></title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/bootstrap.min.css" />
  <link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/style.css" />
</head>
<body>
  <div class="main">
    <div class="full">
      <div class="page-header">
        <h1>Login</h1>
      </div>

      <form class="form-horizontal" id="registerHere" method="post"
            action="${RETURN_URI}">
        <fieldset>
          <div class="control-group">
            <label class="control-label">Username</label>
            <div class="controls">
              <input type="text" class="input-xlarge" id="username"
                     name="j_username" rel="popover"
                     data-content="Enter your username."
                     data-original-title="Username" />
            </div>
          </div>

          <div class="control-group">
            <label class="control-label">Password</label>
            <div class="controls">
              <input type="password" class="input-xlarge" id="password"
                     name="j_password" rel="popover"
                     data-content="What's your password?"
                     data-original-title="Password" />
            </div>
          </div>

          <div class="control-group">
            <label class="control-label">Remember</label>
            <div class="controls">
              <input type="checkbox" name="j_remember" value="true" />
            </div>
          </div>

          <input type="hidden" name="AUTH_STATE" value="${AUTH_STATE}" />
        </fieldset>

        <div class="form-actions">
          <button type="submit" class="btn btn-primary">Login</button>
        </div>
      </form>
    </div>
  </div>
</body>
</html>
