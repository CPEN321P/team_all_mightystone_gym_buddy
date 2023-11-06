import express from 'express';
import { ObjectId } from 'mongodb';
import { getDB } from '../MongoDB/Connect.js';

const router = express.Router();

// ALL REST OPERATIONS
// - Create a gym 
// - Get all gyms 
// - Get a gym by id
// - Edit a gym by id
// - Delete a gym by id 

//ChatGPT use: NO
// Create a new gym
router.post('/', async (req, res) => {
  try {
    const db = getDB();

    const newGym = {
      name: req.body.name || "",
      description: req.body.description || "",
      location: req.body.location || "",
      phone: req.body.phone || "",
      email: req.body.email || "",
      images: req.body.images || []
      // hours
    }

    const result = await db.collection('gyms').insertOne(newGym);

    if (result && result.insertedId) {
      res.status(200).json(result.insertedId.toString());
    }
    else {
      res.status(500).json("Gym not added to the database");
    }
  } catch (error) {
    res.status(500).json("Gym not added to the database");
  }
});

//ChatGPT use: NO
// Get all gyms
router.get('/', async (req, res) => {
  try {
    const db = getDB();

    const gyms = await db.collection('gyms').find().toArray();

    if (gyms) {
      res.status(200).json(gyms);
    } else {
      res.status(404).json("No gyms found");
    }
  } catch (error) {
    res.status(404).json("No gyms found");
  }
});

//ChatGPT use: NO
// Get a gym by id
router.get('/gymId/:gymId', async (req, res) => {
  try {
    const db = getDB();
    const id = new ObjectId(req.params.gymId);

    const gym = await db.collection('gyms').findOne({ _id: id });

    if (gym) {
      res.status(200).json(gym);
    } else {
      res.status(404).send('Gym not found');
    }
  } catch (error) {
    res.status(404).send('Gym not found');
  }
});

//ChatGPT use: NO
// Edit a gym by ID
router.put('/gymId/:gymId', async (req, res) => {
  try {
    const db = getDB();
    const id = new ObjectId(req.params.gymId);

    const gym = await db.collection('gyms').findOne({ _id: id });

    if (!gym) {
      res.status(404).send('Gym not found');
      return;
    }

    const updatedGym = {
      name: req.body.name || gym.name,
      description: req.body.description || gym.description,
      location: req.body.location || gym.location,
      phone: req.body.phone || gym.phone,
      email: req.body.email || gym.email,
      images: req.body.images || gym.images,
      // hours
    }

    const result = await db.collection('gyms').updateOne(
      { _id: id },
      { $set: updatedGym }
    );
    
    if (result.matchedCount == 0) {
      res.status(404).send('Gym not found');
    } else {
      res.status(200).json(updatedGym);
    }
  } catch (error) {
    res.status(500).send('Gym not updated');
  }
});

//ChatGPT use: NO
// Delete a user by ID
router.delete('/gymId/:gymId', async (req, res) => {
  try {
    const db = getDB();
    const id = new ObjectId(req.params.gymId);
    
    const result = await db.collection('gyms').deleteOne({ _id: id });

    if (result.deletedCount === 0) {
      res.status(404).send('Gym not found');
    } else {
      res.status(200).send('Gym deleted successfully');
    }
  } catch (error) {
    res.status(500).send('Gym not deleted');
  }
});

export default router;