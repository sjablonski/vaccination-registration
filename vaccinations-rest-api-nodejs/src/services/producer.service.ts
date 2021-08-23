import kafkaConfig from "configs/kafka.config";
import { Message } from "kafkajs";

const producerService = (function () {
  const producer = kafkaConfig.getInstance().producer();

  return {
    connect: () => producer.connect(),
    disconnect: () => producer.disconnect(),
    send: (topic: string, messages: Message[]) =>
      producer.send({ topic, messages: messages }),
  };
})();

export default producerService;
