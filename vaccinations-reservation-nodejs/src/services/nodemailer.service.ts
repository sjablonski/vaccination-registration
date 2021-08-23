import nodemailer from "nodemailer";
import oauth2Config from "configs/oauth2.config";

const nodemailerService = (function () {
  return {
    createTransporter: async () => {
      const oauth2Client = oauth2Config.getInstance();
      const { clientId, clientSecret, refreshToken } = oauth2Config.getConfig();

      oauth2Client.setCredentials({ refresh_token: refreshToken });

      const accessToken = await oauth2Client.getAccessToken();

      const transporter = nodemailer.createTransport({
        service: "gmail",
        auth: {
          type: "OAuth2",
          user: "covid19.vaccinations.2021@gmail.com",
          clientId,
          clientSecret,
          refreshToken,
          accessToken: accessToken.token?.toString(),
        },
      });
      return transporter;
    },
  };
})();

export default nodemailerService;
