import express from 'express';
import { ObjectId } from 'mongodb';
import { getDB } from '../MongoDB/Connect.js';

const router = express.Router();

// ALL FUNCTIONS (Test and go over all)
// - Create a schedule 
// - Get a schedule by user and date
// - Get a schedule by id 
// - Update a schedule by id 
// - Delete a schedule by id

// Create a new gym schedule
router.post('/', async (req, res) => {
  const db = getDB();

  const newSchedule = {
    userId: req.body.userId || "",
    date: req.body.date || "",
    exercises: req.body.exercises || []
  };

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
  const userId = req.params.userId;
  const date = req.params.date

  const schedule = await db.collection('schedules').findOne({
    $and: [
      {
        userId: userId
      },
      {
        date: date
      }
    ] 
  });

  if (schedule) {
    res.status(200).json(schedule);
  }
  else {
    res.status(404).json("No Schedule Found");
  }
});

router.get('/byUser/:userId', async (req, res) => {
  const db = getDB();
  const userId = req.params.userId;

  const schedule = await db.collection('schedules').find({userId: userId}).toArray();

  if (schedule) {
    res.status(200).json(schedule);
  }
  else {
    res.status(404).json("No Schedule Found");
  }
});

router.put('/byUser/:userId', async (req, res) => {
  const db = getDB();
  const userId = req.params.userId;


  const schedule = await db.collection('schedules').find({userId: userId}).toArray();
  const _id = schedule._id;
  if (schedule) {
    const updatedSchedule = {
      userId: req.body.userId || schedule.userId,
      date: req.body.date || schedule.date,
      exercises: req.body.exercises || schedule.exercises
    };
    const result = await db.collection('schedules').updateOne(
      { _id: id },
      { $set: updatedSchedule }
    );
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

// Update a gym schedule by ID
router.put('/byId/:scheduleId', async (req, res) => {
  const db = getDB();
  const id = new ObjectId(req.params.scheduleId);

  const schedule = await db.collection('schedules').findOne({ _id: id });

  if (!schedule) {
    res.status(404).send('Schedule not found');
    return;
  }

  const updatedSchedule = {
    userId: req.body.userId || schedule.userId,
    date: req.body.date || schedule.date,
    exercises: req.body.exercises || schedule.exercises
  };

  const result = await db.collection('schedules').updateOne(
    { _id: id },
    { $set: updatedSchedule }
  );

  if (result.matchedCount == 0) {
    res.status(404).send('Schedule not found');
  } else {
    res.status(200).json(updatedSchedule);
  }
});

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

router.delete('/byUser/:userId', async (req, res) => {
  const db = getDB();
  const userId = new ObjectId(req.params.userId);

  const result = await db.collection('schedules').deleteOne({ 
    userId: userId,
  });

  if (result.deletedCount === 0) {
    res.status(404).send('Schedule not found');
  } else {
    res.status(200).send('Schedule deleted successfully');
  }
});

router.delete('/', async (req, res) => {
  const db = getDB();
  
  const result = await db.collection('schedules').deleteMany({});
  res.status(200).send('Schedules deleted successfully');
});

export default router;