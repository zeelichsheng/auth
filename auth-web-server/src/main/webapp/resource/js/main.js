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
          $.get("resource/template/" + templateName + ".html", function (data) {
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
  onPageLoad: function () {
    // On click of nav link, remove 'current' from all, add to actual current.
    $("div.side-nav a").click(function () {
      $("div.side-nav a").removeClass("cur");
      $(this).addClass("cur");
    });

    $("#nav-clients-apps").click(function () {
      clientsManagementController.show();
    });
  }
};

// On DOM ready
$(function () {

  // Attach global listeners
  $(".alert").alert();

  $('body').tooltip({
    selector: '[rel=tooltip]'
  });

  $('body').popover({
    selector: '[rel=popover]',
    //See popoverBundle.js
    title: function () {
      return popoverBundle.getTitle(this.attributes['name'].nodeValue);
    },
    content: function () {
      return popoverBundle.getContent(this.attributes['name'].nodeValue);
    }
  });

  // Initialization of window controller.
  main.onPageLoad();
});
