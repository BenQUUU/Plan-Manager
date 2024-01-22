package app.planmanager;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.ArrayList;

public class Lesson implements Comparable<Lesson> {
    private final SimpleStringProperty dayName;
    private final SimpleIntegerProperty lessonNumber;
    private final SimpleIntegerProperty classroom;
    private final SimpleStringProperty subjectName;
    private final SimpleStringProperty subjectTeacherInitials;

    public Lesson(String dayName, int lessonNumber, int classroom, String subjectName, String teacherName, String teacherSurname) {
        this.dayName = new SimpleStringProperty(dayName);
        this.lessonNumber = new SimpleIntegerProperty(lessonNumber);
        this.classroom = new SimpleIntegerProperty(classroom);
        this.subjectName = new SimpleStringProperty(subjectName);
        this.subjectTeacherInitials = new SimpleStringProperty(teacherName.substring(0, 1) + teacherSurname.charAt(0));
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

    @Override
    public int compareTo(Lesson otherLesson) {
        return Integer.compare(this.lessonNumber.get(), otherLesson.getLessonNumber());
    }
}