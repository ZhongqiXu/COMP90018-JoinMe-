package object;

public class Message {

    String message, senderID;
    int time;
    String currenttime;
    String type;

    public Message() {
    }

    public Message(String message, String senderID, int time, String currenttime, String type) {
        this.message = message;
        this.senderID = senderID;
        this.time = time;
        this.currenttime = currenttime;
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public long getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getCurrenttime() {
        return currenttime;
    }

    public void setCurrenttime(String currenttime) {
        this.currenttime = currenttime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
