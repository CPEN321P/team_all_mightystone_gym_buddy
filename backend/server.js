import express from 'express';
import bodyParser from 'body-parser';

import { connectDB } from './MongoDB/Connect.js';
import usersRoutes from './Routes/userRoutes.js';
import chatRoutes from './Routes/chatRoutes.js';
import schedulesRoutes from './Routes/scheduleRoutes.js';
import gymRoutes from './Routes/gymRoutes.js';

const app = express();

app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());

app.use('/users', usersRoutes);
app.use('/chat', chatRoutes);
app.use('/schedules', schedulesRoutes);
app.use('/gyms', gymRoutes);

const run = () => {
    try{
        connectDB("mongodb://127.0.0.1:27017");

        const server = app.listen(8081, (req,res)=>{
          const host = server.address().address;
          const port = server.address().port;
          console.log("Server running at %s:%s",host,port);
        });
    }
    catch(err){
        console.log(err);
    }
}

run();
