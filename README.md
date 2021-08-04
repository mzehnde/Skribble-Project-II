# Set UP

## Required Tools
If you want to make use of the program the following tools are required

- Java JDK:
  - To Check if you have it already installed run
    >java --version
  - tutorial how to install: https://www.youtube.com/watch?v=IJ-PJbvJBGs
  - download here: https://www.oracle.com/java/technologies/javase-jdk16-downloads.html

- Maven: Because the application is compressed into a jar File for the purpose of running the program over the cmd Line we use Maven.   
  - to check if you have it already installed run
    >mvn --version
  - Version: 3.8.1
  - tutorial how to install: https://www.youtube.com/watch?v=RfCWg5ay5B0
  - download here: https://maven.apache.org/download.cgi  

## How to use the program
1. Make sure all required tools are installed
2. Download the source code from Github and save it somewhere on your desktop
3. Open the Code in an IDE (e.g Intellij)
4. Decide which Use Case (Use Case 1: Upload/Download *one* PDF or Use Case 2: Upload/Download *multiple* PDF's at once) you want to use the application for¨
   - Use Case 1: If you decide to use the application for this Use Case, comment out line 20 and line 21 in the Main Class such that only UseCase1 is called.
   - Use Case 2: If you decide to use the application for this Use Case, comment out line 19 in the Main Class such that only UseCase2 is called.
5. Now fill in your username and api-key directly in the code such that you can be logged in and use the API: Line 20 in the Main Class
6. **IMPORTANT:** After every change in the Code (like Step 4/5) you have to run this command while being in the root folder of the application to generate a new jar File that will be run:
   >mvn clean compile assembly:single
7. Now you are set up and can continue with the specific use cases described below


# Upload and Download **one** PDF File

The most simple Use Case of Uploading a PDF File, waiting for it to be signed and downloading the file after, consists of three arguments and three steps.
- Argument 1: The Path of the file that has to be signed.
- Argument 2: The path where the *signed* file should be saved.
- Argument 3: The E-Mail of the person that has to sign the PDF document.

While being in the target folder of the application, the program can be started via the Command Line with this command:

>java -jar UseCase1-1.0-SNAPSHOT-jar-with-dependencies.jar Argument1 Argument2 Argument3

After this call the following three steps will be executed.

## 1. Login the User

The following API-Call will be used for step 1:
> https://api.scribital.com/v1/access/login

First of all you have to be logged in, in order to use the API of Skribble. To do that, a valid Profile has to exist.
You need to specify your username and api-key directly in the code in the Main Class on Line 53 (subject to change).
To Log yourself in, a new instance of User has to be created with your username and api-key. After that, the loginUser() function will be called on that created instance. A request to the API of Skribble will be sent and you will be logged in. You receive a token for verification and are able to work with Skribbles API.


## 2. Create a Signature-Request

The following API-Call will be used for step 2:
> https://api.scribital.com/v1/signature-requests

After logging in, a Signature-Request with the Signers E-Mail and the file that has to be signed and uploaded will be created.
A new instance of SignatureRequest will be created. It consists of the user, the signers E-Mail and the token for authentification.
After that the function createSR can be called in order to send a request to Skribbles API. Argument 1 will be used here in order to upload the wanted file.
Now the file is uploaded to Skribble for the signer to be signed.


## 3. Download the signed file

The following API-Calls will be used for step 3:
- > Polling: https://api.scribital.com/v1/signature-requests/SIGNATURE_REQUEST_ID
- > Download: https://api.scribital.com/v1/documents/SIGNED_DOCUMENT_ID/content

For the download of the signed PDF a new Poller instance has to be instanciated. The Signature-Request-Response of step 2 acts as a field in that class.
Now startPolling() is called to start the Poll. Every 10 seconds the first GET Request on the ID of the Signature-Request is called to check if the document was signed already.
Once the document was signed, the second GET Request will be called in order to download the signed PDF. The Location where it will be saved was specified in argument 2. You can find your signed document there.


# Upload and Download **multiple** PDF Files

There are a lot of possibilities to make use of this Use Case. For example there could be a change in an employment contract that regards all of a firms employees.
With the help of the following explanation of the implemented Use Case it will be possible to send Signature Requests to all employees with their corresponding updated employment contract at once. 

This Use Case consists of three arguments and 5 steps.
- Argument 1: A Path to a CSV File has to be provided. It should consist of two columns: 
  - column 1: The filename of the PDF file to be signed
  - column 2: The E-Mail of the signer that should sign the file of column 1
- Argument 2: The path to the directory where the files specified in the CSV File of argument 1 are saved (the files should be saved in one directory)
- Argument 3: The path where the file with the resulting Signature-Request-ID's or the signed PDF files should be saved

While being in the target folder of the application, the program can be called via the Command Line with this command:

>java -jar UseCase1-1.0-SNAPSHOT-jar-with-dependencies.jar Argument1 Argument2 Argument3

After this call the following steps will be executed.


## 1. Login the User

The following API-Call will be used for step 1:
> https://api.scribital.com/v1/access/login

First of all you have to be logged in, in order to use the API of Skribble. To do that, a valid Profile has to exist.
If you haven't already, you need to specify your username and api-key directly in the code in the Main Class on Line 20.
To Log yourself in, a new instance of User has to be created with your username and api-key. After that, the loginUser() function will be called on that created instance. A request to the API of Skribble will be sent and you will be logged in. You receive a token for verification and are able to work with Skribbles API.


## 2. Read the CSV File

In order to send the Signature Requests to the signers specified in the CSV File you have to read out the file.
A new CSVFile instance is created with the path where it is saved as a field (argument 1). With the readCSVFile() function the File is being read and a List with Strings is created (CSVFileList). Each index corresponds to one row in the CSV File. For Example a CSV File with two rows will look like this:
>TestFile.pdf;example@mail.com, TestFile2.pdf;example2@mail.com


## 3. Make use of the CSVFileList

Such that you can work smoothly with the CSV File, the CSVFile List should be converted into an Array consisting of Signer Entities.
We iterate through the CSVFileList and create a new Signer instance for every index (with filename & E-Mail). We add these instances to a new Array called signerList.
Now we have a List that consists of all the signers specified in the CSV File. These signer Instances consist of the E-Mail where the Signature Request should be sent to and the file that should be uploaded with that Signature-Request.


## 4. Send the Signature-Requests

The following API-Call will be used for step 4:
> https://api.scribital.com/v1/signature-requests

Now that we have a list with all the signers that should receive a Signature-Request we can call the API to process these Requests.
To do that we iterate through our signerList and send out a Signature-Request via API for every index of that list. For every index the Request Body will be filled out with the signers E-Mail and the base64 Content of the PDF file he should sign. We receive a response for every Request that we save in a new Array called responseList.


## 5. Use the response of the Signature-Requests

For step five we have two possibilities. The implementation in the source code uses possibility 1, but you are free to also use possibility 2 if you follow the described steps.
1. We either save the Signature-Request-ID we receive as a response for every Signature-Request in a .txt file such that we can make use of it at a later point. For example after you know every employee signed its contract you can use this file to receive the document ID via API and download the signed Document.
To proceed with this possibility you don't have to do anything because it is already implemented.
To create the file, we iterate through our responseList to write every Signature-Request-ID and corresponding E-Mail to the File. It then can be found under the path specified as argument 3.
2. A second possibility would be polling. To make use of this possibility, comment out step 5 out of the function doUseCase2() in the Main class and delete the comment for step 6 in the Main class.
With this option, every 10 seconds a GET request will be called to the API of Skribble in order to check if the document was signed. If it was signed it will automatically be saved under the path specified in argument 3.
