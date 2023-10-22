var express = require("express")
var app = express()
const bodyParser = require('body-parser');

const {MongoClient} = require("mongodb")
const uri = "mongodb://localhost:27017"
const client = new MongoClient(uri, { useNewUrlParser: true, useUnifiedTopology: true });


var server = app.listen(8081, (req,res)=>{
    var host = server.address().address
    var port = server.address().port
    console.log("Server running at %s:%s",host,port)
})
app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());

const dbName = 'gym-buddies-db';


client.connect(err => {
  if (err) {
    console.error('Error connecting to MongoDB:', err);
    return;
  }
  console.log('Connected to MongoDB');

  const db = client.db(dbName);

  // Create collections for each database (users, chat, and schedules)
  const usersCollection = db.collection('users');
  const chatCollection = db.collection('chat');
  const schedulesCollection = db.collection('schedules');

  // Import and use routes for each database
  const usersRoutes = require('./Routes/userRoutes');
  app.use('/users', usersRoutes(usersCollection));

  const chatRoutes = require('./Routes/chatRoutes');
  app.use('/chat', chatRoutes(chatCollection));

  const schedulesRoutes = require('./Routes/scheduleRoutes');
  app.use('/schedules', schedulesRoutes(schedulesCollection));

  app.listen(port, () => {
    console.log(`Server is running on port ${port}`);
  });
});


async function run(){
    try{
        await client.connect()
        console.log("Successfully connected" )
    }
    catch(err){
        console.log(err)
        await client.close()
    }
}

run()
