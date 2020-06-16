const express = require('express');
const app = express();
const cors = require("cors");
const pool = require("./db");

//middleware
app.use(cors());
app.use(express.json());

app.post('/edit', async (req,res)=>{
    
    const newUser = {
        email: req.body.email,
        password: req.body.password,
        age: req.body.age
    }
    
    try {
    
        const editUserProfile = await pool.query(
            "UPDATE account SET age=$1 WHERE email=$2 AND password=$3 RETURNING *",
            [newUser.age,newUser.email,newUser.password]
        );
        const toPrint = editUserProfile.rows[0];
        res.status(200).send(toPrint);
    } catch (err) {
        console.status(404).send();
    }
});


app.post('/signup', async (req,res)=>{
    
    const newUser = {
        email: req.body.email,
        password: req.body.password,
        name: req.body.name,
        age: req.body.age
    }
    
    try {
    
        const newUserProfile = await pool.query(
            "INSERT INTO account (email,password, name, age) VALUES($1,$2,$3,$4)",
            [newUser.email,newUser.password,newUser.name,newUser.age]
        );

        res.status(200).send(newUserProfile);
    } catch (err) {
        console.status(404).send();
    }
});

app.post('/login',async(req,res)=>{

    const credentials = {
        email: req.body.email,
        password: req.body.password
    }
    
    try {
        const loginRequest = await pool.query("SELECT * FROM account WHERE email = $1 AND password = $2", 
        [credentials.email, credentials.password]);
        const toPrint = loginRequest.rows[0];
        res.status(200).send(toPrint);
    } catch (err) {
        console.status(404).send();
    }

})

app.listen(3000, ()=>{
    console.log("Listening on port 3000...")
})