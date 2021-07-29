package main;

import AllUseCases.User;
import JsonEntities.SignatureRequestResponse;
import UseCase1.Poller;
import UseCase1.SignatureRequest;
import UseCase2.AllSignatureRequests;
import UseCase2.AllSigners;
import UseCase2.CSVFile;
import UseCase2.SignatureRequestIdFile;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Scanner;

import static java.lang.System.exit;

public class Main {

    //@Todo: write arguments in args
    //@Todo: format of classes (static beginning, constructor beginning) -->DONE
    //@Todo: dont do set(sth) when class isn't used later instead return it
    //@Todo: open connection stuff in request class if possible
    //@Todo: dont make token as a static field but return it after login for use in later requests --> DONE

    //make new single jar file (after changin code) with:mvn clean compile assembly:single
    //then go cd target and do:
    //run with cmd line with: java -jar UseCase1-1.0-SNAPSHOT-jar-with-dependencies.jar
    //args[0]: path to csv file
    //args[1]: path to files to be signed (in one directory)
    //args[2]: path where the SR-Id file should be saved
   /* whole cmd:*/ /*java -jar UseCase1-1.0-SNAPSHOT-jar-with-dependencies.jar
     /Users/maxzehnder/Desktop/Skribble/TestFiles/TestCSVFile.csv
     /Users/maxzehnder/Desktop/Skribble/TestFiles
     /Users/maxzehnder/Desktop/Skribble/TestFiles/SignatureRequestIds*/


    public static void main(String[] args) throws IOException {
        //doUseCase1();
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
        //CSVFile csvFile = new CSVFile("/Users/maxzehnder/Desktop/Skribble/TestFiles/TestCSVFile.csv");
        csvFile.readCSVFile();

        //3. populate signer-List with signers of csv file as Signer Entities
        AllSigners allSigners = new AllSigners();
        allSigners.populateSignerList(csvFile, filesToBeSignedPath);

        //4. Process the requests and get response-list of all the requests
        AllSignatureRequests allSignatureRequests = new AllSignatureRequests(allSigners.getSignerList(), token);
        allSignatureRequests.doRequests();

        //5. Write SR Id's to file with corresponding E-Mail
        SignatureRequestIdFile signatureRequestIdFile = new SignatureRequestIdFile(allSignatureRequests.getResponseList(), signatureRequestIdFilePath);
        signatureRequestIdFile.writeIdToFile();

        System.out.println(allSignatureRequests.getResponseList().get(0).getId());
        System.out.println(allSignatureRequests.getResponseList().get(1).getId());


    }


    public static void doUseCase1() throws IOException {
        User user = new User("api_demo_maxag_dd58_0", "8cecd429-3749-4e2a-9bf4-7d520e3196b0");
        StringBuilder token = user.loginUser();
        System.out.println("You have been successfully logged in");


        //handle path of file to be signed
        Scanner filePathInput = new Scanner(System.in);
        System.out.println("Enter the path of your File to be signed:");
        String filePath = filePathInput.nextLine();


        //CREATE SR POST REQUEST
        SignatureRequest signatureRequest = new SignatureRequest(user, token);
        SignatureRequestResponse signatureRequestResponse = signatureRequest.createSR(filePath);
        System.out.println("Please wait until your document is signed");


        //check if signed and download doc after signing
        Poller poller = new Poller(signatureRequestResponse, token);
        poller.startPolling(signatureRequestResponse);
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
