import { Db, MongoClient } from "mongodb";

const mongoConfig = (function () {
  let instance: MongoClient;
  const config = {
    user: "<test>",
    password: "<test>",
    host: "localhost",
    port: 2717,
    database: "vaccinationsDb",
    clientURI() {
      return `mongodb://${this.user}:${this.password}@${this.host}:${this.port}/${this.database}`;
    },
    getDatabaseName() {
      return this.database;
    },
  };

  const createInstance = () => {
    return new MongoClient(config.clientURI(), {
      useUnifiedTopology: true,
    });
  };

  return {
    getInstance: () => {
      if (!instance) {
        instance = createInstance();
      }
      return instance;
    },
    getDatabaseName: config.getDatabaseName,
  };
})();

export default mongoConfig;
