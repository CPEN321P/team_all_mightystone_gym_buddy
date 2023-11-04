import express from 'express';
import { ObjectId } from 'mongodb';
import { getDB } from '../MongoDB/Connect.js';

const router = express.Router();

// ALL REST OPERATIONS
// - Get all chats 
// - Get chat by chat id 
// - Get chat by user ids
// - Send message by user ids
// - Send Message by chat id 
// - Send Schedule by user ids
// - Send Schedule by chat id 

// HELPER FUNCTIONS
// - create new chat 
// - add chat to user
// - send message by id
// - send schedule by id
// - users must have chat
// - check for chat with both users
// - notify recipient 


//ChatGPT use: NO
const createNewChat = async (db, senderId, recieverId) => {
  try {
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
  } catch (error) {
    return 0;
  }
}

//ChatGPT use: NO
const addChatToUser = async (db, userId, chatId) => {
  try {
    const id = new ObjectId(userId);

    const user = await db.collection('users').findOne({ _id: id });

    if (!user) {
      return 0;
    }

    const chatsList = user.chats;

    if (!chatsList) {
      return 0;
    }

    chatsList.push({
      chatId: chatId,
      notification: 0
    });

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
  } catch (error) {
    return 0;
  }
}

//ChatGPT use: NO
const sendMessageById = async (db, _chatId, sender, message) => {
  try {
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
      let otherMember = chat.members[0];
      if (otherMember == sender) {
        otherMember = chat.members[1];
      }

      const resNotify = await notifyRecipient(db, _chatId, otherMember);

      if (!resNotify) {
        return 0;
      }
      
      return 1;
    }
  } catch (error) {
    return 0;
  }
}

//ChatGPT use: NO
const sendScheduleById = async (db, chatId, sender, scheduleId) => {
  try {
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
      let otherMember = chat.members[0];
      if (otherMember == sender) {
        otherMember = chat.members[1];
      }

      const resNotify = await notifyRecipient(db, _chatId, otherMember);

      if (!resNotify) {
        return 0;
      }

      return 1;
    }
  } catch (error) {
    return 0;
  }
}

//ChatGPT use: NO
const userMustHaveChat = async (db, chat) => {
  try {
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
      if (chats1[j].chatId == chatId) {
        i = j;
        break;
      }
    }
    if (i == -1) {
      chats1.push({
        chatId: chatId,
        notification: 0
      });
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
      if (chats2[j].chatId == chatId) {
        i = j;
        break;
      }
    }
    if (i == -1) {
      chats2.push({
        chatId: chatId,
        notification: 0
      });
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
  } catch (error) {
    return 0;
  }
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

//ChatGPT use: NO
const notifyRecipient = async (db, chatId, userId) => {
  try {
    const _userId = new ObjectId(userId);
    const user = await db.collection('users').findOne({ _id: _userId });

    if (!user){
      return 0;
    }

    const chats = user.chats;

    let i = -1;
    for (let j = 0; j < chats.length; j++) {
      if (chats[j].chatId == chatId) {
        i = j;
        break;
      }
    }
    if (i == -1) {
      return 0;
    }

    chats[i].notification = 1;

    const result = await db.collection('users').updateOne(
      { _id: _userId },
      { 
        $set: {
          getChats: 1,
          chats: chats
        } 
      }
    );

    if (result.matchedCount == 0) {
      return 0;
    } else {
      return 1;
    }
  } catch (error) {
    return 0;
  }
}

//ChatGPT use: NO
// Check for new chat
router.get('/isNewChat/:userId', async (req, res) => {
  try {
    const db = getDB();
    const id = new ObjectId(req.params.userId);

    const user = await db.collection('users').findOne({ _id: id });

    if (!user) {
      res.status(404).json("User not found");
    } 

    res.status(200).json(user.getChats);
  } catch (error) {
    res.status(404).send("No chat found");
  }
});

//ChatGPT use: NO
// Get all chats by user ID
router.get('/allChats/:userId/', async (req, res) => {
  try {
    const db = getDB();
    const id = new ObjectId(req.params.userId);

    const user = await db.collection('users').findOne({ _id: id });

    if (!user) {
      res.status(404).send('User not found');
      return;
    }

    const result = await db.collection('users').updateOne(
      { _id: id },
      { 
        $set: {
          getChats: 0,
        } 
      }
    );

    if (!result) {
      res.status(404).send('User not found');
      return;
    }

    const allChats = user.chats;
    const chats = [];

    for (const _chat of allChats) {
      const cid = new ObjectId(_chat.chatId);
      const chat = await db.collection('chat').findOne({ _id: cid });

      if (chat) {
        let otherId = chat.members[0];

        if (otherId == req.params.userId) {
          otherId = chat.members[1];
        }

        const _id = new ObjectId(otherId);
        const otherUser = await db.collection('users').findOne({ _id: _id });

        if (!otherUser) {
          res.status(404).send('User not found');
          return;
        }

        const i = chat.messages.length;

        chats.push({
          chatId: _chat.chatId,
          notification: _chat.notification,
          name: otherUser.name,
          lastMessage: chat.messages[i-1]
        });
      }
    }

    res.status(200).json(chats);
  } catch (error) {
    res.status(500).send('Chats not retrieved');
  }
});

//ChatGPT use: NO
// Get a chat by chat id
router.get('/chatId/:chatId', async (req, res) => {
  try {
    const db = getDB();
    const id = new ObjectId(req.params.chatId);

    const chat = await db.collection('chat').findOne({ _id: id });

    if (chat) {
      res.status(200).json(chat);
    } else {
      res.status(404).send("No chat found");
    }
  } catch (error) {
    res.status(404).send("No chat found");
  }
});

//ChatGPT use: NO
// Get a chat by user ids
router.get('/userId/:user1/:user2', async (req, res) => {
  try {
    const db = getDB();
    const user1 = req.params.user1;
    const user2 = req.params.user2;

    const chat = await checkForChat(db, user1, user2);

    if (chat) {
      res.status(200).json(chat);
    } else {
      const newChatId = await createNewChat(db, user1, user2);
      const newChat = await db.collection('chat').findOne({ _id: newChatId });

      if (newChat) {
        res.status(200).json(newChat);
      } else {
        res.status(500).send("Failed to create chat");
      }
    }
  } catch (error) {
    res.status(404).send("No chat found");
  }
});

//ChatGPT use: NO
// Send message by user ids
router.put('/sendMessage/userId/:senderId/:recieverId', async (req, res) => {
  try {
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
  } catch (error) {
    res.status(404).send('Message not sent');
  }
});

//ChatGPT use: NO
// Send message by chat id
router.put('/sendMessage/chatId/:chatId/:senderId', async (req, res) => {
  try {
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
  } catch (error) {
    res.status(404).send('Message not sent');
  }
});

//ChatGPT use: NO
// Send schedule by user ids
router.put('/sendSchedule/userId/:senderId/:recieverId', async (req, res) => {
  try {
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
  } catch (error) {
    res.status(404).send('Schedule not sent');
  }
});

//ChatGPT use: NO
// Send schedule by chat id
router.put('/sendSchedule/chatId/:chatId/:senderId', async (req, res) => {
  try {
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
  } catch (error) {
    res.status(404).send('Schedule not sent');
  }
});

export default router;