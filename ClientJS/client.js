const io = require("socket.io-client");
const socket = io("https://20.172.9.70:443", {});

socket.on('connect', async () => {
	console.log('Client has connected to the server!');
});