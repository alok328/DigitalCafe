const express = require('express');

const router = express.Router();

router.get('/', (req, res) => {
    console.log('/')
    res.send('Welcome');
});

router.get('/success', (req, res) => {
    console.log('/success')
    let message = 'Login Succesful';
    let user = req.user.firstName
    res.status(200).json({'message':message, 'user': user});
})

router.get('/failure', (req,res) =>{
    console.log('/failure')
    let message = 'Authentication failed';
    res.send(401).json({'message':message});
})
module.exports = router;