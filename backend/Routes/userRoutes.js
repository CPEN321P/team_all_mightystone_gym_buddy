import express from 'express';
import { ObjectId } from 'mongodb';
import { getDB } from '../MongoDB/Connect.js';

const router = express.Router();

// ALL FUNCTIONS
// - Create a new user
// - Get all users
// - Get a specific user by id
// - Get friends by id
// - Get friend requests by id 
// - Update by id 
// - Send Friend Requests
// - Accept friend requests 
// - Decline friend requests 
// - Delete user by id

// ? search users / recommended users

// Create a new user
router.post('/', async (req, res) => {
  const db = getDB();

  const newUser = {
    name: req.body.name || "",
    username: req.body.username || "",
    phone: req.body.phone || "",
    email: req.body.email || "",
    dob: req.body.dob || "",
    pfp: req.body.pfp || "",
    friends: req.body.friends || [],
    friendRequests: req.body.friendRequests || [],
    description: req.body.description || "",
    homeGym: req.body.homeGym || "",
    reported: req.body.reported || 0
  }

  const result = await db.collection('users').insertOne(newUser);

  if (result && result.insertedId) {
    res.status(200).json(result.insertedId);
  }
  else {
    res.status(500).json("User not added to the database");
  }
});

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

// Get friends by ID
router.get('/userId/:userId/friends', async (req, res) => {
  const db = getDB();
  const id = new ObjectId(req.params.userId);

  const user = await db.collection('users').findOne({ _id: id });

  if (!user) {
    res.status(404).send('User not found');
    return;
  }

  const friendsId = user.friends;
  const friends = [];

  for (const friendId of friendsId) {
    const fid = new ObjectId(friendId);
    const friend = await db.collection('users').findOne({ _id: fid })

    if (friend) {
      friends.push(friend);
    }
  }

  res.status(200).send(friends);
});

// Get friend requests by ID
router.get('/userId/:userId/friendRequests', async (req, res) => {
  const db = getDB();
  const id = new ObjectId(req.params.userId);

  const user = await db.collection('users').findOne({ _id: id });

  if (!user) {
    res.status(404).send('User not found');
    return;
  }

  const friendsId = user.friendRequests;
  const friends = [];

  for (const friendId of friendsId) {
    const fid = new ObjectId(friendId);
    const friend = await db.collection('users').findOne({ _id: fid })

    if (friend) {
      friends.push(friend);
    }
  }

  res.status(200).send(friends);
});

// Update a user by ID
router.put('/userId/:userId', async (req, res) => {
  const db = getDB();
  const id = new ObjectId(req.params.userId);

  const user = await db.collection('users').findOne({ _id: id });

  if (!user) {
    res.status(404).send('User not found');
    return;
  }

  // add elements to be updated
  const updatedUser = {
    name: req.body.name || user.name,
    username: req.body.username || user.username,
    phone: req.body.phone || user.phone,
    email: req.body.email || user.email,
    dob: req.body.dob || user.dob,
    pfp: req.body.pfp || user.pfp,
    description: req.body.description || user.description,
    homeGym: req.body.homeGym || user.homeGym,
    reported: req.body.reported || user.reported
  }

  const result = await db.collection('users').updateOne(
    { _id: id },
    { $set: updatedUser }
  );
  
  if (result.modifiedCount === 0) {
    res.status(404).send('User not found');
  } else {
    res.status(200).json(updatedUser);
  }
});

// Send friend request
router.put('/sendFriendRequest/:senderId/:recieverId', async (req, res) => {
  const db = getDB();
  const recieverId = new ObjectId(req.params.recieverId);
  const senderId = new ObjectId(req.params.senderId);

  const recieverUser = await db.collection('users').findOne({ _id: recieverId });

  if (!recieverUser) {
    res.status(404).send('User not found');
    return;
  }

  const friendRequestList = recieverUser.friendRequests;
  const friendsList = recieverUser.friends;

  let i = -1;

  for (let j = 0; j < friendRequestList.length; j++) {
    if (friendRequestList[j] == senderId) {
      i = j;
      break;
    }
  }

  if (i != -1) {
    res.status(500).send('Friend request already sent');
    return;
  }

  for (let j = 0; j < friendsList.length; j++) {
    if (friendsList[j] == senderId) {
      i = j;
      break;
    }
  }

  if (i != -1) {
    res.status(500).send('Already friends');
    return;
  }

  friendRequestList.push(req.params.senderId);

  const result = await db.collection('users').updateOne(
    { _id: recieverId },
    { 
      $set: {
        friendRequests: friendRequestList
      } 
    }
  );
  
  if (result.modifiedCount === 0) {
    res.status(404).send('User not found');
  } else {
    res.status(200).send('Friend Request Sent');
  }
});

// Accept friend request
router.put('/acceptFriendRequest/:senderId/:recieverId', async (req, res) => {
  const db = getDB();
  const recieverId = new ObjectId(req.params.recieverId);
  const senderId = new ObjectId(req.params.senderId);

  const recieverUser = await db.collection('users').findOne({ _id: recieverId });
  const senderUser = await db.collection('users').findOne({ _id: recieverId });

  if (!recieverUser || !senderUser) {
    res.status(404).send('User not found');
    return;
  }

  const friendRequestList = recieverUser.friendRequests;
  const friendsListReciever = recieverUser.friends;
  const friendsListSender = senderUser.friends;

  let i = -1;

  for (let j = 0; j < friendRequestList.length; j++) {
    if (friendRequestList[j] == senderId) {
      i = j;
      break;
    }
  }

  if (i == -1) {
    res.status(404).send('Request not found');
    return;
  }

  friendRequestList.splice(i,1);
  friendsListReciever.push(req.params.senderId);
  friendsListSender.push(req.params.recieverId);

  const resultReciever = await db.collection('users').updateOne(
    { _id: recieverId },
    { 
      $set: {
        friendRequests: friendRequestList,
        friends: friendsListReciever
      } 
    }
  );

  const resultSender = await db.collection('users').updateOne(
    { _id: senderId },
    { 
      $set: {
        friends: friendsListSender
      } 
    }
  );
  
  if (resultReciever.modifiedCount === 0 || resultSender.modifiedCount === 0) {
    res.status(500).send('Friend request not accepted');
  } else {
    res.status(200).send('Friend Request Accepted');
  }
});

// Decline friend request
router.put('/declineFriendRequest/:senderId/:recieverId', async (req, res) => {
  const db = getDB();
  const recieverId = new ObjectId(req.params.recieverId);
  const senderId = new ObjectId(req.params.senderId);

  const recieverUser = await db.collection('users').findOne({ _id: recieverId });

  if (!recieverUser) {
    res.status(404).send('User not found');
    return;
  }

  const friendRequestList = recieverUser.friendRequests;

  let i = -1;

  for (let j = 0; j < friendRequestList.length; j++) {
    if (friendRequestList[j] == senderId) {
      i = j;
      break;
    }
  }

  if (i == -1) {
    res.status(404).send('Request not found');
    return;
  }

  friendRequestList.splice(i,1);

  const resultReciever = await db.collection('users').updateOne(
    { _id: recieverId },
    { 
      $set: {
        friendRequests: friendRequestList
      } 
    }
  );
  
  if (resultReciever.modifiedCount === 0) {
    res.status(500).send('Friend request not declined');
  } else {
    res.status(200).send('Friend Request Declined');
  }
});

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