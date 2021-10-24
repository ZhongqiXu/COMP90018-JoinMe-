package object;


import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.google.android.gms.maps.model.LatLng;

public class Activity implements Serializable {

    private String aid;
    private String title;
    private String datetime;
    private String placeName;
    private LatLng LatLng;
    private String owner; // uid
    private List<String> participants; // uid of participants
    private List<String> candidates; // uid of candidates
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

    public List<String> getCandidates() {
        return candidates;
    }

    public void setCandidates(List<String> candidates) {
        this.candidates = candidates;
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

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public com.google.android.gms.maps.model.LatLng getLatLng() {
        return LatLng;
    }

    public void setLatLng(com.google.android.gms.maps.model.LatLng latLng) {
        this.LatLng = latLng;
    }

    public void mapToActivity(Activity activity, HashMap info){
        activity.setAid((String) info.get("aid"));
        if (info.get("autoJoin") != null) {
            activity.setAutoJoin((Boolean) info.get("autoJoin"));
        }

        activity.setTitle((String) info.get("title"));
        activity.setDatetime((String) info.get("datetime"));
        activity.setDetails((String) info.get("details"));
        activity.setSize(Integer.parseInt(Objects.requireNonNull(info.get("size")).toString()));
        activity.setPlaceName((String) info.get("placeName"));
        activity.setLatLng((com.google.android.gms.maps.model.LatLng) info.get("LatLng"));
        activity.setOwner((String) info.get("owner"));
        if (info.get("participants") != null) {
            activity.setParticipants((List<String>) info.get("participants"));
        }
        if (info.get("candidates") != null) {
            activity.setCandidates((List<String>) info.get("candidates"));
        }
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("aid", aid);
        result.put("autoJoin", autoJoin);
        result.put("title", title);
        result.put("datetime", datetime);
        result.put("details", details);
        result.put("size", size);
        result.put("placeName", placeName);
        result.put("LatLng", LatLng);
        result.put("owner", owner);
        result.put("participants", participants);
        result.put("candidates", candidates);
        return result;
    }
}
