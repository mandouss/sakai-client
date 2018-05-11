# Sakai Box Integration README

# Introduction

This repository contains code to successfully integrate Box Cloud services with Duke's production Sakai server. 
This code contains Java backend code that interacts with Box using Box's Java API as well as PHP code to run on an Apache server. The Java 
backend code and PHP code running on the Apache server listens for POST requests sent by Duke's LTI External Tool interface. This code serves
a couple functions: 
1. It redirects users to the root folder of their Duke Box accounts to allow Box Cloud services to be displayed within Sakai
2. It programmatically creates Box folders within a Duke Service Box Account dynamically.
3. Programmatically adds collaborators to created Box folders

The first functionality follows the format of normal Sakai LTI External Tools. In Sakai, the LTI External Tool has a Launch URL
that points to our Apache server. When this Apache server receives POST requests, it will redirect whoever is connecting, in this case the
specific user on Sakai, to Box. We conferred to this format as specified by the LTI v1.1 specification in order to have Box successfully
be displayed within Sakai.T he second functionality relies upon the Duke Service Box Account. Duke OIT gave us access to a specific Box Account, the Duke Service
Box Account, for us to create folders within. Our backend Java code programmatically authenticates to and connects to that account every
time our Apache server receives a POST request from Sakai. A folder if first created when an Instructor on his or her course site first 
clicks on the Box External Tool. After that, any user of the Sakai course site who clicks on our Box External tool will be added as collaborator
to that folder and will be able to see that folder when Box is displayed within Sakai. This is better explained in later sections.
***Note:*** Our middleware server adds collaborators based on emails. If any users of Sakai attempt to reuse this codebase to configure their
own middleware, they must ensure that their users of Sakai have usernames associated by emails. Additionally each of these emails must be
associated with a Box Account in order to see that email's corresponding Box Account within Sakai.

# Code
The main code here lies within the BoxInputv2 file and the Mid_Server folder. The BoxInputv2 file is a Java executable JAR file code
and the Mid_Server folder contains PHP code. The JAR file was compiled from a Maven project located in the BoxConnect_Input folder. The JAR file
expects three input arguments. Specifically it expects the 1. Role of the user 2. User email 3. Course name. These parameters
are sent by the Sakai LTI Extneral Tool POST request. Our PHP code parse the POST request, gets these three parameters and passes them as input
parameters to our Executable JAR and runs the JAR file. For users trying to reuse our code base they should look at the Java code base closely
to understand how the Java code expects input parameters. They should also look at our PHP code closely to determine how it parse Sakai
LTI POST requests.

# Initial Setup

In order to reproduce our functionality with our code, users must first obtain a Box Account. After doing so, they should create an Application
within the Developer Console. Note, the main configuration that they need to have is to specify that their application uses OAuth 2.0 Authentication.
After this step is finished, they must follow the OAuth 2.0 protocol to successfully obtain access and refresh tokens for this account. These access
and refresh tokens should then be inserted as the only row within the "TOKEN" table of PostgresSQL database we used in our setup. The purpose
of the database is explained more later in this README, but we need to store access and refresh tokens within the database as these tokens
continually expire and must be refreshed. Our Java backend program constantly refreshes these tokens and replaces the values within the database
with the most current tokens. After the above steps, users looking to use our code must setup a server as specified in the next section, and 
finally add these tokens to the database within the server.
 
# Box Sakai Integration Server

We obtained a virtual machine running Ubuntu 16.04 to use for our server. We used this VM to make an Apache Web Server that listens
for connections whenever uses direct traffic to our VM's specific domain node. We used PHP to create a simple web interface that
can successfully direct and redirect network traffic the way we would like. Specifically, when users attempt to click
our Box tool within Sakai, a POST request is sent to our VM by Sakai's External Tool interface. The PHP frontend will parse the parameters of that 
POST request, then run a Java executable file to interact with Box cloud services, and finally redirect users back to
their root folder within their Box account. To change anything on the server administrators should run the
the ssh command on their command line to connect to the VM.

We realized that in order to get our tool to work with Duke's Production Sakai Server we needed our VM to work with HTTPS.
As a result, we registered our VM with the domain name ***sakaiboxintegrator.tk*** and obtained a free certificate for the 
domain from a Certificate Authority. To get a free certificate we used the instructions found at: ***https://certbot.eff.org/lets-encrypt/ubuntuxenial-apache***.
***Note***: These certificates last 90 days, so in order to continually renew them a user should
login to their VM and run the command: ***sudo certbot renew --dry-run*** to renew their certificate. Users attempting to
reproduce our results on their own Sakai sites must get a domain name for their server for free and get a certificate themselves. This
is better explained later in the README.

Additionally, this Server utilized a PostgreSQL database to store access and refresh tokens from Box. In order to authenticate
our Duke Box Service Account with Box we must obtain access and refresh tokens from Box. After we initially obtained our access
and refresh tokens, we can continually refresh our access tokens after they expire every 60 minutes. This involves storing our 
access and refresh tokens since we a request may not necessarily come within the 60 minute timespan. If a request does not come
the access and refresh token stored in memory would be gone, and there would be no way to reconnect to our Duke Box Service Account.
This is the main reason for our database, to continually store our access and refresh tokens. For more details on the access and 
refresh tokens look at the Database section, which explains how to setup the database.
 
# Database: Purpose

The database is used to store an access token and refresh token. The access token is used to connect to our
Duke Box Service Account programmatically and create folders within the account. These access tokens expire
every 60 minutes. As a result, you must send a POST request to Box's Token endpoint URL with your refresh 
token, client ID and client Secret specified. Box will respond with a new set of access and refresh tokens.
The new access token will be valid for another 60 minutes, and the refresh token can be used once again
to get a new set of coins. The database will store each set of new access and refresh tokens whenever
one set expires so that our program can continue to connect to the Duke Box Service Account. 
 
# Database: Configuration

This section explains how our database was set up to allow developers to successfully test our files locally and across platforms.
 We created a PostgresSQL database named "db" with a username: "postgres" and password: "passw0rd". This database
holds one relation named TOKEN with the columns: 1. ID 2. ACCESS_TOKEN 3. REFRESH_TOKEN.
The PSQL Query statemnts below can be used within a Terminal Shell connected to PostgresQL
to create the appropriate relations: 
 
1. ***Create DATABASE db;***

2. ***CREATE TABLE TOKEN(ID SERIAL PRIMARY KEY, ACCESS_TOKEN VARCHAR(100) NOT NULL, REFRESH_TOKEN VARCHAR(100) NOT NULL);***

 
 
# Function: Display Box Within Sakai

Our code and running server can successfully display Box's interface within the Duke Sakai site as an LTI External Tool. It conforms to
the LTI v1.1 specification so users who want to reproduce this result on their own Sakai sites should have their Sakai sites
confirm to that specification.


# Function: Create Box Folders and Invite Collaborators

This section explains the Java backend and how we compiled files in order to have our server work successfully.The Box_Connect_Input folder 
contains Java source code as well as the correct JAR files and other dependencies to correctly use Box's Java SDK. Through Box's Java API
we created functions to create a new Box Folder.

The code in App.java consists of multiple functions that define how to create a new folder and share it with a set of users. 
Additionally we were able to figure out how to use OAuth 2.0 authentication to connect to our Box Account. Users may want to use Server JWT
Authentication, but our Java code does not support that authentication model. Our file utilizes a PostgresSQL database to store access and 
refresh tokens. Access tokens, used to connect to a Box account programmatically, expire every 60 minutes. When the token expires, 
our program will catch the exception associated with an expired access token, send a POST request to Box's token refreshing endpoint, 
receive new tokens, and replace the old tokens within our database. If there is any confusion about the access and refresh tokens, readers
can refer to Box's Documentation about how they use OAuth 2.0.

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
 
# How to Test/Expected Output Within Our Setup
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

For users who want to setup their own server for their own Sakai site, they could test the functionality within their own Sakai sites. This
can be done by creating a new External LTI Tool within their Sakai site and setting their launch URL to the domain name of their server.
After doing so, and ensuring that each of their users have usernames associated with emails, and following all of the steps above, this 
code should provide the above functionalities.





            
