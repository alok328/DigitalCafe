
const express = require('express');
const pss = require('../config/passport');
// const page = require('./index.html')

const router = express.Router();

router.get('/', (req, res) => {
    console.log('/')
    res.sendFile(__dirname + '/' + 'index.html');
});

// router.get('/success', (req, res) => {
//     console.log(pss.usern);
//     let message = 'Login Succesful';
//     let usernn = pss.usern.firstName;
//     res.status(200).json({'message':message, 'user': usernn});
// })

// router.get('/failure', (req,res) =>{
//     console.log('/failure')
//     let message = 'Authentication failed';
//     res.send(401).json({'message':message});
// })
module.exports = router;