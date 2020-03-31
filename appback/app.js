const express = require('express');
const app = express();
const mongoose =  require('mongoose');
const bodyparser =  require('body-parser');

require('dotenv/config'); 

app.use(bodyparser.json());

//import routes
const signup = require('./routes/signup');

app.use('/signup', signup);




//ROUTES
app.get('/', (req, res) => {
    res.send('we are on Home');
});



//connect to Db

mongoose.connect(process.env.DB_Connection, { useNewUrlParser: true, useUnifiedTopology: true},

() =>

    console.log('connected to DB')

);

app.listen(4070);
