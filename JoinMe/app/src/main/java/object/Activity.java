package object;


import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Activity implements Serializable {

    private String aid;
    private String title;
    private String datetime;
    private String owner; // uid
    private List<String> participants; // uid of participants
    private String details;
    private int size;
    private boolean autoJoin;

    public Activity() {}

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public List<String> getParticipants() {
        return participants;
    }

    public void setParticipants(List<String> participants) {
        this.participants = participants;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public boolean isAutoJoin() {
        return autoJoin;
    }

    public void setAutoJoin(boolean autoJoin) {
        this.autoJoin = autoJoin;
    }

    public void stringToActivity(Activity activity, String info){
        for(String entry : info.split(", ")) {
            String[] spl = entry.split("=");
            if (spl.length != 2 ){
                continue;
            }
            String name = spl[0];
            String value = spl[1];
            switch (name){
                case "title":
                    activity.setTitle(value);
                    break;
                case "datetime":
                    activity.setDatetime(value);
                    break;
                case "owner":
                    activity.setOwner(value);
                    break;
                case "details":
                    activity.setDetails(value);
                    break;
                case "size":
                    activity.setSize(Integer.parseInt(value));
                    break;
                case "autoJoin":
                    activity.setAutoJoin(Boolean.parseBoolean(value));
                    break;
                case "aid":
                    activity.setAid(value);
                    break;
            }

        }
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("aid", aid);
        result.put("title", title);
        result.put("autoJoin", autoJoin);
        result.put("details", details);
        result.put("size", size);
        result.put("owner",owner);
        result.put("datetime",datetime);

        return result;
    }
}
