const Pool = require("pg").Pool;

const pool = new Pool({
    user: "postgres",
    password: "password&123",
    host: "localhost",
    port: 5432,
    database: "userprofiles"
});

module.exports = pool;