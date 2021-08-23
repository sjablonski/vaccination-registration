import { Message } from "kafkajs";
import Patient from "models/Patient.model";
import { MailOptions } from "nodemailer/lib/json-transport";
import mongoService from "services/mongo.service";
import nodemailerService from "services/nodemailer.service";

const registrationController = {
  add: async (message: Message) => {
    const transporter = await nodemailerService.createTransporter();
    const patient = JSON.parse(message.value!.toString()) as Patient;

    const vaccination = await mongoService.findOneFree();

    if (vaccination) {
      await mongoService.updateOne(vaccination.id, patient);
      const mailOptions: MailOptions = {
        from: "e-rejestracja covid-19 <covid19.vaccinations.2021@gmail.com>",
        to: patient.email,
        subject: "Rejestracja na szczepienie przeciw COVID-19",
        html: `
        <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
        <html xmlns="http://www.w3.org/1999/xhtml">
          <head>
            <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
            <meta name="viewport" content="width=device-width, initial-scale=1.0" />
            <meta http-equiv="X-UA-Compatible" content="ie=edge" />
            <title>Rejestracja na szczepienie przeciw COVID-19</title>
            <style type="text/css">
              body {
                margin: 0;
                padding: 0;
                background-color: #ffffff;
                font-family: Arial, Helvetica, sans-serif;
                text-align: center;
              }
              .header {
                background-color: #f0f0f0;
                border: 1px solid #e0e0e0;
              }
              .address-name {
                text-transform: uppercase;
              }
              h1 {
                font-size: 32px;
              }
              p {
                margin: 24px 0;
                font-size: 20px;
              }
            </style>
          </head>
          <body>
            <div class="header">
              <h1>Twoje szczepienie przeciw COVID-19</h1>
            </div>
            <div class="main">
              <p style="margin: 48px 0;">
                Zarejestrowałeś się na szczepienie przeciw COVID-19
              </p>
              <p>Twoja wizyta odbędzie się</p>
              <p><b>${vaccination.date}</b> o godzinie <b>${vaccination.time}</b></p>
              <p>w punkcie</p>
              <p class="address-name"><b>${vaccination.address.institution_name}</b></p>
              <p>pod adresem</p>
              <p><b>${vaccination.address.street}, ${vaccination.address.zip} ${vaccination.address.city}</b></p>
            </div>
          </body>
        </html>
        `,
      };
      transporter.sendMail(mailOptions, (err, info) => {
        if (err) {
          console.log(err);
          throw err;
        } else {
          console.log("Email sent: " + info.response);
        }
      });
      console.log("Dodano zgłoszenie: ");
    } else {
      console.log("Brak miejsc");
    }
  },
};

export default registrationController;
