const express = require('express');

const router = express.Router();

router.get('/', (req, res) => {
    console.log('/')
    res.send('Welcome');
});

router.get('/success', (req, res) => {
    console.log('/success')
    let message = 'Login Succesful';
    // var name = req.user.firstName;
    // if(req.user){
    //     name = req.user.firstName
    // }
    // console.log(req.body);
    // console.log("\n")
    console.log(req.sessionStore.sessions)
    // res.status(200).json({'message':message, 'user': 'name'});
})

router.get('/failure', (req,res) =>{
    console.log('/failure')
    let message = 'Authentication failed';
    res.status(401).json({'message':message});
})
module.exports = router;