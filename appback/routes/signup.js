const express = require ('express');

const UserSchema = require('../models/User');

const router = express.Router();

router.get('/', (req, res) => {
    res.send('signup');
});

router.post('/', (req, res) =>{
    const userschema = new UserSchema({
        name: req.body.name,
        ID: req.body.ID,
        password: req.body.password

    })

    userschema.save()
        .then(data =>{
            res.json(data);
        })
        .catch(err => {
            res.json({message: err});
        });

});




module.exports = router;
