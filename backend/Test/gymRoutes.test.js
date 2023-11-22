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
  
    it('There is an error retrieving the gyms from the database', async () => {
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
  
      ObjectId.mockImplementation((id) => {
        return id;
      })
      
      const response = await request(app)
        .get('/gyms/byEmail/gyma@example.com')
        .set('Accept', 'application/json');
  
      expect(response.statusCode).toBe(200);
      expect(response.body.name).toBe(mockGym.name);
      expect(response.body.email).toBe(mockGym.email);
    });
  
    it('Error retrieving gym from database', async () => {
      const mockGym = {
        name: 'Gym A',
        email: 'gyma@example.com',
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
        .get('/gyms/byEmail/example@test.com')
        .set('Accept', 'application/json');
  
      expect(response.statusCode).toBe(404);
      expect(response.body).toBe('Gym not found');
    });
  });