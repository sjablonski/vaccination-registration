import express, { Application } from "express";
import routers from "routes";
import producerService from "services/producer.service";

const app: Application = express();

app.use(express.urlencoded({ extended: false }));
app.use(express.json());

const run = async () => {
  app.use("/", routers);
  app.listen(3000);

  await producerService.connect();
};

run().catch((e) => console.error(`[rest-api/producer] ${e.message}`, e));

const errorTypes = ["unhandledRejection", "uncaughtException"];
const signalTraps = ["SIGTERM", "SIGINT", "SIGUSR2"];

errorTypes.map((type) => {
  process.on(type, async (message) => {
    try {
      console.error(`process.on ${type}, ${message}`);
      await producerService.disconnect();
      process.exit(0);
    } catch (_) {
      process.exit(1);
    }
  });
});

signalTraps.map((type) => {
  process.on(type, async () => {
    try {
      await producerService.disconnect();
    } finally {
      process.kill(process.pid, type);
      process.exit();
    }
  });
});
