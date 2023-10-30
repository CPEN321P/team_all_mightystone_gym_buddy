import express from 'express';
import { ObjectId } from 'mongodb';
import { getDB } from '../MongoDB/Connect.js';

const router = express.Router();

// ALL REST OPERATIONS
// - Get chat by chat id 
// - Get chat by user ids
// - Send message by user ids
// - Send Message by chat id 
// - Send Schedule by user ids
// - Send Schedule by chat id 

const createNewChat = async (db, senderId, recieverId) => {
  const newChat = {
    members: [
      senderId,
      recieverId
    ],
    messages: []
  }

  const result = await db.collection('chat').insertOne(newChat);

  if (!result || !result.insertedId) {
    return 0;
  }

  const resSender = await addChatToUser(db, senderId, result.insertedId.toString());
  if (!resSender) {
    return 0;
  }

  const resReciever = await addChatToUser(db, recieverId, result.insertedId.toString());
  if (!resReciever) {
    return 0;
  }

  return result.insertedId.toString();
}

const addChatToUser = async (db, userId, chatId) => {
  const id = new ObjectId(userId);

  const user = await db.collection('users').findOne({ _id: id });

  if (!user) {
    return 0;
  }

  const chatsList = user.chats;

  if (!chatsList) {
    return 0;
  }

  chatsList.push(chatId)

  const result = await db.collection('users').updateOne(
    { _id: id },
    { 
      $set: {
        chats: chatsList
      } 
    }
  );

  if (result.matchedCount == 0) {
    return 0;
  } else {
    return 1;
  }
}

// Get a chat by chat id
router.get('/chatId/:chatId', async (req, res) => {
  const db = getDB();
  const id = new ObjectId(req.params.chatId);

  const chat = await db.collection('chat').findOne({ _id: id });

  if (chat) {
    res.status(200).json(chat);
  } else {
    res.status(404).send("No chat found");
  }
});

// Get a chat by user ids
router.get('/userId/:user1/:user2', async (req, res) => {
  const db = getDB();
  const user1 = req.params.user1;
  const user2 = req.params.user2;

  const chat = await checkForChat(db, user1, user2);

  if (chat) {
    res.status(200).json(chat);
  } else {
    res.status(404).send("No chat found");
  }
});

const checkForChat = async (db, user1, user2) => {
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
}

// Send message by user ids
router.put('/sendMessage/userId/:senderId/:recieverId', async (req, res) => {
  const db = getDB();
  const message = req.body.message;
  const sender = req.params.senderId;
  const reciever = req.params.recieverId

  const chat = await checkForChat(db, sender, reciever);

  if (chat) {
    const result = await sendMessageById(db, chat._id.toString(), sender, message);

    if (result) {
      res.status(200).json(chat._id.toString());
    } else {
      res.status(404).send('Message not sent');
    }
  } else {
    const newChatId = await createNewChat(db, sender, reciever);

    if (!newChatId) {
      res.status(404).send('Message not sent');
    }

    const result = await sendMessageById(db, newChatId, sender, message);

    if (result) {
      res.status(200).json(newChatId);
    } else {
      res.status(404).send('Message not sent');
    }
  }
});

const userMustHaveChat = async (db, chat) => {
  const chatId = chat._id.toString();
  const user1Id = chat.members[0];
  const user2Id = chat.members[1];
  const _user1Id = new ObjectId(user1Id);
  const _user2Id = new ObjectId(user2Id);
  const user1 = await db.collection('users').findOne({ _id: _user1Id });
  const user2 = await db.collection('users').findOne({ _id: _user2Id });

  if (!user1 || !user2){
    return 0;
  }

  const chats1 = user1.chats;
  const chats2 = user2.chats;

  let i = -1;
  for (let j = 0; j < chats1.length; j++) {
    if (chats1[j] == chatId) {
      i = j;
      break;
    }
  }
  if (i == -1) {
    chats1.push(chatId);
    const result = await db.collection('users').updateOne(
      { _id: _user1Id },
      { $set: {
          chats: chats1
        } 
      }
    );
    if (result.matchedCount == 0) {
      return 0;
    }
  }

  i = -1;
  for (let j = 0; j < chats2.length; j++) {
    if (chats2[j] == chatId) {
      i = j;
      break;
    }
  }
  if (i == -1) {
    chats2.push(chatId);
    const result = await db.collection('users').updateOne(
      { _id: _user2Id },
      { $set: {
          chats: chats2
        } 
      }
    );
    if (result.matchedCount == 0) {
      return 0;
    }
  }

  return 1;
}

const sendMessageById = async (db, _chatId, sender, message) => {
  const chatId = new ObjectId(_chatId);

  const chat = await db.collection('chat').findOne({ _id: chatId });

  if (!chat) {
    return 0;
  }

  const res = await userMustHaveChat(db, chat);

  if (!res) {
    return 0;
  }

  const chatMessages = chat.messages;
  const newMessage = {
    schedule: 0,
    sender: sender,
    body: message
  }
  chatMessages.push(newMessage);

  const result = await db.collection('chat').updateOne(
    { _id: chatId },
    { 
      $set: {
        messages: chatMessages
      } 
    }
  );

  if (result.matchedCount == 0) {
    return 0;
  } else {
    return 1;
  }
}

// Send message by chat id
router.put('/sendMessage/chatId/:chatId/:senderId', async (req, res) => {
  const db = getDB();
  const message = req.body.message;
  const chatId = req.params.chatId;
  const sender = req.params.senderId;

  const result = await sendMessageById(db, chatId, sender, message);

  if (result) {
    res.status(200).send('Message Sent');
  } else {
    res.status(404).send('Message not sent');
  }
});

// Send schedule by user ids
router.put('/sendSchedule/userId/:senderId/:recieverId', async (req, res) => {
  const db = getDB();
  const scheduleId = req.body.scheduleId;
  const sender = req.params.senderId;
  const reciever = req.params.recieverId

  const chat = await checkForChat(db, sender, reciever);

  if (chat) {
    const result = await sendScheduleById(db, chat._id.toString(), sender, scheduleId);

    if (result) {
      res.status(200).json(chat._id.toString());
    } else {
      res.status(404).send('Schedule not sent');
    }
  } else {
    const newChatId = await createNewChat(db, sender, reciever);

    if (!newChatId) {
      res.status(404).send('Schedule not sent');
    }

    const result = await sendScheduleById(db, newChatId, sender, scheduleId);

    if (result) {
      res.status(200).json(newChatId);
    } else {
      res.status(404).send('Schedule not sent');
    }
  }
});

const sendScheduleById = async (db, chatId, sender, scheduleId) => {
  const id = new ObjectId(chatId);

  const chat = await db.collection('chat').findOne({ _id: id });

  if (!chat) {
    return 0;
  }

  const res = await userMustHaveChat(db, chat);

  if (!res) {
    return 0;
  }

  const chatMessages = chat.messages;
  const newMessage = {
    schedule: 1,
    sender: sender,
    body: scheduleId
  }
  chatMessages.push(newMessage);

  const result = await db.collection('chat').updateOne(
    { _id: id },
    { 
      $set: {
        messages: chatMessages
      } 
    }
  );

  if (result.matchedCount == 0) {
    return 0;
  } else {
    return 1;
  }
}

// Send schedule by id
router.put('/sendSchedule/chatId/:chatId/:senderId', async (req, res) => {
  const db = getDB();
  const scheduleId = req.body.scheduleId;
  const chatId = req.params.chatId;
  const sender = req.params.senderId;

  const result = await sendScheduleById(db, chatId, sender, scheduleId);

  if (result) {
    res.status(200).send('Schedule Sent');
  } else {
    res.status(404).send('Schedule not sent');
  }
});

export default router;