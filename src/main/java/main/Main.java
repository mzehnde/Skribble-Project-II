package main;

import AllUseCases.User;
import JsonEntities.SignatureRequestResponse;
import UseCase1.Poller;
import UseCase1.SignatureRequest;
import UseCase2.*;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.exit;

public class Main {

    //@Todo: write arguments in args --> DONE
    //@Todo: format of classes (static beginning, constructor beginning) -->DONE
    //@Todo: dont do set(sth) when class isn't used later instead return it --> DONE
    //@Todo: open connection stuff in request class if possible --> DONE
    //@Todo: dont make token as a static field but return it after login for use in later requests --> DONE
    //@Todo: clean up code --> delete comments etc.

    //make new single jar file (after changin code) with:mvn clean compile assembly:single
    //then go cd target and do:
    //run with cmd line with: java -jar UseCase1-1.0-SNAPSHOT-jar-with-dependencies.jar
    //UseCase1:
    //argument1: filepath: /Users/maxzehnder/Desktop/Skribble/Administratives/PersönlicheAngaben.pdf
    //argument2: savepath: "/Users/maxzehnder/Desktop/Skribble/TestFiles/signed.pdf"



    //UseCase2:
    //args[0]: path to csv file
    //args[1]: path to files to be signed (in one directory)
    //args[2]: path where the SR-Id file should be saved
   /* whole cmd:*/ /*java -jar UseCase1-1.0-SNAPSHOT-jar-with-dependencies.jar
     /Users/maxzehnder/Desktop/Skribble/TestFiles/TestCSVFile.csv
     /Users/maxzehnder/Desktop/Skribble/TestFiles
     /Users/maxzehnder/Desktop/Skribble/TestFiles/SignatureRequestIds*/


    public static void main(String[] args) throws IOException {
        //doUseCase1(args[0], args[1]);
        checkCmdLineInput(args);
        doUseCase2(args[0], args[1], args[2]);
    }



    public static void doUseCase2(String csvFilePath, String filesToBeSignedPath, String signatureRequestIdFilePath) throws IOException {

        //1. Login User
        User user = new User("api_demo_maxag_dd58_0", "8cecd429-3749-4e2a-9bf4-7d520e3196b0");
        StringBuilder token = user.loginUser();
        System.out.println("You have been successfully logged in");

        //2. read the csv file and create a list with signer documents and E-Mails
        CSVFile csvFile = new CSVFile(csvFilePath);
        List<String> csvFileList = csvFile.readCSVFile();

        //3. populate signer-List with signers of csv file as Signer Entities
        AllSigners allSigners = new AllSigners();
        ArrayList<Signer> signerList = allSigners.populateSignerList(csvFileList, filesToBeSignedPath);

        //4. Process the requests and get response-list of all the requests
        AllSignatureRequests allSignatureRequests = new AllSignatureRequests(signerList, token);
        ArrayList<SignatureRequestResponse> responseList = allSignatureRequests.doRequests();

        //5. Write SR Id's to file with corresponding E-Mail
        /*SignatureRequestIdFile signatureRequestIdFile = new SignatureRequestIdFile(responseList, signatureRequestIdFilePath);
        signatureRequestIdFile.writeIdToFile();*/

        //6. or start polling to automatically download the pdf after it is signed
        for (SignatureRequestResponse sr : responseList) {
            Poller poller = new Poller(sr, token);
            poller.startPolling(sr, signatureRequestIdFilePath);
        }

        System.out.println(responseList.get(0).getId());
        //System.out.println(allSignatureRequests.getResponseList().get(1).getId());


    }


    public static void doUseCase1(String pathOfFileToBeSigned, String pathOfFileSigned) throws IOException {
        User user = new User("api_demo_maxag_dd58_0", "8cecd429-3749-4e2a-9bf4-7d520e3196b0");
        StringBuilder token = user.loginUser();
        System.out.println("You have been successfully logged in");

        //CREATE SR POST REQUEST
        SignatureRequest signatureRequest = new SignatureRequest(user, token);
        SignatureRequestResponse signatureRequestResponse = signatureRequest.createSR(pathOfFileToBeSigned);
        System.out.println("Please wait until your document is signed");


        //check if signed and download doc after signing
        Poller poller = new Poller(signatureRequestResponse, token);
        poller.startPolling(signatureRequestResponse, pathOfFileSigned);
    }

    public static void checkCmdLineInput(String[] input){
        if (input.length == 0){
            System.out.println("You are missing all arguments");
        }

        if (input.length == 1){
            System.out.println("You are missing two arguments");
        }

        if (input.length == 2){
            System.out.println("You are missing one argument");
        }

        if (input.length != 3){
            System.out.println("You need exactly three arguments:");
            System .out.println("1. The Path to the CSV File where your signers E-Mails and corresponding filenames of the files to be signed are saved");
            System.out.println("2. The Path where the files to be signed are actually saved");
            System.out.println("3. The Path where you want your Signature-Request-Id File to be saved");
            exit(0);
        }
        if (!input[0].endsWith(".csv")){
            System.out.println("The file with the signer has to be a .CSV File");
            exit(0);
        }


    }


    //Function to convert Json
    public static SignatureRequestResponse convertJsonToEntity(String json) {
        Gson gson = new Gson(); //
        return gson.fromJson(json, SignatureRequestResponse.class);
    }

}
