import { Kafka } from "kafkajs";
import ip from "ip";

const kafkaConfig = (function () {
  let instance: Kafka;
  const host = process.env.HOST_IP || ip.address();

  const createInstance = () => {
    return new Kafka({
      clientId: "rest-api",
      brokers: [`${host}:9092`],
    });
  };

  return {
    getInstance: () => {
      if (!instance) {
        instance = createInstance();
      }
      return instance;
    },
  };
})();

export default kafkaConfig;
