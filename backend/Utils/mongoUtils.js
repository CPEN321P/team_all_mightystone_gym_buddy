const { ObjectId } = require('mongodb');

const createId = (id) => {
    return new ObjectId(id);
}

module.exports = {createId}
