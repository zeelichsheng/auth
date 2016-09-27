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
        <h1>Install Bastion Networks Plug-in</h1>
      </div>

      <form class="form-horizontal" id="grantHere" method="post"
            action="${RETURN_URI}">

        <div>
          We detect that you do not have Bastion Networks plug-in installed for<br>
          your browser. Do you want to install the plug-in?
        </div>

        <div>
          Operating System Type: ${OS_TYPE}<br/>
          Operating System Version: ${OS_VERSION}<br/>
          Browser Type: ${BROWSER_TYPE}<br/>
          Browser Version: ${BROWSER_VERSION}<br/>
        </div>

        <div class="form-actions">
          <button id="agree_install" name="agree_install" value="true" type="submit"
                  class="btn btn-success">Agree</button>
          &nbsp;&nbsp;&nbsp;<em>or</em>&nbsp;&nbsp;&nbsp;
          <button type="submit" name="agree_install" value="false"
                  class="btn btn-danger">Refuse</button>
        </div>

        <input type="hidden" name="BASTION_VERSION" value="${BASTION_VERSION}" />
      </form>
    </div>
  </div>
</body>
</html>
