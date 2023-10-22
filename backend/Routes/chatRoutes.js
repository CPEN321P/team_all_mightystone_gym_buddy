const express = require('express');
const router = express.Router();

module.exports = (chatCollection) => {
  // Get all chat messages
  router.get('/', async (req, res) => {
    const messages = await chatCollection.find().toArray();
    res.json(messages);
  });

  // Create a new chat message
  router.post('/', async (req, res) => {
    const newMessage = req.body;
    const result = await chatCollection.insertOne(newMessage);
    res.json(result.ops[0]);
  });

  // Get a specific chat message by ID
  router.get('/:messageId', async (req, res) => {
    const message = await chatCollection.findOne({ _id: ObjectId(req.params.messageId) });
    if (!message) {
      res.status(404).send('Message not found');
      return;
    }
    res.json(message);
  });

  // Update a chat message by ID
  router.put('/:messageId', async (req, res) => {
    const updatedMessage = req.body;
    const result = await chatCollection.updateOne(
      { _id: ObjectId(req.params.messageId) },
      { $set: updatedMessage }
    );

    if (result.modifiedCount === 0) {
      res.status(404).send('Message not found');
      return;
    }

    res.json(updatedMessage);
  });

  // Delete a chat message by ID
  router.delete('/:messageId', async (req, res) => {
    const result = await chatCollection.deleteOne({ _id: ObjectId(req.params.messageId) });

    if (result.deletedCount === 0) {
      res.status(404).send('Message not found');
      return;
    }

    res.send('Message deleted successfully');
  });

  return router;
};