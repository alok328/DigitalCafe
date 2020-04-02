const express = require('express');
const bc = require('bcryptjs');
const passport = require('passport');
const router = express.Router();

const UserSchema = require('../models/UserSchema')


//login
router.get('/login', (req, res) => {
    res.send('Login Page');
});

//register
router.get('/register', (req, res) => {
    res.send('Register');
});

//handle get

router.post('/register', (req, res) => {
    const userschema = new UserSchema({
        name: req.body.name,
        email: req.body.email,
        password: req.body.password,
        roll: req.body.roll

    });
    const {name, email, password, roll} = req.body;
    let errors = [];

    //check fields
    if(!name || !email || !password || !roll){
        errors.push({message: "Please fill in all the fields"});
    }

    if(errors.length>0){
        //enter your callback to front end again
        console.log(errors);
        res.status(400).json({'message': 'Please enter all the fields!'})

    }else{
        //validation
        UserSchema.findOne({roll: roll})
            .then(user =>{
                if(user){
                    //user exists
                    //enter your callback to front end
                    errors.push({message: 'User already exists'});
                    res.status(409).json({'message': 'User already exists'});
                    
                    console.log('user already exists');
                }else{

                    //storing the data in db
                    const newUser = new UserSchema({
                        name,
                        email,
                        password,
                        roll
                    });

                    //hash password
                    bc.genSalt(10, (err, salt) => 
                    bc.hash(newUser.password, salt, (err, hash) => {
                        if(err) throw err;
                        //set password to hash
                        newUser.password = hash;
                        //save user
                        newUser.save()
                            .then(user => {
                                const msg = 'you have succesfully registered'; //redirect this message onto front end login page
                                // res.redirect('/login'); //redirect to login page
                                res.status(201).json(newUser)
                                console.log(msg);
                            })
                            .catch(err =>{
                                res.status(500).json({message: "Could not save data!"})
                                console.log(err);
                            });

                    }))
        
    
                        /*userschema.save()
                        .then(data =>{
                            res.json(data);
                        })
                        .catch(err => {
                            res.json({message: err});
                        });

                    res.send('pass');*/

                }
            })
        
        
    
        
    }
    
});

//Login Handle
router.post('/login', (req, res, next) =>{
    passport.authenticate('local', {
        successRedirect: '/success',
        failureRedirect: '/failure',
        failureFlash: false
    })(req, res, next);
});



module.exports = router;


/*router.post('/register', (req, res) =>{
    const userschema = new UserSchema({
        name: req.body.name,
        email: req.body.email,
        password: req.body.password,
        balance: req.body.balance

    })

    userschema.save()
        .then(data =>{
            res.json(data);
        })
        .catch(err => {
            res.json({message: err});
        });

});*/