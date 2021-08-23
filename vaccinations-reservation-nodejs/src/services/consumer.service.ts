import kafkaConfig from "configs/kafka.config";

const consumerService = (function () {
  const consumer = kafkaConfig
    .getInstance()
    .consumer({ groupId: "backend-group" });

  return {
    connect: () => consumer.connect(),
    disconnect: () => consumer.disconnect(),
    consumer,
  };
})();

export default consumerService;
