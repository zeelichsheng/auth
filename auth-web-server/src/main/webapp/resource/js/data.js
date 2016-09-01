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

  var dataAjex = function (options) {
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
    listClients: function (resultHandler) {
      dataAjex({
        url: "http://127.0.0.1:8080/clients",
        method: 'GET',
        processData: true,
        success: function (clients) {
          resultHandler(clients.items)
        },
        error: function () {
          resultHandler({})
        }
      })
    }
  }
})();
