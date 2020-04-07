const express = require('express');

const router = express.Router();

router.get('/', (req, res) => {
    res.send('Welcome');
});

router.get('/success', (req, res) => {
    let message = 'Login Succesful';
    let name = 'XYZ'
    console.log({'message':message, 'name': name})
    res.status(200).json({'message':message, 'name': name});
})

router.get('/failure', (req,res) =>{
    let message = 'Authentication failed';
    res.send(401).json({'message':message});
})
module.exports = router;