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

var templateLoader = (function () {
  var tplCache = [];

  return {
    get: function (templateName, callback) {
      if (!tplCache[templateName]) {
        var template = $("#" + templateName);
        if (template.size() == 0) {
          $.get("management/resource/template/" + templateName + ".html", function (data) {
            tplCache[templateName] = Handlebars.compile(data);
            callback(tplCache[templateName]);
          });
        } else {
          tplCache[templateName] = Handlebars.compile(template.html());
          callback(tplCache[templateName]);
        }
      } else {
        callback(tplCache[templateName]);
      }
    }
  }
})();

var main = {

  clearContentView: function() {
    $("div.side-nav a").removeClass("cur");
    clientController.hide();
    accessTokenController.hide();
  },

  onPageLoad: function () {
    $("#nav-clients-apps").click(function () {
      main.clearContentView();
      $("#nav-clients-apps").addClass("cur");
      clientController.showClientsManagement();
    });
  },

  showClientAccessTokens: function(clientId, clientSecret) {
    this.clearContentView();
    $("#nav-access-tokens").addClass("cur");
    accessTokenController.show(clientId, clientSecret);
  }
};

// On DOM ready
$(function () {

  // Attach global listeners
  $(".alert").alert();

  // Initialization of window controller.
  main.onPageLoad();
});
