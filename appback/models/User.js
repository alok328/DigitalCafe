const mongoose = require('mongoose');


const Userschema = mongoose.Schema({
     name: {
         type: String,
         required: true
        },
     ID:{
        type: String,
        required: true,
        //unique: true
    },
     password: {
         type: String,
         required: true
        },
     balance: {
         type: Number,
         required: true,
         default: 20000
     },

     transactions: [{type: String}],
    }
); 

module.exports = mongoose.model('Userinfo', Userschema);