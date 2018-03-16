package energieagent.input;

import java.util.Objects;

/**
 *
 * @author Julius
 */
public class CSVValue {
    private String timestamp;
    private double value;

    public CSVValue(String timestamp, double value) {
        this.timestamp = timestamp;
        this.value = value;
    }

    public String getTimestamp() {
        
        return timestamp;
    }

    public double getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "CSVValue{" + "timestamp=" + timestamp + ", value=" + value + '}';
    }



   
    public boolean equalsTimestamp(CSVValue obj) {
        String thisTime = this.getTimestamp().substring(0,this.getTimestamp().indexOf("."))+
                this.timestamp.substring(this.timestamp.lastIndexOf(" "));
        String objTime = obj.getTimestamp().substring(0,obj.getTimestamp().indexOf("."))+
                obj.timestamp.substring(obj.timestamp.lastIndexOf(" "));
        System.out.println(thisTime);
        if(thisTime.equals(objTime))
        return true;
        
        return false;
    }
    
    
    
    
}
