const express = require('express');

const router = express.Router();

router.get('/', (req, res) => {
    res.send('Welcome');
});

router.get('/dashboard', (req, res) => {
    let message = 'Login Succesful';
    res.status(200).json(message);
})

router.get('/loginf', (req,res) =>{
    let message = 'Authentication failed';
    res.status(401).json(message);
})
module.exports = router;