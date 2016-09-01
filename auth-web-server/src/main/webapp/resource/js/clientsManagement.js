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

var clientsManagementView = (function () {

  var templateId = "tplClientsManagement";
  var containerSelector = "#contentView";
  var handleSelector = "#clientsManagement";

  return {

    refresh: function(clients) {
      this.hide();
      this.show(clients);
    },

    show: function(clients) {
      templateLoader.get(templateId, function(template) {
        $(containerSelector).append(template({clients: clients}));
        $(containerSelector).css("height", "");

        $("a.addClientButton").click(function() {
          // TODO: add add client logic here
        });

        $("a.deleteClientButton").click(function(e) {
          var clientId = $(e.target).closest("tr").attr("data-clientId");
          bootbox.confirm("Delete this client?", function(result) {
            if (result) {
              // TODO: add delete client logic here
            }
          })
        });
      });
    },

    hide: function () {
      $(containerSelector).css("height", $(containerSelector).height());
      $(handleSelector).remove();
    }
  }
})();

var clientsManagementController = (function () {
  var view = clientsManagementView

  return {
    show: function () {
      view.hide()
      data.listClients(function (clients) {
        view.show(clients)
      })
    }
  }
})();
