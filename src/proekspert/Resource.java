package proekspert;

/**
 * Created by mpihe on 2/3/2017.
 */
public class Resource {
    private final String date;
    private final String timeStamp;
    private final String threadID;
    private final String userContext;
    private final String resourceName;
    private final String dataPayloadEleements;
    private int requestDuration = 0;

    public Resource(String resourceInfoString) {
        String[] resourceInfo = resourceInfoString.split(" ");
        this.date = resourceInfo[0];
        this.timeStamp = resourceInfo[1];
        this.threadID = resourceInfo[2].replace("(", "").replace(")", "");
        this.userContext = resourceInfo[3].replace("[", "").replace("]", "");
        this.resourceName = resourceInfo[4];
        this.dataPayloadEleements = resourceInfo[5];
        if (resourceInfo.length == 8) {
            this.requestDuration = Integer.parseInt(resourceInfo[7]);
        } else if (resourceInfo.length == 9) {
            this.requestDuration = Integer.parseInt(resourceInfo[8]);
        }
    }

    public String toString() {
        String returnString = "";

        returnString += "Date: " + date;
        returnString += "\nTimestamp: " + timeStamp;
        returnString += "\nThread ID: " + threadID;
        returnString += "\nUser context: " + userContext;
        returnString += "\nResource name: " + resourceName;
        returnString += "\nData payload elements: " + dataPayloadEleements;
        returnString += "\nRequest duration: " + requestDuration + " ms\n";

        return returnString;
    }

    public String getResourceName() {
        return resourceName;
    }

    public int getRequestDuration() {
        return requestDuration;
    }

    public String getTimeStamp() {
        return timeStamp;
    }
}
