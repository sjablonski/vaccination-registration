import { Router } from "express";
import { registrationController } from "controllers";
import dtoValidationMiddleware from "middlewares/dtoValidation.middleware";
import RegistrationModel from "models/registration.model";

const routers = Router();

routers.post(
  "/registration",
  dtoValidationMiddleware(RegistrationModel),
  registrationController.add
);

export default routers;
