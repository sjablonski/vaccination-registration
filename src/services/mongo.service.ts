import mongoConfig from "configs/mongo.config";

const mongoService = (function () {
  const client = mongoConfig.getInstance();

  return {
    connect: () => client.connect(),
    disconnect: () => client.close(),
    createCollection: async (name: string) => {
      const db = mongoService.db();
      const isExist = await (
        await db.listCollections({}, { nameOnly: true }).toArray()
      ).some((collection) => collection.name === name);

      if (!isExist) {
        db.createCollection(name, (err) => {
          if (err) throw err;
          console.log("Vaccinations collection created!");
        });
      }
    },
    db: (databaseName?: string) =>
      client.db(databaseName || mongoConfig.getDatabaseName()),
  };
})();

export default mongoService;
