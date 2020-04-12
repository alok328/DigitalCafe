const express = require('express');
const bc = require('bcryptjs');
const passport = require('passport');

const router = express.Router();


const UserSchema = require('../models/UserSchema');
const pass = require('../config/passport');


//login
router.get('/login', (req, res) => {
    console.log('/user/login')
    res.send('Login Page');
});

//register
router.get('/register', (req, res) => {
    console.log('/user/register')
    res.send('Register');
});

//handle get

router.post('/register', (req, res) => {
    console.log('/user/register')
    const userschema = new UserSchema({
        firstName: req.body.firstName,
        lastName: req.body.lastName,
        email: req.body.email,
        password: req.body.password,
        roll: req.body.roll,
        hostel: req.body.hostel

    });
    const {firstName, lastName, email, password, roll, hostel} = req.body;
    let errors = [];

    // //check fields
    // if(!name || !email || !password || !roll){
    //     errors.push({message: "Please fill in all the fields"});
    // }

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
                        firstName,
                        lastName,
                        email,
                        password,
                        roll,
                        hostel
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

    const q = {
        roll: req.body.roll
    };

    

    UserSchema.findOne(q, (err, user) =>{
        if(user != null){
            bc.compare(req.body.password, user.password, (err, isMatch)=>{
                if(err) {
                    throw err;
                    console.log(err);
                };

                if(isMatch){
                    console.log(user.firstName);
                    
                    res.status(200).json({'user': user.firstName, 'roll': user.roll});
                }else{
                    res.status(401).json({'message': 'Invalid roll/password'})
                }
            })
        }else res.status(404).json({message: 'User not found'});
    })

    


});


router.get('/:roll/balance', (req, res)=>{
    console.log(req.params)
    const rolln = {roll: req.params.roll};
    UserSchema.findOne(rolln, (err, user) =>{
        if(user != null){
            res.status(200).json({'message': 'remaining balance', 'bal': user.balance})
        }else res.status(404).json({'message': 'not found'})

    });//if (err) throw err;
});



/*router.post('/login', (req, res, next) =>{
    console.log('/user/login')
    passport.authenticate('local', {
        
        successRedirect: '/success',
        failureRedirect: '/failure',
        session: true,
        failureFlash: false
        
    })(req, res, next);
    //res.json(pass.user.name);
});*/


//add user transaction
router.post('/:roll/transaction', (req, res)=>{
    const rolln = {roll: req.params.roll};
    const menun = req.body.menu;
    const pricen = req.body.price;
    const h = req.body.hostel;
    console.log(rolln)
    UserSchema.findOne(rolln, (err, user)=>{
        console.log(user)
        if(user != null){
            if(h == user.hostel){

                console.log(user)
                user.transactions.push({
                    menu: menun,
                    price: pricen
                })
                if(user.balance > 0){
                    var bal = user.balance - pricen
                    user.balance = bal
                    user.save(function(err) {
                        err != null ? res.status(400).send(err) : res.status(201).json(user)
                    })

                }else{res.status(400).json({message: "paihe nai bache account me"});}
                
            }else {res.status(401).json({message: "Not your hostel bitch!"})};
        }else{
            res.status(404).json({message: "Not your hostel bitch!"});
        }
    });
});

// //TODO
// //return transactions array
// router.get(':roll/transaction', (req, res) => {

// })




//retrieve transactions
router.get('/:roll/transaction', (req, res)=>{
    console.log(req.params)
    const rolln = {roll: req.params.roll};
    console.log(rolln)
    UserSchema.findOne(rolln, (err, user) =>{
        if(user != null){
            console.log("user found")
            res.status(200).json(user.transactions)
        }else{
            console.log('user not found!')
            res.status(404).json({'message': 'not found'})
        }
    });//if (err) throw err;
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