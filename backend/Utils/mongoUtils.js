const { ObjectId } = require('mongodb');

const createId = (id) => {
  try {
    return new ObjectId(id);
  } catch (error) {
    return 0;
  }
}

module.exports = {createId}
