package energieagent.input;

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
    
    
}
