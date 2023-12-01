const express = require('express');
const { getDB } = require('../MongoDB/Connect.js');

const router = express.Router();

// ALL FUNCTIONS 
// - Create a schedule 
// - Get a schedule by user and date
// - Get all schedules by user
// - Get a schedule by id 
// - Update a schedule by id 
// - Delete a schedule by id
// - Delete all 

//ChatGPT use: NO
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
    res.status(500).json("Schedule not added to the database");
  }
});

//ChatGPT use: NO
// Get schedule by user id and date
router.get('/byUser/:userId/:date', async (req, res) => {
  const db = getDB();
  const userId = req.params.userId;
  const date = req.params.date

  const schedule = await db.collection('schedules').findOne({
    $and: [
      {
        userId
      },
      {
        date
      }
    ] 
  });

  if (schedule) {
    res.status(200).json(schedule);
  }
  else {
    res.status(404).json('No Schedule Found');
  }
});

//ChatGPT use: NO
// Get all schedules by user id
router.get('/byUser/:userId', async (req, res) => {
  const db = getDB();
  const id = req.params.userId;

  const schedules = await db.collection('schedules').find({ userId: id }).toArray();

  if (schedules) {
    res.status(200).json(schedules);
  }
  else {
    res.status(404).json("Schedules not retrieved");
  }
});

//ChatGPT use: NO
// Update a gym schedule by user ID and date
router.put('/byUser/:userId/:date', async (req, res) => {
  const db = getDB();
  const userId = req.params.userId;
  const date = req.params.date

  const schedule = await db.collection('schedules').findOne({
    $and: [
      {
        userId
      },
      {
        date
      }
    ] 
  });

  if (!schedule) {
    res.status(404).json('Schedule not found');
    return;
  }

  const id = schedule._id;

  const updatedSchedule = {
    userId: req.body.userId || schedule.userId,
    date: req.body.date || schedule.date,
    exercises: req.body.exercises || schedule.exercises
  };

  const result = await db.collection('schedules').updateOne(
    { _id: id },
    { $set: updatedSchedule }
  );

  if (result.matchedCount === 0) {
    res.status(500).json('Schedule not updated');
  } else {
    res.status(200).json(updatedSchedule);
  }
});

module.exports = router;