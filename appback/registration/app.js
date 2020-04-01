const express = require('express');
const app = express();
const mongoose = require ('mongoose');
const PORT = process.env.PORT || 5000;
const bp = require('body-parser');
const session = require('express-session');
//bodyparser
app.use(bp.json());

//express session
app.use(session({
    secret: 'secret',
    resave: true,
    saveUninitialized: true
}));

//DB config
const db = require('./config/keys').MongoURI;

//connectdb
mongoose.connect(db, { useNewUrlParser: true, useUnifiedTopology: true })
    .then(() => console.log('MongoDb Connected'))
    .catch(err => (console.log(err)))
;

//routes

app.use('/', require('./routes/index'));
app.use('/user', require('./routes/user'));


app.listen(PORT, console.log(`Server started on ${PORT}`));