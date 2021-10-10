package com.comp90018.JoinMe;

public class User {

    private String uid;
    private String userName;
    private String gender;
    private String age;
    private String email;
    private String brief;

    public User() {
    }

    public User(String uid, String userName, String gender, String age, String email, String brief) {
        this.uid = uid;
        this.userName = userName;
        this.gender = gender;
        this.age = age;
        this.email = email;
        this.brief = brief;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    @Override
    public String toString() {
        return "User{" +
                "uid='" + uid + '\'' +
                ", userName='" + userName + '\'' +
                ", gender='" + gender + '\'' +
                ", age='" + age + '\'' +
                ", email='" + email + '\'' +
                ", brief='" + brief + '\'' +
                '}';
    }
}
