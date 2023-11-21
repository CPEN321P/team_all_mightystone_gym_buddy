const request = require('supertest');
const app = require('../app.js');
const { getDB } = require('../MongoDB/Connect.js'); // Adjust the path as needed
const { ObjectId } = require('mongodb');

jest.mock('../MongoDB/Connect.js');

jest.mock('mongodb');

expect.extend({
  toContainObject(received, argument) {

    const pass = this.equals(received, 
      expect.arrayContaining([
        expect.objectContaining(argument)
      ])
    )

    if (pass) {
      return {
        message: () => (`expected ${this.utils.printReceived(received)} not to contain object ${this.utils.printExpected(argument)}`),
        pass: true
      }
    } else {
      return {
        message: () => (`expected ${this.utils.printReceived(received)} to contain object ${this.utils.printExpected(argument)}`),
        pass: false
      }
    }
  }
});

describe('Create a new user', () => {
  it('User is added to the database', async () => {
    // Mock the getDB function
    mockDB = {
        collection: jest.fn().mockReturnThis(),
        insertOne: jest.fn().mockReturnValue({ insertedId: 'mockedId' }),
    };
      getDB.mockReturnValue(mockDB);
    
    const mockUser = {
      name: 'John Doe',
      email: 'john.doe@example.com',
    };

    const response = await request(app)
      .post('/users')
      .send(mockUser)
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(200);
    expect(response.body).toBe('mockedId');
  });

  it('User is not added to the database', async () => {
    // Mock the getDB function
    mockDB = {
        collection: jest.fn().mockReturnThis(),
        insertOne: jest.fn().mockReturnValue(null),
    };
      getDB.mockReturnValue(mockDB);
    
    const mockUser = {
      name: 'John Doe',
      email: 'john.doe@example.com',
    };

    const response = await request(app)
      .post('/users')
      .send(mockUser)
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(500);
    expect(response.body).toBe('User not added to the database');
  });
});

describe('Get all users', () => {
  it('Users are retrieved', async () => {
    const mockUsers = [
      {
        name: 'John Doe',
        email: 'john.doe@example.com',
      },
      {
        name: 'Jane Doe',
        email: 'jane.doe@example.com',
      }
    ];

    mockDB = {
        collection: jest.fn().mockReturnThis(),
        find: jest.fn().mockReturnThis(),
        toArray: jest.fn().mockReturnValue(mockUsers),
    };
      
    getDB.mockReturnValue(mockDB);
    
    const response = await request(app)
      .get('/users')
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(200);
    expect(response.body).toContainObject({ name: 'Jane Doe' });
    expect(response.body).toContainObject({ name: 'John Doe' });
  });

  it('There is an error retrieving the users from the database', async () => {
    mockDB = {
        collection: jest.fn().mockReturnThis(),
        find: jest.fn().mockReturnThis(),
        toArray: jest.fn().mockReturnValue(null),
    };
      
    getDB.mockReturnValue(mockDB);
    
    const response = await request(app)
      .get('/users')
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(500);
    expect(response.body).toBe("Could not retrieve data from the database");
  });
});

describe('Get a specific user by ID', () => {
  it('User is retrieved', async () => {
    const mockUser = {
      name: 'John Doe',
      email: 'john.doe@example.com',
    };

    mockDB = {
        collection: jest.fn().mockReturnThis(),
        findOne: jest.fn().mockReturnValue(mockUser),
    };
      
    getDB.mockReturnValue(mockDB);

    ObjectId.mockImplementation((id) => {
      return id;
    })
    
    const response = await request(app)
      .get('/users/userId/123456')
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(200);
    expect(response.body.name).toBe(mockUser.name);
    expect(response.body.email).toBe(mockUser.email);
  });

  it('Invalid user ID', async () => {
    const mockUser = {
      name: 'John Doe',
      email: 'john.doe@example.com',
    };

    mockDB = {
        collection: jest.fn().mockReturnThis(),
        findOne: jest.fn().mockReturnValue(mockUser),
    };
      
    getDB.mockReturnValue(mockDB);

    ObjectId.mockImplementation((id) => {
      throw new error();
    })
    
    const response = await request(app)
      .get('/users/userId/123456')
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(500);
    expect(response.body).toBe('Invalid user ID');
  });

  it('Error retrieving user from database', async () => {
    const mockUser = {
      name: 'John Doe',
      email: 'john.doe@example.com',
    };

    mockDB = {
        collection: jest.fn().mockReturnThis(),
        findOne: jest.fn().mockReturnValue(null),
    };
      
    getDB.mockReturnValue(mockDB);

    ObjectId.mockImplementation((id) => {
      return id;
    })
    
    const response = await request(app)
      .get('/users/userId/123456')
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(404);
    expect(response.body).toBe('User not found');
  });
});

describe('Get a specific user by email', () => {
  it('User is retrieved', async () => {
    const mockUser = {
      name: 'John Doe',
      email: 'john.doe@example.com',
    };

    mockDB = {
        collection: jest.fn().mockReturnThis(),
        findOne: jest.fn().mockReturnValue(mockUser),
    };
      
    getDB.mockReturnValue(mockDB);

    ObjectId.mockImplementation((id) => {
      return id;
    })
    
    const response = await request(app)
      .get('/users/userEmail/example@test.com')
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(200);
    expect(response.body.name).toBe(mockUser.name);
    expect(response.body.email).toBe(mockUser.email);
  });

  it('Error retrieving user from database', async () => {
    const mockUser = {
      name: 'John Doe',
      email: 'john.doe@example.com',
    };

    mockDB = {
        collection: jest.fn().mockReturnThis(),
        findOne: jest.fn().mockReturnValue(null),
    };
      
    getDB.mockReturnValue(mockDB);

    ObjectId.mockImplementation((id) => {
      return id;
    })
    
    const response = await request(app)
      .get('/users/userEmail/example@test.com')
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(404);
    expect(response.body).toBe('User not found');
  });
});

describe('Get recommended users', () => {
  it('Invalid user ID', async () => {
    const mockUser = {
      _id: 12345,
      name: 'John Doe',
      email: 'john.doe@example.com',
      blockedUsers: [],
      friends: []
    };

    const retrievedUsers = [
      {
        _id: 23456,
        name: 'Jane Doe',
        email: 'jane.doe@example.com',
        blockedUsers: [],
        friends: []
      },
      {
        _id: 34567,
        name: 'Jacob Doe',
        email: 'jacob.doe@example.com',
        blockedUsers: [],
        friends: []
      }
    ];

    mockDB = {
        collection: jest.fn().mockReturnThis(),
        find: jest.fn().mockReturnThis(),
        findOne: jest.fn().mockReturnValue(mockUser),
        toArray: jest.fn().mockReturnValue(retrievedUsers),
    };
      
    getDB.mockReturnValue(mockDB);

    ObjectId.mockImplementation((id) => {
      throw new error();
    })
    
    const response = await request(app)
      .get('/users/userId/12345/recommendedUsers')
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(500);
    expect(response.body).toBe('Users not retrieved');
  });

  it('User not found', async () => {
    const mockUser = {
      _id: 12345,
      name: 'John Doe',
      email: 'john.doe@example.com',
      blockedUsers: [],
      friends: []
    };

    const retrievedUsers = [
      {
        _id: 23456,
        name: 'Jane Doe',
        email: 'jane.doe@example.com',
        blockedUsers: [],
        friends: []
      },
      {
        _id: 34567,
        name: 'Jacob Doe',
        email: 'jacob.doe@example.com',
        blockedUsers: [],
        friends: []
      }
    ];

    mockDB = {
        collection: jest.fn().mockReturnThis(),
        find: jest.fn().mockReturnThis(),
        findOne: jest.fn().mockReturnValue(null),
        toArray: jest.fn().mockReturnValue(retrievedUsers),
    };
      
    getDB.mockReturnValue(mockDB);

    ObjectId.mockImplementation((id) => {
      return id;
    })
    
    const response = await request(app)
      .get('/users/userId/12345/recommendedUsers')
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(404);
    expect(response.body).toBe('User not found');
  });

  it('Recommended users retrieved', async () => {
    const mockUser = {
      _id: 12345,
      name: 'John Doe',
      email: 'john.doe@example.com',
      blockedUsers: [],
      friends: []
    };

    const retrievedUsers = [
      {
        _id: 23456,
        name: 'Jane Doe',
        email: 'jane.doe@example.com',
        blockedUsers: [],
        friends: []
      },
      {
        _id: 34567,
        name: 'Jacob Doe',
        email: 'jacob.doe@example.com',
        blockedUsers: [],
        friends: []
      }
    ];

    mockDB = {
        collection: jest.fn().mockReturnThis(),
        find: jest.fn().mockReturnThis(),
        findOne: jest.fn().mockReturnValue(mockUser),
        toArray: jest.fn().mockReturnValue(retrievedUsers),
    };
      
    getDB.mockReturnValue(mockDB);

    ObjectId.mockImplementation((id) => {
      return id;
    })
    
    const response = await request(app)
      .get('/users/userId/12345/recommendedUsers')
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(200);
    expect(response.body).toContainObject({ name: 'Jane Doe' });
    expect(response.body).toContainObject({ name: 'Jacob Doe' });
  });
});