package app.planmanager;

public class Lesson {
    private final String dayName;
    private final int lessonNumber;
    private final int classroom;
    private final String subjectName;
    private final String subjectTeacherInitials;

    public Lesson(String dayName, int lessonNumber, int classroom, String subjectName, String teacherName, String teacherSurname) {
        this.dayName = dayName;
        this.lessonNumber = lessonNumber;
        this.classroom = classroom;
        this.subjectName = subjectName;
        this.subjectTeacherInitials = teacherName.substring(0,1) + teacherSurname.charAt(0);
    }

    public String getDayName() {
        return dayName;
    }

    public int getLessonNumber() {
        return lessonNumber;
    }

    public int getClassroom() {
        return classroom;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public String getSubjectTeacherInitials() {
        return subjectTeacherInitials;
    }

    @Override
    public String toString() {
        return "Lesson{" +
                "dayName='" + dayName + '\'' +
                ", lessonNumber=" + lessonNumber +
                ", classroom=" + classroom +
                ", subjectName='" + subjectName + '\'' +
                ", subjectTeacherInitials='" + subjectTeacherInitials + '\'' +
                '}';
    }
}
