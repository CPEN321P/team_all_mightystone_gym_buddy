const request = require('supertest');
const app = require('../app.js');
const { MongoClient } = require('mongodb');
const { connectDB, getDB } = require('../MongoDB/Connect.js'); // Adjust the path as needed

//jest.mock('../MongoDB/Connect.js');

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
  let mockDB;

  afterAll(() => {
    // Cleanup after all test cases have run
    jest.restoreAllMocks();
  });

  beforeEach(() => {
    // Reset the mock before each test
    jest.clearAllMocks();
  });

  it('should add a new user to the database', async () => {
    const mockDB = {
        collection: jest.fn().mockReturnThis(),
        insertOne: jest.fn(),
      };
      getDB.mockReturnValue(mockDB);
    const mockUser = {
      name: 'John Doe',
      email: 'john.doe@example.com',
      // ... other user properties
    };

    const response = await request(app)
      .post('/')
      .send(mockUser)
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(200);
    expect(response.body).toBe('mockedId');
  });

  it('should handle user not added to the database', async () => {
    // Modify the mockDB behavior for this test case
    mockDB.insertOne.mockReturnValue({ result: { ok: 0 } });

    const mockUser = {
      name: 'John Doe',
      email: 'john.doe@example.com',
      // ... other user properties
    };

    const response = await request(app)
      .post('/')
      .send(mockUser)
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(500);
    expect(response.body).toBe('User not added to the database');
  });

  it('should handle server error', async () => {
    // Modify the getDB behavior to throw an error for this test case
    getDB.mockImplementation(() => {
      throw new Error('Mocked error');
    });

    const mockUser = {
      name: 'John Doe',
      email: 'john.doe@example.com',
      // ... other user properties
    };

    const response = await request(app)
      .post('/')
      .send(mockUser)
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(500);
    expect(response.body).toBe('User not added to the database');
  });
});