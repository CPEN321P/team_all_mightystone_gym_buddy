const request = require('supertest');
const app = require('../app.js');
const { MongoClient } = require('mongodb');
const { connectDB, getDB } = require('../MongoDB/Connect.js'); // Adjust the path as needed

console.log(getDB);

jest.mock('../MongoDB/Connect.js');

// let connection;
// let db;

// beforeAll(async () => {
//   connection = await MongoClient.connect("mongodb://127.0.0.1:27017", {
//     useNewUrlParser: true,
//   });
//   db = await connection.db('gym-buddies-db');
// });

// afterAll(async () => {
//   await connection.close();
//   await db.close();
// });

jest.mock('mongodb');

describe('POST /', () => {
        
  it('should add a new user to the database', async () => {
    // Mock the getDB function
    mockDB = {
        collection: jest.fn().mockReturnThis(),
        insertOne: jest.fn().mockReturnValue({ result: { ok: 1 }, insertedId: 'mockedId' }),
    };
      getDB.mockReturnValue(mockDB);
    
    const mockUser = {
      name: 'John Doe',
      email: 'john.doe@example.com',
      // ... other user properties
    };

    const response = await request(app)
      .post('/users')
      .send(mockUser)
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(200);
    expect(response.body).toBe('mockedId');
  });
});