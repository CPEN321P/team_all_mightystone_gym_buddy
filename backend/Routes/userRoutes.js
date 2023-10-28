import express from 'express';

const router = express.Router();

const usersCollection = {};

// Get all users
router.get('/', async (req, res) => {
  const users = await usersCollection.find().toArray();
  res.json(users);
});

// Create a new user
router.post('/', async (req, res) => {
  const newUser = req.body;
  const result = await usersCollection.insertOne(newUser);
  res.json(result.ops[0]);
});

// Get a specific user by ID
router.get('/:userId', async (req, res) => {
  const user = await usersCollection.findOne({ _id: ObjectId(req.params.userId) });
  if (!user) {
    res.status(404).send('User not found');
    return;
  }
  res.json(user);
});

// Update a user by ID
router.put('/:userId', async (req, res) => {
  const updatedUser = req.body;
  const result = await usersCollection.updateOne(
    { _id: ObjectId(req.params.userId) },
    { $set: updatedUser }
  );

  if (result.modifiedCount === 0) {
    res.status(404).send('User not found');
    return;
  }

  res.json(updatedUser);
});

// Delete a user by ID
router.delete('/:userId', async (req, res) => {
  const result = await usersCollection.deleteOne({ _id: ObjectId(req.params.userId) });

  if (result.deletedCount === 0) {
    res.status(404).send('User not found');
    return;
  }

  res.send('User deleted successfully');
});

export default router;