# Sprint III README

 
# Objective I: Display Box Within Sakai

We were able to successfully display Box's interface within the Duke Sakai site. As explained and shown in the Embed BOX into Sakai folder, we determined what URL we needed to configure with Sakai's Tool interface. After doing so, users are able to access their respective Duke Box accounts within Sakai, and fully interact with the files in their account. For greater detail, look at the README in Embed BOX in Sakai folder.

# Objective II: Write Code To Create and Share Box Folders Programmatically

The Box_Dev folder contains Java source code as well as the correct JAR files and other dependencies to correctly use Box's Java SDK. Through Box's Java API we created functions to create a new Box Folder.

The code in App.java consists of multiple functions that define how to create a new folder and share it with a set of users. We need to now just compile the file into a JAR file, and pass in inputs to the JAR file for the code to run successfully.

 The main functions are: 

1. **Professor_Create**: this function creates a new folder owned by a Box Account, and sets a professor as a Editor through an email

2. **Find_And_Share**: this function will attempt to add an Instructor or a Student as a Collaborator on a folder with the appropriate permissions. If a user is already a collaborator an exception will be thrown, which is caught by this function and handled elegantly. 


The other functions in the code primarily ask for user input and store that information in strings for use by the functions outlined above.

Calls to each of these are made after a few configuration lines are ran to successfully connect to Box's API. We have elected to limit the functionality of our program and not allow instructors to share existing folders and only create new folders.


# How to Test: TO DO

 
# Expected Output: TO DO

 
# Objective III: Deployed Apache Server that is Configurable with Java Backend

We have stood up some middleware that can successfully parse an LTI POST request sent by Sakai. We now just have to be able to pass those POST parameters to our compiled JAR file with our Box functionality and we should be able to successfully configure a new Box tool in Sakai to be used.

NOTE: At the moment our metholodology involves the use of 1. a Web Content Tool 2. an LTI Tool. We were unable to learn how Sakai expects data to be sent back from a 3rd party tool so we do not know how to get an iFrame to successfully display within Sakai.
