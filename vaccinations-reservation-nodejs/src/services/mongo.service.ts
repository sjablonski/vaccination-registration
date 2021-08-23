import mongoConfig from "configs/mongo.config";
import Patient from "models/Patient.model";
import Vaccination from "models/Vaccination.model";

const mongoService = (function () {
  const client = mongoConfig.getInstance();

  const db = (databaseName?: string) =>
    client.db(databaseName || mongoConfig.getDatabaseName());

  const createCollection = async (name: string) => {
    const isExist = await (
      await db().listCollections({}, { nameOnly: true }).toArray()
    ).some((collection) => collection.name === name);

    if (!isExist) {
      db().createCollection(name, (err) => {
        if (err) throw err;
        console.log("Vaccinations collection created!");
      });
    }
  };

  const findOneFree = async () =>
    (await db()
      .collection("vaccination")
      .findOne({ $where: "this.patients.length < 3" })) as Vaccination | null;

  const updateOne = async (id: string, patient: Patient) =>
    db()
      .collection("vaccination")
      .updateOne({ id }, { $addToSet: { patients: patient } });

  return {
    connect: () => client.connect(),
    disconnect: () => client.close(),
    db,
    createCollection,
    findOneFree,
    updateOne,
  };
})();

export default mongoService;
