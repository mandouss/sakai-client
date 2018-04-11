# Sprint III README

In this sprint we were able to finalize a workflow that should allow us to be successful in integrating Box with Sakai. Our proposed methodology is to create some middleware that will listen for HTTP requests from Sakai. When a professor chooses to add Box as a tool in the list of External Tools, a request will be sent to our middleware. Our middleware will respond by either 1. programatically creating a new Box folder under a Duke service account that will set the professor as an Editor and all students as Viewers or 2. sharing an existing folder in the professor's Box Account, whose Shareable Link a professor will provide in an interface developed on Sakai, with a roster of students and setting them as Viewers. After a folder is shared or created, users of the Sakai course website should be able to view Box and its interface within Sakai and see the newly shared or created folder within their account.

For this sprint, we outlined a few objectives we thought were feasible and appropriate steps in accomplishing the methodology explained above. Below are the objectives we've accomplished, explained in detail:


# Objective I: Display Box Within Sakai

We were able to successfully display Box's interface within the Duke Sakai site. As explained and shown in the Embed BOX into Sakai folder, we determined what URL we needed to configure with Sakai's Tool interface. After doing so, users are able to access their respective Duke Box accounts within Sakai, and fully interact with the files in their account. For greater detail, look at the README in Embed BOX in Sakai folder.

# Objective II: Write Code To Create and Share Box Folders Programmatically

The Box_Dev folder contains Java source code as well as the correct JAR files and other dependencies to correctly use Box's Java SDK. Through Box's Java API we created functions to both create a new Box folder to share with a roster of students, and to share an existing Box folder with a roster of students. The App.java file is currently connecting to a Box developer account created by one the Sakai Client/Server team members. In order to test the functionality of the program, users should sign in to the Box Developer Account using the following credentials:

    Username: nisargdbh@gmail.com

    Password: October31

The code in App.java consists of multiple functions that define how to successfully share an existing folder with a set of users, and how to create a new folder and share it with a set of users. Additionally the current program represents the type of business logic we foresee ourselves incorporating on our middleware. 

The current program will ask a user whether they want to create or share a folder, at which point the user must enter **Create** or **Share**. If they want to create a folder, a user will them be prompted for the name of the folder as well as the email of the professor who is to become an Editor for that folder. It will then ask a user for a list of emails, separated by new lines, of the students they want on this folder. When they are done adding student emails to share the new folder with they should type "**exit**"" and press Enter. If they choose to share a folder a user will be asked for an existing shareable link for a folder, which a user must get from their Box account for a folder they want to share. They will then be prompted to enter emails of students they want to share the folder with, again separated by newline characters. Once they are finished entering student emails, they should type in **exit** and press Enter to continue execution.

 The main functions are: 

1. **Professor_Create**: this function creates a new folder owned by a Box Account, and sets a professor as a Editor through an email

2. **Share_With_Students**: this function will add a list of students as a Viewer to a folder. This function is called after Professor_Create, to share the new folder with a student roster

3. **Share_Existing_Folder**: this function will get the folder ID of a folder within a professor's Box account using a shareable link associated with that folder. That folder will then be shared with a list of students who will have Viewer privileges within that folder.

The other functions in the code primarily ask for user input and store that information in strings for use by the functions outlined above.

Calls to each of these are made after a few configuration lines are ran to successfully connect to Box's API.The one issue with the above code segment is that we still had trouble correctly sharing a folder with students. As a result, the Share_Existing_Folder function has the logic we functionally would like, but it does not currently share the folder with users. Instead, it will print out information about the folder using the shared link you provided to our program.


# How to Test

Users should download the BoxDev project folder and import the folder into Eclipse to allow it to run.
After running it, they should follow the prompts and instructions above.
 
# Expected Output

The expected output within Eclipse should be a list of all of the current files within the Box account, followed by a prompt asking users if they want to create or share a folder. If they create a folder, users must ensure they do not choose a name for a folder that they saw in the initial list as two folders cannot have the same name in Box. After creating a folder, adding a professor email as Editor, and choosing which emails to invite as Viewers, users should be able to navigate to the Box account with the above credentials to see that a new folder is formed. Specifically, after logging into the above account, users should click Admin Console on the right hand side of the screen. They should then navigate to and click the Content Manager tab on the top and then press Test Sakai on the right hand panel. They should then see the new folder that they programatically created and be able to look at the folder's properties to see who they were able to successfully add as collaborators. 

Note: Make sure to not try inviting the same collaborator twice as Box will throw an error as collaborators cannot be readded.

 
# Objective III: Deployed Apache Server that is Configurable with Java Backend

Our objective was to stand up a simple web server that acknowledged that someone was trying to connect to the server. A user should navigate to the address: **152.3.64.14** in their Internet browser. This website should then have a small welcome message followed by the IP address of their computer. This was a simple objective to stand up a server that we knew we could easily add Java code to. The above Java API calls and functions defined in Objective II can be used to create logic that we can easily integrate with our source code on this simple server. 