import express from 'express';
import { ObjectId } from 'mongodb';
import { getDB } from '../MongoDB/Connect.js';

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
  try {
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
      blockedUsers: req.body.blockedUsers || [],
      getChats: 0
    }

    const result = await db.collection('users').insertOne(newUser);

    if (result && result.insertedId) {
      res.status(200).json(result.insertedId.toString());
    }
    else {
      res.status(500).json("User not added to the database");
    }
  } catch (error) {
    res.status(500).json("User not added to the database");
  }
});

//ChatGPT use: NO
// Get all users
router.get('/', async (req, res) => {
  try {
    const db = getDB();

    const users = await db.collection('users').find().toArray();

    if (users) {
      res.status(200).json(users);
    } else {
      res.status(500).json("Could not retrieve data from the database");
    }
  } catch (error) {
    res.status(500).json("Could not retrieve data from the database");
  }
});

//ChatGPT use: NO
// Get a specific user by ID
router.get('/userId/:userId', async (req, res) => {
  try {
    const db = getDB();
    const id = new ObjectId(req.params.userId);

    const user = await db.collection('users').findOne({ _id: id });
    if (user) {
      res.status(200).json(user);
    } else {
      res.status(404).send('User not found');
    }
  } catch (error) {
    res.status(500).send('User not retrieved');
  }
});

//ChatGPT use: NO
// Get a specific user by email
router.get('/userEmail/:userEmail', async (req, res) => {
  try {
    const db = getDB();
    const userEmail = req.params.userEmail;

    const user = await db.collection('users').findOne({ email: userEmail });
    if (user) {
      res.status(200).json(user);
    } else {
      res.status(404).send('User not found');
    }
  } catch (error) {
    res.status(500).send('User not retrieved');
  }
});

// const getRecommendedUsers = async(db,userId)=> {
//   const _userId = new ObjectId(userId);
//   const myUser = await db.collection('users').findOne({ _id: _userId });
//   const recommendedUserIdList = [];
//   const weightDiff = [];
//   const ageDiff = [];
//   db.collection('users').forEach(user => {
//     if(user.homeGym == myUser.homeGym){
//       recommendedUserIdList.push(user._id);
//       weightDiff.push(Math.abs(parseInt(user.weight) - parseInt(myUser.weight)));
//       ageDiff.push(Math.abs(parseInt(user.age) - parseInt(myUser.age)));
//     }
//   });
// }

// // modifies recommendedUserIdList
// const filterBlockedProfiles = async(db,userId, recommendedUserIdList)=> {
//   const _userId = new ObjectId(userId);
//   const user = await db.collection('users').findOne({ _id: _userId });
//   recommendedUserIdList.forEach(userId => {
//     if(user.blockedUsers.indexOf(userId) != -1){
//       recommendedUserIdList.filter(item => item !== userId);
//     }
//   });
//   return 0;
// }

//ChatGPT use: YES
// Get recommended users by ID
router.get('/userId/:userId/recommendedUsers', async (req, res) => {
  try {
    const db = getDB();
    var myHomeGym;
    var user;
    var id
    id = new ObjectId(req.params.userId);

    user = await db.collection('users').findOne({ _id: id });
  
    if (!user) {
      res.status(404).send('User not found');
      return;
    } 
    myHomeGym = user.homeGym;
    const friends = user.friends
    const recommendedUsers = await db.collection('users').find({}).toArray();
    const filteredRecommendedUsers = recommendedUsers.filter(recommendedUser => {
      
      return id.toString() !== recommendedUser._id.toString() && !friends.includes(recommendedUser._id.toString());
    });

    if (!filteredRecommendedUsers) {
      res.status(500).send('Could not get recommended users');
      return;
    }

    res.status(200).json(filteredRecommendedUsers);
  } catch (error) {
    res.status(500).send('Users not retrieved');
  }
});

//ChatGPT use: NO
// Get friends by ID
router.get('/userId/:userId/friends', async (req, res) => {
  try {
    const db = getDB();
    var friendsId;
    const id = new ObjectId(req.params.userId);

    const user = await db.collection('users').findOne({ _id: id });

    if (!user) {
      res.status(404).send('User not found');
      return;
    }
    friendsId = user.friends;

    const friends = [];

    for (const friendId of friendsId) {
      const fid = new ObjectId(friendId);
      const friend = await db.collection('users').findOne({ _id: fid })

      if (friend) {
        friends.push(friend);
      }
    }

    res.status(200).json(friends);
  } catch (error) {
    res.status(500).send('Friends not retrieved');
  }
});

//ChatGPT use: NO
// Get friend requests by ID
router.get('/userId/:userId/friendRequests', async (req, res) => {
  try {
    const db = getDB();
    var friendsId;
    const id = new ObjectId(req.params.userId);

    const user = await db.collection('users').findOne({ _id: id });

    if (!user) {
      res.status(404).send('User not found');
      return;
    }

    friendsId = user.friendRequests;
    
    const friends = [];

    for (const friendId of friendsId) {
      const fid = new ObjectId(friendId);
      const friend = await db.collection('users').findOne({ _id: fid })

      if (friend) {
        friends.push(friend);
      }
    }

    res.status(200).json(friends);
  } catch (error) {
    res.status(500).send('Friend requests not retrieved');
  }
});

//ChatGPT use: NO
// Get blocked users by ID
router.get('/userId/:userId/blockedUsers', async (req, res) => {
  try {
    const db = getDB();
    const id = new ObjectId(req.params.userId);

    const user = await db.collection('users').findOne({ _id: id });

    if (!user) {
      res.status(404).send('User not found');
      return;
    }

    const blockedUsersId = user.blockedUsers;
    const blockedUsers = [];

    for (const blockedUserId of blockedUsersId) {
      const buid = new ObjectId(blockedUserId);
      const blockedUser = await db.collection('users').findOne({ _id: buid })

      if (blockedUser) {
        blockedUsers.push(blockedUser);
      }
    }

    res.status(200).json(blockedUsers);
  } catch (error) {
    res.status(500).send('Blocked users not retrieved');
  }
});

//ChatGPT use: NO
// Update a user by ID
router.put('/userId/:userId', async (req, res) => {
  try {
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
      blockedUsers: req.body.blockedUsers || user.blockedUsers,
      getChats: req.body.getChats || user.getChats
    }

    const result = await db.collection('users').updateOne(
      { _id: id },
      { $set: updatedUser }
    );
    
    if (result.matchedCount === 0) {
      res.status(404).send('User not found');
    } else {
      res.status(200).json(updatedUser);
    }
  } catch (error) {
    res.status(500).send('User not updated');
  }
});

//ChatGPT use: PARTIAL
//Add Friend. ONLY FOR MVP, TO BE CHANGED LATER
router.put('/addFriend/:senderId/:recieverId', async (req, res) => {
  try {
    const db = getDB();
    const recieverId = new ObjectId(req.params.recieverId);
    const senderId = new ObjectId(req.params.senderId);

    const recieverUser = await db.collection('users').findOne({ _id: recieverId });
    const senderUser = await db.collection('users').findOne({ _id: senderId });
    const receiverFriends = recieverUser.friends;
    const senderFriends = senderUser.friends;
    if(receiverFriends.indexOf(senderId) == -1){
      receiverFriends.push(senderId);
      senderFriends.push(recieverId);
    }
    else{
      res.status(500).send('Already friends');
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
    res.status(200).send('Friend added');
  } catch (error) {
    res.status(500).send('Friend not added');
  }
});

//ChatGPT use: NO
// Send friend request
router.put('/sendFriendRequest/:senderId/:recieverId', async (req, res) => {
  try {
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
    
    if (result.matchedCount === 0) {
      res.status(404).send('User not found');
    } else {
      res.status(200).send('Friend Request Sent');
    }
  } catch (error) {
    res.status(500).send('Friend Request Not Sent');
  }
});

//ChatGPT use: NO
// Unsend friend request
router.put('/unsendFriendRequest/:senderId/:recieverId', async (req, res) => {
  try {
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
      res.status(500).send('No Friend request');
      return;
    }

    friendRequestList.splice(i, 1);

    const result = await db.collection('users').updateOne(
      { _id: recieverId },
      { 
        $set: {
          friendRequests: friendRequestList
        } 
      }
    );
    
    if (result.matchedCount === 0) {
      res.status(404).send('User not found');
    } else {
      res.status(200).send('Friend Request Unsent');
    }
  } catch (error) {
    res.status(500).send('Friend Request Not Unsent');
  }
});

//ChatGPT use: NO
// Accept friend request
router.put('/acceptFriendRequest/:senderId/:recieverId', async (req, res) => {
  try {
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
    
    if (resultSender.matchedCount === 0 || resultReciever.matchedCount === 0) {
      res.status(500).send('Friend request not accepted');
    } else {
      res.status(200).send('Friend Request Accepted');
    }
  } catch (error) {
    res.status(500).send('Friend Request Not Accepted');
  }
});

//ChatGPT use: NO
// Decline friend request
router.put('/declineFriendRequest/:senderId/:recieverId', async (req, res) => {
  try {
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
    
    if (resultReciever.matchedCount === 0) {
      res.status(500).send('Friend request not declined');
    } else {
      res.status(200).send('Friend Request Declined');
    }
  } catch (error) {
    res.status(500).send('Friend Request Not Declined');
  }
});

//ChatGPT use: NO
// Unfriend user
router.put('/unfriend/:unfrienderId/:unfriendedId', async (req, res) => {
  try {
    const db = getDB();

    const result = await unfriend(db, req.params.unfrienderId, req.params.unfriendedId);

    if (result) {
      res.status(200).send('User unfriended');
    } else {
      res.status(500).send('User not unfriended');
    }
  } catch (error) {
    res.status(500).send('User Not Unfriended');
  }
});

const unfriend = async (db, unfrienderId, unfriendedId) => {
  try {
    const _unfrienderId = new ObjectId(unfrienderId);
    const _unfriendedId = new ObjectId(unfriendedId);

    const unfriender = await db.collection('users').findOne({ _id: _unfrienderId });
    const unfriended = await db.collection('users').findOne({ _id: _unfriendedId });

    if (!unfriender || !unfriended) {
      return 0;
    }

    const unfrienderFriends = unfriender.friends;
    const unfriendedFriends = unfriended.friends;

    let i = -1;
    for (let j = 0; j < unfrienderFriends.length; j++) {
      if (unfrienderFriends[j] == unfriendedId) {
        i = j;
        break;
      }
    }

    if (i == -1) {
      return 0;
    }
    unfrienderFriends.splice(i, 1);

    i = -1;
    for (let j = 0; j < unfriendedFriends.length; j++) {
      if (unfriendedFriends[j] == unfrienderId) {
        i = j;
        break;
      }
    }

    if (i == -1) {
      return 0;
    }
    unfriendedFriends.splice(i, 1);

    const resultUnfriender = await db.collection('users').updateOne(
      { _id: _unfrienderId },
      { 
        $set: {
          friends: unfrienderFriends
        } 
      }
    );
    if (resultUnfriender.matchedCount === 0) {
      return 0;
    }

    const resultUnfriended = await db.collection('users').updateOne(
      { _id: _unfriendedId },
      { 
        $set: {
          friends: unfriendedFriends
        } 
      }
    );
    if (resultUnfriended.matchedCount === 0) {
      return 0;
    }

    return 1;
  } catch (error) {
    return 0;
  }
}

//ChatGPT use: NO
// Block user
router.put('/blockUser/:blockerId/:blockedId', async (req, res) => {
  try {
    const db = getDB();
    const blockerId = new ObjectId(req.params.blockerId);

    const blockerUser = await db.collection('users').findOne({ _id: blockerId });

    if (!blockerUser) {
      res.status(404).send('User not found');
      return;
    }

    await unfriend(db, req.params.blockerId, req.params.blockedId);

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
      res.status(500).send('User not blocked');
    } else {
      res.status(200).send('User blocked');
    }
  } catch (error) {
    res.status(500).send('User Not Blocked');
  }
});

//ChatGPT use: NO
// Unblock user
router.put('/unblockUser/:blockerId/:blockedId', async (req, res) => {
  try {
    const db = getDB();
    const blockerId = new ObjectId(req.params.blockerId);

    const blockerUser = await db.collection('users').findOne({ _id: blockerId });

    if (!blockerUser) {
      res.status(404).send('User not found');
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
      res.status(404).send('Blocked user not found');
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
      res.status(500).send('User not unblocked');
    } else {
      res.status(200).send('User unblocked');
    }
  } catch (error) {
    res.status(500).send('User Not Unblocked');
  }
});

//ChatGPT use: NO
// Delete a chat
router.put('/userId/:userId/deleteChat/:chatId', async (req, res) => {
  try {
    const db = getDB();
    const userId = new ObjectId(req.params.userId);

    const user = await db.collection('users').findOne({ _id: userId });

    if (!user) {
      res.status(404).send('User not found');
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
      res.status(404).send('Chat not found');
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
      res.status(500).send('Chat not deleted');
    } else {
      res.status(200).send('Chat deleted');
    }
  } catch (error) {
    res.status(500).send('Chat Not Deleted');
  }
});

//ChatGPT use: NO
// Delete a user by ID
router.delete('/userId/:userId', async (req, res) => {
  try {
    const db = getDB();
    const _id = new ObjectId(req.params.userId);

    const resultClearData = await clearData(db, _id);

    if (!resultClearData) {
      res.status(500).send('User data not cleared');
      return;
    }

    const result = await db.collection('users').deleteOne({ _id: _id });

    if (!schedulesDelete) {
      res.status(500).send('User schedules not properly deleted');
      return;
    }

    if (result.deletedCount === 0) {
      res.status(404).send('User not found');
    } else {
      res.status(200).send('User deleted successfully');
    }
  } catch (error) {
    res.status(500).send('User Not Deleted');
  }
});

const clearData = async (db, _id) => {
  try {
    const user = await db.collection('users').findOne({ _id: _id });

    if (!user) {
      return 0;
    }

    const chats = user.chats;
  
    for (const currChat of chats) {
      const _chatId = new ObjectId(currChat.chatId);
      const chat = await db.collection('chat').findOne({ _id: _chatId });

      if (!chat) {
        continue;
      }

      let otherMemberId = chat.members[0];

      if (otherMemberId == _id.toString()) {
        otherMemberId = chat.members[1];
      }

      const _otherMemberId = new ObjectId(otherMemberId);
      const otherUser = await db.collection('users').findOne({ _id: _otherMemberId });

      if (!otherUser) {
        continue;
      }

      otherChats = otherUser.chats;

      let i = -1;
      for (let j = 0; j < otherChats.length; j++) {
        if (otherChats[j].chatId == currChat.chatId) {
          i = j;
          break;
        }
      }
      otherChats.splice(i, 1);

      const result = await db.collection('chat').deleteOne({ _id: _chatId });

      if (!result) {
        continue;
      }
    }

    const schedulesDelete = await db.collection('schedules').deleteMany({ userId: id });
  
    if (!schedulesDelete) {
      return 0;
    }
  }
  catch {
    return 0;
  }
}

//ChatGPT use: YES
// Delete all users
//This is for debugging only (DEV USE)
router.delete('/', async (req, res) => {
  try {
    const db = getDB();
    
    const result = await db.collection('users').deleteMany({});
    res.status(200).send('Users deleted successfully');
  } catch (error) {
    res.status(500).send('All Users Not Deleted');
  }
});

export default router;
