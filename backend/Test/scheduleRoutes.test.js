const request = require('supertest');
const app = require('../app.js');
const { getDB } = require('../MongoDB/Connect.js'); // Adjust the path as needed

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

// ChatGPT use: No
describe('Create a new schedule', () => {
    // Input: mockSchedule
    // Expected status code: 500
    // Expected behavior: Schedule is not added to the database because insert fails
    // Expected output: Text "Schedule not added to the database"
  it('Schedule is not created', async () => {
    const mockDB = {
        collection: jest.fn().mockReturnThis(),
        insertOne: jest.fn().mockReturnValue(null),
    };
    getDB.mockReturnValue(mockDB);
    
    const mockSchedule = {
      userId: 12345,
      exercises: []
    };

    const response = await request(app)
      .post('/schedules')
      .send(mockSchedule)
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(500);
    expect(response.body).toBe("Schedule not added to the database");
  });

  // Input: mockSchedule
  // Expected status code: 200
  // Expected behavior: Schedule is added to the database successfully
  // Expected output: id of created schedule
  it('Schedule is created', async () => {
    const mockDB = {
        collection: jest.fn().mockReturnThis(),
        insertOne: jest.fn().mockReturnValue({ insertedId: 'mockedId' }),
    };
    getDB.mockReturnValue(mockDB);
  
    const mockSchedule = {
      userId: 12345,
      exercises: []
    };

    const response = await request(app)
      .post('/schedules')
      .send(mockSchedule)
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(200);
    expect(response.body).toBe('mockedId');
  });
});

// ChatGPT use: No
describe('Get a schedule by user id and date', () => {
  // Input: none
  // Expected status code: 404
  // Expected behavior: Schedule is not not found in the database
  // Expected output: Text "No Schedule Found"
  it('Schedule not retrieved', async () => {
    const mockDB = {
        collection: jest.fn().mockReturnThis(),
        findOne: jest.fn().mockReturnValue(null),
    };

    getDB.mockReturnValue(mockDB);

    const response = await request(app)
      .get('/schedules/byUser/12345/11212023')
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(404);
    expect(response.body).toBe("No Schedule Found");
  });

  // Input: none
  // Expected status code: 200
  // Expected behavior: Schedule is returned
  // Expected output: body contains schedule 
  it('Schedule retrieved', async () => {
    const mockDB = {
        collection: jest.fn().mockReturnThis(),
        findOne: jest.fn().mockReturnValue(1),
    };

    getDB.mockReturnValue(mockDB);

    const response = await request(app)
      .get('/schedules/byUser/12345/11212023')
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(200);
    expect(response.body).toBe(1);
  });
});

// ChatGPT use: No
describe('Get all schedules by user id', () => {
  // Input: none
  // Expected status code: 404
  // Expected behavior: Schedules are not not found in the database
  // Expected output: Text "Schedules not retrieved"
  it('Schedules not retrieved', async () => {

    const mockDB = {
        collection: jest.fn().mockReturnThis(),
        find: jest.fn().mockReturnThis(),
        toArray: jest.fn().mockReturnValue(null)
    };

    getDB.mockReturnValue(mockDB);

    const response = await request(app)
      .get('/schedules/byUser/12345')
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(404);
    expect(response.body).toBe("Schedules not retrieved");
  });

  // Input: none
  // Expected status code: 200
  // Expected behavior: Schedules are returned for the user
  // Expected output: Array of schedules
  it('Schedules retrieved', async () => {
    const mockSchedules = [
      {
        name: 1
      },
      {
        name: 2
      }
    ];

    const mockDB = {
        collection: jest.fn().mockReturnThis(),
        find: jest.fn().mockReturnThis(),
        toArray: jest.fn().mockReturnValue(mockSchedules)
    };

    getDB.mockReturnValue(mockDB);

    const response = await request(app)
      .get('/schedules/byUser/12345')
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(200);
    expect(response.body).toContainObject({ name: 1 });
    expect(response.body).toContainObject({ name: 2 });
  });
});

// ChatGPT use: No
describe('Edit a schedule by user id and date', () => {
  // Input: none
  // Expected status code: 404
  // Expected behavior: Schedules are not not found in the database
  // Expected output: Text "Schedule not found"
  it('Schedule not found', async () => {
    const mockSchedule = {
      name: 1
    };

    const mockDB = {
        collection: jest.fn().mockReturnThis(),
        findOne: jest.fn().mockReturnValue(null),
        updateOne: jest.fn().mockReturnValue({
          matchedCount: 0
        })
    };

    getDB.mockReturnValue(mockDB);

    const response = await request(app)
      .put('/schedules/byUser/12345/11212023')
      .send(mockSchedule)
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(404);
    expect(response.body).toBe("Schedule not found");
  });

  // Input: none
  // Expected status code: 500
  // Expected behavior: Schedules are not not updated because no match in db
  // Expected output: Text "Schedules not updated"
  it('Schedule not updated', async () => {
    const mockSchedule = {
      name: 1
    };

    const mockDB = {
        collection: jest.fn().mockReturnThis(),
        findOne: jest.fn().mockReturnValue(mockSchedule),
        updateOne: jest.fn().mockReturnValue({
          matchedCount: 0
        })
    };

    getDB.mockReturnValue(mockDB);

    const response = await request(app)
      .put('/schedules/byUser/12345/11212023')
      .send(mockSchedule)
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(500);
    expect(response.body).toBe("Schedule not updated");
  });
  // Input: none
  // Expected status code: 200
  // Expected behavior: Schedule updated in the db
  // Expected output: Response body contains schedule
  it('Schedule updated', async () => {
    const mockSchedule = {
      userId: 1
    };

    const mockDB = {
        collection: jest.fn().mockReturnThis(),
        findOne: jest.fn().mockReturnValue(mockSchedule),
        updateOne: jest.fn().mockReturnValue({
          matchedCount: 1
        })
    };

    getDB.mockReturnValue(mockDB);

    const response = await request(app)
      .put('/schedules/byUser/12345/11212023')
      .send(mockSchedule)
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(200);
    expect(response.body.userId).toBe(1);
  });
});
