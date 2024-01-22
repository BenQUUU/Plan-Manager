package app.planmanager;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Lesson {
    private final SimpleStringProperty dayName;
    private final SimpleIntegerProperty lessonNumber;
    private final SimpleIntegerProperty classroom;
    private final SimpleStringProperty subjectName;
    private final SimpleStringProperty subjectTeacherInitials;
    private final SimpleStringProperty hour = new SimpleStringProperty("");

    public Lesson(String dayName, int lessonNumber, int classroom, String subjectName, String teacherName, String teacherSurname) {
        this.dayName = new SimpleStringProperty(dayName);
        this.lessonNumber = new SimpleIntegerProperty(lessonNumber);
        this.classroom = new SimpleIntegerProperty(classroom);
        this.subjectName = new SimpleStringProperty(subjectName);
        this.subjectTeacherInitials = new SimpleStringProperty(teacherName.substring(0,1) + teacherSurname.charAt(0));
    }

    public String getDayName() {
        return dayName.get();
    }

    public SimpleStringProperty dayNameProperty() {
        return dayName;
    }

    public void setDayName(String dayName) {
        this.dayName.set(dayName);
    }

    public int getLessonNumber() {
        return lessonNumber.get();
    }

    public SimpleIntegerProperty lessonNumberProperty() {
        return lessonNumber;
    }

    public void setLessonNumber(int lessonNumber) {
        this.lessonNumber.set(lessonNumber);
    }

    public int getClassroom() {
        return classroom.get();
    }

    public SimpleIntegerProperty classroomProperty() {
        return classroom;
    }

    public void setClassroom(int classroom) {
        this.classroom.set(classroom);
    }

    public String getSubjectName() {
        return subjectName.get();
    }

    public SimpleStringProperty subjectNameProperty() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName.set(subjectName);
    }

    public String getSubjectTeacherInitials() {
        return subjectTeacherInitials.get();
    }

    public SimpleStringProperty subjectTeacherInitialsProperty() {
        return subjectTeacherInitials;
    }

    public void setSubjectTeacherInitials(String subjectTeacherInitials) {
        this.subjectTeacherInitials.set(subjectTeacherInitials);
    }

    @Override
    public String toString() {
        return "Lesson{" +
                "dayName=" + dayName +
                ", lessonNumber=" + lessonNumber +
                ", classroom=" + classroom +
                ", subjectName=" + subjectName +
                ", subjectTeacherInitials=" + subjectTeacherInitials +
                '}';
    }

    public String getHour() {
        return hour.get();
    }

    public SimpleStringProperty hourProperty() {
        return hour;
    }

    public void setHour() {
        switch(this.lessonNumber.get()){
            case 1:
                this.hour.set("8:00-8:45");
                break;
            case 2:
                this.hour.set("9:00-9:45");
                break;
            case 3:
                this.hour.set("10:00-10:45");
                break;
            case 4:
                this.hour.set("11:00-11:45");
                break;
            case 5:
                this.hour.set("12:00-12:45");
                break;
            case 6:
                this.hour.set("13:00-13:45");
                break;
            case 7:
                this.hour.set("14:00-14:45");
                break;
            case 8:
                this.hour.set("15:00-15:45");
                break;
            case 9:
                this.hour.set("16:00-16:45");
                break;
            case 10:
                this.hour.set("17:00-17:45");
                break;
            default:
                break;
        }





    }
}
