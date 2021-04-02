import { Trim } from "class-sanitizer";
import {
  IsEmail,
  IsNotEmpty,
  IsMobilePhone,
  IsString,
  Matches,
} from "class-validator";
import IsValidPesel from "utils/IsValidPesel";

class RegistrationModel {
  @IsNotEmpty()
  @IsString()
  @Trim()
  firstName: string;

  @IsNotEmpty()
  @IsString()
  @Trim()
  lastName: string;

  @IsNotEmpty()
  @IsString()
  @IsValidPesel()
  @Trim()
  pesel: string;

  @IsNotEmpty()
  @Matches(/\d{2}-\d{3}/, { message: "zip must match 00-000" })
  @Trim()
  zip: string;

  @IsNotEmpty()
  @IsEmail()
  @Trim()
  email: string;

  @IsNotEmpty()
  @IsMobilePhone("pl-PL")
  @Trim()
  phone: string;

  constructor(
    firstName: string,
    lastName: string,
    pesel: string,
    zip: string,
    email: string,
    phone: string
  ) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.pesel = pesel;
    this.zip = zip;
    this.email = email;
    this.phone = phone;
  }
}

export default RegistrationModel;
