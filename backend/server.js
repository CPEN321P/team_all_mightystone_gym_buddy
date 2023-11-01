import express from 'express';
import bodyParser from 'body-parser';
import https from 'https';
import fs from 'fs';
import { connectDB } from './MongoDB/Connect.js';
import usersRoutes from './Routes/userRoutes.js';
import chatRoutes from './Routes/chatRoutes.js';
import schedulesRoutes from './Routes/scheduleRoutes.js';
import gymRoutes from './Routes/gymRoutes.js';
import gymUserRoutes from './Routes/gymUserRoutes.js';

const app = express();


app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());

app.use('/users', usersRoutes);
app.use('/chat', chatRoutes);
app.use('/schedules', schedulesRoutes);
app.use('/gyms', gymRoutes);
app.use('/gymsUsers', gymUserRoutes);

const options = {
    key: fs.readFileSync('./private-key.pem'), // Path to your private key file
    cert: fs.readFileSync('./certificate.pem'), // Path to your SSL/TLS certificate file
};

const run = () => {
    try{
        connectDB("mongodb://127.0.0.1:27017");

        const secureServer = https.createServer(options, app);
        const httpsPort = 443; // Default HTTPS port
        secureServer.listen(httpsPort, () => {
            console.log(`HTTPS Server running on port:${httpsPort}`);
        });
    }
    catch(err){
        console.log(err);
    }
}

run();
