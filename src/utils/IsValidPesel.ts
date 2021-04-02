import { registerDecorator, ValidationOptions } from "class-validator";

function IsValidPesel(validationOptions?: ValidationOptions) {
  return function (object: Object, propertyName: string) {
    registerDecorator({
      name: "isValidPesel",
      target: object.constructor,
      propertyName: propertyName,
      options: validationOptions,
      validator: {
        validate(value: any) {
          if (typeof value !== "string") return false;

          const weight = [1, 3, 7, 9, 1, 3, 7, 9, 1, 3];
          let sum = 0;
          const controlNumber = parseInt(value.substring(10, 11));

          for (let i = 0; i < weight.length; i++) {
            sum += parseInt(value.substring(i, i + 1)) * weight[i];
          }
          sum = sum % 10;
          return (10 - sum) % 10 === controlNumber;
        },
      },
    });
  };
}

export default IsValidPesel;
