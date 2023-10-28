import express from 'express';
import { ObjectId } from 'mongodb';
import { getDB } from '../MongoDB/Connect.js';

const router = express.Router();

// Get all users
router.get('/', async (req, res) => {
  const db = getDB();

  const users = await db.collection('users').find().toArray();

  if (users) {
    res.status(200).json(users);
  } else {
    res.status(500).json("Could not retrieve data from the database");
  }
});

// Create a new user
router.post('/', async (req, res) => {
  const db = getDB();

  // Add element checks

  const newUser = req.body;
  const result = await db.collection('users').insertOne(newUser);

  if (result && result.insertedId) {
    res.status(200).json(result.insertedId);
  }
  else {
    res.status(500).json("User not added to the database");
  }
});

// Get a specific user by ID
router.get('/userId/:userId', async (req, res) => {
  const db = getDB();
  const id = new ObjectId(req.params.userId);

  const user = await db.collection('users').findOne({ _id: id });
  if (user) {
    res.status(200).send(user);
  } else {
    res.status(404).send('User not found');
  }
});

// // Update a user by ID
// router.put('/userId/:userId', async (req, res) => {
//   const db = getDB();
//   const id = new ObjectId(req.params.userId);

//   // add elements to be updated
  
//   const updatedUser = req.body;

//   const result = await db.collection('users').updateOne(
//     { _id: id },
//     { $set: updatedUser }
//   );
  
//   if (result.modifiedCount === 0) {
//     res.status(404).send('User not found');
//   } else {
//     res.status(200).json(updatedUser);
//   }
// });

// Delete a user by ID
router.delete('/userId/:userId', async (req, res) => {
  const db = getDB();
  const id = new ObjectId(req.params.userId);
  
  const result = await db.collection('users').deleteOne({ _id: id });

  if (result.deletedCount === 0) {
    res.status(404).send('User not found');
  } else {
    res.status(200).send('User deleted successfully');
  }
});

export default router;