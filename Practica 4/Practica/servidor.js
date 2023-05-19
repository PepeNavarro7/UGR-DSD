var http = require("http");
var url = require("url");
var fs = require("fs");
var path = require("path");
var socketio = require("socket.io");
var MongoClient = require('mongodb').MongoClient;
var MongoServer = require('mongodb').Server;
var mimeTypes = { "html": "text/html", "jpeg": "image/jpeg", "jpg": "image/jpeg", "png": "image/png", "js": "text/javascript", "css": "text/css", "swf": "application/x-shockwave-flash"};

var httpServer = http.createServer(
	function(request, response) {
		var uri = url.parse(request.url).pathname;
		if (uri=="/") uri = "/usuarios.html";
		var fname = path.join(process.cwd(), uri);
		fs.exists(fname, function(exists) {
			if (exists) {
				fs.readFile(fname, function(err, data){
					if (!err) {
						var extension = path.extname(fname).split(".")[1];
						var mimeType = mimeTypes[extension];
						response.writeHead(200, mimeType);
						response.write(data);
						response.end();
					}
					else {
						response.writeHead(200, {"Content-Type": "text/plain"});
						response.write('Error de lectura en el fichero: '+uri);
						response.end();
					}
				});
			}
			else{
				if(uri!="/favicon.ico"){
					console.log("Peticion invalida: "+uri);
					response.writeHead(200, {"Content-Type": "text/plain"});
					response.write('404 Not Found\n');
					response.end();
				}
				
			}
		});
	}
);

MongoClient.connect("mongodb://localhost:27017/", { useUnifiedTopology: true }, function(err, db) {
	httpServer.listen(8080);
	var dbo = db.db("pruebaBaseDatos");
	var io = socketio(httpServer);

	var allClients = new Array();
	var persianaAbierta = false;
	var aireEncendido = false;
	var datosSensores = ""; // ultimos datos de los sensores
	
	// En cada ejecucion del servidor, borramos la coleccion para empezar de cero
	dbo.collection("medidas").drop(function(err, delOK) { if (err) throw err; });
	dbo.createCollection("medidas", function(err, collection){ if (err) throw err; });
	var collection = dbo.collection("medidas");

	
	io.sockets.on('connection', function(client) {
		// Registramos cada conexion nueva, como en el ejemplo connections
		allClients.push({address:client.request.connection.remoteAddress, port:client.request.connection.remotePort});
		//console.log('New connection from ' + client.request.connection.remoteAddress + ':' + client.request.connection.remotePort);
		
		// Notificamos a los clientes nuevos las conexiones y el estado de persiana y aire 
		io.sockets.emit('all-connections', allClients);
		io.sockets.emit('actualizarManuales',{persiana: persianaAbierta, aire: aireEncendido});
		io.sockets.emit('actualizarSensores',datosSensores);
				
		// Recibimos nuevas medidas de la interfaz de los sensores y las enviamos a los usuarios
		client.on('cambioSensores', function(data){
			datosSensores = data;
			console.log(datosSensores);
			collection.insertOne(datosSensores, {safe:true}, function(err, result) {});
			//console.log(collection.find().toArray());
			io.sockets.emit('actualizarSensores',datosSensores);
		});
		// Cuando el cliente cambia el aire o la persiana, se lo notifica al servidor, que se lo traspasa a los clientes despues
		client.on('cambioAire', function(){
			aireEncendido = !aireEncendido;
			io.sockets.emit('actualizarManuales',{persiana: persianaAbierta, aire: aireEncendido});
		});
		client.on('cambioPersiana', function(){
			persianaAbierta = !persianaAbierta;
			io.sockets.emit('actualizarManuales',{persiana: persianaAbierta, aire: aireEncendido});
		});
				
				client.on('alertatemperatura', function(data){
					if(data.limite == "max"){
						if (aireEncendido == false) {
							aireEncendido = true;
						}
						if (persianaAbierta == true) {
							persianaAbierta = false;
						}
						
					}
					if(data.limite == "min"){
						if (aireEncendido == false) {
							aireEncendido = true;
						}
					}
					io.sockets.emit('actualizarActuadores',{persiana: persianaAbierta, aire: aireEncendido});
				});
				client.on('alertaluminosidad', function(data){
					if(data.limite == "max"){
						if (persianaAbierta == true) {
							persianaAbierta = false;
						}
					}
					if(data.limite == "min"){
						if (persianaAbierta == false) {
							persianaAbierta = true;
						}
					}
					io.sockets.emit('actualizarActuadores',{persiana: persianaAbierta, aire: aireEncendido});
				});
		
				client.on('alertas', function(data){
					io.sockets.emit('alertas',data);
				});
		// Metodo que recoge las desconexiones (calcado de connections.js)
		client.on('disconnect', function() {
			//console.log("El cliente "+client.request.connection.remoteAddress+" se va a desconectar");
			//console.log(allClients);

			var index = -1;
			for(var i = 0; i<allClients.length;i++){
				//console.log("Hay "+allClients[i].port);
				if(allClients[i].address == client.request.connection.remoteAddress
					&& allClients[i].port == client.request.connection.remotePort){
					index = i;
				}			
			}

			if (index != -1) {
				allClients.splice(index, 1);
				io.sockets.emit('all-connections', allClients);
			}else{
				console.log("EL USUARIO NO SE HA ENCONTRADO!")
			}
			//console.log('El usuario '+client.request.connection.remoteAddress+' se ha desconectado');
		});
	});
});

	



console.log("Servicio Socket.io & MongoDB iniciado");

