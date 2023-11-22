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

describe('Get friends', () => {
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

    mockDB = {
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

    ObjectId.mockImplementation((id) => {
      throw new error();
    })
    
    const response = await request(app)
      .get('/users/userId/12345/friends')
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(500);
    expect(response.body).toBe('Invalid user Id');
  });

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

    mockDB = {
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

    ObjectId.mockImplementation((id) => {
      return id;
    })
    
    const response = await request(app)
      .get('/users/userId/123456/friends')
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(404);
    expect(response.body).toBe('User not found');
  });

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

    mockDB = {
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

    ObjectId.mockImplementation((id) => {
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

describe('Get friend requests', () => {
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

    mockDB = {
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

    ObjectId.mockImplementation((id) => {
      throw new error();
    })
    
    const response = await request(app)
      .get('/users/userId/12345/friendRequests')
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(500);
    expect(response.body).toBe('Invalid user Id');
  });

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

    mockDB = {
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

    ObjectId.mockImplementation((id) => {
      return id;
    })
    
    const response = await request(app)
      .get('/users/userId/123456/friendRequests')
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(404);
    expect(response.body).toBe('User not found');
  });

  it('Retrieved user friend requests', async () => {
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

    mockDB = {
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

    ObjectId.mockImplementation((id) => {
      return id;
    })
    
    const response = await request(app)
      .get('/users/userId/12345/friendRequests')
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(200);
    expect(response.body).toContainObject({ name: 'Jane Doe' });
    expect(response.body).not.toContainObject({ name: 'Jacob Doe' });
  });
});

describe('Get blocked users', () => {
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

    mockDB = {
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

    ObjectId.mockImplementation((id) => {
      throw new error();
    })
    
    const response = await request(app)
      .get('/users/userId/12345/blockedUsers')
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(500);
    expect(response.body).toBe('Invalid user Id');
  });

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

    mockDB = {
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

    ObjectId.mockImplementation((id) => {
      return id;
    })
    
    const response = await request(app)
      .get('/users/userId/123456/blockedUsers')
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(404);
    expect(response.body).toBe('User not found');
  });

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

    mockDB = {
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

    ObjectId.mockImplementation((id) => {
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

describe('Update user', () => {
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

    mockDB = {
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

    ObjectId.mockImplementation((id) => {
      throw new error();
    })
    
    const response = await request(app)
      .put('/users/userId/12345')
      .send(updatedUser)
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(500);
    expect(response.body).toBe('Invalid user Id');
  });

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

    mockDB = {
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

    ObjectId.mockImplementation((id) => {
      return id;
    })
    
    const response = await request(app)
      .put('/users/userId/123456')
      .send(updatedUser)
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(404);
    expect(response.body).toBe('User not found');
  });

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

    mockDB = {
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
        updateOne: jest.fn().mockImplementation((param) => {
          return { matchedCount: 0 }
        })
    };
      
    getDB.mockReturnValue(mockDB);

    ObjectId.mockImplementation((id) => {
      return id;
    })
    
    const response = await request(app)
      .put('/users/userId/12345')
      .send(updatedUser)
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(404);
    expect(response.body).toBe('User not updated');
  });

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

    mockDB = {
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
        updateOne: jest.fn().mockImplementation((param) => {
          return { matchedCount: 1 }
        })
    };
      
    getDB.mockReturnValue(mockDB);

    ObjectId.mockImplementation((id) => {
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

describe('Add friend', () => {
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

    mockDB = {
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

    ObjectId.mockImplementation((id) => {
      throw new error();
    })
    
    const response = await request(app)
      .put('/users/addFriend/23456/12345')
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(500);
    expect(response.body).toBe('Invalid user Id');
  });

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

    mockDB = {
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

    ObjectId.mockImplementation((id) => {
      return id;
    })
    
    const response = await request(app)
      .put('/users/addFriend/123456/12345')
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(404);
    expect(response.body).toBe('User not found');
  });

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

    mockDB = {
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
        updateOne: jest.fn().mockImplementation((param) => {
          return { matchedCount: 0 }
        })
    };
      
    getDB.mockReturnValue(mockDB);

    ObjectId.mockImplementation((id) => {
      return id;
    })
    
    const response = await request(app)
      .put('/users/addFriend/23456/12345')
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(500);
    expect(response.body).toBe('Already friends');
  });

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

    mockDB = {
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
        updateOne: jest.fn().mockImplementation((param) => {
          return { matchedCount: 0 }
        })
    };
      
    getDB.mockReturnValue(mockDB);

    ObjectId.mockImplementation((id) => {
      return id;
    })
    
    const response = await request(app)
      .put('/users/addFriend/23456/12345')
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(200);
    expect(response.body).toBe('Friend added');
  });
});

describe('Send friend request', () => {
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

    mockDB = {
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

    ObjectId.mockImplementation((id) => {
      throw new error();
    })
    
    const response = await request(app)
      .put('/users/sendFriendRequest/23456/12345')
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(500);
    expect(response.body).toBe('Invalid user Id');
  });

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

    mockDB = {
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

    ObjectId.mockImplementation((id) => {
      return id;
    })
    
    const response = await request(app)
      .put('/users/sendFriendRequest/23456/123456')
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(404);
    expect(response.body).toBe('User not found');
  });

  it('Friend request already sent', async () => {
    const users = [
      {
        _id: 12345,
        name: 'John Doe',
        email: 'john.doe@example.com',
        blockedUsers: [],
        friends: [],
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

    mockDB = {
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
        updateOne: jest.fn().mockImplementation((param) => {
          return { matchedCount: 0 }
        })
    };
      
    getDB.mockReturnValue(mockDB);

    ObjectId.mockImplementation((id) => {
      return id;
    })
    
    const response = await request(app)
      .put('/users/sendFriendRequest/23456/12345')
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(500);
    expect(response.body).toBe('Friend request already sent');
  });

  it('Already friends', async () => {
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

    mockDB = {
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
        updateOne: jest.fn().mockImplementation((param) => {
          return { matchedCount: 0 }
        })
    };
      
    getDB.mockReturnValue(mockDB);

    ObjectId.mockImplementation((id) => {
      return id;
    })
    
    const response = await request(app)
      .put('/users/sendFriendRequest/23456/12345')
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(500);
    expect(response.body).toBe('Already friends');
  });

  it('Friend request not sent', async () => {
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

    mockDB = {
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
        updateOne: jest.fn().mockImplementation((param) => {
          return { matchedCount: 0 }
        })
    };
      
    getDB.mockReturnValue(mockDB);

    ObjectId.mockImplementation((id) => {
      return id;
    })
    
    const response = await request(app)
      .put('/users/sendFriendRequest/23456/12345')
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(500);
    expect(response.body).toBe('Friend Request Not Sent');
  });

  it('Friend request sent', async () => {
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

    mockDB = {
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
        updateOne: jest.fn().mockImplementation((param) => {
          return { matchedCount: 1 }
        })
    };
      
    getDB.mockReturnValue(mockDB);

    ObjectId.mockImplementation((id) => {
      return id;
    })
    
    const response = await request(app)
      .put('/users/sendFriendRequest/23456/12345')
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(200);
    expect(response.body).toBe('Friend Request Sent');
  });
});

describe('Unsend friend request', () => {
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

    mockDB = {
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

    ObjectId.mockImplementation((id) => {
      throw new error();
    })
    
    const response = await request(app)
      .put('/users/unsendFriendRequest/23456/12345')
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(500);
    expect(response.body).toBe('Invalid user Id');
  });

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

    mockDB = {
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

    ObjectId.mockImplementation((id) => {
      return id;
    })
    
    const response = await request(app)
      .put('/users/unsendFriendRequest/23456/123456')
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(404);
    expect(response.body).toBe('User not found');
  });

  it('No friend request', async () => {
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

    mockDB = {
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
        updateOne: jest.fn().mockImplementation((param) => {
          return { matchedCount: 0 }
        })
    };
      
    getDB.mockReturnValue(mockDB);

    ObjectId.mockImplementation((id) => {
      return id;
    })
    
    const response = await request(app)
      .put('/users/unsendFriendRequest/23456/12345')
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(500);
    expect(response.body).toBe('No Friend request');
  });

  it('Friend request not unsent', async () => {
    const users = [
      {
        _id: 12345,
        name: 'John Doe',
        email: 'john.doe@example.com',
        blockedUsers: [],
        friends: [],
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

    mockDB = {
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
        updateOne: jest.fn().mockImplementation((param) => {
          return { matchedCount: 0 }
        })
    };
      
    getDB.mockReturnValue(mockDB);

    ObjectId.mockImplementation((id) => {
      return id;
    })
    
    const response = await request(app)
      .put('/users/unsendFriendRequest/23456/12345')
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(500);
    expect(response.body).toBe('Friend Request Not Unsent');
  });

  it('Friend request sent', async () => {
    const users = [
      {
        _id: 12345,
        name: 'John Doe',
        email: 'john.doe@example.com',
        blockedUsers: [],
        friends: [],
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

    mockDB = {
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
        updateOne: jest.fn().mockImplementation((param) => {
          return { matchedCount: 1 }
        })
    };
      
    getDB.mockReturnValue(mockDB);

    ObjectId.mockImplementation((id) => {
      return id;
    })
    
    const response = await request(app)
      .put('/users/unsendFriendRequest/23456/12345')
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(200);
    expect(response.body).toBe('Friend Request Unsent');
  });
});

describe('Accept friend request', () => {
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

    mockDB = {
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

    ObjectId.mockImplementation((id) => {
      throw new error();
    })
    
    const response = await request(app)
      .put('/users/acceptFriendRequest/23456/12345')
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(500);
    expect(response.body).toBe('Invalid user Id');
  });

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

    mockDB = {
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

    ObjectId.mockImplementation((id) => {
      return id;
    })
    
    const response = await request(app)
      .put('/users/acceptFriendRequest/23456/123456')
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(404);
    expect(response.body).toBe('User not found');
  });

  it('No friend request found', async () => {
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

    mockDB = {
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
        updateOne: jest.fn().mockImplementation((param) => {
          return { matchedCount: 0 }
        })
    };
      
    getDB.mockReturnValue(mockDB);

    ObjectId.mockImplementation((id) => {
      return id;
    })
    
    const response = await request(app)
      .put('/users/acceptFriendRequest/23456/12345')
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(500);
    expect(response.body).toBe('Request not found');
  });

  it('Friend request not accepted', async () => {
    const users = [
      {
        _id: 12345,
        name: 'John Doe',
        email: 'john.doe@example.com',
        blockedUsers: [],
        friends: [],
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

    mockDB = {
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
        updateOne: jest.fn().mockImplementation((param) => {
          return { matchedCount: 0 }
        })
    };
      
    getDB.mockReturnValue(mockDB);

    ObjectId.mockImplementation((id) => {
      return id;
    })
    
    const response = await request(app)
      .put('/users/acceptFriendRequest/23456/12345')
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(500);
    expect(response.body).toBe('Friend request not accepted');
  });

  it('Friend request accepted', async () => {
    const users = [
      {
        _id: 12345,
        name: 'John Doe',
        email: 'john.doe@example.com',
        blockedUsers: [],
        friends: [],
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

    mockDB = {
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
        updateOne: jest.fn().mockImplementation((param) => {
          return { matchedCount: 1 }
        })
    };
      
    getDB.mockReturnValue(mockDB);

    ObjectId.mockImplementation((id) => {
      return id;
    })
    
    const response = await request(app)
      .put('/users/acceptFriendRequest/23456/12345')
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(200);
    expect(response.body).toBe('Friend Request Accepted');
  });
});

describe('Decline friend request', () => {
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

    mockDB = {
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

    ObjectId.mockImplementation((id) => {
      throw new error();
    })
    
    const response = await request(app)
      .put('/users/declineFriendRequest/23456/12345')
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(500);
    expect(response.body).toBe('Invalid user Id');
  });

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

    mockDB = {
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

    ObjectId.mockImplementation((id) => {
      return id;
    })
    
    const response = await request(app)
      .put('/users/declineFriendRequest/23456/123456')
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(404);
    expect(response.body).toBe('User not found');
  });

  it('No friend request found', async () => {
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

    mockDB = {
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
        updateOne: jest.fn().mockImplementation((param) => {
          return { matchedCount: 0 }
        })
    };
      
    getDB.mockReturnValue(mockDB);

    ObjectId.mockImplementation((id) => {
      return id;
    })
    
    const response = await request(app)
      .put('/users/declineFriendRequest/23456/12345')
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(500);
    expect(response.body).toBe('Request not found');
  });

  it('Friend request not declined', async () => {
    const users = [
      {
        _id: 12345,
        name: 'John Doe',
        email: 'john.doe@example.com',
        blockedUsers: [],
        friends: [],
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

    mockDB = {
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
        updateOne: jest.fn().mockImplementation((param) => {
          return { matchedCount: 0 }
        })
    };
      
    getDB.mockReturnValue(mockDB);

    ObjectId.mockImplementation((id) => {
      return id;
    })
    
    const response = await request(app)
      .put('/users/declineFriendRequest/23456/12345')
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(500);
    expect(response.body).toBe('Friend request not declined');
  });

  it('Friend request declined', async () => {
    const users = [
      {
        _id: 12345,
        name: 'John Doe',
        email: 'john.doe@example.com',
        blockedUsers: [],
        friends: [],
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

    mockDB = {
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
        updateOne: jest.fn().mockImplementation((param) => {
          return { matchedCount: 1 }
        })
    };
      
    getDB.mockReturnValue(mockDB);

    ObjectId.mockImplementation((id) => {
      return id;
    })
    
    const response = await request(app)
      .put('/users/declineFriendRequest/23456/12345')
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(200);
    expect(response.body).toBe('Friend Request Declined');
  });
});

describe('Unfriend user', () => {
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

    mockDB = {
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

    ObjectId.mockImplementation((id) => {
      throw new error();
    })
    
    const response = await request(app)
      .put('/users/unfriend/23456/12345')
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(500);
    expect(response.body).toBe('User not unfriended');
  });

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

    mockDB = {
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

    ObjectId.mockImplementation((id) => {
      return id;
    })
    
    const response = await request(app)
      .put('/users/unfriend/23456/123456')
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(500);
    expect(response.body).toBe('User not unfriended');
  });

  it('Unfriender not friends', async () => {
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

    mockDB = {
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
        updateOne: jest.fn().mockImplementation((param) => {
          return { matchedCount: 0 }
        })
    };
      
    getDB.mockReturnValue(mockDB);

    ObjectId.mockImplementation((id) => {
      return id;
    })
    
    const response = await request(app)
      .put('/users/unfriend/23456/12345')
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(500);
    expect(response.body).toBe('User not unfriended');
  });

  it('Unfriended not friends', async () => {
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

    mockDB = {
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
        updateOne: jest.fn().mockImplementation((param) => {
          return { matchedCount: 0 }
        })
    };
      
    getDB.mockReturnValue(mockDB);

    ObjectId.mockImplementation((id) => {
      return id;
    })
    
    const response = await request(app)
      .put('/users/unfriend/23456/12345')
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(500);
    expect(response.body).toBe('User not unfriended');
  });

  it('Friends list not updated', async () => {
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

    mockDB = {
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
        updateOne: jest.fn().mockImplementation((param) => {
          return { matchedCount: 0 }
        })
    };
      
    getDB.mockReturnValue(mockDB);

    ObjectId.mockImplementation((id) => {
      return id;
    })
    
    const response = await request(app)
      .put('/users/unfriend/23456/12345')
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(500);
    expect(response.body).toBe('User not unfriended');
  });

  it('User Unfriended', async () => {
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

    mockDB = {
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
        updateOne: jest.fn().mockImplementation((param) => {
          return { matchedCount: 1 }
        })
    };
      
    getDB.mockReturnValue(mockDB);

    ObjectId.mockImplementation((id) => {
      return id;
    })
    
    const response = await request(app)
      .put('/users/unfriend/23456/12345')
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(200);
    expect(response.body).toBe('User unfriended');
  });
});

describe('Block user', () => {
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

    mockDB = {
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

    ObjectId.mockImplementation((id) => {
      throw new error();
    })
    
    const response = await request(app)
      .put('/users/blockUser/23456/12345')
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(500);
    expect(response.body).toBe('Invalid user ID');
  });

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

    mockDB = {
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

    ObjectId.mockImplementation((id) => {
      return id;
    })
    
    const response = await request(app)
      .put('/users/blockUser/123456/123456')
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(404);
    expect(response.body).toBe('User not found');
  });

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

    mockDB = {
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
        updateOne: jest.fn().mockImplementation((param) => {
          return { matchedCount: 0 }
        })
    };
      
    getDB.mockReturnValue(mockDB);

    ObjectId.mockImplementation((id) => {
      return id;
    })
    
    const response = await request(app)
      .put('/users/unfriend/23456/12345')
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(500);
    expect(response.body).toBe('User not unfriended');
  });

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

    mockDB = {
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
        updateOne: jest.fn().mockImplementation((param) => {
          return { matchedCount: 1 }
        })
    };
      
    getDB.mockReturnValue(mockDB);

    ObjectId.mockImplementation((id) => {
      return id;
    })
    
    const response = await request(app)
      .put('/users/unfriend/23456/12345')
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(200);
    expect(response.body).toBe('User unfriended');
  });
});

describe('Unblock user', () => {
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

    mockDB = {
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

    ObjectId.mockImplementation((id) => {
      throw new error();
    })
    
    const response = await request(app)
      .put('/users/unblockUser/23456/12345')
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(500);
    expect(response.body).toBe('Invalid user ID');
  });

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

    mockDB = {
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

    ObjectId.mockImplementation((id) => {
      return id;
    })
    
    const response = await request(app)
      .put('/users/unblockUser/123456/123456')
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(404);
    expect(response.body).toBe('User not found');
  });

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

    mockDB = {
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
        updateOne: jest.fn().mockImplementation((param) => {
          return { matchedCount: 0 }
        })
    };
      
    getDB.mockReturnValue(mockDB);

    ObjectId.mockImplementation((id) => {
      return id;
    })
    
    const response = await request(app)
      .put('/users/unblockUser/23456/12345')
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(500);
    expect(response.body).toBe('User not in blocked list');
  });

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

    mockDB = {
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
        updateOne: jest.fn().mockImplementation((param) => {
          return { matchedCount: 0 }
        })
    };
      
    getDB.mockReturnValue(mockDB);

    ObjectId.mockImplementation((id) => {
      return id;
    })
    
    const response = await request(app)
      .put('/users/unblockUser/23456/12345')
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(500);
    expect(response.body).toBe('User not unblocked');
  });

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

    mockDB = {
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
        updateOne: jest.fn().mockImplementation((param) => {
          return { matchedCount: 1 }
        })
    };
      
    getDB.mockReturnValue(mockDB);

    ObjectId.mockImplementation((id) => {
      return id;
    })
    
    const response = await request(app)
      .put('/users/unblockUser/23456/12345')
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(200);
    expect(response.body).toBe('User unblocked');
  });
});

describe('Delete a chat', () => {
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

    mockDB = {
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

    ObjectId.mockImplementation((id) => {
      throw new error();
    })
    
    const response = await request(app)
      .put('/users/userId/12345/deleteChat/23456')
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(500);
    expect(response.body).toBe('Invalid user ID');
  });

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

    mockDB = {
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

    ObjectId.mockImplementation((id) => {
      return id;
    })
    
    const response = await request(app)
      .put('/users/userId/123456/deleteChat/23456')
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(404);
    expect(response.body).toBe('User not found');
  });

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

    mockDB = {
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
        updateOne: jest.fn().mockImplementation((param) => {
          return { matchedCount: 0 }
        })
    };
      
    getDB.mockReturnValue(mockDB);

    ObjectId.mockImplementation((id) => {
      return id;
    })
    
    const response = await request(app)
      .put('/users/userId/12345/deleteChat/23456')
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(404);
    expect(response.body).toBe('Chat not in list');
  });

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

    mockDB = {
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
        updateOne: jest.fn().mockImplementation((param) => {
          return { matchedCount: 0 }
        })
    };
      
    getDB.mockReturnValue(mockDB);

    ObjectId.mockImplementation((id) => {
      return id;
    })
    
    const response = await request(app)
      .put('/users/userId/12345/deleteChat/23456')
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(500);
    expect(response.body).toBe('Chat not deleted');
  });

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

    mockDB = {
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
        updateOne: jest.fn().mockImplementation((param) => {
          return { matchedCount: 1 }
        })
    };
      
    getDB.mockReturnValue(mockDB);

    ObjectId.mockImplementation((id) => {
      return id;
    })
    
    const response = await request(app)
      .put('/users/userId/12345/deleteChat/23456')
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(200);
    expect(response.body).toBe('Chat deleted');
  });
});

describe('Delete a user', () => {
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

    mockDB = {
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

    ObjectId.mockImplementation((id) => {
      throw new error();
    })
    
    const response = await request(app)
      .delete('/users/userId/12345')
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(500);
    expect(response.body).toBe('Invalid user Id');
  });

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

    mockDB = {
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

    ObjectId.mockImplementation((id) => {
      return id;
    })
    
    const response = await request(app)
      .delete('/users/userId/123456')
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(500);
    expect(response.body).toBe('User data not cleared');
  });

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

    mockDB = {
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
        deleteOne: jest.fn().mockImplementation((param) => {
          return { deletedCount: 0 }
        }),
        deleteMany: jest.fn().mockReturnValue(true)
    };
      
    getDB.mockReturnValue(mockDB);

    ObjectId.mockImplementation((id) => {
      return id;
    })
    
    const response = await request(app)
      .delete('/users/userId/12345')
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(404);
    expect(response.body).toBe('User not deleted');
  });

  it('User updated', async () => {
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

    mockDB = {
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
        deleteOne: jest.fn().mockImplementation((param) => {
          return { deletedCount: 1 }
        }),
        deleteMany: jest.fn().mockReturnValue(true)
    };
      
    getDB.mockReturnValue(mockDB);

    ObjectId.mockImplementation((id) => {
      return id;
    })
    
    const response = await request(app)
      .delete('/users/userId/12345')
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(200);
    expect(response.body).toBe('User deleted');
  });
});