import { Request, Response } from "express";
import producerService from "services/producer.service";

const registrationController = {
  add: async (req: Request, res: Response) => {
    try {
      await producerService.send("test-topic", [
        { value: JSON.stringify(req.body) },
      ]);
      res.send("add registration");
    } catch (err) {
      console.log("[controller]: ", err);
      res.status(500).json({ error: err });
    }
  },
};

export default registrationController;
