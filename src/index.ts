import registrationController from "controllers/registration.controller";
import express, { Application } from "express";
import consumerService from "services/consumer.service";
import mongoService from "services/mongo.service";

const app: Application = express();

const run = async () => {
  app.listen(3001);

  await consumerService.connect();
  await mongoService.connect();

  const consumer = consumerService.consumer;
  await consumer.subscribe({ topic: "test-topic", fromBeginning: true });
  await consumer.run({
    eachMessage: async ({ topic, partition, message }) => {
      const prefix = `${topic}[${partition} | ${message.offset}] / ${message.timestamp}`;
      console.log(`- ${prefix} ${message.key}#${message.value}`);
      registrationController.add(message);
    },
  });
};

run().catch((e) => console.error(`[backend/consumer] ${e.message}`, e));

const errorTypes = ["unhandledRejection", "uncaughtException"];
const signalTraps = ["SIGTERM", "SIGINT", "SIGUSR2"];

errorTypes.map((type) => {
  process.on(type, async (message) => {
    try {
      console.error(`process.on ${type}, ${message}`);
      await consumerService.disconnect();
      await mongoService.disconnect();
      process.exit(0);
    } catch (_) {
      process.exit(1);
    }
  });
});

signalTraps.map((type) => {
  process.on(type, async () => {
    try {
      await consumerService.disconnect();
      await mongoService.disconnect();
    } finally {
      process.kill(process.pid, type);
      process.exit();
    }
  });
});
