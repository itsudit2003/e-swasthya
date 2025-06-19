package com.raven.model;

public class ModelUser {

    private int userID;
    private String userName;
    private String email;
    private String password;
    private String verifyCode;
    private String status;
    private Integer age;
    private String gender;
    private Integer weight;

    // Constructors
    public ModelUser(int userID, String userName, String email, String password, String verifyCode, String status, int age, String gender, int weight) {
        this.userID = userID;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.verifyCode = verifyCode;
        this.status = status;
        this.age = age;
        this.gender = gender;
        this.weight = weight;
    }

    public ModelUser(int userID, String userName, String email, String password) {
        this(userID, userName, email, password, "", "", 0, "", 0);
    }

    public ModelUser() {
        // Default constructor
    }

    // Getter and Setter methods for all fields

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
