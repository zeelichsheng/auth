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

var clientView = (function () {

  var clientsManagementTemplate = "tplClientsManagement";
  var clientRegistrationTemplate = "tplClientRegistration";

  var containerSelector = "#contentView";
  var clientsManagementHandleSelector = "#clientsManagement";
  var clientRegistrationHandleSelector = "#clientRegistration";

  return {
    showClientsManagement: function(clients) {
      var model = {};
      model.clients = clients;

      templateLoader.get(clientsManagementTemplate, function(template) {
        $(containerSelector).append(template(model));
        $(containerSelector).css("height", "");

        $("#registerClientButton").click(function() {
          clientController.onClickClientRegistration();
        });

        $("a.unregisterClientButton").click(function(e) {
          var clientId = $(e.target).closest("tr").attr("data-clientId");
          bootbox.confirm("Delete this client?", function(result) {
            if (result) {
              // TODO: add delete client logic here
            }
          });
        });
      });
    },

    showClientRegistration: function() {
      var model = {}
      model.availableClientTypes = [
        { "clientType": "CONFIDENTIAL" },
        { "clientType": "PUBLIC"}
      ];

      templateLoader.get(clientRegistrationTemplate, function(template) {
        $(containerSelector).append(template(model));
        $(containerSelector).css("height", "");

        $("#clientRegistrationForm button.cancel").click(function() {
          clientController.onCancelClientRegistration();
        });

        $("#clientRegistrationForm").submit(function() {
          clientController.onSubmitClientRegistration(this);
          return false;
        });
      });
    },

    hide: function() {
      $(containerSelector).css("height", $(containerSelector).height());
      $(clientsManagementHandleSelector).remove();
      $(clientRegistrationHandleSelector).remove();
    },

    refresh: function(clients) {
      this.hide();
      this.showClientsManagement(clients);
    },

    pop: function(text) {
      bootbox.alert(
        text,
        "Close",
        function(result) {
          bootbox.hideAll();
        });
    }
  }
})();

var clientController = (function () {
  var view = clientView;

  return {
    showClientsManagement: function () {
      view.hide();
      data.listClients(function (clients) {
        view.showClientsManagement(clients)
      });
    },

    onClickClientRegistration: function() {
      view.hide();
      view.showClientRegistration();
    },

    onSubmitClientRegistration: function(form) {
      var formAsObject = $(form).serializeObject();

      var clientRegisterSpec = {
        type: formAsObject['clientTypesSelect'],
        redirectUri: formAsObject['clientRedirectUriInput']
      };

      data.registerClient(
        clientRegisterSpec,
        function(client) {
          view.pop("Client " + client.id + " registered");
          view.hide();
          clientController.showClientsManagement();
        },
        function(errorText) {
          view.hide();
          clientController.showClientsManagement();
        });
    },

    onCancelClientRegistration: function() {
      view.hide();
      view.showClientsManagement();
    },

    hide: view.hide
  }
})();
