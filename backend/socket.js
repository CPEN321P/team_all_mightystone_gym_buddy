const { ObjectId } = require('mongodb');
const { getDB } = require('./MongoDB/Connect.js');
const { Server } = require('socket.io');

const getChatByUserId = async (db, user1, user2) => {
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

const sendMessageById = async (db, chat, sender, message) => {
  try {
    const chatMessages = chat.messages;
    const newMessage = {
      schedule: 0,
      sender: sender,
      body: message
    }
    chatMessages.push(newMessage);

    console.log("1")

    const result = await db.collection('chat').updateOne(
      { _id: chat._id },
      { 
        $set: {
          messages: chatMessages
        } 
      }
    );

    console.log("2")

    if (result.matchedCount === 0) {
      console.log("3")
      return 0;
    } else {
      console.log("4")
      let otherMember = chat.members[0];
      if (otherMember == sender) {
        otherMember = chat.members[1];
      }

      const res = await userMustHaveChat(db, chat);
      if (!res) {
        return 0;
      }

      console.log("5")
      
      return 1;
    }
  } catch (error) {
    console.log("error")
    return 0;
  }
}

const sendScheduleById = async (db, chat, sender, scheduleId) => {
  try {
    const chatMessages = chat.messages;
    const newMessage = {
      schedule: 1,
      sender: sender,
      body: scheduleId
    }
    chatMessages.push(newMessage);

    console.log("1")

    const result = await db.collection('chat').updateOne(
      { _id: id },
      { 
        $set: {
          messages: chatMessages
        } 
      }
    );

    console.log("2")

    if (result.matchedCount === 0) {
      console.log("3")
      return 0;
    } else {
      console.log("4")
      let otherMember = chat.members[0];
      if (otherMember == sender) {
        otherMember = chat.members[1];
      }

      const res = await userMustHaveChat(db, chat);
      if (!res) {
        return 0;
      }

      console.log("5")

      return 1;
    }
  } catch (error) {
    console.log("error")
    return 0;
  }
}

const userMustHaveChat = async (db, chat) => {
  try {
    const chatId = chat._id.toString();
    const user1Id = chat.members[0];
    const user2Id = chat.members[1];
    const _user1Id = ObjectId(user1Id);
    const _user2Id = ObjectId(user2Id);
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
      if (result.matchedCount === 0) {
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
      if (result.matchedCount === 0) {
        return 0;
      }
    }

    return 1;
  } catch (error) {
    return 0;
  }
}

const socket = (server) => {
  const io = new Server(server, {});

  io.use(function(socket, next) {
    next();
  })
  .on("connection", (socket) => {

    socket.on("join_chat", async (data) => {
      const db = getDB();
      const myID = data.myID;
      const theirID = data.theirID;

      // find chat by ids
      const chat = await getChatByUserId(db, myID, theirID);

      // join chat
      if (chat) {
        socket.join(chat._id.toString());
      }
    });

    socket.on("send_message", async (data) => {
      const db = getDB();
      const myID = data.myID;
      const theirID = data.theirID;
      const message = data.message;

      // find chat by ids
      const chat = await getChatByUserId(db, myID, theirID);

      // send message to chat db 
      const sent = await sendMessageById(db, chat, myID, message);

      const chatName = chat._id.toString();

      console.log(chatName)

      // send message to socket
      if (sent) {
        console.log("HERE")
        io.in(chatName).emit('new_message', { 
          schedule: 0,
          sender: myID, 
          body: message 
        });
      }
    });

    socket.on("send_schedule", async (data) => {
      const db = getDB();
      const myID = data.myID;
      const theirID = data.theirID;
      const scheduleId = data.scheduleId;

      // find chat by ids
      const chat = await getChatByUserId(db, myID, theirID);

      // send schedule to chat db 
      const sent = await sendScheduleById(db, chat, myID, scheduleId);

      // send schedule to socket
      if (sent) {
        io.in(chat._id.toString()).emit('new_message', { 
          schedule: 1,
          sender: myID, 
          body: scheduleId 
        });
      }
    })
  })
}

module.exports = {
  socket
};
