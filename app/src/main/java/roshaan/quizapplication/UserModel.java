package roshaan.quizapplication;

public class UserModel {

    String email;
    String userType;
    String userID;

    public UserModel() {
    }

    public UserModel(String email, String userType, String userID) {
        this.email = email;
        this.userType = userType;
        this.userID = userID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
