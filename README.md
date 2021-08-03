## Upload and Download **one** PDF File
The most simple Use Case of Uploading a File, waiting for it to be signed and downloading the file after, consists of three arguments and three steps.
-Argument 1: The Path of the file that has to be signed.
-Argument 2: The path where the *signed* file should be saved.
-Argument 3: The E-Mail of the person that has to sign the PDF document.

While being in the target folder of the application, the program can be called via the Command Line with this command:

>java -jar UseCase1-1.0-SNAPSHOT-jar-with-dependencies.jar Argument1 Argument2 Argument3

After this call the following three steps will be executed.

# 1. Login the User

The following API-Call will be used for step 1:
> https://api.scribital.com/v1/access/login

First of all you have to be logged in, in order to use the API of Skribble. To do that, a valid Profile has to exist.
You need to specify your username and api-key directly in the code in the Main Class on Line 53 (subject to change).
To Log yourself in, a new instance of User has to be created with your username and api-key. After that, the loginUser() function will be called on that created instance. A request to the API of Skribble will be sent and you will be logged in. You receive a token for verification and are able to work with Skribbles API.


# 2. Create a Signature-Request

The following API-Call will be used for step 2:
> https://api.scribital.com/v1/signature-requests

After logging in, a Signature-Request with the Signers E-Mail and the file that has to be signed and uploaded will be created.
A new instance of SignatureRequest will be created. It consists of the user, the signers E-Mail and the token for authentification.
After that the function createSR can be called in order to send a request to Skribbles API. Argument 1 will be used here in order to upload the wanted file.
Now the file is uploaded to Skribble for the signer to be signed.


# 3. Download the signed file

The following API-Calls will be used for step 3:
> Polling: https://api.scribital.com/v1/signature-requests/*signatureRequestID*
> Download: https://api.scribital.com/v1/documents/*signedDocumentID*/content

For the download of the signed PDF a new Poller instance has to be instanciated. The Signature-Request-Response of step 2 acts as a field in that class.
Now startPolling() is called to start the Poll. Every 10 seconds the first GET Request on the ID of the Signature-Request is called to check if the document was signed already.
Once the document was signed, the second GET Request will be called in order to download the signed PDF. The Location where it will be saved was specified in argument 2. You can find your signed document there.





