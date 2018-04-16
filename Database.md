# Database Configuration


This database explains how our database was set up to allow developers to successfully test our files locally and across platforms.
 We created a PostgresSQL database named "db" with a username: "postgres" and password: "passw0rd". This database
holds one relation named TOKEN with the columns: 1. ID 2. ACCESS_TOKEN 3. REFRESH_TOKEN.
The PSQL Query statemnts below can be used within a Terminal Shell connected to PostgresQL
to create the appropriate relations:

1. Create DATABASE db;

2. CREATE TABLE TOKEN(ID SERIAL PRIMARY KEY, ACCESS_TOKEN VARCHAR(100) NOT NULL, REFRESH_TOKEN VARCHAR(100) NOT NULL);


For successful use of the database and tables, ensure you have a username named "postgres" whose password
is "passw0rd" as our Java file expects a user with such credentials.


# Purpose

The database is used to store an access token and refresh token. The access token is used to connect to our
Duke Box Service Account programmatically and create folders within the account. These access tokens expire
every 60 minutes. As a result, you must send a POST request to Box's Token endpoint URL with your refresh 
token, client ID and client Secret specified. Box will respond with a new set of access and refresh tokens.
The new access token will be valid for another 60 minutes, and the refresh token can be used once again
to get a new set of coins. The database will store each set of new access and refresh tokens whenever
one set expires so that our program can continue to connect to the Duke Box Service Account.