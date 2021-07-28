package UseCase2;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CSVFile {

    private String filePath;
    private List<String> CSVFileList;


    public CSVFile(String filePath) {
        this.filePath = filePath;
    }


    public String getFilePath() {
        return filePath;
    }

    public List<String> getCSVFileList() {
        return CSVFileList;
    }


    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setCSVFileList(List<String> CSVFile) {
        this.CSVFileList = CSVFile;
    }


    //read the CSV File and create a list with signer emails and their files to be signed
    public void readCSVFile() throws FileNotFoundException {
        List<List<String>> records = new ArrayList<>();
        List<String> csvFileList = new ArrayList<>();

        try (CSVReader csvReader = new CSVReader(new FileReader(this.getFilePath()))) {

            String[] values = null;
            while ((values = csvReader.readNext()) != null) {
                records.add(Arrays.asList(values));
            }

            for (List<String> record : records) {
                csvFileList.add(record.get(0));
            }

            setCSVFileList(csvFileList);
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
    }

}
