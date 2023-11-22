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

describe('Create a new schedule', () => {
  it('Schedule is not created', async () => {
    mockDB = {
        collection: jest.fn().mockReturnThis(),
        insertOne: jest.fn().mockReturnValue(null),
    };
      getDB.mockReturnValue(mockDB);
    
      const mockSchedule = {
        userId: 12345,
        date: 11212023,
        exercises: []
      };

    const response = await request(app)
      .post('/schedules')
      .send(mockSchedule)
      .set('Accept', 'application/json');

    expect(response.statusCode).toBe(500);
    expect(response.body).toBe("Schedule not added to the database");
  });

  it('Schedule is created', async () => {
    mockDB = {
        collection: jest.fn().mockReturnThis(),
        insertOne: jest.fn().mockReturnValue({ insertedId: 'mockedId' }),
    };
      getDB.mockReturnValue(mockDB);
    
      const mockSchedule = {
        userId: 12345,
        date: 11212023,
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

describe('Get a schedule by user id and date', () => {
  it('Schedule not retrieved', async () => {
    mockDB = {
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

  it('Schedule retrieved', async () => {
    mockDB = {
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

describe('Get all schedules by user id', () => {
  it('Schedules not retrieved', async () => {
    const mockSchedules = [
      {
        name: 1
      },
      {
        name: 2
      }
    ];

    mockDB = {
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

  it('Schedules retrieved', async () => {
    const mockSchedules = [
      {
        name: 1
      },
      {
        name: 2
      }
    ];

    mockDB = {
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

describe('Edit a schedule by user id and date', () => {
  it('Schedule not found', async () => {
    const mockSchedule = {
      name: 1
    };

    mockDB = {
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

  it('Schedule not updated', async () => {
    const mockSchedule = {
      name: 1
    };

    mockDB = {
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

  it('Schedule not updated', async () => {
    const mockSchedule = {
      userId: 1
    };

    mockDB = {
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
