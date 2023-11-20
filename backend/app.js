const express = require('express');
const bodyParser = require('body-parser');
const usersRoutes = require('./Routes/userRoutes.js');
const chatRoutes = require('./Routes/chatRoutes.js');
const schedulesRoutes = require('./Routes/scheduleRoutes.js');
const gymRoutes = require('./Routes/gymRoutes.js');
const gymUserRoutes = require('./Routes/gymUserRoutes.js');
const app = express();


app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());

app.use('/users', usersRoutes);
app.use('/chat', chatRoutes);
app.use('/schedules', schedulesRoutes);
app.use('/gyms', gymRoutes);
app.use('/gymsUsers', gymUserRoutes);

module.exports = app;