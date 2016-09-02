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

 var accessTokenView = (function() {
   var templateId = "tplClientAccessTokens";
   var containerSelector = "#contentView";
   var handleSelector = "#clientAccessTokens";

   return {
     show: function(clientId, clientSecret, authCodeAccessTokens) {
       var model = {};
       model.clientId = clientId;
       model.clientSecret = clientSecret;
       model.authCodeAccessTokens = authCodeAccessTokens;

       templateLoader.get(templateId, function(template) {
         $(containerSelector).append(template(model));
         $(containerSelector).css("height", "");

         $("a.revokeAccessTokenButton").click(function(e) {
           var clientId = document.getElementById("clientIdForToken").getAttribute("data-clientId");
           var clientSecret = document.getElementById("clientSecretForToken").getAttribute("data-clientSecret");
           var accessToken = $(e.target).closest("tr").attr("data-accessTokenId");

           bootbox.confirm("Revoke access token " + accessToken + " from client " + clientId + " ?",
             function(result) {
               if (result) {
                 accessTokenController.onClickAccessTokenRevocation(clientId, clientSecret, accessToken);
               }
           });
         });
       });
     },

     hide: function() {
       $(containerSelector).css("height", $(containerSelector).height());
       $(handleSelector).remove();
     },

     refresh: function(accessTokens) {
       this.hide();
       this.show(accessTokens);
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

 var accessTokenController = (function() {

   return {
     show: function(clientId, clientSecret) {
       accessTokenView.hide();
       this.listAuthCodeAccessTokens(clientId, clientSecret);
     },

     listAuthCodeAccessTokens: function(clientId, clientSecret) {
       data.listAuthCodeAccessTokens(
         clientId,
         function(authCodeAccessTokens) {
           accessTokenView.show(clientId, clientSecret, authCodeAccessTokens);
         },
         function(errorText) {
         });
     },

     onClickAccessTokenRevocation: function(clientId, clientSecret, accessToken) {
       var accessTokenRevokeSpec = {
         clientSecret: clientSecret
       };

       data.revokeAuthCodeAccessToken(clientId, accessToken, accessTokenRevokeSpec,
         function() {
           accessTokenView.pop("Access token " + accessToken + " revoked.");
           accessTokenView.hide();
           accessTokenController.show(clientId, clientSecret);
         },
         function(errorText) {
           accessTokenView.hide();
           accessTokenController.show(clientId, clientSecret);
         })
     },

     hide: accessTokenView.hide
   }
 })();
