package object;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Activity implements Serializable {

    private String aid;
    private String title;
    private Date time;
    private String owner; // uid
    private List<String> participants; // uid of participants
    private String details;
    private int size;
    private boolean autoJoin;

    public Activity(String title, Date time, String owner, List<String> participants, String details, int size, boolean autoJoin) {
        this.title = title;
        this.time = time;
        this.owner = owner;
        this.participants = participants;
        this.details = details;
        this.size = size;
        this.autoJoin = autoJoin;
    }

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

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
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
}
