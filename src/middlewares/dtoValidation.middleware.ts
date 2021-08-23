import { plainToClass } from "class-transformer";
import { validate, ValidationError } from "class-validator";
import { Request, Response, NextFunction } from "express";

const dtoValidationMiddleware = (type: any, skipMissingProperties = false) => {
  return async (req: Request, res: Response, next: NextFunction) => {
    try {
      const dto = plainToClass(type, req.body);
      const errors = await validate(dto, { skipMissingProperties });
      if (errors.length > 0) {
        const dtoErrors = errors.map((error: ValidationError) => {
          return {
            [error.property]: (Object as any).values(error.constraints),
          };
        });
        res.status(422).json({ error: dtoErrors });
      } else {
        next();
      }
    } catch (ex) {
      res.status(500);
    }
  };
};

export default dtoValidationMiddleware;
