/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package energieagent.input;

import java.io.IOException;
import java.util.logging.Level;
import helper.CSVHelper;
import java.util.logging.Logger;

/**
 *
 * @author Julius
 */
public class CSVInput extends Thread{
    String timestamp="";
    String timestampNew = "";
    double ausspeicherLeistung;
    double elektrischeLeistung;
    double speicherstand;

    /**
     *
     */
    public CSVInput() {
        
    }
    
    private CSVValue readCSV(String path){
        CSVHelper helper = new CSVHelper(path);
        CSVValue newValue = null;

        
        String[] values = helper.getLine(1);

        newValue = new CSVValue(values[0], Double.parseDouble(values[1]));


        return newValue;
    }
    
    /**
     *Get current Values from CSV files.
     * This method get the values of the meassuring CSV files and check the 
     * timestamps of the different CSV files. 
     * If the timestamps come to an agreement the values will be stored in
     * global variables.
     * 
     */
    private void getData(){
        CSVValue ausspeicherCSV = readCSV("..\\Dateien\\ausspeicherleistung.csv");
        CSVValue elektrCSV = readCSV("..\\Dateien\\elektrLeistung.csv");
        CSVValue speicherCSV = readCSV("..\\Dateien\\speicherstand.csv");
        System.out.println(ausspeicherCSV);
        System.out.println(elektrCSV);
        System.out.println(speicherCSV);
        
        if (ausspeicherCSV.equalsTimestamp(elektrCSV) && ausspeicherCSV.equalsTimestamp(speicherCSV)){
            System.out.println("Timestamp ok");
            this.timestamp = ausspeicherCSV.getTimestamp();
            this.ausspeicherLeistung = ausspeicherCSV.getValue();
            this.elektrischeLeistung = elektrCSV.getValue();
            this.speicherstand = speicherCSV.getValue();
        }
    }
    
    /**
    *Exportes new values collected to a CSV files.
    * If the CSV file has already 100 entries the oldest entry will be removed
    * and the new added.
    * 
    */
    private void exportToCSV(){
        String csvFile = "..\\Dateien\\aktuelleWerte.csv";
        CSVHelper helper = new CSVHelper(csvFile);
        
        //Check if already 100 entries exist
        if(helper.getLinesValue()>100){
            try {
                helper.deleteEntry(2);
            } catch (IOException ex) {
                Logger.getLogger(CSVInput.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        /**
         *Check if file already exists.
         * If not add the name of the entries.
         */
        if(helper.getLinesValue()==0){
            String[] title = {"Timestamp", "Ausspeicherleitsung", "Elektrische Leistung", "Speicherfuellstand"};
            helper.addLine(title);
        }
        //Put new values in a string array for the helper.addLine(...) method.
        String value[] = {timestamp, Double.toString(this.ausspeicherLeistung), Double.toString(this.elektrischeLeistung), Double.toString(this.speicherstand)};
//        for (String value1 : value) {
//            System.out.println(value1);
//        }
        //add new entry if timestamp is not null
        if(timestamp!=null){
            helper.addLine(value);
        }
   
    }

    /**
     *Executes the input thread.
     * The thread get the values with the {@link #getData() getData} function.
     * If the values are not already read the values will be exported with
     * {@link #exportToCSV() exportToCSV} function.
     * Thread waits 30 seconds and repeat the function.
     * 
     */
    @Override
    public void run() {
        while(true){
            try {
                getData();
                
                //check if timestamp is already read.
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
