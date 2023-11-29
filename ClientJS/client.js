const io = require("socket.io-client");
const socket = io("https://tams.westus3.cloudapp.azure.com/", {});

socket.on('connect', async () => {
	console.log('Client has connected to the server!');
});