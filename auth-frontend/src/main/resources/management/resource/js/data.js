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

var data = (function () {

  var dataAjax = function (options) {
    if (options.error) {
      var originalError = options.error;
    }

    options.error = function (xhr, textStatus, errorThrown) {
      if (xhr.status == 0) {
        bootbox.alert(
          "Failed to communicated with the auth server.",
          "Close",
          function (result) {
            bootbox.hideAll();
          });
      }

      if (originalError != undefined) {
        originalError(xhr, textStatus, errorThrown);
      }
    };

    return $.ajax(options);
  };

  return {
    registerClient: function (clientRegisterSpec, successHandler, failureHandler) {
      dataAjax({
        url: "../api/clients",
        type: "POST", // jQuery backward compatibility
        method: "POST",
        contentType: "application/json; charset=UTF-8",
        data: JSON.stringify(clientRegisterSpec),
        dataType: "json",
        success: successHandler,
        error: function (xhr, textStatus, errorThrown) {
          failureHandler(xhr.responseText);
        }
      });
    },

    listClients: function (resultHandler) {
      dataAjax({
        url: "../api/clients",
        type: "GET", // jQuery backward compatibility
        method: "GET",
        dataType: "json",
        success: function (clients) {
          resultHandler(clients.items);
        },
        error: function () {
          resultHandler({});
        }
      });
    },

    unregisterClient: function (clientId, clientUnregistrationSpec, successHandler, failureHandler) {
      dataAjax({
        url: "../api/clients/" + clientId + "/unregister",
        type: "POST", // jQuery backward compatibility
        method: "POST",
        contentType: "application/json; charset=UTF-8",
        data: JSON.stringify(clientUnregistrationSpec),
        success: successHandler,
        error: function (xhr, textStatus, errorThrown) {
          failureHandler(xhr.responseText);
        }
      });
    },

    listAuthCodeAccessTokens: function (clientId, successHandler, failureHandler) {
      dataAjax({
        url: "../api/auth-code/" + clientId + "/access-tokens",
        type: "GET", // jQuery backward compatibility
        method: "GET",
        dataType: "json",
        success: function (accessTokens) {
          successHandler(accessTokens.items);
        },
        error: function (xhr, textStatus, errorThrown) {
          failureHandler(xhr.responseText);
        }
      });
    },

    revokeAuthCodeAccessToken: function (
      clientId, accessToken, accessTokenRevocationSpec, successHandler, failureHandler) {
      dataAjax({
        url: "../api/auth-code/" + clientId + "/access-tokens/" + accessToken + "/revoke",
        type: "POST", // jQuery backward compatibility
        method: "POST",
        contentType: "application/json; charset=UTF-8",
        data: JSON.stringify(accessTokenRevocationSpec),
        dataType: "json",
        success: successHandler,
        error: function(xhr, textStatus, errorThrown) {
          failureHandler(xhr.responseText);
        }
      });
    }
  }
})();
