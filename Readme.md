# Sakai Server/Client Repository README

# Introduction

This repository contains code to successfully use Box Cloud services within Duke's production Sakai server. This code contains Java backend code
that interacts with Box using Box's Java API as well as PHP code to run on an Apache server. We set up an Apache server on a VM obtained
from Duke and successfully integrated our backend Java code with our PHP code. This code base allows Instructors at Duke to add Box as an
External Tool and allows any users of the Sakai course site to be able to access and interact with their Duke Box cloud service accounts 
within Sakai. The following sections go more in detail about what we accomplished and how we achieved our goals.
 
# Box Sakai Integration Server

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

Additionally, this Server utilized a PostgreSQL database to store access and refresh tokens from Box. In order to authenticate
our Duke Box Service Account with Box we must obtain access and refresh tokens from Box. After we initially obtained our access
and refresh tokens, we can continually refresh our access tokens after they expire every 60 minutes. This involves storing our 
access and refresh tokens since we a request may not necessarily come within the 60 minute timespan. If a request does not come
the access and refresh token stored in memory would be gone, and there would be no way to reconnect to our Duke Box Service Account.
This is the main reason for our database, to continually store our access and refresh tokens. For more details on the access and 
refresh tokens look at the Database sectoin.
 
# Database

The database is used to store an access token and refresh token. The access token is used to connect to our
Duke Box Service Account programmatically and create folders within the account. These access tokens expire
every 60 minutes. As a result, you must send a POST request to Box's Token endpoint URL with your refresh 
token, client ID and client Secret specified. Box will respond with a new set of access and refresh tokens.
The new access token will be valid for another 60 minutes, and the refresh token can be used once again
to get a new set of coins. The database will store each set of new access and refresh tokens whenever
one set expires so that our program can continue to connect to the Duke Box Service Account. 
 
# Database Configuration

This section explains how our database was set up to allow developers to successfully test our files locally and across platforms.
 We created a PostgresSQL database named "db" with a username: "postgres" and password: "passw0rd". This database
holds one relation named TOKEN with the columns: 1. ID 2. ACCESS_TOKEN 3. REFRESH_TOKEN.
The PSQL Query statemnts below can be used within a Terminal Shell connected to PostgresQL
to create the appropriate relations: 
 
1. Create DATABASE db;

2. CREATE TABLE TOKEN(ID SERIAL PRIMARY KEY, ACCESS_TOKEN VARCHAR(100) NOT NULL, REFRESH_TOKEN VARCHAR(100) NOT NULL);

 
 
# Functions: Display Box Within Sakai

Our code and running server can successfully display Box's interface within the Duke Sakai site as an LTI External Tool. 
After standing up a server we had to redirect users to a webpage associated with Box. For greater detail, please
look at the Server Setup section of the README.

# Code To Create Folders Programmatically

This section explains the Java backend and how we compiled files in order to have our server work successfully.The Box_Connect_Input folder 
contains Java source code as well as the correct JAR files and other dependencies to correctly use Box's Java SDK. Through Box's Java API
we created functions to create a new Box Folder.

The code in App.java consists of multiple functions that define how to create a new folder and share it with a set of users. Additionally we were able to figure out how to use OAuth 2.0 authentication that avoids having OIT formally do a security review, which is what would have been necessary had we used JWT Server Authentication. Our file utilizes a PostgresSQL database to store access and refresh tokens. Access tokens, used to connect to a Box account programmatically, expire every 60 minutes. When the token expires, our program will catch the exception associated with an expired access token, send a POST request to Box's token refreshing endpoint, receive new tokens, and replace the old tokens within our database. We need to now just determine how to package our Maven project into one executable .Java or JAR file, to successfully run on our PHP server.

 The main functions are: 

1. **Professor_Create**: this function creates a new folder owned by a Box Account, and sets a professor as a Editor through an email

2. **Find_And_Share**: this function will attempt to add an Instructor or a Student as a Collaborator on a folder with the appropriate permissions. If a user is already a collaborator an exception will be thrown, which is caught by this function and handled elegantly. 

3. **Send_Post**: this function is used to refresh access and refresh tokens when access tokens expire after 60 minutes

4. **PSQL_GetToken**: this function pulls the access and refresh token values from the database and sets them equal to global variables for use within our program.

5. **PSQL_Update**: this function is called to update the database values of the access and refresh tokens once a set of tokens has expired.

The other functions in the code primarily ask for user input and store that information in strings for use by the functions outlined above.

Calls to each of these are made after a few configuration lines are ran to successfully connect to Box's API. We have elected to limit the functionality of our program and not allow instructors to share existing folders and only create new folders.


# Workflow
The typical workflow would be for a professor to go their Sakai course site, go to the Manage Tools section of the course site, 
scroll down to the External Tools, and check the box next to the Box tool to add Box as an External Tool within their site.
 After doing so, they should go to their home page and click on the Box tool. This will trigger a POST request to be sent
 from the Duke Sakai server to our Box Sakai Integrator middleware. This middleware will then connect to Duke's Box Service Account
 and create a new Course site within that Box account that  matches the naming convention of the course site. The Instructor will then
 be added as a collaborator on the file and be given "Editor" privileges. After the folder associated with the course site is created 
 from the Instructor's initial click on the Box tool, any user who has access to that course site must click the Box  tool themselves to 
 be added as Collaborators. If a Student clicks on the tool, they will be granted Viewer privileges to the Box folder, while if an 
 Instructor clicks on Box, they will be given Editor privileges.
 
 # How to Test/Expected Output
We have our Box Tool integrated on both a test Sakai server as well as Duke's production Sakai server. To test on the test Sakai server,
users must request a username and password from the Sakai Server/Client development team. After doing so they will added to the ECE651
test Course site. There, they will see a Box tool tab. Users should click on that tool. After clicking on that tool two things should happen:
1. Users should be able to view their Duke Box Account files within Box's User Interface

2. Users should see that they now have gained access to a ECE651 Box course folder. If the user is a Student within the ECE651 Sakai
course site they will have Viewer permissions. If the user is an Instructor, they will have Editor privileges.

To test on Duke's production Sakai server users should go to the BOX INT 100 1630 Sakai Course Site. If they do not have access to this site, 
they should contact any members of the Sakai  Server/Client team to gain access. There they should see the Box tool on the left hand
pane. When a user clicks on Box,a POST request will be sent to our stood up middleware. In response the middleware will add the user 
as a collaborator to the Box Course folder associated with this Sakai course site. In this case the course folder is called BOX INT 100 1630.
After clicking the Box tool, users should see their Duke Box Account files within Box's User Interface and gain access as either a Viewer or
Editor to the BOX INT 100 1630 Box folder depending on whether they are a Student or Instructor who is accessing the BOX INT 100 Course Site.
***Note***: This first initial click on the Box tool by the Instructor of a course sitewill create the Box folder associated with the course site.
Any subsequent clicks will add users as collaborators, but the first initial click by an ***Instructor*** creates a Box folder for a 
course site. This is outlined within the Workflow section.





            
