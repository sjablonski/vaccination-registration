import { Trim } from "class-sanitizer";
import { IsEmail, IsNotEmpty, IsMobilePhone, IsString } from "class-validator";
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
  @IsEmail()
  @Trim()
  email: string;

  @IsNotEmpty()
  @IsMobilePhone("pl-PL")
  @Trim()
  phoneNumber: string;

  constructor(
    firstName: string,
    lastName: string,
    pesel: string,
    email: string,
    phoneNumber: string
  ) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.pesel = pesel;
    this.email = email;
    this.phoneNumber = phoneNumber;
  }
}

export default RegistrationModel;
