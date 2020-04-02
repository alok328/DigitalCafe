const LocalStrategy = require('passport-local').Strategy;
const mongoose = require('mongoose');
const bcrypt = require('bcryptjs');

//load user model

const User = require('../models/UserSchema');

module.exports = function(passport){
    passport.use(
        new LocalStrategy({usernameField: 'roll'}, (roll, password, done) => {
        //find user
        User.findOne({roll: roll})
            .then(user =>{
                if(!user){
                    return done(null, false, {message: 'Roll number not registered'});
                }

            //match password
            bcrypt.compare(password, user.password, (err, isMatch) =>{
                if(err) throw err;

                if(isMatch){
                    return done(null, user);
                }else{
                    return done(null, false, {message: 'Password incorrect'});
                }
            })


            })
            .catch(err => console.log(err));

        })



    );

    /*passport.serializeUser( function(id, done)  {
        done(null, User.roll);
    });

    passport.deserializeUser(function(id, done){
        User.findById(roll, function(err, user){
            done(err, user);
        });
    });*/


    passport.serializeUser(function(user, done) {
        done(null, user);
      });
      
      passport.deserializeUser(function(user, done) {
        done(null, user);
      });



}



