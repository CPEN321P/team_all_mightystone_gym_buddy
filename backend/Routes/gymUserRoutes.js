const express = require('express');
const { ObjectId } = require('mongodb');
const { getDB } = require('../MongoDB/Connect.js');

const router = express.Router();

// ALL REST OPERATIONS
// - Create a new gym user 
// - Get all gym users
// - Get a specific gym user by id 
// - Get a specific gym user by email
// - Make an Announcement (test)
// - Edit an Announcement (do)
// - Delete an Announcement (do)
// - Update by id
// - Delete user by id

//ChatGPT use: NO
// Create a new gym user
router.post('/', async (req, res) => {
  const db = getDB();

  const newGymUser = {
    name: req.body.name || "",
    username: req.body.username || "",
    gymId: req.body.gymId || "",
    phone: req.body.phone || "",
    email: req.body.email || "",
    pfp: req.body.pfp || "",
    description: req.body.description || "",
    reported: req.body.reported || 0,
    announcements: req.body.announcements || [],
  }

  const result = await db.collection('gymUsers').insertOne(newGymUser);

  if (result && result.insertedId) {
    res.status(200).json(result.insertedId.toString());
  }
  else {
    res.status(500).json("Gym user not added to the database");
  }
});

//ChatGPT use: NO
// Get all gym users
router.get('/', async (req, res) => {
  const db = getDB();

  const gymUsers = await db.collection('gymUsers').find().toArray();

  if (gymUsers) {
    res.status(200).json(gymUsers);
  } else {
    res.status(500).json("Could not retrieve data from the database");
  }
});

//ChatGPT use: NO
// Get a specific gym user by ID
router.get('/userId/:userId', async (req, res) => {
  const db = getDB();
  let _id;

  try {
    _id = new ObjectId(req.params.userId);
  } catch (error) {
    res.status(500).send("Invalid Id");
    return;
  }

  const gymUser = await db.collection('gymUsers').findOne({ _id });
  if (gymUser) {
    res.status(200).json(gymUser);
  } else {
    res.status(404).send('Gym user not found');
  }
});

//ChatGPT use: NO
// Get a specific gym user by email
router.get('/userEmail/:userEmail', async (req, res) => {
  const db = getDB();
  const userEmail = req.params.userEmail;

  const gymUser = await db.collection('gymUsers').findOne({ email: userEmail });
  if (gymUser) {
    res.status(200).json(gymUser);
  } else {
    res.status(404).send('User not found');
  }
});

//ChatGPT use: NO
// Make an announcement
router.put('/makeAnnouncement/:userId', async (req, res) => {
  const db = getDB();
  const announcement = req.body;
  let _id;
  let announcementId;

  try {
    _id = new ObjectId(req.params.userId);
    announcementId = new ObjectId();
  } catch (error) {
    res.status(500).send('Announcement not made');
    return;
  }

  const gymUser = await db.collection('gymUsers').findOne({ _id });

  if (!gymUser) {
    res.status(404).send('Gym user not found');
    return;
  }

  const announcementsList = gymUser.announcements;
  announcementsList.push({
    _id: announcementId,
    header: announcement.header,
    body: announcement.body
  });

  const result = await db.collection('gymUsers').updateOne(
    { _id },
    { 
      $set: {
        announcements: announcementsList
      } 
    }
  );

  if (result.matchedCount === 0) {
    res.status(500).send('Announcement not made');
  } else {
    res.status(200).json(announcementsList);
  }
});

//ChatGPT use: NO
// Edit an announcement
router.put('/editAnnouncement/:userId/:announcementId', async (req, res) => {
  const db = getDB();
  let id;

  try {
    id = new ObjectId(req.params.userId);
  } catch (error) {
    res.status(500).send('Announcement not updated');
    return;
  }

  const announcementId = req.params.announcementId;
  const announcement = req.body;

  const gymUser = await db.collection('gymUsers').findOne({ _id: id });

  if (!gymUser) {
    res.status(404).send('Gym user not found');
    return;
  }

  const announcements = gymUser.announcements;
  
  let i = -1;
  for (let j = 0; j < announcements.length; j++) {
    if (announcements[j]._id == announcementId) {
      i = j;
      break;
    }
  }

  if (i == -1) {
    res.status(404).send('Announcement not found');
    return;
  }

  announcements[i].header = announcement.header;
  announcements[i].body = announcement.body;

  const result = await db.collection('gymUsers').updateOne(
    { _id: id },
    { 
      $set: {
        announcements
      } 
    }
  );

  if (result.matchedCount === 0) {
    res.status(500).send('Announcement not updated');
  } else {
    res.status(200).json(announcements);
  }
});

//ChatGPT use: NO
// Delete an announcement
router.put('/deleteAnnouncement/:userId/:announcementId', async (req, res) => {
  const db = getDB();
  let id;
  
  try {
    id = new ObjectId(req.params.userId);
  } catch (error) {
    res.status(500).send('Announcement not deleted');
    return;
  }

  const announcementId = req.params.announcementId;

  const gymUser = await db.collection('gymUsers').findOne({ _id: id });

  if (!gymUser) {
    res.status(404).send('Gym user not found');
    return;
  }

  const announcements = gymUser.announcements;
  
  let i = -1;
  for (let j = 0; j < announcements.length; j++) {
    if (announcements[j]._id == announcementId) {
      i = j;
      break;
    }
  }

  if (i == -1) {
    res.status(404).send('Announcement not found');
    return;
  }

  announcements.splice(i,1);

  const result = await db.collection('gymUsers').updateOne(
    { _id: id },
    { 
      $set: {
        announcements
      } 
    }
  );

  if (result.matchedCount === 0) {
    res.status(500).send('Announcement not deleted');
  } else {
    res.status(200).send('Announcement deleted');
  }
});

//ChatGPT use: NO
// Update a gym user by ID
router.put('/userId/:userId', async (req, res) => {
  const db = getDB();
  let id;

  try {
    id = new ObjectId(req.params.userId);
  } catch (error) {
    res.status(500).send('Gym user updated');
    return;
  }

  const gymUser = await db.collection('gymUsers').findOne({ _id: id });

  if (!gymUser) {
    res.status(404).send('Gym user not found');
    return;
  }

  const updatedGymUser = {
    name: req.body.name || gymUser.name,
    username: req.body.username || gymUser.username,
    gymId: req.body.gymId || gymUser.gymId,
    phone: req.body.phone || gymUser.phone,
    email: req.body.email || gymUser.email,
    pfp: req.body.pfp || gymUser.pfp,
    description: req.body.description || gymUser.description,
    reported: req.body.reported || gymUser.reported,
    announcements: req.body.announcements || gymUser.announcements
  }

  const result = await db.collection('gymUsers').updateOne(
    { _id: id },
    { $set: updatedGymUser }
  );
  
  if (result.matchedCount === 0) {
    res.status(404).send('Gym user not found');
  } else {
    res.status(200).json(updatedGymUser);
  }
});

//ChatGPT use: NO
// Delete a gym user by ID
router.delete('/userId/:userId', async (req, res) => {
  const db = getDB();
  let id;

  try {
    id = new ObjectId(req.params.userId);
  } catch (error) {
    res.status(500).send('Gym user deleted');
    return;
  }

    const result = await db.collection('gymUsers').deleteOne({ _id: id });

    if (result.deletedCount === 0) {
      res.status(404).send('Gym user not found');
    } else {
      res.status(200).send('Gym user deleted successfully');
    }
});

//ChatGPT use: YES
// Delete all gym users
//This is for debugging only (DEV USE)
router.delete('/', async (req, res) => {
  try {
    const db = getDB();
    await db.collection('gymUsers').deleteMany({});
    res.status(200).send('Users deleted successfully');
  } catch (error) {
    res.status(500).send('All Users Not Deleted');
  }
});

module.exports = router;