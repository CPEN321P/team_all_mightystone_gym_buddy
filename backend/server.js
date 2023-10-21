var express = require("express")
var app = express()

const {MongoClient} = require("mongodb")
const uri = "mongodb://localhost:27017"
const client = new MongoClient(uri)


var server = app.listen(8081, (req,res)=>{
    var host = server.address().address
    var port = server.address().port
    console.log("Server running at %s:%s",host,port)
})



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
