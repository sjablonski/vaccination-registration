import { OAuth2Client } from "google-auth-library";
import { google } from "googleapis";

const oauth2Config = (function () {
  const config = {
    clientId:
      "<test>",
    clientSecret: "<test>",
    refreshToken:
      "<test>",
    redirectUri: "https://developers.google.com/oauthplayground",
  };
  let instance: OAuth2Client;

  const createInstance = () => {
    return new google.auth.OAuth2(
      config.clientId,
      config.clientSecret,
      config.redirectUri
    );
  };

  return {
    getInstance: () => {
      if (!instance) {
        instance = createInstance();
      }
      return instance;
    },
    getConfig: () => config,
  };
})();

export default oauth2Config;
