package object;

public class firebasemodel {
    String name;
    String image;
    String uid;
    String uid_contacter;
    int time;

    public firebasemodel(String name, String image, String uid, String uid_contacter, int time) {
        this.name = name;
        this.image = image;
        this.uid = uid;
        this.uid_contacter = uid_contacter;
        this.time=time;
    }

    public firebasemodel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUid_contacter() {
        return uid_contacter;
    }

    public void setUid_contacter(String uid_contacter) {
        this.uid_contacter = uid_contacter;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

}
