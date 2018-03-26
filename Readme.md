# Sprint II README

In this sprint we were able to finalize a workflow that should allow us to be successful in integrating Box with Sakai. Our proposed methodology is to create some middleware that will listen for HTTP requests from Sakai. When a professor chooses to add Box as a tool in the list of External Tools, a request will be sent to our middleware. Our middleware will respond by either 1. programatically creating a new Box folder under a Duke service account that will set the professor as an Editor and all students as Viewers or 2. sharing an existing folder in the professor's Box Account, whose Shareable Link a professor will provide in an interface developed on Sakai, with a roster of students and setting them as Viewers. After a folder is shared or created, users of the Sakai course website should be able to view Box and its interface within Sakai and see the newly shared or created folder within their account.

For this sprint, we outlined a few objectives we thought were feasible and appropriate steps in accomplishing the methodology explained above. Below are the objectives we've accomplished, explained in detail:


# Objective I: Display Box Within Sakai

We were able to successfully display Box's interface within the Duke Sakai site. As explained and shown in the Embed BOX into Sakai folder, we determined what URL we needed to configure with Sakai's Tool interface. After doing so, users are able to access their respective Duke Box accounts within Sakai, and fully interact with the files in their account. For greater detail, look at the README in Embed BOX in Sakai folder.

# Objective II: Write Code To Create and Share Box Folders Programmatically

The Box_API folder contains Java source code as well as the correct JAR files and other dependencies to correctly use Box's Java SDK. Through Box's Java API we created functions to both create a new Box folder to share with a roster of students, and to share an existing Box folder with a roster of students. The Box_test.java file is currently connecting to a Box developer account created by one the Sakai Client/Server team members. In order to test the functionality of the program, users should sign in to the Box Developer Account using the following credentials:

    Username: nisargdbh@gmail.com

    Password: October31

The code in BOX_test.java consists of multiple functions that define how to successfully share an existing folder with a set of users, and how to create a new folder and share it with a set of users. The main functions are: 

1. **Professor_Create**: this function creates a new folder owned by a Box Account, and sets a professor as a Editor through an email

2. **Share_With_Students**: this function will add a list of students as a Viewer to a folder. This function is called after Professor_Create, to share the new folder with a student roster

3. **Share_Existing_Folder**: this function will get the folder ID of a folder within a professor's Box account using a shareable link associated with that folder. That folder will then be shared with a list of students who will have Viewer privileges within that folder.

Calls to each of these are made after a few configuration lines are ran to successfully connect to Box's API.The one issue with the above code segment is that we still had trouble correctly using OAuth 2.0 authentication, which Box's API relies upon. As a result, in order to connect to the above username's account, we used a Developer Token. Developer Token's are primarily used as a testing methodology as they are  only valid for 1 hour time slots.


# How to Test

Users should download the Box_API project folder and import the folder into Eclipse to allow it to run. When testing the above functionality, users should sign in to the above Box Developer account, click on the "Test Sakai" app, navigate to Configuration, and press Generate Developer Token. After the token is generated, the token must be copied and pasted in line 105 as a parameter for myaccount.Boxconnect(). After doing so they should click on "My Apps" on the left hand panel and then "My Files" once again on the left hand panel. After a new window opens displaying all of the files owned by this account, the user should run the Box_test.java file within Eclipse. 

# Expected Output

The expected output within Eclipse should be a list of all of the current files within the Box account, followed by the name of the folder that will be shared using a shareable link, followed by a new list of all of the current files within the Box account except this list now has a new folder created named "Sigma". If the permissions are examined in the webpage displaying all of the files of this Box account, the user should see that the new folder "Sigma" has made a professor, in this case the email "nrd10@duke.edu", an Editor and made a student, here the email "zw73@duke.edu", a Viewer. If the folder that was shared using a shareable link is examined, "Omnicron", we should see that the email "zw73@duke.edu", who would be 
a student in this scenario, is now a Viewer.
 
# Objective III: Deployed Apache Server that is Configurable with Java Backend

