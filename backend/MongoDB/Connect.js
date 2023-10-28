import { MongoClient } from "mongodb";

const connectDB = (uri) => {
  const client = new MongoClient(uri);

  const dbName = 'gym-buddies-db';
  const db = client.db(dbName);

  // Create collections for each database (users, chat, and schedules)
  const usersCollection = db.collection('users');
  const chatCollection = db.collection('chat');
  const schedulesCollection = db.collection('schedules');

  console.log("MongoDB Connected")
}

export default connectDB;