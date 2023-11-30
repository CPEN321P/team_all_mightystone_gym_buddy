const express = require('express');
const { getDB } = require('../MongoDB/Connect.js');
const { createId } = require('../Utils/mongoUtils.js');
const { clearData, unfriend, getSimilarity } = require('../Utils/userUtils.js')

const router = express.Router();

// ALL REST OPERATIONS
// - Create a new user
// - Get all users
// - Get a specific user by id
// - Get specific user by email
// - Get recommended users by ID
// - Get friends by id
// - Get friend requests by id 
// - Get blocked users
// - Update by id 
// - Add friend (for MVP only)
// - Send Friend Requests
// - Unsend friend request
// - Accept friend requests 
// - Decline friend requests
// - Unfriend 
// - Block users 
// - Unblock users 
// - Delete chat
// - Delete user by id
// - Delete all users

//ChatGPT use: NO
// Create a new user
router.post('/', async (req, res) => {
  const db = getDB();
  const newUser = {
    name: req.body.name || "",
    phone: req.body.phone || "",
    email: req.body.email || "",
    age: req.body.age || "",
    gender: req.body.gender || "",
    weight: req.body.weight || "",
    pfp: req.body.pfp || "",
    friends: req.body.friends || [],
    friendRequests: req.body.friendRequests || [],
    description: req.body.description || "",
    homeGym: req.body.homeGym || "",
    reported: req.body.reported || 0,
    chats: req.body.chats || [],
    blockedUsers: req.body.blockedUsers || []
  }

  const result = await db.collection('users').insertOne(newUser);
  if (result && result.insertedId) {
    res.status(200).json(result.insertedId.toString());
    return;
  }
  else {
    res.status(500).json("User not added to the database");
  }
});

//ChatGPT use: NO
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

//ChatGPT use: NO
// Get a specific user by ID
router.get('/userId/:userId', async (req, res) => {
  const db = getDB();

  let id;

  try {
    id = createId(req.params.userId);
  } catch (error) {
    res.status(500).json('Invalid user ID');
    return;
  }

  const user = await db.collection('users').findOne({ _id: id });

  if (user) {
    res.status(200).json(user);
  } else {
    res.status(404).json('User not found');
  }
});

//ChatGPT use: NO
// Get a specific user by email
router.get('/userEmail/:userEmail', async (req, res) => {
  const db = getDB();
  const userEmail = req.params.userEmail;

  const user = await db.collection('users').findOne({ email: userEmail });
  if (user) {
    res.status(200).json(user);
  } else {
    res.status(404).json('User not found');
  }
});

//ChatGPT use: YES
// Get recommended users by ID
router.get('/userId/:userId/recommendedUsers', async (req, res) => {
  const db = getDB();
  var myHomeGym;
  var user;
  var id
  
  try {    
    id = createId(req.params.userId);
  } catch (error) {
    res.status(500).json('Users not retrieved');
    return;
  }

  user = await db.collection('users').findOne({ _id: id });

  if (!user) {
    res.status(404).json('User not found');
    return;
  } 

  myHomeGym = user.homeGym;
  const recommendedUsers = await db.collection('users').find({}).toArray();
  
  const filteredRecommendedUsers = recommendedUsers.filter(recommendedUser => {
    return ((id.toString() !== recommendedUser._id.toString()) && (!user.friends.includes(recommendedUser._id.toString()))  && (!user.blockedUsers.includes(recommendedUser._id.toString())) && (!recommendedUser.blockedUsers.includes(user._id.toString())));
  });

  filteredRecommendedUsers.forEach(rec_user => {
    var similarity = getSimilarity(user, rec_user);
  });

  filteredRecommendedUsers.sort((userA, userB) => getSimilarity(user, userB) - getSimilarity(user, userA));
  res.status(200).json(filteredRecommendedUsers);
});

//ChatGPT use: NO
// Get friends by ID
router.get('/userId/:userId/friends', async (req, res) => {
  const db = getDB();
  var friendsId;
  let id;

  try {
    id = createId(req.params.userId);
  } catch (error) {
    res.status(500).json('Invalid user Id');
    return;
  }

  const user = await db.collection('users').findOne({ _id: id });

  if (!user) {
    res.status(404).json('User not found');
    return;
  }
  friendsId = user.friends;

  const friends = [];

  for (const friendId of friendsId) {
    const fid = createId(friendId);
    const friend = await db.collection('users').findOne({ _id: fid })

    if (friend) {
      friends.push(friend);
    }
  }

  res.status(200).json(friends);
});

//ChatGPT use: NO
// Get blocked users by ID
router.get('/userId/:userId/blockedUsers', async (req, res) => {
  const db = getDB();
  let id;

  try {
    id = createId(req.params.userId);
  } catch (error) {
    res.status(500).json('Invalid user Id');
    return;
  }

  const user = await db.collection('users').findOne({ _id: id });

  if (!user) {
    res.status(404).json('User not found');
    return;
  }

  const blockedUsersId = user.blockedUsers;
  const blockedUsers = [];

  for (const blockedUserId of blockedUsersId) {
    const buid = createId(blockedUserId);
    const blockedUser = await db.collection('users').findOne({ _id: buid })

    if (blockedUser) {
      blockedUsers.push(blockedUser);
    }
  }

  res.status(200).json(blockedUsers);
});

//ChatGPT use: NO
// Update a user by ID
router.put('/userId/:userId', async (req, res) => {
  const db = getDB();
  let id;

  try {
    id = createId(req.params.userId);
  } catch (error) {
    res.status(500).json('Invalid user Id');
    return;
  }

  const user = await db.collection('users').findOne({ _id: id });

  if (!user) {
    res.status(404).json('User not found');
    return;
  }

  // add elements to be updated
  const updatedUser = {
    name: req.body.name || user.name,
    phone: req.body.phone || user.phone,
    email: req.body.email || user.email,
    age: req.body.age || user.age,
    gender: req.body.gender || user.gender,
    weight: req.body.weight || user.weight,
    pfp: req.body.pfp || user.pfp,
    friends: req.body.friends || user.friends,
    friendRequests: req.body.friendRequests || user.friendRequests,
    description: req.body.description || user.description,
    homeGym: req.body.homeGym || user.homeGym,
    reported: req.body.reported || user.reported,
    chats: req.body.chats || user.chats,
    blockedUsers: req.body.blockedUsers || user.blockedUsers
  }

  const result = await db.collection('users').updateOne(
    { _id: id },
    { $set: updatedUser }
  );
  
  if (result.matchedCount === 0) {
    res.status(404).json('User not updated');
  } else {
    res.status(200).json(updatedUser);
  }
});

//ChatGPT use: PARTIAL
//Add Friend. ONLY FOR MVP, TO BE CHANGED LATER
router.put('/addFriend/:senderId/:recieverId', async (req, res) => {
  const db = getDB();
  let recieverId;
  let senderId;

  try {
    recieverId = createId(req.params.recieverId);
    senderId = createId(req.params.senderId);
  } catch (error) {
    res.status(500).json('Invalid user Id');
    return;
  }

  const recieverUser = await db.collection('users').findOne({ _id: recieverId });
  const senderUser = await db.collection('users').findOne({ _id: senderId });

  if (!recieverUser || !senderUser) {
    res.status(404).json('User not found');
    return;
  }

  const receiverFriends = recieverUser.friends;
  const senderFriends = senderUser.friends;

  if(receiverFriends.indexOf(senderId) == -1){
    receiverFriends.push(senderId.toString());
    senderFriends.push(recieverId.toString());
  }
  else{
    res.status(500).json('Already friends');
    return;
  }

  const result = await db.collection('users').updateOne(
    { _id: recieverId },
    { 
      $set: {
        friends: receiverFriends
      } 
    }
  );
  const result2 = await db.collection('users').updateOne(
    { _id: senderId },
    { 
      $set: {
        friends: senderFriends
      } 
    }
  );
  res.status(200).json('Friend added');
});

//ChatGPT use: NO
// Block user
router.put('/blockUser/:blockerId/:blockedId', async (req, res) => {
  const db = getDB();
  let blockerId
  try {
    blockerId = createId(req.params.blockerId);
  } catch (error) {
    res.status(500).json('Invalid user ID');
    return;
  }

    const blockerUser = await db.collection('users').findOne({ _id: blockerId });

    if (!blockerUser) {
      res.status(404).json('User not found');
      return;
    }

    const r = await unfriend(db, req.params.blockerId, req.params.blockedId);

    console.log("unfriend: " + r)

    const blockedUsersList = blockerUser.blockedUsers;
    blockedUsersList.push(req.params.blockedId);

    const result = await db.collection('users').updateOne(
      { _id: blockerId },
      { 
        $set: {
          blockedUsers: blockedUsersList
        } 
      }
    );
    
    if (result.matchedCount === 0) {
      res.status(500).json('User not blocked');
    } else {
      res.status(200).json('User blocked');
    }
});

//ChatGPT use: NO
// Unblock user
router.put('/unblockUser/:blockerId/:blockedId', async (req, res) => {
  const db = getDB();
  let blockerId;

  try {
    blockerId = createId(req.params.blockerId);
  } catch (error) {
    res.status(500).json('Invalid user ID');
    return;
  }

  const blockerUser = await db.collection('users').findOne({ _id: blockerId });

  if (!blockerUser) {
    res.status(404).json('User not found');
    return;
  }

  const blockedUsersList = blockerUser.blockedUsers;

  let i = -1;

  for (let j = 0; j < blockedUsersList.length; j++) {
    if (blockedUsersList[j] == req.params.blockedId) {
      i = j;
      break;
    }
  }

  if (i == -1) {
    res.status(500).json('User not in blocked list');
    return;
  }

  blockedUsersList.splice(i,1);

  const result = await db.collection('users').updateOne(
    { _id: blockerId },
    { 
      $set: {
        blockedUsers: blockedUsersList
      } 
    }
  );
  
  if (result.matchedCount === 0) {
    res.status(500).json('User not unblocked');
  } else {
    res.status(200).json('User unblocked');
  }
});

//ChatGPT use: NO
// Delete a chat
router.put('/userId/:userId/deleteChat/:chatId', async (req, res) => {
  const db = getDB();
  let userId;

  try {
    userId = createId(req.params.userId);
  } catch (error) {
    res.status(500).json('Invalid user ID');
    return;
  }    

  const user = await db.collection('users').findOne({ _id: userId });

  if (!user) {
    res.status(404).json('User not found');
    return;
  }

  const chatsLists = user.chats;

  let i = -1;

  for (let j = 0; j < chatsLists.length; j++) {
    if (chatsLists[j] == req.params.chatId) {
      i = j;
      break;
    }
  }

  if (i == -1) {
    res.status(404).json('Chat not in list');
    return;
  }

  chatsLists.splice(i,1);

  const result = await db.collection('users').updateOne(
    { _id: userId },
    { 
      $set: {
        chats: chatsLists
      } 
    }
  );
  
  if (result.matchedCount === 0) {
    res.status(500).json('Chat not deleted');
  } else {
    res.status(200).json('Chat deleted');
  }
});

//ChatGPT use: NO
// Delete a user by ID
router.delete('/userId/:userId', async (req, res) => {
  const db = getDB();
  let _id;

  try {
    _id = createId(req.params.userId);
  } catch (error) {
    res.status(500).json('Invalid user Id');
    return;
  }

  const resultClearData = await clearData(db, _id);

  if (!resultClearData) {
    res.status(500).json('User data not cleared');
    return;
  }

  const result = await db.collection('users').deleteOne({ _id: _id });

  if (result.deletedCount === 0) {
    res.status(404).json('User not deleted');
  } else {
    res.status(200).json('User deleted');
  }
});

// Delete all users
// This is for debugging only (DEV USE)
router.delete('/', async (req, res) => {
  const db = getDB();

  await db.collection('users').deleteMany({});
  
  res.status(200).json('Users Deleted');
});

module.exports = router;
