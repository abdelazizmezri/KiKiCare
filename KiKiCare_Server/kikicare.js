var mysql= require('mysql');
var express=require('express');

const app=express();
const port=1225;

var con=mysql.createConnection({
	host:"localhost",
	user:"root",
	password:"",
	database:"kikicare_bd"
});

app.get('/', (req, res) => {
res.send("Serveur KiKiCare");
	});

app.post('/AddUser', function (req, res) {
  var first_name = req.query.first_name;
  var last_name = req.query.last_name;
  var email = req.query.email;
  var password = req.query.password;
  var url_image = req.query.url_image;
  var mode = req.query.mode;
  var sql = "INSERT INTO USER (email, first_name, last_name, password, url_image, mode ) VALUES('" + email + "', '" + first_name + "' , '" + last_name + "', '" + password + "', '" + url_image + "', '" + mode + "')";
  con.query(sql, function (err, result) {
    if(err) throw err;
  	console.log("user " + email + "added !");
  });
  res.statusCode = 200;
  res.setHeader('Content-Type', 'text/plain');
  res.send("user " + email + " added !");

});

app.post('/UpdateUser', function (req, res) {
  var first_name = req.query.first_name;
  var last_name = req.query.last_name;
  var email = req.query.email;
  var password = req.query.password;
  var url_image = req.query.url_image;
  var sql = "UPDATE USER SET email = '"+ email +"', first_name = '"+ first_name +"', last_name = '"+ last_name +"', password = '"+ password +"', url_image = '"+ url_image +"' WHERE email LIKE '"+ email +"'";
  con.query(sql, function (err, result) {
    if(err) throw err;
  	console.log("user " + email + "updated !");
  });
  res.statusCode = 200;
  res.setHeader('Content-Type', 'text/plain');
  res.send("user " + email + " updated !");
});

app.get('/getUser', function (req, res) {
  var sql = "SELECT * FROM USER WHERE EMAIL LIKE '" + req.query.email + "'";
  	con.query(sql, function (err, result) {
  		if(err) throw err;
  		res.json(result[0]);
			console.log("get " + req.query.email);
  	});
});

app.post('/AddAnimal', function (req, res) {
  var name = req.query.name;
  var id_user = req.query.id_user;
  var sexe = req.query.sexe;
  var type = req.query.type;
  var image = req.query.image;
  var date_nais = req.query.date_nais;
  var race = req.query.race;
  var size = req.query.size;
  var sql = "INSERT INTO ANIMAL (id_user, name, sexe, type, date_nais, race,size,image ) VALUES('" + id_user + "', '" + name + "' , '" + sexe+ "', '" + type + "', '" + date_nais + "', '" + race +"', '" + size +"', '" + image + "')";
  con.query(sql, function (err, result) {
    if(err) throw err;
  	console.log("animal " + name + "added !");
  });
  res.statusCode = 200;
  res.setHeader('Content-Type', 'text/plain');
  res.send("animal " + name + " added !");

});

app.get('/getAnimal', function (req, res) {
  var sql = "SELECT * FROM ANIMAL WHERE NAME LIKE '" + req.query.name + "'";
  	con.query(sql, function (err, result) {
  		if(err) throw err;
  		res.json(result[0]);
			console.log("get " + req.query.name);
  	});
});

app.get('/getAnimalsByUser', function (req, res) {
  var sql = "SELECT * FROM ANIMAL WHERE for_adoption= 0 and id_user ='" + req.query.id_user + "'";
  	con.query(sql, function (err, result) {
  		if(err) throw err;
  		res.json(result);
			console.log("get " + req.query.id_user);
  	});
});

app.delete('/deleteAnimal', function (req, res) {
  var sql = "DELETE FROM ANIMAL WHERE id='" + req.query.id + "'";

  con.query(sql, function (err, result) {
    if(err) throw err;
  	console.log("animal " + req.query.id+ "deleted!");
  });
  res.statusCode = 200;
  res.setHeader('Content-Type', 'text/plain');
  res.send("animal " + req.query.id+ "deleted!");

});

app.listen(port, () => console.log("serveur listening on : " + port));
