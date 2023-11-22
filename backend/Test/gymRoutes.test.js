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

describe('Create a new gym', () => {
    it('Gym is added to the database', async () => {
      // Mock the getDB function
      mockDB = {
          collection: jest.fn().mockReturnThis(),
          insertOne: jest.fn().mockReturnValue({ insertedId: 'mockedId' }),
      };
        getDB.mockReturnValue(mockDB);
      
      const mockGym = {
        name: 'Gym ABC',
        description: 'Very good gym',
        location: 'Ubc Point Grey',
        email: 'john.doe@example.com',
      };
  
      const response = await request(app)
        .post('/gyms')
        .send(mockGym)
        .set('Accept', 'application/json');
  
      expect(response.statusCode).toBe(200);
      expect(response.body).toBe('mockedId');
    });
    it('Database Error', async () => {
      // Mock the getDB function
      mockDB = {
          collection: jest.fn().mockReturnThis(),
          insertOne: jest.fn().mockReturnValue(null),
      };
      getDB.mockImplementation(()=>{
        throw new error();
      });
      
      const mockGym = {
        fruit: 'banana',
        color: 'yellow'
      };
  
      const response = await request(app)
        .post('/gyms')
        .send(mockGym)
        .set('Accept', 'application/json');
  
      expect(response.statusCode).toBe(500);
      expect(response.body).toBe('Gym not added to the database');
    });
    it('Gym is not added to the database', async () => {
      // Mock the getDB function
      mockDB = {
          collection: jest.fn().mockReturnThis(),
          insertOne: jest.fn().mockReturnValue(null),
      };
        getDB.mockReturnValue(mockDB);
      
      const mockGym = {
        fruit: 'banana',
        color: 'yellow'
      };
  
      const response = await request(app)
        .post('/gyms')
        .send(mockGym)
        .set('Accept', 'application/json');
  
      expect(response.statusCode).toBe(500);
      expect(response.body).toBe('Gym not added to the database');
    });
  });

  describe('Get all gyms', () => {
    it('Gyms are retrieved', async () => {
      const mockGyms = [
        {
          name: 'Gym A',
          email: 'john.doe@example.com',
        },
        {
          name: 'Gym B',
          email: 'jane.doe@example.com',
        }
      ];
  
      mockDB = {
          collection: jest.fn().mockReturnThis(),
          find: jest.fn().mockReturnThis(),
          toArray: jest.fn().mockReturnValue(mockGyms),
      };
        
      getDB.mockReturnValue(mockDB);
      
      const response = await request(app)
        .get('/gyms')
        .set('Accept', 'application/json');
  
      expect(response.statusCode).toBe(200);
      expect(response.body).toContainObject({ name: 'Gym A' });
      expect(response.body).toContainObject({ name: 'Gym B' });
    });
  
    it('Gym not found', async () => {
      mockDB = {
          collection: jest.fn().mockReturnThis(),
          find: jest.fn().mockReturnThis(),
          toArray: jest.fn().mockReturnValue(null),
      };
        
      getDB.mockReturnValue(mockDB);
      
      const response = await request(app)
        .get('/gyms')
        .set('Accept', 'application/json');
  
      expect(response.statusCode).toBe(404);
      expect(response.body).toBe("No gyms found");
    });

    it('There is an error retrieving the gyms from the database', async () => {
      mockDB = {
          collection: jest.fn().mockReturnThis(),
          find: jest.fn().mockReturnThis(),
          toArray: jest.fn().mockReturnValue(null),
      };
        
      getDB.mockImplementation(()=>{
        throw new error();
      });
      
      const response = await request(app)
        .get('/gyms')
        .set('Accept', 'application/json');
  
      expect(response.statusCode).toBe(500);
      expect(response.body).toBe("Could not retrieve data from the database");
    });
  });

  describe('Get a specific gym by ID', () => {
    it('Gym is retrieved', async () => {
      const mockGym = {
        name: 'Gym A',
        email: 'john.doe@example.com',
      };
  
      mockDB = {
          collection: jest.fn().mockReturnThis(),
          findOne: jest.fn().mockReturnValue(mockGym),
      };
        
      getDB.mockReturnValue(mockDB);
  
      ObjectId.mockImplementation((id) => {
        return id;
      })
      
      const response = await request(app)
        .get('/gyms/gymId/123456')
        .set('Accept', 'application/json');
  
      expect(response.statusCode).toBe(200);
      expect(response.body.name).toBe(mockGym.name);
      expect(response.body.email).toBe(mockGym.email);
    });

    it('Invalid gym ID', async () => {
        const mockGym = {
          name: 'Gym A',
          email: 'john.doe@example.com',
        };
    
        mockDB = {
            collection: jest.fn().mockReturnThis(),
            findOne: jest.fn().mockReturnValue(mockGym),
        };
          
        getDB.mockReturnValue(mockDB);
    
        ObjectId.mockImplementation((id) => {
          throw new error();
        })
        
        const response = await request(app)
          .get('/gyms/gymId/123456')
          .set('Accept', 'application/json');
    
        expect(response.statusCode).toBe(500);
        expect(response.body).toBe('Invalid gym ID');
      });
  
    it('Error retrieving gym from database', async () => {
      const mockGym = {
        name: 'Gym A',
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
        .get('/gyms/gymId/987654')
        .set('Accept', 'application/json');
  
      expect(response.statusCode).toBe(404);
      expect(response.body).toBe('Gym not found');
    });
  });

  describe('Get a specific gym by email', () => {
    it('Gym is retrieved', async () => {
      const mockGym = {
        name: 'Gym A',
        email: 'gyma@example.com',
      };
  
      mockDB = {
          collection: jest.fn().mockReturnThis(),
          findOne: jest.fn().mockReturnValue(mockGym),
      };
        
      getDB.mockReturnValue(mockDB);
      
      const response = await request(app)
        .get('/gyms/byEmail/gyma@example.com')
        .set('Accept', 'application/json');
  
      expect(response.statusCode).toBe(200);
      expect(response.body.name).toBe(mockGym.name);
      expect(response.body.email).toBe(mockGym.email);
    });
  
    it('No gym found', async () => {
      mockDB = {
          collection: jest.fn().mockReturnThis(),
          findOne: jest.fn().mockReturnValue(null),
      };
        
      getDB.mockReturnValue(mockDB);
      
      const response = await request(app)
        .get('/gyms/byEmail/example@test.com')
        .set('Accept', 'application/json');
  
      expect(response.statusCode).toBe(404);
      expect(response.body).toBe('Gym not found');
    });

    it('Error retrieving gym from database', async () => {
  
      mockDB = {
          collection: jest.fn().mockReturnThis(),
          findOne: jest.fn().mockReturnValue(null),
      };
        
      getDB.mockImplementation(()=>{
        throw new error();
      });
      
      
      const response = await request(app)
        .get('/gyms/byEmail/example@test.com')
        .set('Accept', 'application/json');
  
      expect(response.statusCode).toBe(500);
      expect(response.body).toBe('Could not retrieve data from the database');
    });
  });

  describe('Update gym', () => {
    it('Invalid gym ID', async () => {
      const updatedGym = {
        email: 'newgyma@example.com',
      }
  
      const gyms = [
        {
          _id: 12345,
          name: 'Gym A',
          email: 'gyma@example.com',
          phone: '987654321'
        },
        {
          _id: 23456,
          name: 'Gym B',
          email: 'gyma@example.com',
        },
      ];
  
      mockDB = {
          collection: jest.fn().mockReturnThis(),
          find: jest.fn().mockReturnThis(),
          findOne: jest.fn().mockImplementation((param) => {
            const id = param._id;
            for (const gym of gyms) {
              if (gym._id == id) {
                return gym;
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
        .put('/gyms/gymId/123456')
        .send(updatedGym)
        .set('Accept', 'application/json');
  
      expect(response.statusCode).toBe(500);
      expect(response.body).toBe('Gym not updated');
    });
  
    it('Gym not found', async () => {
      const updatedGym = {
        name: 'Gym X',
      }
  
      const gyms = [
        {
          _id: 12345,
          name: 'Gym A',
          email: 'gyma@example.com',
          phone: '987654321'
        },
        {
          _id: 23456,
          name: 'Gym B',
          email: 'gyma@example.com',
        },
      ];
  
      mockDB = {
          collection: jest.fn().mockReturnThis(),
          find: jest.fn().mockReturnThis(),
          findOne: jest.fn().mockImplementation((param) => {
            const id = param._id;
            for (const gym of gyms) {
              if (gym._id == id) {
                return gym;
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
        .put('/gyms/gymId/123456')
        .send(updatedGym)
        .set('Accept', 'application/json');
  
      expect(response.statusCode).toBe(404);
      expect(response.body).toBe('Gym not found');
    });
  
    it('Gym not updated', async () => {
      const updatedGym = {
        name: 'Gym X',
      }
  
      const gyms = [
        {
          _id: 12345,
          name: 'Gym A',
          email: 'gyma@example.com',
          phone: '987654321'
        },
        {
          _id: 23456,
          name: 'Gym B',
          email: 'gyma@example.com',
        },
      ];
  
      mockDB = {
          collection: jest.fn().mockReturnThis(),
          find: jest.fn().mockReturnThis(),
          findOne: jest.fn().mockImplementation((param) => {
            const id = param._id;
            for (const gym of gyms) {
              if (gym._id == id) {
                return gym;
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
        .put('/gyms/gymId/12345')
        .send(updatedGym)
        .set('Accept', 'application/json');
  
      expect(response.statusCode).toBe(404);
      expect(response.body).toBe('Gym not found');
    });
  
    it('Gym updated', async () => {
      const updatedGym = {
        email: 'newgyma@example.com',
        phone: '123456789'
      }
  
      const gyms = [
        {
          _id: 12345,
          name: 'Gym A',
          email: 'gyma@example.com',
          phone: '987654321'
        },
        {
          _id: 23456,
          name: 'Gym B',
          email: 'gyma@example.com',
        },
      ];
  
      mockDB = {
          collection: jest.fn().mockReturnThis(),
          find: jest.fn().mockReturnThis(),
          findOne: jest.fn().mockImplementation((param) => {
            const id = param._id;
            for (const gym of gyms) {
              if (gym._id == id) {
                return gym;
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
        .put('/gyms/gymId/12345')
        .send(updatedGym)
        .set('Accept', 'application/json');
  
      expect(response.statusCode).toBe(200);
      expect(response.body.email).toBe('newgyma@example.com');
      expect(response.body.phone).toBe('123456789');
    });
  });

  describe('Delete gym', () => {
    it('Invalid gym ID', async () => {
      mockDB = {
          collection: jest.fn().mockReturnThis(),
      };
        
      getDB.mockReturnValue(mockDB);
  
      ObjectId.mockImplementation((id) => {
        throw new error();
      })
      
      const response = await request(app)
        .delete('/gyms/gymId/123456')
        .set('Accept', 'application/json');
  
      expect(response.statusCode).toBe(500);
      expect(response.body).toBe('Gym not deleted');
    });
  
    it('Gym not found', async () => {
      mockDB = {
          collection: jest.fn().mockReturnThis(),
          deleteOne: jest.fn().mockImplementation((param) => {
            return { deletedCount: 0 }
          })
      };
        
      getDB.mockReturnValue(mockDB);
  
      ObjectId.mockImplementation((id) => {
        return id;
      })
      
      const response = await request(app)
        .delete('/gyms/gymId/123456')
        .set('Accept', 'application/json');
  
      expect(response.statusCode).toBe(404);
      expect(response.body).toBe('Gym not found');
    });
  
    it('Gym not deleted', async () => {  
      mockDB = {
          collection: jest.fn().mockReturnThis(),
          deleteOne: jest.fn().mockImplementation((param) => {
            return { deletedCount: 0 }
          })
      };

      getDB.mockReturnValue(mockDB);
  
      ObjectId.mockImplementation((id) => {
        return id;
      })
      
      const response = await request(app)
        .delete('/gyms/gymId/12345')
        .set('Accept', 'application/json');
  
      expect(response.statusCode).toBe(404);
      expect(response.body).toBe('Gym not found');
    });
  
    it('Gym deleted', async () => {
      mockDB = {
          collection: jest.fn().mockReturnThis(),
          deleteOne: jest.fn().mockImplementation((param) => {
            return { deletedCount: 1 }
          })
      };
        
      getDB.mockReturnValue(mockDB);
  
      ObjectId.mockImplementation((id) => {
        return id;
      })
      
      const response = await request(app)
        .delete('/gyms/gymId/12345')
        .set('Accept', 'application/json');
  
      expect(response.statusCode).toBe(200);
      expect(response.body).toBe('Gym deleted successfully');
    });
  });