import express from 'express';
import { ObjectId } from 'mongodb';
import { getDB } from '../MongoDB/Connect.js';

const router = express.Router();

// ALL FUNCTIONS (Test and go over all)
// - Create a schedule (check elements)
// - Get a schedule by user and date 
// - Get a schedule by id 
// - Update a schedule by id (check elements)
// - Delete a schedule by id

// Create a new gym schedule
router.post('/', async (req, res) => {
  const db = getDB();

  const newSchedule = req.body;

  const result = await db.collection('schedules').insertOne(newSchedule);

  if (result && result.insertedId) {
    res.status(200).json(result.insertedId.toString());
  }
  else {
    res.status(500).json("User not added to the database");
  }
});

// Get schedule by user id and date
router.get('/byUser/:userId/:date', async (req, res) => {
  const db = getDB();

  const schedule = await db.collection('schedules').findOne({ 
    user_id: req.params.userId,
    date: parseInt(req.params.date)
  });

  if (schedule) {
    res.status(200).json(schedule);
  }
  else {
    res.status(404).json("No Schedule Found");
  }
});

// Get a specific gym schedule by ID
router.get('/byId/:scheduleId', async (req, res) => {
  const db = getDB();
  const id = new ObjectId(req.params.scheduleId);

  const schedule = await db.collection('schedules').findOne({ 
    _id: id,
  });

  if (schedule) {
    res.status(200).json(schedule);
  }
  else {
    res.status(404).json("No Schedule Found");
  }
});

// // Update a gym schedule by ID
// router.put('/byId/:scheduleId', async (req, res) => {
//   const db = getDB();

//   const updatedSchedule = req.body;
//   const result = await db.collection('schedules').updateOne(
//     { _id: ObjectId(req.params.scheduleId) },
//     { $set: updatedSchedule }
//   );

//   if (result.matchedCount == 0) {
//     res.status(404).send('Schedule not found');
//     return;
//   }

//   res.json(updatedSchedule);
// });

// Delete a gym schedule by ID
router.delete('/byId/:scheduleId', async (req, res) => {
  const db = getDB();
  const id = new ObjectId(req.params.scheduleId);

  const result = await db.collection('schedules').deleteOne({ 
    _id: id,
  });

  if (result.deletedCount === 0) {
    res.status(404).send('Schedule not found');
  } else {
    res.status(200).send('Schedule deleted successfully');
  }
});

export default router;