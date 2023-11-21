const MongoClient =  require("mongodb");

const dbName = 'gym-buddies-db';
let db;

const connectDB = (uri) => {
  MongoClient.connect(uri)
    .then((client) => {
      console.log("MongoDB Connected");
      db = client.db(dbName);
    })
    .catch(err => {
      console.log(err)
    })
}

const getDB = () => {
  return db;
}

module.exports = {
  connectDB,
  getDB
};