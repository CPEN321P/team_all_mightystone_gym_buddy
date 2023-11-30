const { ObjectId } = require('mongodb');
const { createId } = require('./mongoUtils');

const clearData = async (db, _id) => {
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
    
    await db.collection('chat').deleteOne({ _id: _chatId });
  }
  
  await db.collection('schedules').deleteMany({ userId: _id.toString() });

  return 1;
}

// metrics used: gym, gender, age, weight (in kilos), common friends
const getSimilarity = (user1, user2) => {

  const metricWeights = {
    weight: 0.2,
    homeGym: 0.3,
    gender: 0.1,
    age: 0.2,
    commonFriends: 0.2
  };

  const weightSimilarity = 1 - Math.abs(user1.weight - user2.weight)/ Math.max(user1.weight, user2.weight);
  const gymSimilarity = user1.homeGym === user2.homeGym ? 1 : 0;
  const genderSimilarity = user1.gender === user2.gender ? 1 : 0;
  const ageSimilarity = 1 - Math.abs(user1.age - user2.age) / Math.max(user1.age, user2.age);
  const friendsIntersection = user1.friends.filter(friend => user2.friends.includes(friend));
  const commonFriendsSimilarity = friendsIntersection.length / Math.min(user1.friends.length, user2.friends.length) || 0;

  const similarityScore = (
    metricWeights.weight * weightSimilarity +
    metricWeights.homeGym * gymSimilarity +
    metricWeights.gender * genderSimilarity +
    metricWeights.age * ageSimilarity +
    metricWeights.commonFriends * commonFriendsSimilarity
  );

  return similarityScore;

}

//ChatGPT use: NO
const unfriend = async (db, unfrienderId, unfriendedId) => {
    let _unfrienderId;
    let _unfriendedId;

    try {
      _unfrienderId = createId(unfrienderId);
      _unfriendedId = createId(unfriendedId);
    } catch (error) {
      return 0;
    }

    const unfriender = await db.collection('users').findOne({ _id: _unfrienderId });
    const unfriended = await db.collection('users').findOne({ _id: _unfriendedId });

    if (!unfriender || !unfriended) {
      return 0;
    }

    await removeChats(db, unfrienderId, unfriendedId, unfriender, unfriended);

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

    const resultUnfriended = await db.collection('users').updateOne(
      { _id: _unfriendedId },
      { 
        $set: {
          friends: unfriendedFriends
        } 
      }
    );

    if (resultUnfriender.matchedCount === 0 || resultUnfriended.matchedCount === 0) {
      return 0;
    }

    return 1;
}

//ChatGPT use: NO
const removeChats = async (db, id1, id2, user1, user2) => {
  const chat = checkForChat(db, id1, id2);

  if (!chat) {
    return;
  }

  console.log(chat);
  const chatId = chat._id.toString();

  await removeChatFromList(db, user1, chatId);
  await removeChatFromList(db, user2, chatId);
}

//ChatGPT use: NO
const removeChatFromList = async (db, user1, chatId) => {
  const chats = user1.chats;

  let i = -1;

  for (let j = 0; j < chats.length; j++) {
    if (chats[j] == chatId) {
      i = j;
      break;
    }
  }

  if (i == -1) {
    return;
  }

  chats.splice(i,1);

  await db.collection('users').updateOne(
    { _id: user1._id.toString() },
    { 
      $set: {
        chats: chats
      } 
    }
  );
}

//ChatGPT use: NO
const checkForChat = async (db, user1, user2) => {
  try {
    return db.collection('chat').findOne({ 
      $and: [
        {
          members: {
            $elemMatch: {
              $eq: user1
            }
          }
        },
        {
          members: {
            $elemMatch: {
              $eq: user2
            }
          }
        }
      ]
    });
  } catch (error) {
    return 0;
  }
}

module.exports = {clearData, unfriend, getSimilarity}
  