# Sakai Server/Client Repository README

 
# Objective I: Display Box Within Sakai

We were able to successfully display Box's interface within the Duke Sakai site as a Web Content Tool. As explained and shown in the Embed BOX into Sakai folder, we determined what URL we needed to configure with Sakai's Tool interface. After doing so, users are able to access their respective Duke Box accounts within Sakai, and fully interact with the files in their account. For greater detail, look at the README in Embed BOX in Sakai folder. The issue here is that this uses a separate interface from the LTI External Tool interface within Sakai. We are able to display Box with Sakai as a Web Content Tool, but this uses a hardcoded link and will not allow us to dynamically change what folder a user may be viewing within their Box account.

# Objective II: Write Code To Create Folders Programmatically

The Box_Connect_Input folder contains Java source code as well as the correct JAR files and other dependencies to correctly use Box's Java SDK. Through Box's Java API we created functions to create a new Box Folder.

The code in App.java consists of multiple functions that define how to create a new folder and share it with a set of users. Additionally we were able to figure out how to use OAuth 2.0 authentication that avoids having OIT formally do a security review, which is what would have been necessary had we used JWT Server Authentication. Our file utilizes a PostgresSQL database to store access and refresh tokens. Access tokens, used to connect to a Box account programmatically, expire every 60 minutes. When the token expires, our program will catch the exception associated with an expired access token, send a POST request to Box's token refreshing endpoint, receive new tokens, and replace the old tokens within our database. We need to now just determine how to package our Maven project into one executable .Java or JAR file, to successfully run on our PHP server.

 The main functions are: 

1. **Professor_Create**: this function creates a new folder owned by a Box Account, and sets a professor as a Editor through an email

2. **Find_And_Share**: this function will attempt to add an Instructor or a Student as a Collaborator on a folder with the appropriate permissions. If a user is already a collaborator an exception will be thrown, which is caught by this function and handled elegantly. 

3. **Send_Post**: this function is used to refresh access and refresh tokens when access tokens expire after 60 minutes

4. **PSQL_GetToken**: this function pulls the access and refresh token values from the database and sets them equal to global variables for use within our program.

5. **PSQL_Update**: this function is called to update the database values of the access and refresh tokens once a set of tokens has expired.

The other functions in the code primarily ask for user input and store that information in strings for use by the functions outlined above.

Calls to each of these are made after a few configuration lines are ran to successfully connect to Box's API. We have elected to limit the functionality of our program and not allow instructors to share existing folders and only create new folders.

# How to Test/Expected Output
At the moment we are still testing with Duke's production Sakai server. We are in the middle of setting up a test Sakai server for use by all Sakai development teams. Anyone looking to test this functionality should go to the BOX INT 100 1630 Sakai Course Site. If they do not have access to this site, they should contact any members of the Sakai Server/Client team to gain access. There they should see two tools: Box Linker and Box. When a user clicks on Box Linker, a POST request will be sent to our stood up middleware. In response the middleware will add the user as a collaborator to the Box Course folder associated with this Sakai course site. This can be verified by going to the Box tool. Users should click on the Box tool and then check if they are able to see a new Course folder that is shared with their account. 

# Workflow
The typical workflow would be for a professor to go their new course site, go to the Manage Tools section of the course site, and add both Box Linker and Box as tools they want incorporated on their site. After doing so, they should click on the Box Linker tool. This will trigger a new Course site that matches the naming convention of the course site to be created that will give the Instructor "Editor" privileges. After a folder is created any user who has access to that course site must click the Box Linker tool themselves to be added as Collaborators. If a Student clicks on the tool, they will be granted Viewer privileges to the Box folder, while if an Instructor clicks on Box Linker, they will be given Editor privileges.
 

# Objective III: Deployed Apache Server that is Configurable with Java Backend

We have stood up some middleware that can successfully parse an LTI POST request sent by Sakai. We now just have to be able to pass those POST parameters to our compiled JAR file with our Box functionality and we should be able to successfully configure a new Box tool in Sakai to be used.

NOTE: At the moment our metholodology involves the use of 1. a Web Content Tool 2. an LTI Tool. We were unable to learn how Sakai expects data to be sent back from a 3rd party tool so we do not know how to get an iFrame to successfully display within Sakai.

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


# Database Purpose

The database is used to store an access token and refresh token. The access token is used to connect to our
Duke Box Service Account programmatically and create folders within the account. These access tokens expire
every 60 minutes. As a result, you must send a POST request to Box's Token endpoint URL with your refresh 
token, client ID and client Secret specified. Box will respond with a new set of access and refresh tokens.
The new access token will be valid for another 60 minutes, and the refresh token can be used once again
to get a new set of coins. The database will store each set of new access and refresh tokens whenever
one set expires so that our program can continue to connect to the Duke Box Service Account. 

# Server Setup

We obtained a virtual machine from Duke running Ubuntu 16.04. We used this VM to make an Apache Web Server that listens
for connections whenever uses direct traffic to our VM's specific IP. We used PHP to create a simple web interface that
can successfully direct and redirect network traffic the way we would like. Specifically, when users attempt to click
our Box Sakai Integrator tool, a POST request is sent to our VM. The PHP frontend will parse the parameters of that 
POST request, then run a Java executable file to interact with Box cloud services, and finally redirect users back to
their root folder within their Box account. To change anything on our server administrators should run the
the ssh command on their command line to connect to ***vcm@vcm-3385.vm.duke.edu*** and enter the correct password.

We realized that in order to get our tool to work with Duke's Production Sakai Server we needed our VM to work with HTTPS.
As a result, we registered our VM with the domain name ***sakaiboxintegrator.tk*** and obtained a free certificate for the 
domain from a Certificate Authority. These certificates last 90 days, so in order to continually renew them a user should
login to our VM and run the command: ***sudo certbot renew --dry-run*** to renew their certificate.
            
