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

app.get('/getUser', function (req, res) {
  var sql = "SELECT * FROM USER WHERE EMAIL LIKE '" + req.query.email + "'";
  	con.query(sql, function (err, result) {
  		if(err) throw err;
  		res.json(result[0]);
			console.log("get " + req.query.email);
  	});
});

app.listen(port, () => console.log("serveur listening on : " + port));
