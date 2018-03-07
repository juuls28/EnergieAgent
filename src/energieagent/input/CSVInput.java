/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package energieagent.input;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Julius
 */
public class CSVInput extends Thread{
    String timestamp;
    String timestampNew = "";
    double ausspeicherLeistung;
    double elektrischeLeistung;
    double speicherstand;

    public CSVInput() {
        
    }
    
    private CSVValue readCSV(String path){
        String line = "";
        String csvSplitBy = ";";
        CSVValue newValue = null;

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {

            while ((line = br.readLine()) != null) {

                String[] values = line.split(csvSplitBy);

                newValue = new CSVValue(values[0], Double.parseDouble(values[1]));

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return newValue;
    }
    
    private void getData(){
        CSVValue ausspeicherCSV = readCSV("..\\Dateien\\ausspeicherleistung.csv");
        CSVValue elektrCSV = readCSV("..\\Dateien\\elektrLeistung.csv");
        CSVValue speicherCSV = readCSV("..\\Dateien\\speicherstand.csv");
        
        if (ausspeicherCSV.getTimestamp().equals(elektrCSV.getTimestamp())&&ausspeicherCSV.getTimestamp().equals(speicherCSV.getTimestamp())){
            this.timestamp = ausspeicherCSV.getTimestamp();
            this.ausspeicherLeistung = ausspeicherCSV.getValue();
            this.elektrischeLeistung = elektrCSV.getValue();
            this.speicherstand = speicherCSV.getValue();
        }
    }
    
    private void exportToCSV(){
        String csvFile = "..\\Dateien\\aktuelleWerte.csv";
        Path path = Paths.get(csvFile);
        if(Files.exists(path)){
            System.out.println("Datei existiert!");
            try {
                
            } catch (IOException ex) {
                Logger.getLogger(CSVInput.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else{
            try (FileWriter writer = new FileWriter(csvFile)) {
                CSVWriter.writeLine(writer, Arrays.asList("Timestamp", "Auspeicherleistung","Elektrische Leistung","Speicherfuellstand"));
                CSVWriter.writeLine(writer, Arrays.asList(timestamp, Double.toString(this.ausspeicherLeistung),Double.toString(this.elektrischeLeistung),Double.toString(this.speicherstand)));

                writer.flush();
            } catch (IOException ex) {
                Logger.getLogger(CSVInput.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void run() {
        while(true){
            try {
                getData();
                if(!this.timestampNew.equals(this.timestamp)){
                    System.out.println("Timestamp: " + timestamp +" elektr. Leistung: " + elektrischeLeistung + " Ausspeicherleistung: " + ausspeicherLeistung + " Speicherstand: " + speicherstand);
                    exportToCSV();
                    this.timestampNew = timestamp;
                    System.out.println("EXPORTED!");
                }
                Thread.sleep(30000);
            } catch (InterruptedException ex) {
                Logger.getLogger(CSVInput.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
