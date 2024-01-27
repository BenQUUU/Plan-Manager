package app.planmanager;

public record EditPlanContainer(String planName, String dayName, int lessonNumber, String subject, int classroom) {

    @Override
    public String toString() {
        return "EditPlanContainer{" +
                "planName='" + planName + '\'' +
                ", dayName='" + dayName + '\'' +
                ", lessonNumber=" + lessonNumber +
                ", subject='" + subject + '\'' +
                ", classroom=" + classroom +
                '}';
    }
}
