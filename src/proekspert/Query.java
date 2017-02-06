package proekspert;

/**
 * Created by mpihe on 2/4/2017.
 */
public class Query {
    private final String date;
    private final String timeStamp;
    private final String threadID;
    private final String userContext;
    private final String uriQuery;
    private final int requestDuration;
    private boolean isResource = false;


    public Query(String resourceInfoString) {
        String[] resourceInfo = resourceInfoString.split(" ");
        this.date = resourceInfo[0];
        this.timeStamp = resourceInfo[1];
        this.threadID = resourceInfo[2].replace("(", "").replace(")", "");
        this.userContext = resourceInfo[3].replace("[", "").replace("]", "");
        this.uriQuery = resourceInfo[4];
        this.requestDuration = Integer.parseInt(resourceInfo[6]);
        setIsResource();
    }


    private void setIsResource() {
        if (this.uriQuery.contains("msisdn=") || this.uriQuery.contains("action=NOTIFICATION")) {
            this.isResource = true;
        }
    }

    public boolean getIsResource() {
        return this.isResource;
    }


    public String getUriQuery() {
        return this.uriQuery;
    }

    public int getRequestDuration() {
        return requestDuration;
    }


    public String toString() {
        String returnString = "";

        returnString += "Date: " + date;
        returnString += "\nTimestamp: " + timeStamp;
        returnString += "\nThread ID: " + threadID;
        returnString += "\nUser context: " + userContext;
        returnString += "\nURI + query: " + uriQuery;
        returnString += "\nRequest duration: " + requestDuration + " ms\n";

        return returnString;
    }

    public String getTimeStamp() {
        return timeStamp;
    }
}
