import express from 'express';

const router = express.Router();

const schedulesCollection = {};

// Get all gym schedules
router.get('/', async (req, res) => {
  const schedules = await schedulesCollection.find().toArray();
  res.json(schedules);
});

// Create a new gym schedule
router.post('/', async (req, res) => {
  const newSchedule = req.body;
  const result = await schedulesCollection.insertOne(newSchedule);
  res.json(result.ops[0]);
});

// Get a specific gym schedule by ID
router.get('/:scheduleId', async (req, res) => {
  const schedule = await schedulesCollection.findOne({ _id: ObjectId(req.params.scheduleId) });
  if (!schedule) {
    res.status(404).send('Schedule not found');
    return;
  }
  res.json(schedule);
});

// Update a gym schedule by ID
router.put('/:scheduleId', async (req, res) => {
  const updatedSchedule = req.body;
  const result = await schedulesCollection.updateOne(
    { _id: ObjectId(req.params.scheduleId) },
    { $set: updatedSchedule }
  );

  if (result.modifiedCount === 0) {
    res.status(404).send('Schedule not found');
    return;
  }

  res.json(updatedSchedule);
});

// Delete a gym schedule by ID
router.delete('/:scheduleId', async (req, res) => {
  const result = await schedulesCollection.deleteOne({ _id: ObjectId(req.params.scheduleId) });

  if (result.deletedCount === 0) {
    res.status(404).send('Schedule not found');
    return;
  }

  res.send('Schedule deleted successfully');
});

export default router;