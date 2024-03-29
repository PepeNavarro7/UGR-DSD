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
		if (uri=="/") uri = "/mongo-test.html";
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
	var io = socketio(httpServer);
	var dbo = db.db("pruebaBaseDatos");
	
	// En cada ejecucion del servidor, borramos la coleccion para empezar de cero
	dbo.collection("test").drop(function(err, delOK) { if (err) throw err; });
	dbo.createCollection("test", function(err, collection){ if (err) throw err; });
	var collection = dbo.collection("test");

	io.sockets.on('connection', function(client) {
		// Se le pasa al cliente su propia dirección
		client.emit('my-address', {host:client.request.connection.remoteAddress, port:client.request.connection.remotePort});
		// El cliente la manda de vuelta para que se guarde
		client.on('poner', function (data) {
			collection.insertOne(data, {safe:true}, function(err, result) {});
		});
		// El cliente pide que se le envíen los datos guardados
		client.on('obtener', function (data) {
			collection.find(data).toArray(function(err, results){
				client.emit('obtener', results);
			});
		});
	});
});

console.log("Servicio MongoDB iniciado");

