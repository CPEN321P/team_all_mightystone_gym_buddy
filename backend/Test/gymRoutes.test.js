const request = require('supertest');
const app = require('../app.js');
const { getDB } = require('../MongoDB/Connect.js'); // Adjust the path as needed
const { createId} = require('../Utils/mongoUtils.js');

jest.mock('../MongoDB/Connect.js');
jest.mock('../Utils/mongoUtils.js')

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
  
  //ChatGPT use: No
  describe('Create a new gym', () => {
    // Input: mockGym
    // Expected status code: 200
    // Expected behavior: Successfully added
    // Expected output: The gym object
    it('Gym is added to the database', async () => {
      // Mock the getDB function
      const mockDB = {
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

    // Input: mockGym
    // Expected status code: 500
    // Expected behavior: Gym is not added to the database
    // Expected output: Error message
    it('Gym is not added to the database', async () => {
      // Mock the getDB function
      const mockDB = {
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

  //ChatGPT use: No
  describe('Get all gyms', () => {
    // Input: None
    // Expected status code: 200
    // Expected behavior: All gyms retrieved
    // Expected output: List of gym objects
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
  
      const mockDB = {
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
  
    // Input: None
    // Expected status code: 404
    // Expected behavior: Gym not found
    // Expected output: Error message
    it('Gym not found', async () => {
      const mockDB = {
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
  });

  //ChatGPT use: No
  describe('Get a specific gym by ID', () => {
    // Input: None
    // Expected status code: 200
    // Expected behavior: Gym retrieved
    // Expected output: Gym object
    it('Gym is retrieved', async () => {
      const mockGym = {
        name: 'Gym A',
        email: 'john.doe@example.com',
      };
  
      const mockDB = {
          collection: jest.fn().mockReturnThis(),
          findOne: jest.fn().mockReturnValue(mockGym),
      };
        
      getDB.mockReturnValue(mockDB);
  
      createId.mockImplementation((id) => {
        return id;
      })
      
      const response = await request(app)
        .get('/gyms/gymId/123456')
        .set('Accept', 'application/json');
  
      expect(response.statusCode).toBe(200);
      expect(response.body.name).toBe(mockGym.name);
      expect(response.body.email).toBe(mockGym.email);
    });

    // Input: None
    // Expected status code: 500
    // Expected behavior: Error thrown by createId()
    // Expected output: Error message
    it('Invalid gym ID', async () => {
        const mockGym = {
          name: 'Gym A',
          email: 'john.doe@example.com',
        };
    
        const mockDB = {
            collection: jest.fn().mockReturnThis(),
            findOne: jest.fn().mockReturnValue(mockGym),
        };
          
        getDB.mockReturnValue(mockDB);
    
        createId.mockImplementation((id) => {
          throw new Error('');
        })
        
        const response = await request(app)
          .get('/gyms/gymId/123456')
          .set('Accept', 'application/json');
    
        expect(response.statusCode).toBe(500);
        expect(response.body).toBe('Invalid gym ID');
      });
  
    // Input: None
    // Expected status code: 404
    // Expected behavior: Gym not retieved 
    // Expected output: Error message
    it('Error retrieving gym from database', async () => {
  
      const mockDB = {
          collection: jest.fn().mockReturnThis(),
          findOne: jest.fn().mockReturnValue(null),
      };
        
      getDB.mockReturnValue(mockDB);
  
      createId.mockImplementation((id) => {
        return id;
      })
      
      const response = await request(app)
        .get('/gyms/gymId/987654')
        .set('Accept', 'application/json');
  
      expect(response.statusCode).toBe(404);
      expect(response.body).toBe('Gym not found');
    });
  });

  //ChatGPT use: No
  describe('Get a specific gym by email', () => {
    // Input: None
    // Expected status code: 200
    // Expected behavior: Gym retrieved
    // Expected output: Gym object
    it('Gym is retrieved', async () => {
      const mockGym = {
        name: 'Gym A',
        email: 'gyma@example.com',
      };
  
      const mockDB = {
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
  
    // Input: None
    // Expected status code: 404
    // Expected behavior: Gym not found
    // Expected output: Error message
    it('No gym found', async () => {
      const mockDB = {
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
  });

  //ChatGPT use: No
  describe('Update gym', () => {
    // Input: none
    // Expected status code: 500
    // Expected behavior: Failure because of invalid Id
    // Expected output: Text "Gym not updated"
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
  
      const mockDB = {
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
  
      createId.mockImplementation((id) => {
        throw new Error('');
      })
      
      const response = await request(app)
        .put('/gyms/gymId/123456')
        .send(updatedGym)
        .set('Accept', 'application/json');
  
      expect(response.statusCode).toBe(500);
      expect(response.body).toBe('Gym not updated');
    });
  
    // Input: none
    // Expected status code: 404
    // Expected behavior: Failure because gym not found
    // Expected output: Text "Gym not found"
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
  
      const mockDB = {
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
  
      createId.mockImplementation((id) => {
        return id;
      })
      
      const response = await request(app)
        .put('/gyms/gymId/123456')
        .send(updatedGym)
        .set('Accept', 'application/json');
  
      expect(response.statusCode).toBe(404);
      expect(response.body).toBe('Gym not found');
    });
  
    // Input: none
    // Expected status code: 404
    // Expected behavior: Failure because gym not found
    // Expected output: Text "Gym not found"
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
  
      const mockDB = {
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
          updateOne: jest.fn().mockReturnValue({ matchedCount: 0 })
      };

      getDB.mockReturnValue(mockDB);
  
      createId.mockImplementation((id) => {
        return id;
      })
      
      const response = await request(app)
        .put('/gyms/gymId/12345')
        .send(updatedGym)
        .set('Accept', 'application/json');
  
      expect(response.statusCode).toBe(404);
      expect(response.body).toBe('Gym not found');
    });
  
    // Input: none
    // Expected status code: 200
    // Expected behavior: Success: gym updated
    // Expected output: Updated gym
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
  
      const mockDB = {
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
          updateOne: jest.fn().mockReturnValue({ matchedCount: 1 })
      };
        
      getDB.mockReturnValue(mockDB);
  
      createId.mockImplementation((id) => {
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

  //ChatGPT use: No
  describe('Delete gym', () => {
    // Input: none
    // Expected status code: 500
    // Expected behavior: Failure because of invalid Id
    // Expected output: Text "Gym not deleted"
    it('Invalid gym ID', async () => {
      const mockDB = {
          collection: jest.fn().mockReturnThis(),
      };
        
      getDB.mockReturnValue(mockDB);
  
      createId.mockImplementation((id) => {
        throw new Error('');
      })
      
      const response = await request(app)
        .delete('/gyms/gymId/123456')
        .set('Accept', 'application/json');
  
      expect(response.statusCode).toBe(500);
      expect(response.body).toBe('Gym not deleted');
    });
    
    // Input: none
    // Expected status code: 404
    // Expected behavior: Failure because gym not found
    // Expected output: Text "Gym not found"
    it('Gym not found', async () => {
      const mockDB = {
          collection: jest.fn().mockReturnThis(),
          deleteOne: jest.fn().mockImplementation((param) => {
            return { deletedCount: 0 }
          })
      };
        
      getDB.mockReturnValue(mockDB);
  
      createId.mockImplementation((id) => {
        return id;
      })
      
      const response = await request(app)
        .delete('/gyms/gymId/123456')
        .set('Accept', 'application/json');
  
      expect(response.statusCode).toBe(404);
      expect(response.body).toBe('Gym not found');
    });
    
    // Input: none
    // Expected status code: 404
    // Expected behavior: Failure because gym not found
    // Expected output: Text "Gym not found"
    it('Gym not deleted', async () => {  
      const mockDB = {
          collection: jest.fn().mockReturnThis(),
          deleteOne: jest.fn().mockImplementation((param) => {
            return { deletedCount: 0 }
          })
      };

      getDB.mockReturnValue(mockDB);
  
      createId.mockImplementation((id) => {
        return id;
      })
      
      const response = await request(app)
        .delete('/gyms/gymId/12345')
        .set('Accept', 'application/json');
  
      expect(response.statusCode).toBe(404);
      expect(response.body).toBe('Gym not found');
    });
  
    // Input: none
    // Expected status code: 200
    // Expected behavior: Success: gym deleted
    // Expected output: Text "Gym deleted successfully"
    it('Gym deleted', async () => {
      const mockDB = {
          collection: jest.fn().mockReturnThis(),
          deleteOne: jest.fn().mockImplementation((param) => {
            return { deletedCount: 1 }
          })
      };
        
      getDB.mockReturnValue(mockDB);
  
      createId.mockImplementation((id) => {
        return id;
      })
      
      const response = await request(app)
        .delete('/gyms/gymId/12345')
        .set('Accept', 'application/json');
  
      expect(response.statusCode).toBe(200);
      expect(response.body).toBe('Gym deleted successfully');
    });
  });