package app.planmanager;

enum Group{
    user,
    teacher,
    admin
}

public class User {
    private int userID_;
    private String name_;
    private String surname_;
    private String email_;
    private Group group_;

    public User(int userID, String name, String surname, String email, Group group) {
        this.userID_ = userID;
        this.name_ = name;
        this.surname_ = surname;
        this.email_ = email;
        this.group_ = group;
    }

    public int getUserID_() {
        return userID_;
    }

    public void setUserID_(int userID_) {
        this.userID_ = userID_;
    }

    public String getName_() {
        return name_;
    }

    public void setName_(String name_) {
        this.name_ = name_;
    }

    public String getSurname_() {
        return surname_;
    }

    public void setSurname_(String surname_) {
        this.surname_ = surname_;
    }

    public String getEmail_() {
        return email_;
    }

    public void setEmail_(String email_) {
        this.email_ = email_;
    }

    public Group getGroup_() {
        return group_;
    }

    public void setGroup_(Group group_) {
        this.group_ = group_;
    }

    @Override
    public String toString() {
        return "User{" +
                "userID_=" + userID_ +
                ", name_='" + name_ + '\'' +
                ", surname_='" + surname_ + '\'' +
                ", email_='" + email_ + '\'' +
                ", group_=" + group_ +
                '}';
    }
}
