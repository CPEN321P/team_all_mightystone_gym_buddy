const request = require('supertest');
const app = require('../app.js');
const { getDB } = require('../MongoDB/Connect.js');
const {clearData} = require('../Utils/userUtils.js')
const {createId} = require('../Utils/mongoUtils.js')

jest.mock('../MongoDB/Connect.js');
jest.mock('../Utils/userUtils.js');
jest.mock('../Utils/mongoUtils.js');

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
  // Input: mock user
  // Expected status code: 200
  // Expected behaviour: user added to database
  // Expected output: the inserted Id of the new user
  it('User is added to the database', async () => {
    const mockDB = {
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

  // Input: mock user
  // Expected status code: 500
  // Expected behaviour: user not added to database
  // Expected output: error message
  it('User is not added to the database', async () => {
    const mockDB = {
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

// ChatGPT use: No
describe('Get all users', () => {
  // Input: none
  // Expected status code: 200
  // Expected behaviour: all users are retrieved
  // Expected output: list of all users
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

    const mockDB = {
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

  // Input: none
  // Expected status code: 500
  // Expected behaviour: all users not retrieved
  // Expected output: error message
  it('There is an error retrieving the users from the database', async () => {
    const mockDB = {
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

// ChatGPT use: No
describe('Get a specific user by ID', () => {
  // Input: User Id
  // Expected status code: 200
  // Expected behaviour: user retrieved
  // Expected output: user object
  it('User is retrieved', async () => {
    const mockUser = {
      name: 'John Doe',
      email: 'john.doe@example.com',
    };

    const mockDB = {
        collection: jest.fn().mockReturnThis(),
        findOne: jest.fn().mockReturnValue(mockUser),
    };
      
    getDB.mockReturnValue(mockDB);

    createId.mockImplementation((id) => {
      return id;
    })
    
    const response = await request(app)
      .get('/users/userId/123456')
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(200);
    expect(response.body.name).toBe(mockUser.name);
    expect(response.body.email).toBe(mockUser.email);
  });

  // Input: User Id
  // Expected status code: 404
  // Expected behaviour: error thrown by createId()
  // Expected output: error message
  it('Invalid user ID', async () => {
    const mockUser = {
      name: 'John Doe',
      email: 'john.doe@example.com',
    };

    const mockDB = {
        collection: jest.fn().mockReturnThis(),
        findOne: jest.fn().mockReturnValue(mockUser),
    };
      
    getDB.mockReturnValue(mockDB);

    createId.mockImplementation((id) => {
      throw new Error('');
    })
    
    const response = await request(app)
      .get('/users/userId/123456')
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(500);
    expect(response.body).toBe('Invalid user ID');
  });

  // Input: User Id
  // Expected status code: 404
  // Expected behaviour: user not found
  // Expected output: error message
  it('Error retrieving user from database', async () => {

    const mockDB = {
        collection: jest.fn().mockReturnThis(),
        findOne: jest.fn().mockReturnValue(null),
    };
      
    getDB.mockReturnValue(mockDB);

    createId.mockImplementation((id) => {
      return id;
    })
    
    const response = await request(app)
      .get('/users/userId/123456')
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(404);
    expect(response.body).toBe('User not found');
  });
});

// ChatGPT use: No
describe('Get a specific user by email', () => {
  // Input: User Email
  // Expected status code: 200
  // Expected behaviour: user retrieved
  // Expected output: user object
  it('User is retrieved', async () => {
    const mockUser = {
      name: 'John Doe',
      email: 'john.doe@example.com',
    };

    const mockDB = {
        collection: jest.fn().mockReturnThis(),
        findOne: jest.fn().mockReturnValue(mockUser),
    };
      
    getDB.mockReturnValue(mockDB);

    createId.mockImplementation((id) => {
      return id;
    })
    
    const response = await request(app)
      .get('/users/userEmail/example@test.com')
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(200);
    expect(response.body.name).toBe(mockUser.name);
    expect(response.body.email).toBe(mockUser.email);
  });

  // Input: User Email
  // Expected status code: 404
  // Expected behaviour: user not found
  // Expected output: error message
  it('Error retrieving user from database', async () => {

    const mockDB = {
        collection: jest.fn().mockReturnThis(),
        findOne: jest.fn().mockReturnValue(null),
    };
      
    getDB.mockReturnValue(mockDB);

    createId.mockImplementation((id) => {
      return id;
    })
    
    const response = await request(app)
      .get('/users/userEmail/example@test.com')
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(404);
    expect(response.body).toBe('User not found');
  });
});

// ChatGPT use: No
describe('Get recommended users', () => {
  // Input: User Id
  // Expected status code: 500
  // Expected behaviour: error thrown by createId()
  // Expected output: error message
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

    const mockDB = {
        collection: jest.fn().mockReturnThis(),
        find: jest.fn().mockReturnThis(),
        findOne: jest.fn().mockReturnValue(mockUser),
        toArray: jest.fn().mockReturnValue(retrievedUsers),
    };
      
    getDB.mockReturnValue(mockDB);

    createId.mockImplementation((id) => {
      throw new Error('');
    })
    
    const response = await request(app)
      .get('/users/userId/12345/recommendedUsers')
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(500);
    expect(response.body).toBe('Users not retrieved');
  });

  // Input: User Id
  // Expected status code: 404
  // Expected behaviour: user not found
  // Expected output: error message
  it('User not found', async () => {

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

    const mockDB = {
        collection: jest.fn().mockReturnThis(),
        find: jest.fn().mockReturnThis(),
        findOne: jest.fn().mockReturnValue(null),
        toArray: jest.fn().mockReturnValue(retrievedUsers),
    };
      
    getDB.mockReturnValue(mockDB);

    createId.mockImplementation((id) => {
      return id;
    })
    
    const response = await request(app)
      .get('/users/userId/12345/recommendedUsers')
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(404);
    expect(response.body).toBe('User not found');
  });

  // Input: User Id
  // Expected status code: 200
  // Expected behaviour: user retrieved
  // Expected output: user object
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

    const mockDB = {
        collection: jest.fn().mockReturnThis(),
        find: jest.fn().mockReturnThis(),
        findOne: jest.fn().mockReturnValue(mockUser),
        toArray: jest.fn().mockReturnValue(retrievedUsers),
    };
      
    getDB.mockReturnValue(mockDB);

    createId.mockImplementation((id) => {
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

// ChatGPT use: No
describe('Get friends', () => {
  // Input: User Id
  // Expected status code: 500
  // Expected behaviour: error thrown by createId()
  // Expected output: error message
  it('Invalid user ID', async () => {
    const users = [
      {
        _id: 12345,
        name: 'John Doe',
        email: 'john.doe@example.com',
        blockedUsers: [],
        friends: [
          23456,
          34567
        ]
      },
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

    const mockDB = {
        collection: jest.fn().mockReturnThis(),
        find: jest.fn().mockReturnThis(),
        findOne: jest.fn().mockImplementation((param) => {
          const id = param._id;
          for (const user of users) {
            if (user._id == id) {
              return user;
            }
          }
          return null;
        })
    };
      
    getDB.mockReturnValue(mockDB);

    createId.mockImplementation((id)=>{
      throw new Error('');
    });
    
    const response = await request(app)
      .get('/users/userId/12345/friends')
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(500);
    expect(response.body).toBe('Invalid user Id');
  });

  // Input: User Id
  // Expected status code: 404
  // Expected behaviour: user not found
  // Expected output: error message
  it('Cannot find user', async () => {
    const users = [
      {
        _id: 12345,
        name: 'John Doe',
        email: 'john.doe@example.com',
        blockedUsers: [],
        friends: [
          23456,
          34567
        ]
      },
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

    const mockDB = {
        collection: jest.fn().mockReturnThis(),
        find: jest.fn().mockReturnThis(),
        findOne: jest.fn().mockImplementation((param) => {
          const id = param._id;
          for (const user of users) {

            if (user._id == id) {
              return user;
            }
          }
          return null;
        })
    };
      
    getDB.mockReturnValue(mockDB);

    // createId.mockImplementation((id) => {
    //   return id;
    // })
    createId.mockImplementation((id)=>{
      return id;
    });
    
    const response = await request(app)
      .get('/users/userId/123456/friends')
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(404);
    expect(response.body).toBe('User not found');
  });

  // Input: User Id
  // Expected status code: 200
  // Expected behaviour: users friends list retrieved
  // Expected output: user object
  it('Retrieved user friends', async () => {
    const users = [
      {
        _id: 12345,
        name: 'John Doe',
        email: 'john.doe@example.com',
        blockedUsers: [],
        friends: [
          23456
        ]
      },
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

    const mockDB = {
        collection: jest.fn().mockReturnThis(),
        find: jest.fn().mockReturnThis(),
        findOne: jest.fn().mockImplementation((param) => {
          const id = param._id;
          for (const user of users) {
            if (user._id == id) {
              return user;
            }
          }
          return null;
        })
    };
      
    getDB.mockReturnValue(mockDB);

    createId.mockImplementation((id) => {
      return id;
    })
    
    const response = await request(app)
      .get('/users/userId/12345/friends')
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(200);
    expect(response.body).toContainObject({ name: 'Jane Doe' });
    expect(response.body).not.toContainObject({ name: 'Jacob Doe' });
  });
});

// ChatGPT use: No
describe('Get blocked users', () => {
  // Input: User Id
  // Expected status code: 500
  // Expected behaviour: error thrown by createId()
  // Expected output: error message
  it('Invalid user ID', async () => {
    const users = [
      {
        _id: 12345,
        name: 'John Doe',
        email: 'john.doe@example.com',
        blockedUsers: [],
        friends: [
          23456,
          34567
        ]
      },
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

    const mockDB = {
        collection: jest.fn().mockReturnThis(),
        find: jest.fn().mockReturnThis(),
        findOne: jest.fn().mockImplementation((param) => {
          const id = param._id;
          for (const user of users) {
            if (user._id == id) {
              return user;
            }
          }
          return null;
        })
    };
      
    getDB.mockReturnValue(mockDB);

    createId.mockImplementation((id) => {
      throw new Error('');
    })
    
    const response = await request(app)
      .get('/users/userId/12345/blockedUsers')
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(500);
    expect(response.body).toBe('Invalid user Id');
  });

  // Input: User Id
  // Expected status code: 404
  // Expected behaviour: user not found
  // Expected output: error message
  it('Cannot find user', async () => {
    const users = [
      {
        _id: 12345,
        name: 'John Doe',
        email: 'john.doe@example.com',
        blockedUsers: [],
        friends: [
          23456,
          34567
        ],
        friendRequests: [
          23456,
          34567
        ]
      },
      {
        _id: 23456,
        name: 'Jane Doe',
        email: 'jane.doe@example.com',
        blockedUsers: [],
        friends: [],
        friendRequests: []
      },
      {
        _id: 34567,
        name: 'Jacob Doe',
        email: 'jacob.doe@example.com',
        blockedUsers: [],
        friends: [],
        friendRequests: []
      }
    ];

    const mockDB = {
        collection: jest.fn().mockReturnThis(),
        find: jest.fn().mockReturnThis(),
        findOne: jest.fn().mockImplementation((param) => {
          const id = param._id;
          for (const user of users) {

            if (user._id == id) {
              return user;
            }
          }
          return null;
        })
    };
      
    getDB.mockReturnValue(mockDB);

    createId.mockImplementation((id) => {
      return id;
    })
    
    const response = await request(app)
      .get('/users/userId/123456/blockedUsers')
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(404);
    expect(response.body).toBe('User not found');
  });

  // Input: User Id
  // Expected status code: 200
  // Expected behaviour: users blocked list retrieved
  // Expected output: user object
  it('Retrieved user friend requests', async () => {
    const users = [
      {
        _id: 12345,
        name: 'John Doe',
        email: 'john.doe@example.com',
        blockedUsers: [
          23456
        ],
        friends: [
          23456,
          34567
        ],
        friendRequests: [
          23456
        ]
      },
      {
        _id: 23456,
        name: 'Jane Doe',
        email: 'jane.doe@example.com',
        blockedUsers: [],
        friends: [],
        friendRequests: []
      },
      {
        _id: 34567,
        name: 'Jacob Doe',
        email: 'jacob.doe@example.com',
        blockedUsers: [],
        friends: [],
        friendRequests: []
      }
    ];

    const mockDB = {
        collection: jest.fn().mockReturnThis(),
        find: jest.fn().mockReturnThis(),
        findOne: jest.fn().mockImplementation((param) => {
          const id = param._id;
          for (const user of users) {
            if (user._id == id) {
              return user;
            }
          }
          return null;
        })
    };
      
    getDB.mockReturnValue(mockDB);

    createId.mockImplementation((id) => {
      return id;
    })
    
    const response = await request(app)
      .get('/users/userId/12345/blockedUsers')
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(200);
    expect(response.body).toContainObject({ name: 'Jane Doe' });
    expect(response.body).not.toContainObject({ name: 'Jacob Doe' });
  });
});

// ChatGPT use: No
describe('Update user', () => {
  // Input: User Id
  // Expected status code: 500
  // Expected behaviour: error thrown by ObjectId()
  // Expected output: error message
  it('Invalid user ID', async () => {
    const updatedUser = {
      name: 'John Doe II',
    }

    const users = [
      {
        _id: 12345,
        name: 'John Doe',
        email: 'john.doe@example.com',
        blockedUsers: [],
        friends: [
          23456,
          34567
        ]
      },
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

    const mockDB = {
        collection: jest.fn().mockReturnThis(),
        find: jest.fn().mockReturnThis(),
        findOne: jest.fn().mockImplementation((param) => {
          const id = param._id;
          for (const user of users) {
            if (user._id == id) {
              return user;
            }
          }
          return null;
        })
    };
      
    getDB.mockReturnValue(mockDB);

    createId.mockImplementation((id) => {
      throw new Error('');
    })
    
    const response = await request(app)
      .put('/users/userId/12345')
      .send(updatedUser)
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(500);
    expect(response.body).toBe('Invalid user Id');
  });

  // Input: User Id
  // Expected status code: 404
  // Expected behaviour: user not found
  // Expected output: error message
  it('User not found', async () => {
    const updatedUser = {
      name: 'John Doe II',
    }

    const users = [
      {
        _id: 12345,
        name: 'John Doe',
        email: 'john.doe@example.com',
        blockedUsers: [],
        friends: [
          23456,
          34567
        ]
      },
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

    const mockDB = {
        collection: jest.fn().mockReturnThis(),
        find: jest.fn().mockReturnThis(),
        findOne: jest.fn().mockImplementation((param) => {
          const id = param._id;
          for (const user of users) {
            if (user._id == id) {
              return user;
            }
          }
          return null;
        })
    };
      
    getDB.mockReturnValue(mockDB);

    createId.mockImplementation((id) => {
      return id;
    })
    
    const response = await request(app)
      .put('/users/userId/123456')
      .send(updatedUser)
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(404);
    expect(response.body).toBe('User not found');
  });

  // Input: User Id
  // Expected status code: 404
  // Expected behaviour: user not updated
  // Expected output: error message
  it('User not updated', async () => {
    const updatedUser = {
      name: 'John Doe II',
    }

    const users = [
      {
        _id: 12345,
        name: 'John Doe',
        email: 'john.doe@example.com',
        blockedUsers: [],
        friends: [
          23456,
          34567
        ]
      },
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

    const mockDB = {
        collection: jest.fn().mockReturnThis(),
        find: jest.fn().mockReturnThis(),
        findOne: jest.fn().mockImplementation((param) => {
          const id = param._id;
          for (const user of users) {
            if (user._id == id) {
              return user;
            }
          }
          return null;
        }),
        updateOne: jest.fn().mockReturnValue({ matchedCount: 0 })
    };
      
    getDB.mockReturnValue(mockDB);

    createId.mockImplementation((id) => {
      return id;
    })
    
    const response = await request(app)
      .put('/users/userId/12345')
      .send(updatedUser)
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(404);
    expect(response.body).toBe('User not updated');
  });

  // Input: User Id
  // Expected status code: 200
  // Expected behaviour: user updated
  // Expected output: updated user object
  it('User updated', async () => {
    const updatedUser = {
      name: 'John Doe II',
    }

    const users = [
      {
        _id: 12345,
        name: 'John Doe',
        email: 'john.doe@example.com',
        blockedUsers: [],
        friends: [
          23456,
          34567
        ]
      },
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

    const mockDB = {
        collection: jest.fn().mockReturnThis(),
        find: jest.fn().mockReturnThis(),
        findOne: jest.fn().mockImplementation((param) => {
          const id = param._id;
          for (const user of users) {
            if (user._id == id) {
              return user;
            }
          }
          return null;
        }),
        updateOne: jest.fn().mockReturnValue({ matchedCount: 1 })
    };
      
    getDB.mockReturnValue(mockDB);

    createId.mockImplementation((id) => {
      return id;
    })
    
    const response = await request(app)
      .put('/users/userId/12345')
      .send(updatedUser)
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(200);
    expect(response.body.name).toBe('John Doe II');
  });
});

// ChatGPT use: No
describe('Add friend', () => {
  // Input: User Ids
  // Expected status code: 500
  // Expected behaviour: error thrown by createId()
  // Expected output: error message
  it('Invalid user ID', async () => {
    const users = [
      {
        _id: 12345,
        name: 'John Doe',
        email: 'john.doe@example.com',
        blockedUsers: [],
        friends: [
          23456
        ]
      },
      {
        _id: 23456,
        name: 'Jane Doe',
        email: 'jane.doe@example.com',
        blockedUsers: [],
        friends: [
          12345
        ]
      },
      {
        _id: 34567,
        name: 'Jacob Doe',
        email: 'jacob.doe@example.com',
        blockedUsers: [],
        friends: []
      }
    ];

    const mockDB = {
        collection: jest.fn().mockReturnThis(),
        find: jest.fn().mockReturnThis(),
        findOne: jest.fn().mockImplementation((param) => {
          const id = param._id;
          for (const user of users) {
            if (user._id == id) {
              return user;
            }
          }
          return null;
        })
    };
      
    getDB.mockReturnValue(mockDB);

    createId.mockImplementation((id) => {
      throw new Error('');
    })
    
    const response = await request(app)
      .put('/users/addFriend/23456/12345')
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(500);
    expect(response.body).toBe('Invalid user Id');
  });

  // Input: User Ids
  // Expected status code: 404
  // Expected behaviour: user not found
  // Expected output: error message
  it('User not found', async () => {
    const users = [
      {
        _id: 12345,
        name: 'John Doe',
        email: 'john.doe@example.com',
        blockedUsers: [],
        friends: [
          23456,
          34567
        ]
      },
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

    const mockDB = {
        collection: jest.fn().mockReturnThis(),
        find: jest.fn().mockReturnThis(),
        findOne: jest.fn().mockImplementation((param) => {
          const id = param._id;
          for (const user of users) {
            if (user._id == id) {
              return user;
            }
          }
          return null;
        })
    };
      
    getDB.mockReturnValue(mockDB);

    createId.mockImplementation((id) => {
      return id;
    })
    
    const response = await request(app)
      .put('/users/addFriend/123456/12345')
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(404);
    expect(response.body).toBe('User not found');
  });

  // Input: User Ids
  // Expected status code: 500
  // Expected behaviour: user not friended
  // Expected output: error message
  it('Already friends', async () => {
    const users = [
      {
        _id: 12345,
        name: 'John Doe',
        email: 'john.doe@example.com',
        blockedUsers: [],
        friends: [
          '23456'
        ]
      },
      {
        _id: 23456,
        name: 'Jane Doe',
        email: 'jane.doe@example.com',
        blockedUsers: [
          '12345'
        ],
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

    const mockDB = {
        collection: jest.fn().mockReturnThis(),
        find: jest.fn().mockReturnThis(),
        findOne: jest.fn().mockImplementation((param) => {
          const id = param._id;
          for (const user of users) {
            if (user._id == id) {
              return user;
            }
          }
          return null;
        }),
        updateOne: jest.fn().mockReturnValue({ matchedCount: 0 })
    };
      
    getDB.mockReturnValue(mockDB);

    createId.mockImplementation((id) => {
      return id;
    })
    
    const response = await request(app)
      .put('/users/addFriend/23456/12345')
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(500);
    expect(response.body).toBe('Already friends');
  });

  // Input: User Ids
  // Expected status code: 200
  // Expected behaviour: friend added
  // Expected output: success message
  it('Friends added', async () => {
    const users = [
      {
        _id: 12345,
        name: 'John Doe',
        email: 'john.doe@example.com',
        blockedUsers: [],
        friends: []
      },
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

    const mockDB = {
        collection: jest.fn().mockReturnThis(),
        find: jest.fn().mockReturnThis(),
        findOne: jest.fn().mockImplementation((param) => {
          const id = param._id;
          for (const user of users) {
            if (user._id == id) {
              return user;
            }
          }
          return null;
        }),
        updateOne: jest.fn().mockReturnValue({ matchedCount: 0 })
    };
      
    getDB.mockReturnValue(mockDB);

    createId.mockImplementation((id) => {
      return id;
    })
    
    const response = await request(app)
      .put('/users/addFriend/23456/12345')
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(200);
    expect(response.body).toBe('Friend added');
  });
});

// ChatGPT use: No
describe('Block user', () => {
  // Input: User Ids
  // Expected status code: 500
  // Expected behaviour: error thrown by createId()
  // Expected output: error message
  it('Invalid user ID', async () => {
    const users = [
      {
        _id: 12345,
        name: 'John Doe',
        email: 'john.doe@example.com',
        blockedUsers: [],
        friends: []
      },
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

    const mockDB = {
        collection: jest.fn().mockReturnThis(),
        find: jest.fn().mockReturnThis(),
        findOne: jest.fn().mockImplementation((param) => {
          const id = param._id;
          for (const user of users) {
            if (user._id == id) {
              return user;
            }
          }
          return null;
        })
    };
      
    getDB.mockReturnValue(mockDB);

    createId.mockImplementation((id) => {
      throw new Error('');
    })
    
    const response = await request(app)
      .put('/users/blockUser/23456/12345')
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(500);
    expect(response.body).toBe('Invalid user ID');
  });

  // Input: User Ids
  // Expected status code: 404
  // Expected behaviour: user not found
  // Expected output: error message
  it('User not found', async () => {
    const users = [
      {
        _id: 12345,
        name: 'John Doe',
        email: 'john.doe@example.com',
        blockedUsers: [],
        friends: []
      },
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

    const mockDB = {
        collection: jest.fn().mockReturnThis(),
        find: jest.fn().mockReturnThis(),
        findOne: jest.fn().mockImplementation((param) => {
          const id = param._id;
          for (const user of users) {
            if (user._id == id) {
              return user;
            }
          }
          return null;
        })
    };
      
    getDB.mockReturnValue(mockDB);

    createId.mockImplementation((id) => {
      return id;
    })
    
    const response = await request(app)
      .put('/users/blockUser/123456/123456')
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(404);
    expect(response.body).toBe('User not found');
  });

  // Input: User Ids
  // Expected status code: 500
  // Expected behaviour: user not blocked
  // Expected output: error message
  it('User not blocked', async () => {
    const users = [
      {
        _id: 12345,
        name: 'John Doe',
        email: 'john.doe@example.com',
        blockedUsers: [],
        friends: [],
        friendRequests: []
      },
      {
        _id: 23456,
        name: 'Jane Doe',
        email: 'jane.doe@example.com',
        blockedUsers: [],
        friends: [
          12345
        ],
        friendRequests: []
      },
      {
        _id: 34567,
        name: 'Jacob Doe',
        email: 'jacob.doe@example.com',
        blockedUsers: [],
        friends: [],
        friendRequests: []
      }
    ];

    const mockDB = {
        collection: jest.fn().mockReturnThis(),
        find: jest.fn().mockReturnThis(),
        findOne: jest.fn().mockImplementation((param) => {
          const id = param._id;
          for (const user of users) {
            if (user._id == id) {
              return user;
            }
          }
          return null;
        }),
        updateOne: jest.fn().mockReturnValue({ matchedCount: 0 })
    };
      
    getDB.mockReturnValue(mockDB);

    createId.mockImplementation((id) => {
      return id;
    })
    
    const response = await request(app)
      .put('/users/blockUser/23456/12345')
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(500);
    expect(response.body).toBe('User not blocked');
  });

  // Input: User Ids
  // Expected status code: 200
  // Expected behaviour: user blocked
  // Expected output: error message
  it('User blocked', async () => {
    const users = [
      {
        _id: 12345,
        name: 'John Doe',
        email: 'john.doe@example.com',
        blockedUsers: [],
        friends: [
          23456
        ],
        friendRequests: []
      },
      {
        _id: 23456,
        name: 'Jane Doe',
        email: 'jane.doe@example.com',
        blockedUsers: [],
        friends: [
          12345
        ],
        friendRequests: []
      },
      {
        _id: 34567,
        name: 'Jacob Doe',
        email: 'jacob.doe@example.com',
        blockedUsers: [],
        friends: [],
        friendRequests: []
      }
    ];

    const mockDB = {
        collection: jest.fn().mockReturnThis(),
        find: jest.fn().mockReturnThis(),
        findOne: jest.fn().mockImplementation((param) => {
          const id = param._id;
          for (const user of users) {
            if (user._id == id) {
              return user;
            }
          }
          return null;
        }),
        updateOne: jest.fn().mockReturnValue({ matchedCount: 1 })
    };
      
    getDB.mockReturnValue(mockDB);

    createId.mockImplementation((id) => {
      return id;
    })
    
    const response = await request(app)
      .put('/users/blockUser/23456/12345')
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(200);
    expect(response.body).toBe('User blocked');
  });
});

// ChatGPT use: No
describe('Unblock user', () => {
  // Input: User Ids
  // Expected status code: 500
  // Expected behaviour: error thrown by createId()
  // Expected output: error message
  it('Invalid user ID', async () => {
    const users = [
      {
        _id: 12345,
        name: 'John Doe',
        email: 'john.doe@example.com',
        blockedUsers: [],
        friends: []
      },
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

    const mockDB = {
        collection: jest.fn().mockReturnThis(),
        find: jest.fn().mockReturnThis(),
        findOne: jest.fn().mockImplementation((param) => {
          const id = param._id;
          for (const user of users) {
            if (user._id == id) {
              return user;
            }
          }
          return null;
        })
    };
      
    getDB.mockReturnValue(mockDB);

    createId.mockImplementation((id) => {
      throw new Error('');
    })
    
    const response = await request(app)
      .put('/users/unblockUser/23456/12345')
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(500);
    expect(response.body).toBe('Invalid user ID');
  });

  // Input: User Ids
  // Expected status code: 404
  // Expected behaviour: user not found
  // Expected output: error message
  it('User not found', async () => {
    const users = [
      {
        _id: 12345,
        name: 'John Doe',
        email: 'john.doe@example.com',
        blockedUsers: [],
        friends: []
      },
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

    const mockDB = {
        collection: jest.fn().mockReturnThis(),
        find: jest.fn().mockReturnThis(),
        findOne: jest.fn().mockImplementation((param) => {
          const id = param._id;
          for (const user of users) {
            if (user._id == id) {
              return user;
            }
          }
          return null;
        })
    };
      
    getDB.mockReturnValue(mockDB);

    createId.mockImplementation((id) => {
      return id;
    })
    
    const response = await request(app)
      .put('/users/unblockUser/123456/123456')
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(404);
    expect(response.body).toBe('User not found');
  });

  // Input: User Ids
  // Expected status code: 500
  // Expected behaviour: user not in blocked list
  // Expected output: error message
  it('User not in blocked list', async () => {
    const users = [
      {
        _id: 12345,
        name: 'John Doe',
        email: 'john.doe@example.com',
        blockedUsers: [],
        friends: [],
        friendRequests: []
      },
      {
        _id: 23456,
        name: 'Jane Doe',
        email: 'jane.doe@example.com',
        blockedUsers: [],
        friends: [
          12345
        ],
        friendRequests: []
      },
      {
        _id: 34567,
        name: 'Jacob Doe',
        email: 'jacob.doe@example.com',
        blockedUsers: [],
        friends: [],
        friendRequests: []
      }
    ];

    const mockDB = {
        collection: jest.fn().mockReturnThis(),
        find: jest.fn().mockReturnThis(),
        findOne: jest.fn().mockImplementation((param) => {
          const id = param._id;
          for (const user of users) {
            if (user._id == id) {
              return user;
            }
          }
          return null;
        }),
        updateOne: jest.fn().mockReturnValue({ matchedCount: 0 })
    };
      
    getDB.mockReturnValue(mockDB);

    createId.mockImplementation((id) => {
      return id;
    })
    
    const response = await request(app)
      .put('/users/unblockUser/23456/12345')
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(500);
    expect(response.body).toBe('User not in blocked list');
  });

  // Input: User Ids
  // Expected status code: 500
  // Expected behaviour: user not unblocked
  // Expected output: error message
  it('User not unblocked', async () => {
    const users = [
      {
        _id: 12345,
        name: 'John Doe',
        email: 'john.doe@example.com',
        blockedUsers: [],
        friends: [],
        friendRequests: []
      },
      {
        _id: 23456,
        name: 'Jane Doe',
        email: 'jane.doe@example.com',
        blockedUsers: [
          12345
        ],
        friends: [],
        friendRequests: []
      },
      {
        _id: 34567,
        name: 'Jacob Doe',
        email: 'jacob.doe@example.com',
        blockedUsers: [],
        friends: [],
        friendRequests: []
      }
    ];

    const mockDB = {
        collection: jest.fn().mockReturnThis(),
        find: jest.fn().mockReturnThis(),
        findOne: jest.fn().mockImplementation((param) => {
          const id = param._id;
          for (const user of users) {
            if (user._id == id) {
              return user;
            }
          }
          return null;
        }),
        updateOne: jest.fn().mockReturnValue({ matchedCount: 0 })
    };
      
    getDB.mockReturnValue(mockDB);

    createId.mockImplementation((id) => {
      return id;
    })
    
    const response = await request(app)
      .put('/users/unblockUser/23456/12345')
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(500);
    expect(response.body).toBe('User not unblocked');
  });

  // Input: User Ids
  // Expected status code: 200
  // Expected behaviour: user unblocked
  // Expected output: success message
  it('User unblocked', async () => {
    const users = [
      {
        _id: 12345,
        name: 'John Doe',
        email: 'john.doe@example.com',
        blockedUsers: [],
        friends: [],
        friendRequests: []
      },
      {
        _id: 23456,
        name: 'Jane Doe',
        email: 'jane.doe@example.com',
        blockedUsers: [
          12345
        ],
        friends: [],
        friendRequests: []
      },
      {
        _id: 34567,
        name: 'Jacob Doe',
        email: 'jacob.doe@example.com',
        blockedUsers: [],
        friends: [],
        friendRequests: []
      }
    ];

    const mockDB = {
        collection: jest.fn().mockReturnThis(),
        find: jest.fn().mockReturnThis(),
        findOne: jest.fn().mockImplementation((param) => {
          const id = param._id;
          for (const user of users) {
            if (user._id == id) {
              return user;
            }
          }
          return null;
        }),
        updateOne: jest.fn().mockReturnValue({ matchedCount: 1 })
    };
      
    getDB.mockReturnValue(mockDB);

    createId.mockImplementation((id) => {
      return id;
    })
    
    const response = await request(app)
      .put('/users/unblockUser/23456/12345')
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(200);
    expect(response.body).toBe('User unblocked');
  });
});

// ChatGPT use: No
describe('Delete a chat', () => {
  // Input: User Id, Chat Id
  // Expected status code: 500
  // Expected behaviour: error thrown by createId()
  // Expected output: error message
  it('Invalid user ID', async () => {
    const users = [
      {
        _id: 12345,
        name: 'John Doe',
        email: 'john.doe@example.com',
        blockedUsers: [],
        friends: []
      },
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

    const mockDB = {
        collection: jest.fn().mockReturnThis(),
        find: jest.fn().mockReturnThis(),
        findOne: jest.fn().mockImplementation((param) => {
          const id = param._id;
          for (const user of users) {
            if (user._id == id) {
              return user;
            }
          }
          return null;
        })
    };
      
    getDB.mockReturnValue(mockDB);

    createId.mockImplementation((id) => {
      throw new Error('');
    })
    
    const response = await request(app)
      .put('/users/userId/12345/deleteChat/23456')
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(500);
    expect(response.body).toBe('Invalid user ID');
  });

  // Input: User Id, Chat Id
  // Expected status code: 404
  // Expected behaviour: user not found
  // Expected output: error message
  it('User not found', async () => {
    const users = [
      {
        _id: 12345,
        name: 'John Doe',
        email: 'john.doe@example.com',
        blockedUsers: [],
        friends: []
      },
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

    const mockDB = {
        collection: jest.fn().mockReturnThis(),
        find: jest.fn().mockReturnThis(),
        findOne: jest.fn().mockImplementation((param) => {
          const id = param._id;
          for (const user of users) {
            if (user._id == id) {
              return user;
            }
          }
          return null;
        })
    };
      
    getDB.mockReturnValue(mockDB);

    createId.mockImplementation((id) => {
      return id;
    })
    
    const response = await request(app)
      .put('/users/userId/123456/deleteChat/23456')
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(404);
    expect(response.body).toBe('User not found');
  });

  // Input: User Id, Chat Id
  // Expected status code: 404
  // Expected behaviour: chat not is user chats list
  // Expected output: error message
  it('Chat not in list', async () => {
    const users = [
      {
        _id: 12345,
        name: 'John Doe',
        email: 'john.doe@example.com',
        blockedUsers: [],
        friends: [],
        friendRequests: [],
        chats: []
      },
      {
        _id: 23456,
        name: 'Jane Doe',
        email: 'jane.doe@example.com',
        blockedUsers: [],
        friends: [
          12345
        ],
        friendRequests: [],
        chats: []
      },
      {
        _id: 34567,
        name: 'Jacob Doe',
        email: 'jacob.doe@example.com',
        blockedUsers: [],
        friends: [],
        friendRequests: [],
        chats: []
      }
    ];

    const mockDB = {
        collection: jest.fn().mockReturnThis(),
        find: jest.fn().mockReturnThis(),
        findOne: jest.fn().mockImplementation((param) => {
          const id = param._id;
          for (const user of users) {
            if (user._id == id) {
              return user;
            }
          }
          return null;
        }),
        updateOne: jest.fn().mockReturnValue({ matchedCount: 0 })
    };
      
    getDB.mockReturnValue(mockDB);

    createId.mockImplementation((id) => {
      return id;
    })
    
    const response = await request(app)
      .put('/users/userId/12345/deleteChat/23456')
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(404);
    expect(response.body).toBe('Chat not in list');
  });

  // Input: User Id, Chat Id
  // Expected status code: 500
  // Expected behaviour: chat not deleted
  // Expected output: error message
  it('Chat not deleted', async () => {
    const users = [
      {
        _id: 12345,
        name: 'John Doe',
        email: 'john.doe@example.com',
        blockedUsers: [],
        friends: [],
        friendRequests: [],
        chats: [
          23456
        ]
      },
      {
        _id: 23456,
        name: 'Jane Doe',
        email: 'jane.doe@example.com',
        blockedUsers: [],
        friends: [
          12345
        ],
        friendRequests: [],
        chats: []
      },
      {
        _id: 34567,
        name: 'Jacob Doe',
        email: 'jacob.doe@example.com',
        blockedUsers: [],
        friends: [],
        friendRequests: [],
        chats: []
      }
    ];

    const mockDB = {
        collection: jest.fn().mockReturnThis(),
        find: jest.fn().mockReturnThis(),
        findOne: jest.fn().mockImplementation((param) => {
          const id = param._id;
          for (const user of users) {
            if (user._id == id) {
              return user;
            }
          }
          return null;
        }),
        updateOne: jest.fn().mockReturnValue({ matchedCount: 0 })
    };
      
    getDB.mockReturnValue(mockDB);

    createId.mockImplementation((id) => {
      return id;
    })
    
    const response = await request(app)
      .put('/users/userId/12345/deleteChat/23456')
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(500);
    expect(response.body).toBe('Chat not deleted');
  });

  // Input: User Id, Chat Id
  // Expected status code: 200
  // Expected behaviour: chat deleted
  // Expected output: success message
  it('Chat deleted', async () => {
    const users = [
      {
        _id: 12345,
        name: 'John Doe',
        email: 'john.doe@example.com',
        blockedUsers: [],
        friends: [],
        friendRequests: [],
        chats: [
          23456
        ]
      },
      {
        _id: 23456,
        name: 'Jane Doe',
        email: 'jane.doe@example.com',
        blockedUsers: [],
        friends: [
          12345
        ],
        friendRequests: [],
        chats: []
      },
      {
        _id: 34567,
        name: 'Jacob Doe',
        email: 'jacob.doe@example.com',
        blockedUsers: [],
        friends: [],
        friendRequests: [],
        chats: []
      }
    ];

    const mockDB = {
        collection: jest.fn().mockReturnThis(),
        find: jest.fn().mockReturnThis(),
        findOne: jest.fn().mockImplementation((param) => {
          const id = param._id;
          for (const user of users) {
            if (user._id == id) {
              return user;
            }
          }
          return null;
        }),
        updateOne: jest.fn().mockReturnValue({ matchedCount: 1 })
    };
      
    getDB.mockReturnValue(mockDB);

    createId.mockImplementation((id) => {
      return id;
    })
    
    const response = await request(app)
      .put('/users/userId/12345/deleteChat/23456')
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(200);
    expect(response.body).toBe('Chat deleted');
  });
});

// ChatGPT use: No
describe('Delete a user', () => {
  // Input: User Id
  // Expected status code: 500
  // Expected behaviour: error thrown by createId()
  // Expected output: error message
  it('Invalid user ID', async () => {
    const users = [
      {
        _id: 12345,
        name: 'John Doe',
        email: 'john.doe@example.com',
        blockedUsers: [],
        friends: []
      },
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

    const mockDB = {
        collection: jest.fn().mockReturnThis(),
        find: jest.fn().mockReturnThis(),
        findOne: jest.fn().mockImplementation((param) => {
          const id = param._id;
          for (const user of users) {
            if (user._id == id) {
              return user;
            }
          }
          return null;
        })
    };
      
    getDB.mockReturnValue(mockDB);

    createId.mockImplementation((id) => {
      throw new Error('');
    })
    
    const response = await request(app)
      .delete('/users/userId/12345')
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(500);
    expect(response.body).toBe('Invalid user Id');
  });

  // Input: User Id
  // Expected status code: 500
  // Expected behaviour: user not found
  // Expected output: error message
  it('User not found', async () => {
    const users = [
      {
        _id: 12345,
        name: 'John Doe',
        email: 'john.doe@example.com',
        blockedUsers: [],
        friends: [
          23456,
          34567
        ]
      },
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

    const mockDB = {
        collection: jest.fn().mockReturnThis(),
        find: jest.fn().mockReturnThis(),
        findOne: jest.fn().mockImplementation((param) => {
          const id = param._id;
          for (const user of users) {
            if (user._id == id) {
              return user;
            }
          }
          return null;
        })
    };
      
    getDB.mockReturnValue(mockDB);

    createId.mockImplementation((id) => {
      return id;
    })
    
    const response = await request(app)
      .delete('/users/userId/123456')
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(500);
    expect(response.body).toBe('User data not cleared');
  });

  // Input: User Id
  // Expected status code: 404
  // Expected behaviour: user not deleted
  // Expected output: error message
  it('User not deleted', async () => {
    const users = [
      {
        _id: 12345,
        name: 'John Doe',
        email: 'john.doe@example.com',
        blockedUsers: [],
        friends: [],
        chats: []
      },
      {
        _id: 23456,
        name: 'Jane Doe',
        email: 'jane.doe@example.com',
        blockedUsers: [],
        friends: [],
        chats: []
      },
      {
        _id: 34567,
        name: 'Jacob Doe',
        email: 'jacob.doe@example.com',
        blockedUsers: [],
        friends: [],
        chats: []
      }
    ];

    const mockDB = {
        collection: jest.fn().mockReturnThis(),
        find: jest.fn().mockReturnThis(),
        findOne: jest.fn().mockImplementation((param) => {
          const id = param._id;
          for (const user of users) {
            if (user._id == id) {
              return user;
            }
          }
          return null;
        }),
        deleteOne: jest.fn().mockReturnValue({ deletedCount: 0 }),
        deleteMany: jest.fn().mockReturnValue(true)
    };
      
    getDB.mockReturnValue(mockDB);

    clearData.mockImplementation((db,id)=>{
      return true;
    });

    createId.mockImplementation((id) => {
      return id;
    });
    
    const response = await request(app)
      .delete('/users/userId/12345')
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(404);
    expect(response.body).toBe('User not deleted');
  });

  // Input: User Id
  // Expected status code: 200
  // Expected behaviour: user deleted
  // Expected output: success message
  it('User deleted', async () => {
    const users = [
      {
        _id: 12345,
        name: 'John Doe',
        email: 'john.doe@example.com',
        blockedUsers: [],
        friends: [],
        chats: [
          {
            chatId: 1
          },
          {
            chatId: 2
          },
          {
            chatId: 3
          }
        ]
      },
      {
        _id: 23456,
        name: 'Jane Doe',
        email: 'jane.doe@example.com',
        blockedUsers: [],
        friends: [],
        chats: [
          {
            chatId: 1
          }
        ]
      },
      {
        _id: 34567,
        name: 'Jacob Doe',
        email: 'jacob.doe@example.com',
        blockedUsers: [],
        friends: [],
        chats: []
      }
    ];

    const mockDB = {
        collection: jest.fn().mockReturnThis(),
        find: jest.fn().mockReturnThis(),
        findOne: jest.fn().mockImplementation((param) => {
          const id = param._id;
          for (const user of users) {
            if (user._id == id) {
              return user;
            }
          }
          return null;
        }),
        deleteOne: jest.fn().mockReturnValue({ deletedCount: 1 }),
    };
    clearData.mockImplementation((db,id)=>{
      return true;
    })  
    getDB.mockReturnValue(mockDB);

    createId.mockImplementation((id) => {
      return id;
    })
    
    const response = await request(app)
      .delete('/users/userId/12345')
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(200);
    expect(response.body).toBe('User deleted');
  });
});