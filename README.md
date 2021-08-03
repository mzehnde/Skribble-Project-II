##Upload and Download **one** PDF File
The most simple Use Case of Uploading a File, waiting for it to be signed and downloading the file after, consists of two arguments and three steps.
-Argument 1: The Path of the file that has to be signed.
-Argument 2: The path where the *signed* file should be saved.
The program can be called via the Command Line with this command:

>jjh

#1. Login the User
First of all the user has to be logged in, in order to use the API of Skribble. To do that, a valid Profile has to exist.
The User needs his username and his corresponding api-key.
To Log the user in, A new instance of User has to be created with said username and api-key. After that, you can call the loginUser() function on that created instance.
The API of Skribble will be called and the specified user is logged in. Now he can work with the API.
#2. Create a Signature-Request
After logging in, the user has to create a Signature-Request with the Signers E-Mail and the file that has to be signed and uploaded.




