const mongoose = require('mongoose');


const Userschema = new mongoose.Schema({
    firstName: {
        type: String,
        required: true
    },
    lastName: {
        type: String,
        required: true
        },
    email: {
        type: String,
        required: true,
        //unique: true
    },
    password: {
        type: String,
        required: true
    },

    roll: {
        type: String,
        required: true
    },

    date: {
        type: Date,
        required: true,
        default: Date.now
    },
    balance: {
        type: Number,
        //required: true,
        default: 20000
    },

    transactions: [{
        menu: String,
        price: Number,
        date: { type: Date, default: Date.now },
        balance: Number
        }],
    
    hostel: {
        type: Number,
        required: true,
    }
}); 

module.exports = mongoose.model('User', Userschema);