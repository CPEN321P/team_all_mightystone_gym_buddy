const https = require('https');
const fs = require('fs');
const { connectDB } = require('./MongoDB/Connect.js');

const { socket } = require('./socket.js');

const app = require('./app.js');

const options = {
    key: fs.readFileSync('/etc/letsencrypt/live/tams.westus3.cloudapp.azure.com/privkey.pem'), // Path to your private key file
    cert: fs.readFileSync('/etc/letsencrypt/live/tams.westus3.cloudapp.azure.com/fullchain.pem'), // Path to your SSL/TLS certificate file
};

const run = () => {
    try{
        connectDB("mongodb://127.0.0.1:27017");
        //ChatGPT use: YES
        const secureServer = https.createServer(options, app);
        const httpsPort = 443; // Default HTTPS port
        socket(secureServer);
        secureServer.listen(httpsPort, () => {
            console.log(`HTTPS Server running on port:${httpsPort}`);
        });
        //ChatGPT use: NO
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

